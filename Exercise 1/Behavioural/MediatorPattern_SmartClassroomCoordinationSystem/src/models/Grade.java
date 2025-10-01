package models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a grade for a student submission.
 * Immutable class with validation logic.
 */
public class Grade {
    private final String id;
    private final String submissionId;
    private final String studentId;
    private final String assignmentId;
    private final int score;
    private final int maxScore;
    private final String feedback;
    private final String graderId;
    private final LocalDateTime gradedAt;
    private final GradeLevel gradeLevel;
    
    public enum GradeLevel {
        A_PLUS(95, "A+"), A(90, "A"), A_MINUS(85, "A-"),
        B_PLUS(82, "B+"), B(78, "B"), B_MINUS(75, "B-"),
        C_PLUS(72, "C+"), C(68, "C"), C_MINUS(65, "C-"),
        D(60, "D"), F(0, "F");
        
        private final int minScore;
        private final String displayName;
        
        GradeLevel(int minScore, String displayName) {
            this.minScore = minScore;
            this.displayName = displayName;
        }
        
        public int getMinScore() { return minScore; }
        public String getDisplayName() { return displayName; }
        
        public static GradeLevel fromScore(double percentage) {
            for (GradeLevel level : values()) {
                if (percentage >= level.minScore) {
                    return level;
                }
            }
            return F;
        }
    }
    
    public Grade(String id, String submissionId, String studentId, String assignmentId,
                int score, int maxScore, String feedback, String graderId) {
        this.id = Objects.requireNonNull(id, "Grade ID cannot be null");
        this.submissionId = Objects.requireNonNull(submissionId, "Submission ID cannot be null");
        this.studentId = Objects.requireNonNull(studentId, "Student ID cannot be null");
        this.assignmentId = Objects.requireNonNull(assignmentId, "Assignment ID cannot be null");
        this.graderId = Objects.requireNonNull(graderId, "Grader ID cannot be null");
        
        if (score < 0 || maxScore <= 0 || score > maxScore) {
            throw new IllegalArgumentException("Invalid score values");
        }
        
        this.score = score;
        this.maxScore = maxScore;
        this.feedback = feedback != null ? feedback : "";
        this.gradedAt = LocalDateTime.now();
        this.gradeLevel = GradeLevel.fromScore(getPercentage());
    }
    
    // Getters
    public String getId() { return id; }
    public String getSubmissionId() { return submissionId; }
    public String getStudentId() { return studentId; }
    public String getAssignmentId() { return assignmentId; }
    public int getScore() { return score; }
    public int getMaxScore() { return maxScore; }
    public String getFeedback() { return feedback; }
    public String getGraderId() { return graderId; }
    public LocalDateTime getGradedAt() { return gradedAt; }
    public GradeLevel getGradeLevel() { return gradeLevel; }
    
    /**
     * Calculates percentage score
     * @return percentage as double
     */
    public double getPercentage() {
        return (double) score / maxScore * 100;
    }
    
    /**
     * Gets letter grade representation
     * @return letter grade string
     */
    public String getLetterGrade() {
        return gradeLevel.getDisplayName();
    }
    
    /**
     * Checks if grade is passing (>= 60%)
     * @return true if passing, false otherwise
     */
    public boolean isPassing() {
        return getPercentage() >= 60.0;
    }
    
    /**
     * Gets formatted grade display
     * @return formatted grade string
     */
    public String getFormattedGrade() {
        return String.format("%d/%d (%.1f%% - %s)", 
                           score, maxScore, getPercentage(), getLetterGrade());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Grade that = (Grade) obj;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Grade{student='%s', score=%d/%d, letter='%s'}", 
                           studentId, score, maxScore, getLetterGrade());
    }
}
