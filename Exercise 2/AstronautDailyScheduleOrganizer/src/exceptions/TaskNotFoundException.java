package exceptions;

/**
 * Custom exception for task not found scenarios
 * Provides specific information about missing tasks
 */
public class TaskNotFoundException extends Exception {
    private final String taskId;

    /**
     * Constructor with task ID
     * @param message Error message
     * @param taskId ID of the task that was not found
     */
    public TaskNotFoundException(String message, String taskId) {
        super(message);
        this.taskId = taskId;
    }

    /**
     * Gets the ID of the task that was not found
     * @return Task ID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Gets detailed error information
     * @return Formatted error details
     */
    public String getErrorDetails() {
        return String.format("Task with ID '%s' was not found in the schedule", taskId);
    }
}
