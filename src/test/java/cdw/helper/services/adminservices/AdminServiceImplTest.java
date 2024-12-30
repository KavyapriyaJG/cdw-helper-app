package cdw.helper.services.adminservices;

import cdw.helper.constants.HelperAppConstants;
import cdw.helper.dto.UserUpdationDTO;
import cdw.helper.entities.User;
import cdw.helper.exceptions.HelperAppException;
import cdw.helper.exceptions.ResourceNotFoundException;
import cdw.helper.mappers.UserMapper;
import cdw.helper.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite that holds test cases for Admin Service Operations
 */
public class AdminServiceImplTest {

    private AdminServiceImpl adminService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        adminService = new AdminServiceImpl(userRepository, userMapper);
    }

    @Test
    void fetchAllUsers_ShouldReturnAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = adminService.fetchAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void verifyUser_ShouldApproveUser_WhenUserExists() {
        Long userId = 1L;
        User user = new User();
        user.setApprovedByAdmin(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = adminService.verifyUser(userId);

        assertTrue(user.isApprovedByAdmin());
        assertEquals("Approved Successfully !", result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void verifyUser_ShouldThrowException_WhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        HelperAppException exception = assertThrows(HelperAppException.class, () -> adminService.verifyUser(userId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(HelperAppConstants.HA001, exception.getMessage());
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() {
        Long userId = 1L;
        User user = new User();
        UserUpdationDTO userUpdationDTO = new UserUpdationDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.mapUpdatedUserToUserDTO(userUpdationDTO, user)).thenReturn(user);

        adminService.updateUser(userId, userUpdationDTO);

        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).mapUpdatedUserToUserDTO(userUpdationDTO, user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        Long userId = 1L;
        UserUpdationDTO userUpdationDTO = new UserUpdationDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> adminService.updateUser(userId, userUpdationDTO));

        assertEquals(HelperAppConstants.HA001, exception.getMessage());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        adminService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        HelperAppException exception = assertThrows(HelperAppException.class, () -> adminService.deleteUser(userId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(HelperAppConstants.HA001, exception.getMessage());
    }

    @Test
    void deleteUser_ShouldThrowException_WhenDeleteFails() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(userRepository).deleteById(userId);

        HelperAppException exception = assertThrows(HelperAppException.class, () -> adminService.deleteUser(userId));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals(HelperAppConstants.HA006, exception.getMessage());
    }
}
