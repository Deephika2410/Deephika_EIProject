package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton Logger class for handling application logging.
 * This class provides centralized logging functionality with different
 * log levels and both console and file output capabilities.
 * 
 * Implements the Singleton pattern to ensure consistent logging
 * throughout the application.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class Logger {
    
    /**
     * Enumeration for different log levels
     */
    public enum LogLevel {
        DEBUG(0, "DEBUG"),
        INFO(1, "INFO"),
        WARNING(2, "WARN"),
        ERROR(3, "ERROR");
        
        private final int priority;
        private final String label;
        
        LogLevel(int priority, String label) {
            this.priority = priority;
            this.label = label;
        }
        
        public int getPriority() {
            return priority;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    private static Logger instance;
    private static final Object lock = new Object();
    
    private LogLevel currentLogLevel;
    private boolean consoleOutput;
    private boolean fileOutput;
    private String logFileName;
    private final DateTimeFormatter dateFormatter;
    
    /**
     * Private constructor for Singleton pattern
     */
    private Logger() {
        this.currentLogLevel = LogLevel.INFO;
        this.consoleOutput = true;
        this.fileOutput = false;
        this.logFileName = "smart_city_infrastructure.log";
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * Gets the singleton instance of the Logger
     * @return Logger instance
     */
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
    
    /**
     * Sets the current log level
     * @param level The log level to set
     */
    public void setLogLevel(LogLevel level) {
        this.currentLogLevel = level;
    }
    
    /**
     * Gets the current log level
     * @return Current log level
     */
    public LogLevel getLogLevel() {
        return currentLogLevel;
    }
    
    /**
     * Enables or disables console output
     * @param enabled true to enable console output, false to disable
     */
    public void setConsoleOutput(boolean enabled) {
        this.consoleOutput = enabled;
    }
    
    /**
     * Enables or disables file output
     * @param enabled true to enable file output, false to disable
     */
    public void setFileOutput(boolean enabled) {
        this.fileOutput = enabled;
    }
    
    /**
     * Sets the log file name
     * @param fileName The name of the log file
     */
    public void setLogFileName(String fileName) {
        if (fileName != null && !fileName.trim().isEmpty()) {
            this.logFileName = fileName.trim();
        }
    }
    
    /**
     * Logs a debug message
     * @param message The message to log
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }
    
    /**
     * Logs an info message
     * @param message The message to log
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }
    
    /**
     * Logs a warning message
     * @param message The message to log
     */
    public void warning(String message) {
        log(LogLevel.WARNING, message);
    }
    
    /**
     * Logs an error message
     * @param message The message to log
     */
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }
    
    /**
     * Logs an error message with exception details
     * @param message The message to log
     * @param throwable The exception to log
     */
    public void error(String message, Throwable throwable) {
        String fullMessage = message + " - Exception: " + throwable.getMessage();
        log(LogLevel.ERROR, fullMessage);
    }
    
    /**
     * Logs a message with the specified log level
     * @param level The log level
     * @param message The message to log
     */
    private void log(LogLevel level, String message) {
        // Check if the log level is enabled
        if (level.getPriority() < currentLogLevel.getPriority()) {
            return;
        }
        
        String timestamp = LocalDateTime.now().format(dateFormatter);
        String formattedMessage = String.format("[%s] [%s] %s", 
            timestamp, level.getLabel(), message);
        
        // Output to console if enabled
        if (consoleOutput) {
            if (level == LogLevel.ERROR || level == LogLevel.WARNING) {
                System.err.println(formattedMessage);
            } else {
                System.out.println(formattedMessage);
            }
        }
        
        // Output to file if enabled
        if (fileOutput) {
            writeToFile(formattedMessage);
        }
    }
    
    /**
     * Writes a message to the log file
     * @param message The message to write
     */
    private void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true))) {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            // Fallback to console if file writing fails
            System.err.println("Failed to write to log file: " + e.getMessage());
            System.err.println("Original message: " + message);
        }
    }
    
    /**
     * Logs system startup information
     */
    public void logSystemStartup() {
        info("=".repeat(60));
        info("Smart City Infrastructure Management System Started");
        info("Timestamp: " + LocalDateTime.now().format(dateFormatter));
        info("Log Level: " + currentLogLevel.getLabel());
        info("Console Output: " + consoleOutput);
        info("File Output: " + fileOutput);
        if (fileOutput) {
            info("Log File: " + logFileName);
        }
        info("=".repeat(60));
    }
    
    /**
     * Logs system shutdown information
     */
    public void logSystemShutdown() {
        info("=".repeat(60));
        info("Smart City Infrastructure Management System Shutdown");
        info("Timestamp: " + LocalDateTime.now().format(dateFormatter));
        info("=".repeat(60));
    }
    
    /**
     * Logs performance metrics
     * @param operation The operation that was performed
     * @param duration The duration in milliseconds
     */
    public void logPerformance(String operation, long duration) {
        info("Performance - Operation: " + operation + ", Duration: " + duration + "ms");
    }
    
    /**
     * Logs component hierarchy information
     * @param componentType The type of component
     * @param componentName The name of component
     * @param operation The operation performed
     * @param success Whether the operation was successful
     */
    public void logComponentOperation(String componentType, String componentName, 
                                    String operation, boolean success) {
        String status = success ? "SUCCESS" : "FAILED";
        info("Component Operation - Type: " + componentType + 
             ", Name: " + componentName + 
             ", Operation: " + operation + 
             ", Status: " + status);
    }
}
