package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple logging utility for the application
 * Implements single responsibility principle
 * Thread-safe logging operations
 */
public class Logger {
    
    private static final String LOG_FILE = "astronaut_schedule.log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Log levels
    public enum LogLevel {
        INFO, WARN, ERROR, DEBUG
    }

    // Private constructor to prevent instantiation
    private Logger() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Logs an informational message
     * @param message Message to log
     */
    public static synchronized void info(String message) {
        log(LogLevel.INFO, message, null);
    }

    /**
     * Logs a warning message
     * @param message Message to log
     */
    public static synchronized void warn(String message) {
        log(LogLevel.WARN, message, null);
    }

    /**
     * Logs an error message
     * @param message Message to log
     */
    public static synchronized void error(String message) {
        log(LogLevel.ERROR, message, null);
    }

    /**
     * Logs an error message with exception details
     * @param message Message to log
     * @param exception Exception to log
     */
    public static synchronized void error(String message, Throwable exception) {
        log(LogLevel.ERROR, message, exception);
    }

    /**
     * Logs a debug message
     * @param message Message to log
     */
    public static synchronized void debug(String message) {
        log(LogLevel.DEBUG, message, null);
    }

    /**
     * Core logging method
     * @param level Log level
     * @param message Message to log
     * @param exception Optional exception
     */
    private static void log(LogLevel level, String message, Throwable exception) {
        try {
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);
            
            // Log to console
            System.out.println(logEntry);
            
            // Log to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
                writer.println(logEntry);
                
                if (exception != null) {
                    writer.println("Exception details:");
                    exception.printStackTrace(writer);
                }
            }
            
        } catch (IOException e) {
            // Fallback to console only if file logging fails
            System.err.println("Failed to write to log file: " + e.getMessage());
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            System.err.println(String.format("[%s] [%s] %s", timestamp, level, message));
            
            if (exception != null) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Logs method entry for debugging
     * @param className Class name
     * @param methodName Method name
     */
    public static void logMethodEntry(String className, String methodName) {
        debug(String.format("Entering %s.%s()", className, methodName));
    }

    /**
     * Logs method exit for debugging
     * @param className Class name
     * @param methodName Method name
     */
    public static void logMethodExit(String className, String methodName) {
        debug(String.format("Exiting %s.%s()", className, methodName));
    }

    /**
     * Logs method entry with parameters
     * @param className Class name
     * @param methodName Method name
     * @param parameters Method parameters
     */
    public static void logMethodEntry(String className, String methodName, Object... parameters) {
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) params.append(", ");
            params.append(parameters[i]);
        }
        debug(String.format("Entering %s.%s(%s)", className, methodName, params.toString()));
    }
}
