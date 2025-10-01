package factories;

import models.Task;
import models.PriorityLevel;
import exceptions.TaskValidationException;
import utils.ValidationUtils;
import utils.Logger;

import java.time.LocalTime;

/**
 * Factory class for creating validated Task objects
 * Implements Factory Design Pattern
 * Follows SOLID principles with single responsibility for task creation
 */
public class TaskFactory {

    // Private constructor to prevent instantiation (static factory methods only)
    private TaskFactory() {
        throw new UnsupportedOperationException("Factory class cannot be instantiated");
    }

    /**
     * Creates a new task with validation
     * @param description Task description
     * @param startTimeStr Start time as string (HH:mm format)
     * @param endTimeStr End time as string (HH:mm format)
     * @param priorityStr Priority level as string
     * @return Validated Task object
     * @throws TaskValidationException if any validation fails
     */
    public static Task createTask(String description, String startTimeStr, 
                                String endTimeStr, String priorityStr) 
                                throws TaskValidationException {
        
        Logger.logMethodEntry("TaskFactory", "createTask", description, startTimeStr, endTimeStr, priorityStr);
        
        try {
            // Validate and sanitize all inputs
            String validatedDescription = ValidationUtils.validateAndSanitizeDescription(description);
            LocalTime startTime = ValidationUtils.validateAndParseTime(startTimeStr, "Start time");
            LocalTime endTime = ValidationUtils.validateAndParseTime(endTimeStr, "End time");
            
            // Validate time order
            ValidationUtils.validateTimeOrder(startTime, endTime);
            
            // Validate and parse priority level
            PriorityLevel priorityLevel;
            try {
                priorityLevel = PriorityLevel.fromString(priorityStr);
            } catch (IllegalArgumentException e) {
                throw new TaskValidationException("Invalid priority level: " + e.getMessage(), e);
            }

            // Create the task
            Task task = new Task(validatedDescription, startTime, endTime, priorityLevel, false);
            
            Logger.info("Successfully created task: " + task.getDescription());
            Logger.logMethodExit("TaskFactory", "createTask");
            
            return task;
            
        } catch (IllegalArgumentException e) {
            Logger.error("Task validation failed: " + e.getMessage(), e);
            throw new TaskValidationException("Task validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            Logger.error("Unexpected error during task creation: " + e.getMessage(), e);
            throw new TaskValidationException("Unexpected error during task creation: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a new task with LocalTime objects (for internal use)
     * @param description Task description
     * @param startTime Start time
     * @param endTime End time
     * @param priorityLevel Priority level
     * @return Validated Task object
     * @throws TaskValidationException if any validation fails
     */
    public static Task createTask(String description, LocalTime startTime, 
                                LocalTime endTime, PriorityLevel priorityLevel) 
                                throws TaskValidationException {
        
        Logger.logMethodEntry("TaskFactory", "createTask", description, startTime, endTime, priorityLevel);
        
        try {
            // Validate inputs
            String validatedDescription = ValidationUtils.validateAndSanitizeDescription(description);
            
            if (startTime == null || endTime == null || priorityLevel == null) {
                throw new TaskValidationException("Start time, end time, and priority level cannot be null");
            }
            
            // Validate time order
            ValidationUtils.validateTimeOrder(startTime, endTime);

            // Create the task
            Task task = new Task(validatedDescription, startTime, endTime, priorityLevel, false);
            
            Logger.info("Successfully created task: " + task.getDescription());
            Logger.logMethodExit("TaskFactory", "createTask");
            
            return task;
            
        } catch (IllegalArgumentException e) {
            Logger.error("Task validation failed: " + e.getMessage(), e);
            throw new TaskValidationException("Task validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            Logger.error("Unexpected error during task creation: " + e.getMessage(), e);
            throw new TaskValidationException("Unexpected error during task creation: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a completed task
     * @param description Task description
     * @param startTimeStr Start time as string
     * @param endTimeStr End time as string
     * @param priorityStr Priority level as string
     * @return Validated completed Task object
     * @throws TaskValidationException if any validation fails
     */
    public static Task createCompletedTask(String description, String startTimeStr, 
                                         String endTimeStr, String priorityStr) 
                                         throws TaskValidationException {
        
        Task task = createTask(description, startTimeStr, endTimeStr, priorityStr);
        Logger.info("Created completed task: " + task.getDescription());
        return task.withCompletionStatus(true);
    }

    /**
     * Validates task creation parameters without creating the task
     * Useful for pre-validation in UI scenarios
     * @param description Task description
     * @param startTimeStr Start time as string
     * @param endTimeStr End time as string
     * @param priorityStr Priority level as string
     * @throws TaskValidationException if any validation fails
     */
    public static void validateTaskParameters(String description, String startTimeStr, 
                                            String endTimeStr, String priorityStr) 
                                            throws TaskValidationException {
        
        Logger.logMethodEntry("TaskFactory", "validateTaskParameters", description, startTimeStr, endTimeStr, priorityStr);
        
        try {
            // Validate all parameters without creating the task
            ValidationUtils.validateAndSanitizeDescription(description);
            LocalTime startTime = ValidationUtils.validateAndParseTime(startTimeStr, "Start time");
            LocalTime endTime = ValidationUtils.validateAndParseTime(endTimeStr, "End time");
            ValidationUtils.validateTimeOrder(startTime, endTime);
            PriorityLevel.fromString(priorityStr);
            
            Logger.debug("Task parameters validation successful");
            Logger.logMethodExit("TaskFactory", "validateTaskParameters");
            
        } catch (IllegalArgumentException e) {
            Logger.error("Task parameter validation failed: " + e.getMessage(), e);
            throw new TaskValidationException("Task parameter validation failed: " + e.getMessage(), e);
        }
    }
}
