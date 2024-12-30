package cdw.helper.services.residentservices;

import cdw.helper.constants.HelperAppConstants;
import cdw.helper.dto.AppointmentDTO;
import cdw.helper.dto.AvailableHelpersDTO;
import cdw.helper.entities.Appointment;
import cdw.helper.entities.User;
import cdw.helper.exceptions.HelperAppException;
import cdw.helper.exceptions.ResourceNotFoundException;
import cdw.helper.mappers.AppointmentMapper;
import cdw.helper.repositories.AppointmentRepository;
import cdw.helper.repositories.HelperDetailsRepository;
import cdw.helper.repositories.UserRepository;
import cdw.helper.utilities.DateUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite that holds testcases for Resident Service Operations
 */
public class ResidentServiceImplTest {

    private ResidentServiceImpl residentService;
    private UserRepository userRepository;
    private HelperDetailsRepository helperDetailsRepository;
    private AppointmentRepository appointmentRepository;
    private AppointmentMapper appointmentMapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        helperDetailsRepository = mock(HelperDetailsRepository.class);
        appointmentRepository = mock(AppointmentRepository.class);
        appointmentMapper = mock(AppointmentMapper.class);
        residentService = new ResidentServiceImpl(userRepository, helperDetailsRepository, appointmentRepository, appointmentMapper);
    }

    @Test
    void bookAppointment_ShouldSaveAppointment_WhenValidData() {
        Long residentId = 1L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setHelperId(2L);

        User resident = new User();
        User helper = new User();
        Appointment appointment = new Appointment();

        when(userRepository.findById(residentId)).thenReturn(Optional.of(resident));
        when(userRepository.findById(appointmentDTO.getHelperId())).thenReturn(Optional.of(helper));
        when(appointmentMapper.mapAppointmentDTOtoAppointment(residentId, appointmentDTO)).thenReturn(appointment);

        residentService.bookAppointment(residentId, appointmentDTO);

        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void bookAppointment_ShouldThrowException_WhenResidentNotFound() {
        Long residentId = 1L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        when(userRepository.findById(residentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> residentService.bookAppointment(residentId, appointmentDTO));

        assertEquals(HelperAppConstants.HA009, exception.getMessage());
    }

    @Test
    void bookAppointment_ShouldThrowException_WhenHelperNotFound() {
        Long residentId = 1L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setHelperId(2L);

        User resident = new User();

        when(userRepository.findById(residentId)).thenReturn(Optional.of(resident));
        when(userRepository.findById(appointmentDTO.getHelperId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> residentService.bookAppointment(residentId, appointmentDTO));

        assertEquals(HelperAppConstants.HA010, exception.getMessage());
    }

    @Test
    void bookAppointment_ShouldThrowException_WhenHelperIsBusy() {
        Long residentId = 1L;
        Long helperId = 2L;
        String scheduledAt = "2025-01-01 10:00";

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setHelperId(helperId);
        appointmentDTO.setScheduledAt(scheduledAt);

        Appointment appointment = new Appointment();
        appointment.setScheduledAt(DateUtility.convertStringToLocalDateTime(scheduledAt));

        User resident = new User();
        User helper = new User();

        when(userRepository.findById(residentId)).thenReturn(Optional.of(resident));
        when(userRepository.findById(helperId)).thenReturn(Optional.of(helper));
        when(appointmentMapper.mapAppointmentDTOtoAppointment(residentId, appointmentDTO)).thenReturn(appointment);
        when(appointmentRepository.existsByHelperIdAndScheduledAt(helperId, appointment.getScheduledAt())).thenReturn(true);

        HelperAppException exception = assertThrows(HelperAppException.class,
                () -> residentService.bookAppointment(residentId, appointmentDTO));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(HelperAppConstants.HA011, exception.getMessage());

        verify(userRepository, times(1)).findById(residentId);
        verify(userRepository, times(1)).findById(helperId);
        verify(appointmentRepository, times(1)).existsByHelperIdAndScheduledAt(helperId, appointment.getScheduledAt());
    }


    @Test
    void fetchAllAvailableHelpers_ShouldReturnList_WhenSkillAndDateTimeAreProvided() {
        String skill = "Plumber";
        String dateTime = "2024-12-30 10:00";
        LocalDateTime dateTimeConverted = DateUtility.convertStringToLocalDateTime(dateTime);

        List<AvailableHelpersDTO> mockHelpers = Arrays.asList(
                new AvailableHelpersDTO(1L, "Kavya", "Priya", "Plumber", 5f, 4f),
                new AvailableHelpersDTO(2L, "Govind", "Rajan", "Plumber", 4f, 5f)
        );

        when(appointmentRepository.fetchAllAvailableHelpers(dateTimeConverted, skill)).thenReturn(mockHelpers);

        List<AvailableHelpersDTO> helpers = residentService.fetchAllAvailableHelpers(skill, dateTime);

        assertNotNull(helpers);
        assertEquals(2, helpers.size());
        assertEquals("Kavya", helpers.get(0).getFirstName());
        verify(appointmentRepository, times(1)).fetchAllAvailableHelpers(dateTimeConverted, skill);
    }

    @Test
    void fetchAllAvailableHelpers_ShouldReturnList_WhenOnlySkillIsProvided() {
        String skill = "Electrician";
        String dateTime = null;

        List<AvailableHelpersDTO> mockHelpers = Collections.singletonList(
                new AvailableHelpersDTO(1L, "Kavya", "Priya", "Electrician", 5f, 4f)
        );

        when(appointmentRepository.fetchAllAvailableHelpers(null, skill)).thenReturn(mockHelpers);

        List<AvailableHelpersDTO> helpers = residentService.fetchAllAvailableHelpers(skill, dateTime);

        assertNotNull(helpers);
        assertEquals(1, helpers.size());
        assertEquals("Electrician", helpers.get(0).getSpecialization());
        verify(appointmentRepository, times(1)).fetchAllAvailableHelpers(null, skill);
    }

    @Test
    void fetchAllAvailableHelpers_ShouldReturnList_WhenOnlyDateTimeIsProvided() {
        String skill = null;
        String dateTime = "2024-12-30 15:00";
        LocalDateTime dateTimeConverted = DateUtility.convertStringToLocalDateTime(dateTime);

        List<AvailableHelpersDTO> mockHelpers = Arrays.asList(
                new AvailableHelpersDTO(1L, "Kavya", "Priya", "Electrician", 5f, 4f),
                new AvailableHelpersDTO(1L, "Govind", "Rajan", "Electrician", 3f, 2f)
        );

        when(appointmentRepository.fetchAllAvailableHelpers(dateTimeConverted, null)).thenReturn(mockHelpers);

        List<AvailableHelpersDTO> helpers = residentService.fetchAllAvailableHelpers(skill, dateTime);

        assertNotNull(helpers);
        assertEquals(2, helpers.size());
        assertEquals("Electrician", helpers.get(0).getSpecialization());
        verify(appointmentRepository, times(1)).fetchAllAvailableHelpers(dateTimeConverted, null);
    }

    @Test
    void fetchAllAvailableHelpers_ShouldReturnList_WhenNoSkillAndDateTimeAreProvided() {
        String skill = null;
        String dateTime = null;

        List<AvailableHelpersDTO> mockHelpers = Arrays.asList(
                new AvailableHelpersDTO(1L, "Kavya", "Priya", "Electrician", 5f, 4f),
                new AvailableHelpersDTO(2L, "Govind", "Rajan", "Electrician", 3f, 2f)

        );

        when(appointmentRepository.fetchAllAvailableHelpers(null, null)).thenReturn(mockHelpers);

        List<AvailableHelpersDTO> helpers = residentService.fetchAllAvailableHelpers(skill, dateTime);

        assertNotNull(helpers);
        assertEquals(2, helpers.size());
        assertEquals("Electrician", helpers.get(0).getSpecialization());
        verify(appointmentRepository, times(1)).fetchAllAvailableHelpers(null, null);
    }

    @Test
    void fetchAllAvailableHelpers_ShouldThrowException_WhenRepositoryFails() {
        String skill = "Gardening";
        String dateTime = "2024-12-30 10:00";
        LocalDateTime dateTimeConverted = DateUtility.convertStringToLocalDateTime(dateTime);

        when(appointmentRepository.fetchAllAvailableHelpers(dateTimeConverted, skill))
                .thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            residentService.fetchAllAvailableHelpers(skill, dateTime);
        });

        assertEquals("Database error", exception.getMessage());
        verify(appointmentRepository, times(1)).fetchAllAvailableHelpers(dateTimeConverted, skill);
    }
}
