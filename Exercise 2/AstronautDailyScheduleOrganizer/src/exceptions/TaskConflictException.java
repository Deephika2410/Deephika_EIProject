package exceptions;

/**
 * Custom exception for task conflict scenarios
 * Provides specific information about scheduling conflicts
 */
public class TaskConflictException extends Exception {
    private final String conflictingTaskId;
    private final String conflictingTaskDescription;

    /**
     * Constructor with conflict details
     * @param message Error message
     * @param conflictingTaskId ID of the conflicting task
     * @param conflictingTaskDescription Description of the conflicting task
     */
    public TaskConflictException(String message, String conflictingTaskId, 
                               String conflictingTaskDescription) {
        super(message);
        this.conflictingTaskId = conflictingTaskId;
        this.conflictingTaskDescription = conflictingTaskDescription;
    }

    /**
     * Gets the ID of the conflicting task
     * @return Conflicting task ID
     */
    public String getConflictingTaskId() {
        return conflictingTaskId;
    }

    /**
     * Gets the description of the conflicting task
     * @return Conflicting task description
     */
    public String getConflictingTaskDescription() {
        return conflictingTaskDescription;
    }

    /**
     * Gets detailed conflict information
     * @return Formatted conflict details
     */
    public String getConflictDetails() {
        return String.format("Conflicts with task: '%s' (ID: %s)", 
                           conflictingTaskDescription, conflictingTaskId);
    }
}
