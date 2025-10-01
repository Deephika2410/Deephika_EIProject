package observers;

import models.Task;
import utils.Logger;

/**
 * Console-based implementation of TaskObserver
 * Provides user notifications through console output
 * Follows SOLID principles with dependency inversion
 */
public class ConsoleTaskObserver implements TaskObserver {
    
    private static final String SEPARATOR = "================================================";
    private static final String NOTIFICATION_PREFIX = "[NOTIFICATION] ";
    private static final String SUCCESS_PREFIX = "[SUCCESS] ";
    private static final String WARNING_PREFIX = "[WARNING] ";
    private static final String ERROR_PREFIX = "[ERROR] ";

    @Override
    public void onTaskAdded(Task task) {
        if (task == null) {
            Logger.warn("Received null task in onTaskAdded notification");
            return;
        }
        
        Logger.info("Task added notification: " + task.getDescription());
        
        System.out.println(SEPARATOR);
        System.out.println(SUCCESS_PREFIX + "Task Added Successfully!");
        System.out.println("Description: " + task.getDescription());
        System.out.println("Time: " + task.getFormattedStartTime() + " - " + task.getFormattedEndTime());
        System.out.println("Priority: " + task.getPriorityLevel().getDisplayName());
        System.out.println("Duration: " + task.getDurationMinutes() + " minutes");
        System.out.println("Status: No conflicts detected");
        System.out.println(SEPARATOR);
    }

    @Override
    public void onTaskUpdated(Task oldTask, Task newTask) {
        if (oldTask == null || newTask == null) {
            Logger.warn("Received null task in onTaskUpdated notification");
            return;
        }
        
        Logger.info("Task updated notification: " + oldTask.getDescription() + " -> " + newTask.getDescription());
        
        System.out.println(SEPARATOR);
        System.out.println(SUCCESS_PREFIX + "Task Updated Successfully!");
        System.out.println("Previous: " + oldTask.getDescription() + 
                         " (" + oldTask.getFormattedStartTime() + " - " + oldTask.getFormattedEndTime() + ")");
        System.out.println("Updated:  " + newTask.getDescription() + 
                         " (" + newTask.getFormattedStartTime() + " - " + newTask.getFormattedEndTime() + ")");
        System.out.println("New Priority: " + newTask.getPriorityLevel().getDisplayName());
        System.out.println(SEPARATOR);
    }

    @Override
    public void onTaskRemoved(Task task) {
        if (task == null) {
            Logger.warn("Received null task in onTaskRemoved notification");
            return;
        }
        
        Logger.info("Task removed notification: " + task.getDescription());
        
        System.out.println(SEPARATOR);
        System.out.println(SUCCESS_PREFIX + "Task Removed Successfully!");
        System.out.println("Removed: " + task.getDescription());
        System.out.println("Time slot freed: " + task.getFormattedStartTime() + " - " + task.getFormattedEndTime());
        System.out.println(SEPARATOR);
    }

    @Override
    public void onTaskCompletionChanged(Task task) {
        if (task == null) {
            Logger.warn("Received null task in onTaskCompletionChanged notification");
            return;
        }
        
        String status = task.isCompleted() ? "COMPLETED" : "PENDING";
        Logger.info("Task completion changed: " + task.getDescription() + " -> " + status);
        
        System.out.println(SEPARATOR);
        System.out.println(SUCCESS_PREFIX + "Task Status Updated!");
        System.out.println("Task: " + task.getDescription());
        System.out.println("New Status: " + status);
        
        if (task.isCompleted()) {
            System.out.println("Congratulations! Task completed successfully.");
        } else {
            System.out.println("Task marked as pending again.");
        }
        System.out.println(SEPARATOR);
    }

    @Override
    public void onTaskConflict(Task newTask, Task conflictingTask) {
        if (newTask == null || conflictingTask == null) {
            Logger.warn("Received null task in onTaskConflict notification");
            return;
        }
        
        Logger.warn("Task conflict detected: " + newTask.getDescription() + 
                   " conflicts with " + conflictingTask.getDescription());
        
        System.out.println(SEPARATOR);
        System.out.println(WARNING_PREFIX + "SCHEDULE CONFLICT DETECTED!");
        System.out.println();
        System.out.println("Attempted to add:");
        System.out.println("  " + newTask.getDescription() + 
                         " (" + newTask.getFormattedStartTime() + " - " + newTask.getFormattedEndTime() + ")");
        System.out.println();
        System.out.println("Conflicts with existing task:");
        System.out.println("  " + conflictingTask.getDescription() + 
                         " (" + conflictingTask.getFormattedStartTime() + " - " + conflictingTask.getFormattedEndTime() + ")");
        System.out.println();
        System.out.println("Suggestion: Choose a different time slot or modify the existing task.");
        System.out.println(SEPARATOR);
    }

    @Override
    public void onOperationError(String operation, String error) {
        if (operation == null || error == null) {
            Logger.warn("Received null parameters in onOperationError notification");
            return;
        }
        
        Logger.error("Operation error: " + operation + " - " + error);
        
        System.out.println(SEPARATOR);
        System.out.println(ERROR_PREFIX + "Operation Failed!");
        System.out.println("Operation: " + operation);
        System.out.println("Error: " + error);
        System.out.println("Please check your input and try again.");
        System.out.println(SEPARATOR);
    }

    @Override
    public void onProductivityAnalysisCompleted(String analysisResult) {
        if (analysisResult == null || analysisResult.trim().isEmpty()) {
            Logger.warn("Received empty analysis result in onProductivityAnalysisCompleted notification");
            return;
        }
        
        Logger.info("Productivity analysis completed");
        
        System.out.println(SEPARATOR);
        System.out.println(NOTIFICATION_PREFIX + "PRODUCTIVITY ANALYSIS COMPLETED!");
        System.out.println();
        System.out.println(analysisResult);
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a general notification message
     * @param message The message to display
     */
    public void displayNotification(String message) {
        if (message == null || message.trim().isEmpty()) {
            Logger.warn("Attempted to display empty notification");
            return;
        }
        
        Logger.info("General notification: " + message);
        
        System.out.println(SEPARATOR);
        System.out.println(NOTIFICATION_PREFIX + message);
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a success message
     * @param message The success message to display
     */
    public void displaySuccess(String message) {
        if (message == null || message.trim().isEmpty()) {
            Logger.warn("Attempted to display empty success message");
            return;
        }
        
        Logger.info("Success message: " + message);
        
        System.out.println(SEPARATOR);
        System.out.println(SUCCESS_PREFIX + message);
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a warning message
     * @param message The warning message to display
     */
    public void displayWarning(String message) {
        if (message == null || message.trim().isEmpty()) {
            Logger.warn("Attempted to display empty warning message");
            return;
        }
        
        Logger.warn("Warning message: " + message);
        
        System.out.println(SEPARATOR);
        System.out.println(WARNING_PREFIX + message);
        System.out.println(SEPARATOR);
    }
}
