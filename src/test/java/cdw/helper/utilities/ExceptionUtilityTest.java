package cdw.helper.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Suite that holds test cases for exception utility methods
 */
public class ExceptionUtilityTest {
    private ExceptionUtility exceptionUtility;

    @BeforeEach
    void setUp() {
        exceptionUtility = new ExceptionUtility();
    }

    @Test
    void getPropertyCode_ShouldReturnPropertyValue_WhenCodeExists() throws IOException {
        String code = "HA001";
        String expectedMessage = "An unexpected error occurred !";
        Properties mockProperties = new Properties();
        mockProperties.setProperty(code, expectedMessage);

        Mockito.mockStatic(PropertiesLoaderUtils.class);
        when(PropertiesLoaderUtils.loadAllProperties("errorCodes.properties")).thenReturn(mockProperties);

        String result = exceptionUtility.getPropertyCode(code);
        assertEquals(expectedMessage, result);
        Mockito.clearAllCaches();
    }

    @Test
    void getPropertyCode_ShouldReturnNull_WhenCodeDoesNotExist() throws IOException {
        String code = "HA999";
        Properties mockProperties = new Properties();

        Mockito.mockStatic(PropertiesLoaderUtils.class);
        when(PropertiesLoaderUtils.loadAllProperties("errorCodes.properties")).thenReturn(mockProperties);

        String result = exceptionUtility.getPropertyCode(code);
        assertNull(result);
        Mockito.clearAllCaches();
    }
}
