package cdw.helper.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Suite that holds utility methods for Logs
 */
public class LogUtilityTest {

    private LogUtility logUtility;
    private Logger mockLogger;

    @BeforeEach
    void setUp() {
        mockLogger = mock(Logger.class);
        try (MockedStatic<LogManager> mockedLogManager = Mockito.mockStatic(LogManager.class)) {
            mockedLogManager.when(() -> LogManager.getLogger(any(Class.class))).thenReturn(mockLogger);
            logUtility = new LogUtility();
        }
    }

    @Test
    void error_ShouldLogErrorMessage() {
        String message = "Error occurred";
        logUtility.error(message);
        verify(mockLogger, times(1)).error(message);
    }

    @Test
    void debug_ShouldLogDebugMessage() {
        String message = "Debugging issue";
        logUtility.debug(message);
        verify(mockLogger, times(1)).debug(message);
    }

    @Test
    void info_ShouldLogInfoMessage() {
        String message = "Information logged";
        logUtility.info(message);
        verify(mockLogger, times(1)).info(message);
    }

    @Test
    void warning_ShouldLogWarningMessage() {
        String message = "Warning issued";
        logUtility.warning(message);
        verify(mockLogger, times(1)).warn(message);
    }

    @Test
    void entryLog_ShouldLogEntryMessage() {
        String signature = "cdw.helper.controllers.AdminController.method()";
        logUtility.entryLog(signature);

        verify(mockLogger, times(1)).info("Entering - location::" + signature);
    }

    @Test
    void exitLog_ShouldLogExitMessageWithReturnValue() {
        String signature = "cdw.helper.controllers.AdminController.method()";
        String returnValue = "Success";
        logUtility.exitLog(signature, returnValue);

        verify(mockLogger, times(1)).info("Exiting - location::" + signature + " return value::" + returnValue);
    }
}
