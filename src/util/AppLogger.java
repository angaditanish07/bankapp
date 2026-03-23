package util;

import java.io.*;
import java.util.logging.*;

public class AppLogger {

    private static final Logger log = Logger.getLogger("UPIApp");

    static {
        try {
            FileHandler fh = new FileHandler("app.log", true);
            fh.setFormatter(new SimpleFormatter());
            log.addHandler(fh);
            log.setLevel(Level.ALL);
            log.setUseParentHandlers(true);
        } catch (IOException e) {
            System.err.println("Logger init failed: " + e.getMessage());
        }
    }

    public static void info(String msg)   { log.info(msg); }
    public static void warn(String msg)   { log.warning(msg); }
    public static void error(String msg)  { log.severe(msg); }
    public static void error(Throwable t) { log.log(Level.SEVERE, t.getMessage(), t); }
}
