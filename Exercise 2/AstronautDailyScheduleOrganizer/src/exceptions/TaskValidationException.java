package exceptions;

/**
 * Custom exception for task-related validation errors
 * Follows exception handling best practices
 */
public class TaskValidationException extends Exception {
    
    /**
     * Constructor with message
     * @param message Error message
     */
    public TaskValidationException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Error message
     * @param cause Root cause exception
     */
    public TaskValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
