package com.cdw.helper.app.common.utilities;

import com.cdw.helper.app.common.constants.HelperAppConstants;
import com.cdw.helper.app.common.exceptions.HelperAppException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class that holds date Utilities
 */
public class DateUtility {

    /**
     * Converts string to a local date instance
     * @param dateTimeString
     * @return
     */
    public static LocalDate convertStringToLocalDate(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateTimeString, formatter);
        } catch (Exception e) {
            throw new HelperAppException(HttpStatus.CONFLICT, HelperAppConstants.HA002);
        }
    }

    /**
     * Converts string to a local date time instance
     * @param dateTimeString
     * @return
     */
    public static LocalDateTime convertStringToLocalDateTime(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (Exception e) {
            throw new HelperAppException(HttpStatus.CONFLICT, HelperAppConstants.HA007);
        }
    }

}
