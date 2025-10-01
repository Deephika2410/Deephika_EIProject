package ui;

import models.Task;
import models.PriorityLevel;
import services.ScheduleManager;
import observers.ConsoleTaskObserver;
import analyzers.ProductivityAnalysisContext;
import exporters.ScheduleExporterFactory;
import exceptions.*;
import utils.Logger;
import utils.ValidationUtils;

import java.util.List;
import java.util.Scanner;
import java.io.IOException;

/**
 * Console-based user interface for the Astronaut Daily Schedule Organizer
 * Implements event-driven menu system without infinite loops
 * Follows SOLID principles with single responsibility
 */
public class ConsoleInterface {
    
    private final ScheduleManager scheduleManager;
    private final ConsoleTaskObserver observer;
    private final ProductivityAnalysisContext analysisContext;
    private final Scanner scanner;
    private boolean running;
    
    /**
     * Constructor initializes the console interface
     */
    public ConsoleInterface() {
        this.scheduleManager = ScheduleManager.getInstance();
        this.observer = new ConsoleTaskObserver();
        this.analysisContext = new ProductivityAnalysisContext();
        this.scanner = new Scanner(System.in);
        this.running = true;
        
        // Register observer with schedule manager
        scheduleManager.addObserver(observer);
        
        Logger.info("ConsoleInterface initialized");
    }
    
    /**
     * Starts the application main loop
     */
    public void start() {
        Logger.info("Starting Astronaut Daily Schedule Organizer");
        
        displayWelcomeMessage();
        
        while (running) {
            try {
                displayMainMenu();
                handleUserChoice();
            } catch (Exception e) {
                Logger.error("Unexpected error in main loop: " + e.getMessage(), e);
                observer.displayWarning("An unexpected error occurred: " + e.getMessage());
                // Continue running instead of crashing
            }
        }
        
        shutdown();
    }
    
    /**
     * Displays welcome message
     */
    private void displayWelcomeMessage() {
        System.out.println("=".repeat(80));
        System.out.println("     ASTRONAUT DAILY SCHEDULE ORGANIZER");
        System.out.println("=".repeat(80));
        System.out.println("Welcome to your personal task management system!");
        System.out.println("Organize your daily schedule with SOLID principles and design patterns.");
        System.out.println("=".repeat(80));
        System.out.println();
    }
    
    /**
     * Displays the main menu
     */
    private void displayMainMenu() {
        System.out.println("MAIN MENU:");
        System.out.println("1. Add Task");
        System.out.println("2. Remove Task");
        System.out.println("3. Edit Task");
        System.out.println("4. View All Tasks");
        System.out.println("5. View Tasks by Priority");
        System.out.println("6. Mark Task Completed/Pending");
        System.out.println("7. Run Productivity Analyzer");
        System.out.println("8. Export Schedule");
        System.out.println("9. Clear All Tasks");
        System.out.println("0. Exit");
        System.out.println("-".repeat(40));
        System.out.print("Enter your choice (0-9): ");
    }
    
