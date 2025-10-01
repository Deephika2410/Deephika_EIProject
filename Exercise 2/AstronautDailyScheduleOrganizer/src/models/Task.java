package models;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

/**
 * Task model representing a scheduled activity for astronauts
 * Implements immutable design pattern for thread safety
 * Follows SOLID principles with single responsibility
 */
public class Task {
    private final String id;
    private final String description;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final PriorityLevel priorityLevel;
    private final boolean completed;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructor for Task - public to allow factory usage
     * @param description Task description
     * @param startTime Start time of the task
     * @param endTime End time of the task
     * @param priorityLevel Priority level of the task
     * @param completed Whether the task is completed
     */
    public Task(String description, LocalTime startTime, LocalTime endTime, 
         PriorityLevel priorityLevel, boolean completed) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priorityLevel = priorityLevel;
        this.completed = completed;
    }

    /**
     * Copy constructor for creating modified versions
     * @param original Original task to copy from
     * @param description New description
     * @param startTime New start time
     * @param endTime New end time
     * @param priorityLevel New priority level
     * @param completed New completion status
     */
    public Task(Task original, String description, LocalTime startTime, LocalTime endTime, 
         PriorityLevel priorityLevel, boolean completed) {
        this.id = original.id; // Keep same ID for updates
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priorityLevel = priorityLevel;
        this.completed = completed;
    }

    // Getters following JavaBean conventions
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public boolean isCompleted() {
        return completed;
    }

    /**
     * Gets formatted start time as string
     * @return Formatted start time (HH:mm)
     */
    public String getFormattedStartTime() {
        return startTime.format(timeFormatter);
    }

    /**
     * Gets formatted end time as string
     * @return Formatted end time (HH:mm)
     */
    public String getFormattedEndTime() {
        return endTime.format(timeFormatter);
    }

    /**
     * Gets duration of the task in minutes
     * @return Duration in minutes
     */
    public long getDurationMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    /**
     * Checks if this task overlaps with another task
     * @param other Other task to check overlap with
     * @return true if tasks overlap, false otherwise
     */
    public boolean overlapsWith(Task other) {
        if (other == null) {
            return false;
        }
        
        // Tasks overlap if one starts before the other ends and vice versa
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }

    /**
     * Creates a new task with updated completion status
     * @param completed New completion status
     * @return New Task instance with updated status
     */
    public Task withCompletionStatus(boolean completed) {
        return new Task(this, this.description, this.startTime, this.endTime, 
                       this.priorityLevel, completed);
    }

    /**
     * Creates a new task with updated properties
     * @param description New description
     * @param startTime New start time
     * @param endTime New end time
     * @param priorityLevel New priority level
     * @return New Task instance with updated properties
     */
    public Task withUpdatedProperties(String description, LocalTime startTime, 
                                    LocalTime endTime, PriorityLevel priorityLevel) {
        return new Task(this, description, startTime, endTime, priorityLevel, this.completed);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String status = completed ? "[COMPLETED]" : "[PENDING]";
        return String.format("%s %s (%s - %s) [%s] %s",
                status,
                description,
                getFormattedStartTime(),
                getFormattedEndTime(),
                priorityLevel.getDisplayName(),
                "Duration: " + getDurationMinutes() + " min");
    }

    /**
     * Returns a detailed string representation for logging
     * @return Detailed string representation
     */
    public String toDetailedString() {
        return String.format("Task{id='%s', description='%s', startTime=%s, endTime=%s, " +
                           "priorityLevel=%s, completed=%s, duration=%d min}",
                id, description, startTime, endTime, priorityLevel, completed, getDurationMinutes());
    }
}
