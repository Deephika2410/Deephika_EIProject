package exporters;

import models.Task;
import utils.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Factory class for creating schedule exporters
 * Implements Factory pattern for different export formats
 * Follows SOLID principles with single responsibility
 */
public class ScheduleExporterFactory {
    
    /**
     * Enumeration of supported export formats
     */
    public enum ExportFormat {
        TEXT("txt", "Plain Text"),
        CSV("csv", "Comma Separated Values");
        
        private final String extension;
        private final String description;
        
        ExportFormat(String extension, String description) {
            this.extension = extension;
            this.description = description;
        }
        
        public String getExtension() {
            return extension;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // Private constructor to prevent instantiation
    private ScheduleExporterFactory() {
        throw new UnsupportedOperationException("Factory class cannot be instantiated");
    }
    
    /**
     * Creates a schedule exporter for the specified format
     * @param format Export format
     * @return ScheduleExporter instance
     * @throws IllegalArgumentException if format is not supported
     */
    public static ScheduleExporter createExporter(ExportFormat format) {
        if (format == null) {
            throw new IllegalArgumentException("Export format cannot be null");
        }
        
        ScheduleExporter exporter;
        
        switch (format) {
            case TEXT:
                exporter = new TextScheduleExporter();
                break;
            case CSV:
                exporter = new CsvScheduleExporter();
                break;
            default:
                throw new IllegalArgumentException("Unsupported export format: " + format);
        }
        
        return exporter;
    }
    
    /**
     * Creates a schedule exporter by file extension
     * @param fileExtension File extension (e.g., "txt", "csv")
     * @return ScheduleExporter instance
     * @throws IllegalArgumentException if extension is not supported
     */
    public static ScheduleExporter createExporterByExtension(String fileExtension) {
        if (fileExtension == null || fileExtension.trim().isEmpty()) {
            throw new IllegalArgumentException("File extension cannot be null or empty");
        }
        
        String normalizedExtension = fileExtension.toLowerCase().trim();
        if (normalizedExtension.startsWith(".")) {
            normalizedExtension = normalizedExtension.substring(1);
        }
        
        for (ExportFormat format : ExportFormat.values()) {
            if (format.getExtension().equals(normalizedExtension)) {
                return createExporter(format);
            }
        }
        
        throw new IllegalArgumentException("Unsupported file extension: " + fileExtension);
    }
    
    /**
     * Exports schedule using the specified format
     * @param tasks List of tasks to export
     * @param fileName Output file name
     * @param format Export format
     * @throws IOException if export fails
     * @throws IllegalArgumentException if parameters are invalid
     */
    public static void exportSchedule(List<Task> tasks, String fileName, ExportFormat format) 
            throws IOException {
        
        if (tasks == null) {
            throw new IllegalArgumentException("Task list cannot be null");
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        
        if (format == null) {
            throw new IllegalArgumentException("Export format cannot be null");
        }
        
        try {
            ScheduleExporter exporter = createExporter(format);
            exporter.exportSchedule(tasks, fileName);
        } catch (IOException e) {
            throw e;
        }
    }
    
    /**
     * Gets all available export formats
     * @return Array of supported export formats
     */
    public static ExportFormat[] getAvailableFormats() {
        return ExportFormat.values();
    }
    
    /**
     * Gets a formatted string of available export formats
     * @return Formatted string listing available formats
     */
    public static String getAvailableFormatsString() {
        StringBuilder formats = new StringBuilder();
        formats.append("Available export formats:\n");
        
        for (int i = 0; i < ExportFormat.values().length; i++) {
            ExportFormat format = ExportFormat.values()[i];
            formats.append(String.format("%d. %s (.%s)\n", 
                         i + 1, format.getDescription(), format.getExtension()));
        }
        
        return formats.toString();
    }
    
    /**
     * Validates if a file extension is supported
     * @param fileExtension File extension to validate
     * @return true if supported, false otherwise
     */
    public static boolean isSupportedExtension(String fileExtension) {
        if (fileExtension == null || fileExtension.trim().isEmpty()) {
            return false;
        }
        
        String normalizedExtension = fileExtension.toLowerCase().trim();
        if (normalizedExtension.startsWith(".")) {
            normalizedExtension = normalizedExtension.substring(1);
        }
        
        for (ExportFormat format : ExportFormat.values()) {
            if (format.getExtension().equals(normalizedExtension)) {
                return true;
            }
        }
        
        return false;
    }
}
