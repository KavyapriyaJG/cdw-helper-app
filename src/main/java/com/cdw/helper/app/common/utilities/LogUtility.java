package com.cdw.helper.app.common.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 * Utility methods for Logging
 */
public class LogUtility {
    private static Logger LOGGER;

    public LogUtility() {
        LOGGER = LogManager.getLogger(ApplicationContext.class);
    }

    public void error(String message) {
        LOGGER.error(message);
    }

    public void debug(String message) {
        LOGGER.debug(message);
    }

    public void info(String message) {
        LOGGER.info(message);
    }

    public void warning(String message) {
        LOGGER.warn(message);
    }

    public void entryLog(String signature) {
        LOGGER.info("Entering - location::"+signature);
    }

    public void exitLog(String signature,String returnValue) {
        LOGGER.info("Exiting - location::"+signature+" return value::"+returnValue);
    }
}