    /**
     * Handles user menu choice
     */
    private void handleUserChoice() {
        try {
            String input = scanner.nextLine();
            int choice = ValidationUtils.validateMenuChoice(input, 0, 9);
            
            switch (choice) {
                case 1:
                    handleAddTask();
                    break;
                case 2:
                    handleRemoveTask();
                    break;
                case 3:
                    handleEditTask();
                    break;
                case 4:
                    handleViewAllTasks();
                    break;
                case 5:
                    handleViewTasksByPriority();
                    break;
                case 6:
                    handleMarkTaskCompletion();
                    break;
                case 7:
                    handleProductivityAnalysis();
                    break;
                case 8:
                    handleExportSchedule();
                    break;
                case 9:
                    handleClearAllTasks();
                    break;
                case 0:
                    handleExit();
                    break;
                default:
                    observer.displayWarning("Invalid choice. Please try again.");
            }
            
            if (running && choice != 0) {
                pauseForUser();
            }
            
        } catch (IllegalArgumentException e) {
            observer.displayWarning("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            Logger.error("Error handling user choice: " + e.getMessage(), e);
            observer.displayWarning("An error occurred: " + e.getMessage());
        }
    }
    
    /**
     * Handles adding a new task
     */
    private void handleAddTask() {
        Logger.logMethodEntry("ConsoleInterface", "handleAddTask");
        
        try {
            System.out.println("\n=== ADD NEW TASK ===");
            
            System.out.print("Enter task description: ");
            String description = scanner.nextLine();
            
            System.out.print("Enter start time (HH:mm): ");
            String startTime = scanner.nextLine();
            
            System.out.print("Enter end time (HH:mm): ");
            String endTime = scanner.nextLine();
            
            System.out.print("Enter priority level (LOW/MEDIUM/HIGH/CRITICAL): ");
            String priority = scanner.nextLine();
            
            scheduleManager.addTask(description, startTime, endTime, priority);
            
        } catch (TaskValidationException e) {
            observer.displayWarning("Task validation failed: " + e.getMessage());
        } catch (TaskConflictException e) {
            observer.displayWarning("Task conflict: " + e.getMessage());
            System.out.println("Conflicting task details: " + e.getConflictDetails());
        } catch (Exception e) {
            Logger.error("Error adding task: " + e.getMessage(), e);
            observer.displayWarning("Failed to add task: " + e.getMessage());
        }
        
        Logger.logMethodExit("ConsoleInterface", "handleAddTask");
    }
    
    /**
     * Handles removing a task
     */
    private void handleRemoveTask() {
        Logger.logMethodEntry("ConsoleInterface", "handleRemoveTask");
        
        try {
            List<Task> tasks = scheduleManager.getAllTasksSortedByTime();
            
            if (tasks.isEmpty()) {
                observer.displayWarning("No tasks available to remove.");
                return;
            }
            
            System.out.println("\n=== REMOVE TASK ===");
            displayTaskList(tasks);
            
            System.out.print("Enter the number of the task to remove (1-" + tasks.size() + "): ");
            String input = scanner.nextLine();
            
            int taskIndex = ValidationUtils.validateMenuChoice(input, 1, tasks.size()) - 1;
            Task taskToRemove = tasks.get(taskIndex);
            
            scheduleManager.removeTask(taskToRemove.getId());
            
        } catch (TaskNotFoundException e) {
            observer.displayWarning("Task not found: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            observer.displayWarning("Invalid selection: " + e.getMessage());
        } catch (Exception e) {
            Logger.error("Error removing task: " + e.getMessage(), e);
            observer.displayWarning("Failed to remove task: " + e.getMessage());
        }
        
        Logger.logMethodExit("ConsoleInterface", "handleRemoveTask");
    }
    
    /**
     * Handles editing a task
     */
    private void handleEditTask() {
        Logger.logMethodEntry("ConsoleInterface", "handleEditTask");
        
        try {
            List<Task> tasks = scheduleManager.getAllTasksSortedByTime();
            
            if (tasks.isEmpty()) {
                observer.displayWarning("No tasks available to edit.");
                return;
            }
            
            System.out.println("\n=== EDIT TASK ===");
            displayTaskList(tasks);
            
            System.out.print("Enter the number of the task to edit (1-" + tasks.size() + "): ");
            String input = scanner.nextLine();
            
            int taskIndex = ValidationUtils.validateMenuChoice(input, 1, tasks.size()) - 1;
            Task taskToEdit = tasks.get(taskIndex);
            
            System.out.println("Current task details:");
            System.out.println(taskToEdit);
            System.out.println();
            
            System.out.print("Enter new description (or press Enter to keep current): ");
            String description = scanner.nextLine();
            if (description.trim().isEmpty()) {
                description = taskToEdit.getDescription();
            }
            
            System.out.print("Enter new start time (HH:mm) (or press Enter to keep current): ");
            String startTime = scanner.nextLine();
            if (startTime.trim().isEmpty()) {
                startTime = taskToEdit.getFormattedStartTime();
            }
            
            System.out.print("Enter new end time (HH:mm) (or press Enter to keep current): ");
            String endTime = scanner.nextLine();
            if (endTime.trim().isEmpty()) {
                endTime = taskToEdit.getFormattedEndTime();
            }
            
            System.out.print("Enter new priority (LOW/MEDIUM/HIGH/CRITICAL) (or press Enter to keep current): ");
            String priority = scanner.nextLine();
            if (priority.trim().isEmpty()) {
                priority = taskToEdit.getPriorityLevel().name();
            }
            
            scheduleManager.updateTask(taskToEdit.getId(), description, startTime, endTime, priority);
            
        } catch (TaskNotFoundException e) {
            observer.displayWarning("Task not found: " + e.getMessage());
        } catch (TaskValidationException e) {
            observer.displayWarning("Task validation failed: " + e.getMessage());
        } catch (TaskConflictException e) {
            observer.displayWarning("Task conflict: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            observer.displayWarning("Invalid selection: " + e.getMessage());
        } catch (Exception e) {
            Logger.error("Error editing task: " + e.getMessage(), e);
            observer.displayWarning("Failed to edit task: " + e.getMessage());
        }
        
        Logger.logMethodExit("ConsoleInterface", "handleEditTask");
    }
    
    /**
     * Handles viewing all tasks
     */
    private void handleViewAllTasks() {
        Logger.logMethodEntry("ConsoleInterface", "handleViewAllTasks");
        
        try {
            List<Task> tasks = scheduleManager.getAllTasksSortedByTime();
            
            System.out.println("\n=== ALL TASKS (Sorted by Start Time) ===");
            
            if (tasks.isEmpty()) {
                System.out.println("No tasks scheduled.");
            } else {
                System.out.println("Total tasks: " + tasks.size());
                System.out.println("-".repeat(80));
                
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
            }
            
        } catch (Exception e) {
            Logger.error("Error viewing tasks: " + e.getMessage(), e);
            observer.displayWarning("Failed to retrieve tasks: " + e.getMessage());
        }
        
        Logger.logMethodExit("ConsoleInterface", "handleViewAllTasks");
    }
    
    /**
     * Handles viewing tasks by priority
     */
    private void handleViewTasksByPriority() {
        Logger.logMethodEntry("ConsoleInterface", "handleViewTasksByPriority");
        
        try {
            System.out.println("\n=== VIEW TASKS BY PRIORITY ===");
            System.out.println("Select priority level:");
            System.out.println("1. CRITICAL");
            System.out.println("2. HIGH");
            System.out.println("3. MEDIUM");
            System.out.println("4. LOW");
            System.out.print("Enter your choice (1-4): ");
            
            String input = scanner.nextLine();
            int choice = ValidationUtils.validateMenuChoice(input, 1, 4);
            
            PriorityLevel selectedPriority;
            switch (choice) {
                case 1:
                    selectedPriority = PriorityLevel.CRITICAL;
                    break;
                case 2:
                    selectedPriority = PriorityLevel.HIGH;
                    break;
                case 3:
                    selectedPriority = PriorityLevel.MEDIUM;
                    break;
                case 4:
                    selectedPriority = PriorityLevel.LOW;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid priority choice");
            }
            
            List<Task> tasks = scheduleManager.getTasksByPriority(selectedPriority);
            
            System.out.println("\n=== " + selectedPriority.getDisplayName().toUpperCase() + " PRIORITY TASKS ===");
            
            if (tasks.isEmpty()) {
                System.out.println("No tasks found with " + selectedPriority.getDisplayName() + " priority.");
            } else {
                System.out.println("Found " + tasks.size() + " task(s):");
                System.out.println("-".repeat(80));
                
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
            }
            
        } catch (IllegalArgumentException e) {
            observer.displayWarning("Invalid selection: " + e.getMessage());
        } catch (Exception e) {
            Logger.error("Error viewing tasks by priority: " + e.getMessage(), e);
            observer.displayWarning("Failed to retrieve tasks by priority: " + e.getMessage());
        }
        
        Logger.logMethodExit("ConsoleInterface", "handleViewTasksByPriority");
    }
    
    /**
     * Handles marking task completion status
     */
    private void handleMarkTaskCompletion() {
        Logger.logMethodEntry("ConsoleInterface", "handleMarkTaskCompletion");
        
        try {
            List<Task> tasks = scheduleManager.getAllTasksSortedByTime();
            
            if (tasks.isEmpty()) {
                observer.displayWarning("No tasks available to mark completion.");
                return;
            }
            
            System.out.println("\n=== MARK TASK COMPLETION ===");
            displayTaskList(tasks);
            
            System.out.print("Enter the number of the task to update (1-" + tasks.size() + "): ");
            String input = scanner.nextLine();
            
            int taskIndex = ValidationUtils.validateMenuChoice(input, 1, tasks.size()) - 1;
            Task selectedTask = tasks.get(taskIndex);
            
            String currentStatus = selectedTask.isCompleted() ? "COMPLETED" : "PENDING";
            String newStatus = selectedTask.isCompleted() ? "PENDING" : "COMPLETED";
            
            System.out.println("Current status: " + currentStatus);
            System.out.print("Mark as " + newStatus + "? (y/n): ");
            
            String confirmation = scanner.nextLine();
            if (confirmation.trim().toLowerCase().startsWith("y")) {
                scheduleManager.markTaskCompletion(selectedTask.getId(), !selectedTask.isCompleted());
            } else {
                System.out.println("Operation cancelled.");
            }
            
        } catch (TaskNotFoundException e) {
            observer.displayWarning("Task not found: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            observer.displayWarning("Invalid selection: " + e.getMessage());
        } catch (Exception e) {
            Logger.error("Error marking task completion: " + e.getMessage(), e);
            observer.displayWarning("Failed to update task completion: " + e.getMessage());
        }
        
        Logger.logMethodExit("ConsoleInterface", "handleMarkTaskCompletion");
    }
    
    /**
     * Handles productivity analysis
     */
    private void handleProductivityAnalysis() {
        Logger.logMethodEntry("ConsoleInterface", "handleProductivityAnalysis");
        
        try {
            List<Task> tasks = scheduleManager.getAllTasksSortedByTime();
            
            System.out.println("\n=== PRODUCTIVITY ANALYZER ===");
            
            if (tasks.isEmpty()) {
                observer.displayWarning("No tasks available for analysis.");
                return;
            }
            
            System.out.println("Running productivity analysis on " + tasks.size() + " tasks...");
            System.out.println();
            
            String analysisResult = analysisContext.performAnalysis(tasks);
            scheduleManager.notifyProductivityAnalysis(analysisResult);
            
        } catch (Exception e) {
            Logger.error("Error during productivity analysis: " + e.getMessage(), e);
            observer.displayWarning("Failed to perform productivity analysis: " + e.getMessage());
        }
        
        Logger.logMethodExit("ConsoleInterface", "handleProductivityAnalysis");
    }
    
    /**
     * Handles schedule export
     */
    private void handleExportSchedule() {
        Logger.logMethodEntry("ConsoleInterface", "handleExportSchedule");
        
        try {
            List<Task> tasks = scheduleManager.getAllTasksSortedByTime();
            
            if (tasks.isEmpty()) {
                observer.displayWarning("No tasks available to export.");
                return;
            }
            
            System.out.println("\n=== EXPORT SCHEDULE ===");
            System.out.println(ScheduleExporterFactory.getAvailableFormatsString());
            
            System.out.print("Select export format (1-" + ScheduleExporterFactory.getAvailableFormats().length + "): ");
            String formatInput = scanner.nextLine();
            
            int formatChoice = ValidationUtils.validateMenuChoice(formatInput, 1, ScheduleExporterFactory.getAvailableFormats().length);
            ScheduleExporterFactory.ExportFormat selectedFormat = ScheduleExporterFactory.getAvailableFormats()[formatChoice - 1];
            
            System.out.print("Enter filename (without extension): ");
            String fileName = scanner.nextLine();
            
            if (fileName.trim().isEmpty()) {
                fileName = "astronaut_schedule_" + java.time.LocalDate.now().toString();
            }
            
            ScheduleExporterFactory.exportSchedule(tasks, fileName, selectedFormat);
            
            observer.displaySuccess("Schedule exported successfully to: " + fileName + "." + selectedFormat.getExtension());
            
        } catch (IOException e) {
            Logger.error("Error exporting schedule: " + e.getMessage(), e);
            observer.displayWarning("Failed to export schedule: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            observer.displayWarning("Invalid selection: " + e.getMessage());
        } catch (Exception e) {
            Logger.error("Unexpected error during export: " + e.getMessage(), e);
            observer.displayWarning("Failed to export schedule: " + e.getMessage());
        }
        
        Logger.logMethodExit("ConsoleInterface", "handleExportSchedule");
    }
    
    /**
     * Handles clearing all tasks
     */
    private void handleClearAllTasks() {
        Logger.logMethodEntry("ConsoleInterface", "handleClearAllTasks");
        
        try {
            int taskCount = scheduleManager.getTaskCount();
            
            if (taskCount == 0) {
                observer.displayWarning("No tasks to clear.");
                return;
            }
            
            System.out.println("\n=== CLEAR ALL TASKS ===");
            System.out.println("WARNING: This will remove all " + taskCount + " tasks from your schedule.");
            System.out.print("Are you sure you want to continue? (type 'YES' to confirm): ");
            
            String confirmation = scanner.nextLine();
            
            if ("YES".equals(confirmation.trim())) {
                scheduleManager.clearAllTasks();
                observer.displaySuccess("All tasks have been cleared from the schedule.");
            } else {
                System.out.println("Operation cancelled.");
            }
            
        } catch (Exception e) {
            Logger.error("Error clearing tasks: " + e.getMessage(), e);
            observer.displayWarning("Failed to clear tasks: " + e.getMessage());
        }
        
        Logger.logMethodExit("ConsoleInterface", "handleClearAllTasks");
    }
    
    /**
     * Handles application exit
     */
    private void handleExit() {
        Logger.logMethodEntry("ConsoleInterface", "handleExit");
        
        System.out.println("\n=== EXIT ===");
        System.out.println("Thank you for using Astronaut Daily Schedule Organizer!");
        
        int taskCount = scheduleManager.getTaskCount();
        if (taskCount > 0) {
            System.out.println("You have " + taskCount + " task(s) in your schedule.");
            System.out.print("Would you like to export your schedule before exiting? (y/n): ");
            
            String exportChoice = scanner.nextLine();
            if (exportChoice.trim().toLowerCase().startsWith("y")) {
                try {
                    String fileName = "astronaut_schedule_backup_" + java.time.LocalDate.now().toString();
                    ScheduleExporterFactory.exportSchedule(
                        scheduleManager.getAllTasksSortedByTime(), 
                        fileName, 
                        ScheduleExporterFactory.ExportFormat.TEXT
                    );
                    System.out.println("Schedule exported to: " + fileName + ".txt");
                } catch (Exception e) {
                    Logger.error("Error during exit export: " + e.getMessage(), e);
                    System.out.println("Failed to export schedule: " + e.getMessage());
                }
            }
        }
        
        running = false;
        Logger.info("Application exit requested by user");
        Logger.logMethodExit("ConsoleInterface", "handleExit");
    }
    
    /**
     * Displays a numbered list of tasks
     */
    private void displayTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        
        System.out.println("Available tasks:");
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println("-".repeat(80));
    }
    
    /**
     * Pauses for user to read output
     */
    private void pauseForUser() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
        System.out.println();
    }
    
    /**
     * Cleanup method called on application shutdown
     */
    private void shutdown() {
        Logger.info("Shutting down Astronaut Daily Schedule Organizer");
        
        try {
            // Remove observer
            scheduleManager.removeObserver(observer);
            
            // Close scanner
            scanner.close();
            
            System.out.println("Goodbye! Stay organized and reach for the stars!");
            
        } catch (Exception e) {
            Logger.error("Error during shutdown: " + e.getMessage(), e);
        }
        
        Logger.info("Application shutdown complete");
    }
}
