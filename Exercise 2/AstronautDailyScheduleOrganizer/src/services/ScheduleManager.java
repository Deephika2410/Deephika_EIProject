package services;

import models.Task;
import models.PriorityLevel;
import observers.TaskObserver;
import exceptions.TaskConflictException;
import exceptions.TaskNotFoundException;
import exceptions.TaskValidationException;
import factories.TaskFactory;
import utils.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.time.LocalTime;

/**
 * Singleton ScheduleManager for managing astronaut tasks
 * Implements Singleton Design Pattern with thread safety
 * Follows SOLID principles with single responsibility
 * Implements Observer pattern for notifications
 */
public class ScheduleManager {
    
    private static volatile ScheduleManager instance;
    private static final Object lock = new Object();
    
    // Thread-safe collections for concurrent access
    private final Map<String, Task> tasks;
    private final List<TaskObserver> observers;
    
    /**
     * Private constructor to prevent direct instantiation
     * Initializes thread-safe collections
     */
    private ScheduleManager() {
        this.tasks = new ConcurrentHashMap<>();
        this.observers = new CopyOnWriteArrayList<>();
        Logger.info("ScheduleManager instance created");
    }
    
    /**
     * Gets the singleton instance using double-checked locking
     * Thread-safe implementation
     * @return ScheduleManager singleton instance
     */
    public static ScheduleManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ScheduleManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Adds an observer for task notifications
     * @param observer TaskObserver to add
     */
    public void addObserver(TaskObserver observer) {
        if (observer == null) {
            Logger.warn("Attempted to add null observer");
            return;
        }
        
        observers.add(observer);
        Logger.info("Observer added to ScheduleManager");
    }
    
    /**
     * Removes an observer
     * @param observer TaskObserver to remove
     */
    public void removeObserver(TaskObserver observer) {
        if (observer == null) {
            Logger.warn("Attempted to remove null observer");
            return;
        }
        
        observers.remove(observer);
        Logger.info("Observer removed from ScheduleManager");
    }
    
    /**
     * Adds a new task to the schedule
     * @param description Task description
     * @param startTimeStr Start time string
     * @param endTimeStr End time string
     * @param priorityStr Priority level string
     * @throws TaskValidationException if task validation fails
     * @throws TaskConflictException if task conflicts with existing tasks
     */
    public void addTask(String description, String startTimeStr, String endTimeStr, String priorityStr) 
            throws TaskValidationException, TaskConflictException {
        
        Logger.logMethodEntry("ScheduleManager", "addTask", description, startTimeStr, endTimeStr, priorityStr);
        
        try {
            // Create validated task using factory
            Task newTask = TaskFactory.createTask(description, startTimeStr, endTimeStr, priorityStr);
            
            // Check for conflicts
            Task conflictingTask = findConflictingTask(newTask);
            if (conflictingTask != null) {
                // Notify observers of conflict
                notifyTaskConflict(newTask, conflictingTask);
                
                throw new TaskConflictException(
                    String.format("Task conflicts with existing task '%s' (%s - %s)", 
                                conflictingTask.getDescription(),
                                conflictingTask.getFormattedStartTime(),
                                conflictingTask.getFormattedEndTime()),
                    conflictingTask.getId(),
                    conflictingTask.getDescription()
                );
            }
            
            // Add task to schedule
            tasks.put(newTask.getId(), newTask);
            
            // Notify observers of successful addition
            notifyTaskAdded(newTask);
            
            Logger.info("Task added successfully: " + newTask.getDescription());
            Logger.logMethodExit("ScheduleManager", "addTask");
            
        } catch (TaskValidationException | TaskConflictException e) {
            notifyOperationError("Add Task", e.getMessage());
            throw e;
        } catch (Exception e) {
            String errorMsg = "Unexpected error while adding task: " + e.getMessage();
            Logger.error(errorMsg, e);
            notifyOperationError("Add Task", errorMsg);
            throw new TaskValidationException(errorMsg, e);
        }
    }
    
    /**
     * Removes a task from the schedule
     * @param taskId ID of the task to remove
     * @throws TaskNotFoundException if task is not found
     */
    public void removeTask(String taskId) throws TaskNotFoundException {
        Logger.logMethodEntry("ScheduleManager", "removeTask", taskId);
        
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                throw new TaskNotFoundException("Task ID cannot be null or empty", taskId);
            }
            
            Task removedTask = tasks.remove(taskId.trim());
            if (removedTask == null) {
                throw new TaskNotFoundException("Task not found with ID: " + taskId, taskId);
            }
            
            // Notify observers of successful removal
            notifyTaskRemoved(removedTask);
            
