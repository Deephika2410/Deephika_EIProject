package models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a student submission for an assignment.
 * Immutable class following the Value Object pattern.
 */
public class Submission {
    private final String id;
    private final String assignmentId;
    private final String assignmentTitle;
    private final String studentId;
    private final String content;
    private final LocalDateTime submittedAt;
    private final String filePath; // Optional file attachment path
    
    public Submission(String id, String assignmentId, String assignmentTitle,
                     String studentId, String content, String filePath) {
        this.id = Objects.requireNonNull(id, "Submission ID cannot be null");
        this.assignmentId = Objects.requireNonNull(assignmentId, "Assignment ID cannot be null");
        this.assignmentTitle = Objects.requireNonNull(assignmentTitle, "Assignment title cannot be null");
        this.studentId = Objects.requireNonNull(studentId, "Student ID cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.filePath = filePath; // Can be null
        this.submittedAt = LocalDateTime.now();
    }
    
    // Alternative constructor without file
    public Submission(String id, String assignmentId, String assignmentTitle,
                     String studentId, String content) {
        this(id, assignmentId, assignmentTitle, studentId, content, null);
    }
    
    // Getters
    public String getId() { return id; }
    public String getAssignmentId() { return assignmentId; }
    public String getAssignmentTitle() { return assignmentTitle; }
    public String getStudentId() { return studentId; }
    public String getContent() { return content; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public String getFilePath() { return filePath; }
    
    /**
     * Checks if submission has file attachment
     * @return true if has file, false otherwise
     */
    public boolean hasFileAttachment() {
        return filePath != null && !filePath.trim().isEmpty();
    }
    
    /**
     * Gets word count of the content
     * @return number of words in content
     */
    public int getWordCount() {
        return content.trim().split("\\s+").length;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Submission that = (Submission) obj;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Submission{id='%s', assignment='%s', student='%s', submittedAt=%s}", 
                           id, assignmentTitle, studentId, submittedAt);
    }
}
