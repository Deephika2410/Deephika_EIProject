package observers;

import models.Task;

/**
 * Observer interface for task-related notifications
 * Implements Observer Design Pattern
 * Follows SOLID principles with interface segregation
 */
public interface TaskObserver {
    
    /**
     * Called when a task is successfully added
     * @param task The added task
     */
    void onTaskAdded(Task task);
    
    /**
     * Called when a task is successfully updated
     * @param oldTask The original task
     * @param newTask The updated task
     */
    void onTaskUpdated(Task oldTask, Task newTask);
    
    /**
     * Called when a task is successfully removed
     * @param task The removed task
     */
    void onTaskRemoved(Task task);
    
    /**
     * Called when a task completion status changes
     * @param task The task with updated completion status
     */
    void onTaskCompletionChanged(Task task);
    
    /**
     * Called when a task conflict is detected
     * @param newTask The task causing the conflict
     * @param conflictingTask The existing conflicting task
     */
    void onTaskConflict(Task newTask, Task conflictingTask);
    
    /**
     * Called when a schedule operation fails
     * @param operation The operation that failed
     * @param error The error message
     */
    void onOperationError(String operation, String error);
    
    /**
     * Called when productivity analysis is completed
     * @param analysisResult The analysis result
     */
    void onProductivityAnalysisCompleted(String analysisResult);
}