            Logger.info("Task removed successfully: " + removedTask.getDescription());
            Logger.logMethodExit("ScheduleManager", "removeTask");
            
        } catch (TaskNotFoundException e) {
            notifyOperationError("Remove Task", e.getMessage());
            throw e;
        } catch (Exception e) {
            String errorMsg = "Unexpected error while removing task: " + e.getMessage();
            Logger.error(errorMsg, e);
            notifyOperationError("Remove Task", errorMsg);
            throw new TaskNotFoundException(errorMsg, taskId);
        }
    }
    
    /**
     * Updates an existing task
     * @param taskId ID of the task to update
     * @param description New description
     * @param startTimeStr New start time
     * @param endTimeStr New end time
     * @param priorityStr New priority level
     * @throws TaskNotFoundException if task is not found
     * @throws TaskValidationException if validation fails
     * @throws TaskConflictException if updated task conflicts with others
     */
    public void updateTask(String taskId, String description, String startTimeStr, 
                          String endTimeStr, String priorityStr) 
            throws TaskNotFoundException, TaskValidationException, TaskConflictException {
        
        Logger.logMethodEntry("ScheduleManager", "updateTask", taskId, description, startTimeStr, endTimeStr, priorityStr);
        
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                throw new TaskNotFoundException("Task ID cannot be null or empty", taskId);
            }
            
            Task existingTask = tasks.get(taskId.trim());
            if (existingTask == null) {
                throw new TaskNotFoundException("Task not found with ID: " + taskId, taskId);
            }
            
            // Create updated task using factory
            Task updatedTask = TaskFactory.createTask(description, startTimeStr, endTimeStr, priorityStr);
            
            // Check for conflicts with other tasks (excluding the current task)
            Task conflictingTask = findConflictingTaskExcluding(updatedTask, taskId);
            if (conflictingTask != null) {
                notifyTaskConflict(updatedTask, conflictingTask);
                
                throw new TaskConflictException(
                    String.format("Updated task conflicts with existing task '%s' (%s - %s)", 
                                conflictingTask.getDescription(),
                                conflictingTask.getFormattedStartTime(),
                                conflictingTask.getFormattedEndTime()),
                    conflictingTask.getId(),
                    conflictingTask.getDescription()
                );
            }
            
            // Update the task (create new instance with same ID and updated properties)
            Task finalUpdatedTask = existingTask.withUpdatedProperties(
                updatedTask.getDescription(),
                updatedTask.getStartTime(),
                updatedTask.getEndTime(),
                updatedTask.getPriorityLevel()
            );
            
            tasks.put(taskId.trim(), finalUpdatedTask);
            
            // Notify observers of successful update
            notifyTaskUpdated(existingTask, finalUpdatedTask);
            
            Logger.info("Task updated successfully: " + finalUpdatedTask.getDescription());
            Logger.logMethodExit("ScheduleManager", "updateTask");
            
        } catch (TaskNotFoundException | TaskValidationException | TaskConflictException e) {
            notifyOperationError("Update Task", e.getMessage());
            throw e;
        } catch (Exception e) {
            String errorMsg = "Unexpected error while updating task: " + e.getMessage();
            Logger.error(errorMsg, e);
            notifyOperationError("Update Task", errorMsg);
            throw new TaskValidationException(errorMsg, e);
        }
    }
    
    /**
     * Marks a task as completed or pending
     * @param taskId ID of the task to update
     * @param completed New completion status
     * @throws TaskNotFoundException if task is not found
     */
    public void markTaskCompletion(String taskId, boolean completed) throws TaskNotFoundException {
        Logger.logMethodEntry("ScheduleManager", "markTaskCompletion", taskId, completed);
        
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                throw new TaskNotFoundException("Task ID cannot be null or empty", taskId);
            }
            
            Task existingTask = tasks.get(taskId.trim());
            if (existingTask == null) {
                throw new TaskNotFoundException("Task not found with ID: " + taskId, taskId);
            }
            
            // Create updated task with new completion status
            Task updatedTask = existingTask.withCompletionStatus(completed);
            tasks.put(taskId.trim(), updatedTask);
            
            // Notify observers of completion status change
            notifyTaskCompletionChanged(updatedTask);
            
            String status = completed ? "completed" : "pending";
            Logger.info("Task marked as " + status + ": " + updatedTask.getDescription());
            Logger.logMethodExit("ScheduleManager", "markTaskCompletion");
            
        } catch (TaskNotFoundException e) {
            notifyOperationError("Mark Task Completion", e.getMessage());
            throw e;
        } catch (Exception e) {
            String errorMsg = "Unexpected error while marking task completion: " + e.getMessage();
            Logger.error(errorMsg, e);
            notifyOperationError("Mark Task Completion", errorMsg);
            throw new TaskNotFoundException(errorMsg, taskId);
        }
    }
    
    /**
     * Gets all tasks sorted by start time
     * @return List of tasks sorted by start time
     */
    public List<Task> getAllTasksSortedByTime() {
        Logger.logMethodEntry("ScheduleManager", "getAllTasksSortedByTime");
        
        List<Task> sortedTasks = tasks.values().stream()
            .sorted(Comparator.comparing(Task::getStartTime))
            .collect(Collectors.toList());
        
        Logger.info("Retrieved " + sortedTasks.size() + " tasks sorted by time");
        Logger.logMethodExit("ScheduleManager", "getAllTasksSortedByTime");
        
        return sortedTasks;
    }
    
    /**
     * Gets tasks filtered by priority level
     * @param priorityLevel Priority level to filter by
     * @return List of tasks with specified priority level
     */
    public List<Task> getTasksByPriority(PriorityLevel priorityLevel) {
        Logger.logMethodEntry("ScheduleManager", "getTasksByPriority", priorityLevel);
        
        if (priorityLevel == null) {
            Logger.warn("Null priority level provided");
            return new ArrayList<>();
        }
        
        List<Task> filteredTasks = tasks.values().stream()
            .filter(task -> task.getPriorityLevel() == priorityLevel)
            .sorted(Comparator.comparing(Task::getStartTime))
            .collect(Collectors.toList());
        
        Logger.info("Retrieved " + filteredTasks.size() + " tasks with priority " + priorityLevel);
        Logger.logMethodExit("ScheduleManager", "getTasksByPriority");
        
        return filteredTasks;
    }
    
    /**
     * Gets a task by its ID
     * @param taskId Task ID to search for
     * @return Task if found, null otherwise
     */
    public Task getTaskById(String taskId) {
        Logger.logMethodEntry("ScheduleManager", "getTaskById", taskId);
        
        if (taskId == null || taskId.trim().isEmpty()) {
            Logger.warn("Null or empty task ID provided");
            return null;
        }
        
        Task task = tasks.get(taskId.trim());
        
        if (task != null) {
            Logger.debug("Task found: " + task.getDescription());
        } else {
            Logger.debug("Task not found with ID: " + taskId);
        }
        
        Logger.logMethodExit("ScheduleManager", "getTaskById");
        return task;
    }
    
    /**
     * Gets the total number of tasks
     * @return Number of tasks in the schedule
     */
    public int getTaskCount() {
        return tasks.size();
    }
    
    /**
     * Clears all tasks from the schedule
     */
    public void clearAllTasks() {
        Logger.logMethodEntry("ScheduleManager", "clearAllTasks");
        
        int removedCount = tasks.size();
        tasks.clear();
        
        Logger.info("Cleared all tasks from schedule (" + removedCount + " tasks removed)");
        Logger.logMethodExit("ScheduleManager", "clearAllTasks");
    }
    
    // Private helper methods
    
    /**
     * Finds a task that conflicts with the given task
     * @param newTask Task to check for conflicts
     * @return Conflicting task if found, null otherwise
     */
    private Task findConflictingTask(Task newTask) {
        return tasks.values().stream()
            .filter(existingTask -> existingTask.overlapsWith(newTask))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Finds a task that conflicts with the given task, excluding a specific task ID
     * @param newTask Task to check for conflicts
     * @param excludeTaskId Task ID to exclude from conflict check
     * @return Conflicting task if found, null otherwise
     */
    private Task findConflictingTaskExcluding(Task newTask, String excludeTaskId) {
        return tasks.values().stream()
            .filter(existingTask -> !existingTask.getId().equals(excludeTaskId))
            .filter(existingTask -> existingTask.overlapsWith(newTask))
            .findFirst()
            .orElse(null);
    }
    
    // Observer notification methods
    
    private void notifyTaskAdded(Task task) {
        observers.forEach(observer -> {
            try {
                observer.onTaskAdded(task);
            } catch (Exception e) {
                Logger.error("Error notifying observer of task addition", e);
            }
        });
    }
    
    private void notifyTaskUpdated(Task oldTask, Task newTask) {
        observers.forEach(observer -> {
            try {
                observer.onTaskUpdated(oldTask, newTask);
            } catch (Exception e) {
                Logger.error("Error notifying observer of task update", e);
            }
        });
    }
    
    private void notifyTaskRemoved(Task task) {
        observers.forEach(observer -> {
            try {
                observer.onTaskRemoved(task);
            } catch (Exception e) {
                Logger.error("Error notifying observer of task removal", e);
            }
        });
    }
    
    private void notifyTaskCompletionChanged(Task task) {
        observers.forEach(observer -> {
            try {
                observer.onTaskCompletionChanged(task);
            } catch (Exception e) {
                Logger.error("Error notifying observer of task completion change", e);
            }
        });
    }
    
    private void notifyTaskConflict(Task newTask, Task conflictingTask) {
        observers.forEach(observer -> {
            try {
                observer.onTaskConflict(newTask, conflictingTask);
            } catch (Exception e) {
                Logger.error("Error notifying observer of task conflict", e);
            }
        });
    }
    
    private void notifyOperationError(String operation, String error) {
        observers.forEach(observer -> {
            try {
                observer.onOperationError(operation, error);
            } catch (Exception e) {
                Logger.error("Error notifying observer of operation error", e);
            }
        });
    }
    
    public void notifyProductivityAnalysis(String analysisResult) {
        observers.forEach(observer -> {
            try {
                observer.onProductivityAnalysisCompleted(analysisResult);
            } catch (Exception e) {
                Logger.error("Error notifying observer of productivity analysis", e);
            }
        });
    }
}
