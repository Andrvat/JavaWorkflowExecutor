package ru.nsu.andrvat.loggersFeatures;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggersHelper {
    private static Logger logger = null;

    public static Logger getLoggerInstance(String callingClassname) {
        if (logger == null) {
            logger = Logger.getLogger(callingClassname);
            logger.setLevel(Level.ALL);
            try {
                FileHandler fileHandler = new FileHandler("logs/log.xml");
                fileHandler.setLevel(Level.ALL);
                logger.addHandler(fileHandler);
            } catch (SecurityException e) {
                logger.log(Level.SEVERE,
                        "Couldn't create log file because security properties blocked it", e);
            } catch (IOException e) {
                logger.log(Level.SEVERE,
                        "Couldn't create log file because input-output was occurred", e);
            }
        }
        return logger;
    }
}
