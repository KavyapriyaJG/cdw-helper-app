package com.cdw.helper.app.common.utilities;

import com.cdw.helper.app.common.constants.HelperAppConstants;
import com.cdw.helper.app.common.exceptions.HelperAppException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Suite that holds test cases for date utility methods
 */
public class DateUtilityTest {
    @Test
    void convertStringToLocalDate_ShouldReturnLocalDate_WhenValidStringProvided() {
        String dateString = "2025-01-01";
        LocalDate result = DateUtility.convertStringToLocalDate(dateString);
        assertEquals(LocalDate.of(2025, 01, 01), result);
    }

    @Test
    void convertStringToLocalDate_ShouldThrowException_WhenInvalidStringProvided() {
        String invalidDateString = "July 19th";
        HelperAppException exception = assertThrows(HelperAppException.class, () -> {
            DateUtility.convertStringToLocalDate(invalidDateString);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(HelperAppConstants.HA002, exception.getMessage());
    }

    @Test
    void convertStringToLocalDateTime_ShouldReturnLocalDateTime_WhenValidStringProvided() {
        String dateTimeString = "2025-12-12 15:30";
        LocalDateTime result = DateUtility.convertStringToLocalDateTime(dateTimeString);
        assertEquals(LocalDateTime.of(2025, 12, 12, 15, 30), result);
    }

    @Test
    void convertStringToLocalDateTime_ShouldThrowException_WhenInvalidStringProvided() {
        String invalidDateTimeString = "July 19th, 15:30";
        HelperAppException exception = assertThrows(HelperAppException.class, () -> {
            DateUtility.convertStringToLocalDateTime(invalidDateTimeString);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(HelperAppConstants.HA007, exception.getMessage());
    }
}
