package models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an assignment in the classroom system.
 * This class follows the Single Responsibility Principle by only
 * handling assignment-related data and behavior.
 */
public class Assignment {
    private final String id;
    private final String title;
    private final String description;
    private final String teacherId;
    private final LocalDateTime createdAt;
    private final LocalDateTime dueDate;
    private final int maxPoints;
    
    public Assignment(String id, String title, String description, 
                     String teacherId, LocalDateTime dueDate, int maxPoints) {
        this.id = Objects.requireNonNull(id, "Assignment ID cannot be null");
        this.title = Objects.requireNonNull(title, "Assignment title cannot be null");
        this.description = Objects.requireNonNull(description, "Assignment description cannot be null");
        this.teacherId = Objects.requireNonNull(teacherId, "Teacher ID cannot be null");
        this.dueDate = Objects.requireNonNull(dueDate, "Due date cannot be null");
        this.maxPoints = maxPoints;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTeacherId() { return teacherId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getDueDate() { return dueDate; }
    public int getMaxPoints() { return maxPoints; }
    
    /**
     * Checks if the assignment is past due
     * @return true if past due, false otherwise
     */
    public boolean isPastDue() {
        return LocalDateTime.now().isAfter(dueDate);
    }
    
    /**
     * Gets time remaining until due date in hours
     * @return hours remaining (negative if past due)
     */
    public long getHoursUntilDue() {
        return java.time.Duration.between(LocalDateTime.now(), dueDate).toHours();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Assignment that = (Assignment) obj;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Assignment{id='%s', title='%s', due=%s, maxPoints=%d}", 
                           id, title, dueDate, maxPoints);
    }
}
