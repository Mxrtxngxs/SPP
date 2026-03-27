package mx.uv.spp.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogConfig {

    private static final Logger LOGGER = Logger.getLogger(LogConfig.class.getName());

    private LogConfig() {
    }

    public static void setup() {
        try {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String fileName = "spp_log_" + currentDate + ".log";

            FileHandler fileHandler = new FileHandler(fileName, true);
            fileHandler.setFormatter(new SimpleFormatter());

            Logger rootLogger = Logger.getLogger("");
            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.INFO);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize logger file", e);
        }
    }
}