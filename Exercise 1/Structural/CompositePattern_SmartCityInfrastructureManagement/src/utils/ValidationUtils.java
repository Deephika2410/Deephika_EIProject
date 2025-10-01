package utils;

import exceptions.InfrastructureException;

/**
 * Utility class for common validation operations.
 * This class provides static methods for validating inputs and
 * enforcing business rules throughout the application.
 * 
 * Follows the Single Responsibility Principle by focusing
 * solely on validation logic.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public final class ValidationUtils {
    
    // Private constructor to prevent instantiation of utility class
    private ValidationUtils() {
        throw new AssertionError("ValidationUtils is a utility class and should not be instantiated");
    }
    
    /**
     * Validates that a string is not null or empty.
     * @param value The string value to validate
     * @param fieldName The name of the field being validated (for error messages)
     * @throws InfrastructureException if validation fails
     */
    public static void validateNotNullOrEmpty(String value, String fieldName) throws InfrastructureException {
        if (value == null) {
            throw InfrastructureException.validationError(fieldName, "null");
        }
        
        if (value.trim().isEmpty()) {
            throw InfrastructureException.validationError(fieldName, "empty string");
        }
    }
    
    /**
     * Validates that an object is not null.
     * @param value The object to validate
     * @param fieldName The name of the field being validated (for error messages)
     * @throws InfrastructureException if validation fails
     */
    public static void validateNotNull(Object value, String fieldName) throws InfrastructureException {
        if (value == null) {
            throw InfrastructureException.validationError(fieldName, "null");
        }
    }
    
    /**
     * Validates that a numeric value is positive.
     * @param value The numeric value to validate
     * @param fieldName The name of the field being validated (for error messages)
     * @throws InfrastructureException if validation fails
     */
    public static void validatePositive(double value, String fieldName) throws InfrastructureException {
        if (value < 0) {
            throw InfrastructureException.validationError(fieldName, String.valueOf(value) + " (must be positive)");
        }
    }
    
    /**
     * Validates that a numeric value is within a specified range.
     * @param value The numeric value to validate
     * @param min The minimum allowed value (inclusive)
     * @param max The maximum allowed value (inclusive)
     * @param fieldName The name of the field being validated (for error messages)
     * @throws InfrastructureException if validation fails
     */
    public static void validateRange(double value, double min, double max, String fieldName) 
            throws InfrastructureException {
        if (value < min || value > max) {
            throw InfrastructureException.validationError(fieldName, 
                String.valueOf(value) + " (must be between " + min + " and " + max + ")");
        }
    }
    
    /**
     * Validates that an integer value is positive.
     * @param value The integer value to validate
     * @param fieldName The name of the field being validated (for error messages)
     * @throws InfrastructureException if validation fails
     */
    public static void validatePositive(int value, String fieldName) throws InfrastructureException {
        if (value < 0) {
            throw InfrastructureException.validationError(fieldName, String.valueOf(value) + " (must be positive)");
        }
    }
    
    /**
     * Validates that an integer value is within a specified range.
     * @param value The integer value to validate
     * @param min The minimum allowed value (inclusive)
     * @param max The maximum allowed value (inclusive)
     * @param fieldName The name of the field being validated (for error messages)
     * @throws InfrastructureException if validation fails
     */
    public static void validateRange(int value, int min, int max, String fieldName) 
            throws InfrastructureException {
        if (value < min || value > max) {
            throw InfrastructureException.validationError(fieldName, 
                String.valueOf(value) + " (must be between " + min + " and " + max + ")");
        }
    }
    
    /**
     * Validates that a string matches a basic ID pattern.
     * IDs should contain only alphanumeric characters, hyphens, and underscores.
     * @param id The ID string to validate
     * @param fieldName The name of the field being validated (for error messages)
     * @throws InfrastructureException if validation fails
     */
    public static void validateId(String id, String fieldName) throws InfrastructureException {
        validateNotNullOrEmpty(id, fieldName);
        
        String trimmedId = id.trim();
        
        // Check if ID contains only valid characters
        if (!trimmedId.matches("^[a-zA-Z0-9_-]+$")) {
            throw InfrastructureException.validationError(fieldName, 
                id + " (must contain only alphanumeric characters, hyphens, and underscores)");
        }
        
        // Check length constraints
        if (trimmedId.length() < 1 || trimmedId.length() > 50) {
            throw InfrastructureException.validationError(fieldName, 
                id + " (must be between 1 and 50 characters)");
        }
    }
    
    /**
     * Validates that a string has a reasonable length for a name.
     * @param name The name string to validate
     * @param fieldName The name of the field being validated (for error messages)
     * @throws InfrastructureException if validation fails
     */
    public static void validateName(String name, String fieldName) throws InfrastructureException {
        validateNotNullOrEmpty(name, fieldName);
        
        String trimmedName = name.trim();
        
        // Check length constraints
        if (trimmedName.length() < 1 || trimmedName.length() > 100) {
            throw InfrastructureException.validationError(fieldName, 
                name + " (must be between 1 and 100 characters)");
        }
        
        // Check for basic invalid characters (optional - can be adjusted based on requirements)
        if (trimmedName.contains("\n") || trimmedName.contains("\t")) {
            throw InfrastructureException.validationError(fieldName, 
                name + " (cannot contain newline or tab characters)");
        }
    }
    
    /**
     * Validates that a component type is one of the allowed types.
     * @param type The component type to validate
     * @param allowedTypes Array of allowed type values
     * @param fieldName The name of the field being validated (for error messages)
     * @throws InfrastructureException if validation fails
     */
    public static void validateComponentType(String type, String[] allowedTypes, String fieldName) 
            throws InfrastructureException {
        validateNotNullOrEmpty(type, fieldName);
        
        String trimmedType = type.trim();
        
        for (String allowedType : allowedTypes) {
            if (allowedType.equalsIgnoreCase(trimmedType)) {
                return; // Valid type found
            }
        }
        
        StringBuilder allowedTypesStr = new StringBuilder();
        for (int i = 0; i < allowedTypes.length; i++) {
            if (i > 0) allowedTypesStr.append(", ");
            allowedTypesStr.append(allowedTypes[i]);
        }
        
        throw InfrastructureException.validationError(fieldName, 
            type + " (must be one of: " + allowedTypesStr.toString() + ")");
    }
    
    /**
     * Sanitizes a string by trimming whitespace and ensuring it's safe for display.
     * @param input The input string to sanitize
     * @return Sanitized string, or empty string if input is null
     */
    public static String sanitizeString(String input) {
        if (input == null) {
            return "";
        }
        
        return input.trim();
    }
    
    /**
     * Checks if a string is null or empty after trimming.
     * @param value The string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
