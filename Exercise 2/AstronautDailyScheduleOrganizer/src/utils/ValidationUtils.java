package utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for input validation and sanitization
 * Implements defensive programming practices
 * Follows SOLID principles with single responsibility
 */
public class ValidationUtils {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final int MIN_DESCRIPTION_LENGTH = 3;
    private static final int MAX_DESCRIPTION_LENGTH = 100;

    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Validates and sanitizes task description
     * @param description Task description to validate
     * @return Sanitized description
     * @throws IllegalArgumentException if description is invalid
     */
    public static String validateAndSanitizeDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Task description cannot be null");
        }

        String sanitized = description.trim();
        
        if (sanitized.isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }

        if (sanitized.length() < MIN_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Task description must be at least %d characters long", 
                            MIN_DESCRIPTION_LENGTH));
        }

        if (sanitized.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Task description cannot exceed %d characters", 
                            MAX_DESCRIPTION_LENGTH));
        }

        // Remove any potential malicious characters (basic sanitization)
        sanitized = sanitized.replaceAll("[<>\"'&]", "");
        
        return sanitized;
    }

    /**
     * Validates and parses time string
     * @param timeStr Time string in HH:mm format
     * @param fieldName Name of the field for error messages
     * @return Parsed LocalTime object
     * @throws IllegalArgumentException if time format is invalid
     */
    public static LocalTime validateAndParseTime(String timeStr, String fieldName) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }

        String sanitizedTime = timeStr.trim();
        
        try {
            return LocalTime.parse(sanitizedTime, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                String.format("Invalid %s format: '%s'. Expected format: HH:mm (24-hour format)", 
                            fieldName.toLowerCase(), timeStr), e);
        }
    }

    /**
     * Validates that start time is before end time
     * @param startTime Start time
     * @param endTime End time
     * @throws IllegalArgumentException if time order is invalid
     */
    public static void validateTimeOrder(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                String.format("Start time (%s) must be before end time (%s)", 
                            startTime.format(TIME_FORMATTER), 
                            endTime.format(TIME_FORMATTER)));
        }

        // Check for reasonable task duration (at least 15 minutes, max 8 hours)
        long durationMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
        
        if (durationMinutes < 15) {
            throw new IllegalArgumentException(
                "Task duration must be at least 15 minutes");
        }

        if (durationMinutes > 480) { // 8 hours
            throw new IllegalArgumentException(
                "Task duration cannot exceed 8 hours");
        }
    }

    /**
     * Validates string input for null, empty, or whitespace-only values
     * @param input Input string to validate
     * @param fieldName Name of the field for error messages
     * @return Trimmed input string
     * @throws IllegalArgumentException if input is invalid
     */
    public static String validateStringInput(String input, String fieldName) {
        if (input == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }

        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }

        return trimmed;
    }

    /**
     * Validates numeric input for menu choices
     * @param input Input string to validate
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @return Parsed integer value
     * @throws IllegalArgumentException if input is invalid
     */
    public static int validateMenuChoice(String input, int min, int max) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Menu choice cannot be empty");
        }

        try {
            int choice = Integer.parseInt(input.trim());
            if (choice < min || choice > max) {
                throw new IllegalArgumentException(
                    String.format("Menu choice must be between %d and %d", min, max));
            }
            return choice;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Menu choice must be a valid number", e);
        }
    }

    /**
     * Sanitizes input to prevent injection attacks (basic implementation)
     * @param input Input string to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        // Remove potential dangerous characters
        return input.replaceAll("[<>\"'&;\\\\]", "").trim();
    }

    /**
     * Checks if a string represents a valid task ID format
     * @param taskId Task ID to validate
     * @return true if valid format, false otherwise
     */
    public static boolean isValidTaskIdFormat(String taskId) {
        if (taskId == null || taskId.trim().isEmpty()) {
            return false;
        }

        // UUID format validation (basic check)
        String trimmed = taskId.trim();
        return trimmed.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    }
}
