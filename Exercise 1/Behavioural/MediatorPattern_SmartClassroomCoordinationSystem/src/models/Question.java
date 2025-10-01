package models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a question asked by a student in the classroom.
 * Immutable class following clean code principles.
 */
public class Question {
    private final String id;
    private final String studentId;
    private final String content;
    private final String subject;
    private final QuestionPriority priority;
    private final LocalDateTime askedAt;
    private final boolean isAnonymous;
    
    public enum QuestionPriority {
        LOW, MEDIUM, HIGH, URGENT
    }
    
    public Question(String id, String studentId, String content, String subject, 
                   QuestionPriority priority, boolean isAnonymous) {
        this.id = Objects.requireNonNull(id, "Question ID cannot be null");
        this.studentId = Objects.requireNonNull(studentId, "Student ID cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.subject = Objects.requireNonNull(subject, "Subject cannot be null");
        this.priority = Objects.requireNonNull(priority, "Priority cannot be null");
        this.isAnonymous = isAnonymous;
        this.askedAt = LocalDateTime.now();
    }
    
    // Constructor with default priority
    public Question(String id, String studentId, String content, String subject) {
        this(id, studentId, content, subject, QuestionPriority.MEDIUM, false);
    }
    
    // Getters
    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getContent() { return content; }
    public String getSubject() { return subject; }
    public QuestionPriority getPriority() { return priority; }
    public LocalDateTime getAskedAt() { return askedAt; }
    public boolean isAnonymous() { return isAnonymous; }
    
    /**
     * Gets display name for the student (anonymous or actual)
     * @param actualName The actual student name
     * @return Display name
     */
    public String getDisplayStudentName(String actualName) {
        return isAnonymous ? "Anonymous Student" : actualName;
    }
    
    /**
     * Checks if question is urgent based on priority
     * @return true if urgent, false otherwise
     */
    public boolean isUrgent() {
        return priority == QuestionPriority.URGENT;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Question that = (Question) obj;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Question{id='%s', subject='%s', priority=%s, askedAt=%s}", 
                           id, subject, priority, askedAt);
    }
}
