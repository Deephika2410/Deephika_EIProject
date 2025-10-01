package participants;

import models.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Grading System participant that automatically evaluates student submissions.
 * Demonstrates automated processing and intelligent grading algorithms.
 * 
 * Design Principles Applied:
 * - Single Responsibility: Handles only grading operations
 * - Strategy Pattern: Different grading strategies
 * - Observer Pattern: Reacts to submission events
 */
public class GradingSystem extends ClassroomParticipant {
    
    private final Map<String, Grade> gradesPublished;
    private final Queue<Submission> submissionsToGrade;
    private final GradingStrategy gradingStrategy;
    private final Map<String, Integer> assignmentMaxScores;
    private boolean autoGradingEnabled;
    
    // Strategy interface for different grading approaches
    public interface GradingStrategy {
        Grade gradeSubmission(Submission submission, int maxScore, String graderId);
        String getStrategyName();
    }
    
    /**
     * Simple automated grading strategy based on content analysis
     */
    public static class AutomatedGradingStrategy implements GradingStrategy {
        @Override
        public Grade gradeSubmission(Submission submission, int maxScore, String graderId) {
            // Simulate automated grading based on content characteristics
            String content = submission.getContent().toLowerCase();
            int score = calculateScore(content, maxScore);
            String feedback = generateFeedback(content, score, maxScore);
            
            String gradeId = "AUTO_GRADE_" + submission.getId() + "_" + System.currentTimeMillis();
            return new Grade(gradeId, submission.getId(), submission.getStudentId(), 
                           submission.getAssignmentId(), score, maxScore, feedback, graderId);
        }
        
        private int calculateScore(String content, int maxScore) {
            int score = 0;
            
            // Basic scoring algorithm (in real system, would be more sophisticated)
            // Length factor (more content generally better, up to a point)
            int wordCount = content.split("\\s+").length;
            if (wordCount >= 50) score += maxScore * 0.3;
            else if (wordCount >= 25) score += maxScore * 0.2;
            else score += maxScore * 0.1;
            
            // Quality indicators
            if (content.contains("analysis") || content.contains("conclusion")) score += maxScore * 0.2;
            if (content.contains("example") || content.contains("evidence")) score += maxScore * 0.15;
            if (content.contains("research") || content.contains("source")) score += maxScore * 0.15;
            
            // Structure indicators
            if (content.contains("introduction") || content.contains("overview")) score += maxScore * 0.1;
            if (content.contains("summary") || content.contains("conclusion")) score += maxScore * 0.1;
            
            // Randomize slightly to simulate real grading variance
            score += (int)(Math.random() * (maxScore * 0.1)) - (maxScore * 0.05);
            
            return Math.max(0, Math.min(score, maxScore));
        }
        
        private String generateFeedback(String content, int score, int maxScore) {
            double percentage = (double)score / maxScore * 100;
            StringBuilder feedback = new StringBuilder();
            
            feedback.append("Automated Grading Results:\n");
            
            if (percentage >= 90) {
                feedback.append("Excellent work! Your submission demonstrates strong understanding.");
            } else if (percentage >= 80) {
                feedback.append("Good work! Well-structured response with solid content.");
            } else if (percentage >= 70) {
                feedback.append("Satisfactory work. Consider adding more detail and examples.");
            } else if (percentage >= 60) {
                feedback.append("Needs improvement. Please review the assignment requirements.");
            } else {
                feedback.append("Unsatisfactory. Please see teacher for additional guidance.");
            }
            
            // Content-specific feedback
            if (content.length() < 100) {
                feedback.append("\n- Consider expanding your response with more details.");
            }
            if (!content.contains("example")) {
                feedback.append("\n- Including examples would strengthen your argument.");
            }
            
            return feedback.toString();
        }
        
        @Override
        public String getStrategyName() {
            return "Automated Content Analysis";
        }
    }
    
    /**
     * Rubric-based grading strategy
     */
    public static class RubricGradingStrategy implements GradingStrategy {
        @Override
        public Grade gradeSubmission(Submission submission, int maxScore, String graderId) {
            // Simulate rubric-based grading
            String content = submission.getContent();
            int score = evaluateAgainstRubric(content, maxScore);
            String feedback = generateRubricFeedback(content, score, maxScore);
            
            String gradeId = "RUBRIC_GRADE_" + submission.getId() + "_" + System.currentTimeMillis();
            return new Grade(gradeId, submission.getId(), submission.getStudentId(), 
                           submission.getAssignmentId(), score, maxScore, feedback, graderId);
        }
        
        private int evaluateAgainstRubric(String content, int maxScore) {
            int score = 0;
            String lowerContent = content.toLowerCase();
            
            // Criteria 1: Content Knowledge (40% of grade)
            if (lowerContent.contains("concept") && lowerContent.contains("theory")) {
                score += maxScore * 0.4;
            } else if (lowerContent.contains("concept") || lowerContent.contains("theory")) {
                score += maxScore * 0.25;
            } else {
                score += maxScore * 0.1;
            }
            
            // Criteria 2: Critical Thinking (30% of grade)
            if (lowerContent.contains("analysis") && lowerContent.contains("evaluation")) {
                score += maxScore * 0.3;
            } else if (lowerContent.contains("analysis") || lowerContent.contains("evaluation")) {
                score += maxScore * 0.2;
            } else {
                score += maxScore * 0.1;
            }
            
            // Criteria 3: Organization (20% of grade)
            if (content.split("\\n").length >= 3) { // Multiple paragraphs
                score += maxScore * 0.2;
            } else {
                score += maxScore * 0.1;
            }
            
            // Criteria 4: Mechanics (10% of grade)
            if (content.length() >= 200) { // Adequate length
                score += maxScore * 0.1;
            } else {
                score += maxScore * 0.05;
            }
            
            return Math.min(score, maxScore);
        }
        
        private String generateRubricFeedback(String content, int score, int maxScore) {
            return String.format("Rubric-based evaluation completed. Score: %d/%d\nReview the assignment rubric for detailed criteria.", 
                               score, maxScore);
        }
        
        @Override
        public String getStrategyName() {
            return "Rubric-Based Evaluation";
        }
    }
    
    public GradingSystem(String id, String name, GradingStrategy strategy) {
        super(id, name);
        this.gradingStrategy = strategy != null ? strategy : new AutomatedGradingStrategy();
        this.gradesPublished = new ConcurrentHashMap<>();
        this.submissionsToGrade = new LinkedList<>();
        this.assignmentMaxScores = new ConcurrentHashMap<>();
        this.autoGradingEnabled = true;
    }
    
    // Constructor with default automated strategy
    public GradingSystem(String id, String name) {
        this(id, name, new AutomatedGradingStrategy());
    }
    
    @Override
    public String getParticipantType() {
        return "GRADING_SYSTEM";
    }
    
    /**
     * Sets the maximum score for an assignment
     * @param assignmentId Assignment ID
     * @param maxScore Maximum possible score
     */
    public void setAssignmentMaxScore(String assignmentId, int maxScore) {
        assignmentMaxScores.put(assignmentId, maxScore);
        System.out.println("[GRADER] " + getName() + " registered max score for assignment: " + maxScore + " points");
    }
    
    /**
     * Processes a submission for grading
     * @param submission The submission to grade
     */
    public void processSubmission(Submission submission) {
        if (!autoGradingEnabled) {
            submissionsToGrade.add(submission);
            System.out.println("[GRADER] " + getName() + " queued submission for manual review: " + submission.getAssignmentTitle());
            return;
        }
        
        System.out.println("[GRADER] " + getName() + " (" + gradingStrategy.getStrategyName() + ") is grading submission: " + submission.getAssignmentTitle());
        
        // Get max score for the assignment
        int maxScore = assignmentMaxScores.getOrDefault(submission.getAssignmentId(), 100);
        
        // Grade the submission using the selected strategy
        Grade grade = gradingStrategy.gradeSubmission(submission, maxScore, getId());
        gradesPublished.put(grade.getId(), grade);
        
        System.out.println("[GRADER] Generated Grade for Assignment: " + grade.getAssignmentId() + " -> " + grade.getScore() + "/100 (" + grade.getFeedback() + ")");
        
        // Publish grade through mediator
        if (mediator != null) {
            mediator.publishGrade(grade, getId());
        }
    }
    
    /**
     * Enables or disables automatic grading
     * @param enabled Whether auto-grading should be enabled
     */
    public void setAutoGradingEnabled(boolean enabled) {
        this.autoGradingEnabled = enabled;
        String status = enabled ? "enabled" : "disabled";
        System.out.println("[GRADER] " + getName() + " auto-grading " + status);
        
        // Process queued submissions if auto-grading was re-enabled
        if (enabled && !submissionsToGrade.isEmpty()) {
            System.out.println("[GRADER] " + getName() + " processing " + submissionsToGrade.size() + " queued submissions");
            while (!submissionsToGrade.isEmpty()) {
                processSubmission(submissionsToGrade.poll());
            }
        }
    }
    
    /**
     * Manually reviews queued submissions
     */
    public void processQueuedSubmissions() {
        if (submissionsToGrade.isEmpty()) {
            System.out.println("[GRADER] " + getName() + " has no queued submissions to process");
            return;
        }
        
        System.out.println("[GRADER] " + getName() + " processing " + submissionsToGrade.size() + " queued submissions");
        while (!submissionsToGrade.isEmpty()) {
            processSubmission(submissionsToGrade.poll());
        }
    }
    
    @Override
    protected void processNotification(Notification notification) {
        String notificationType = notification.getNotificationType();
        String message = notification.getFormattedMessage();
        
        switch (notificationType) {
            case "SUBMISSION_RECEIVED":
                handleSubmissionReceived(notification);
                break;
            case "ASSIGNMENT_POSTED":
                handleAssignmentPosted(notification);
                break;
            case "GRADING_REQUEST":
                handleManualGradingRequest(notification);
                break;
            default:
                System.out.println("[GRADER] " + getName() + " received: " + message);
        }
    }
    
    private void handleSubmissionReceived(Notification notification) {
        System.out.println("[GRADER] " + getName() + " notified of new submission: " + notification.getMessage());
        // In a real system, would extract submission details and process it
        // For demo, we'll simulate processing
        simulateSubmissionProcessing();
    }
    
    private void handleAssignmentPosted(Notification notification) {
        System.out.println("[GRADER] " + getName() + " registered new assignment for grading");
        // Could automatically set up grading rubric or criteria
    }
    
    private void handleManualGradingRequest(Notification notification) {
        System.out.println("[GRADER] " + getName() + " received manual grading request");
        processQueuedSubmissions();
    }
    
    private void simulateSubmissionProcessing() {
        // Simulate a submission being received and processed
        String mockSubmissionId = "MOCK_SUB_" + System.currentTimeMillis();
        String mockContent = "This is a sample submission with analysis, examples, and conclusions. " +
                           "The student demonstrates understanding of key concepts and provides evidence " +
                           "to support their arguments. The work shows critical thinking and organization.";
        
        Submission mockSubmission = new Submission(
            mockSubmissionId, 
            "MOCK_ASSIGN_1", 
            "Sample Assignment", 
            "STUDENT_" + (int)(Math.random() * 100), 
            mockContent
        );
        
        processSubmission(mockSubmission);
    }
    
    /**
     * Generates grading statistics report
     */
    public void generateGradingReport() {
        System.out.println("\n[GRADER] === GRADING SYSTEM REPORT ===");
        System.out.println("Strategy: " + gradingStrategy.getStrategyName());
        System.out.println("Auto-grading enabled: " + autoGradingEnabled);
        System.out.println("Total grades published: " + gradesPublished.size());
        System.out.println("Submissions in queue: " + submissionsToGrade.size());
        
        if (!gradesPublished.isEmpty()) {
            double averageScore = gradesPublished.values().stream()
                .mapToDouble(Grade::getPercentage)
                .average()
                .orElse(0.0);
            System.out.println("Average score: " + String.format("%.1f%%", averageScore));
        }
        
        System.out.println("================================\n");
    }
    
    // Getters for system information
    public String getGradingStrategy() {
        return gradingStrategy.getStrategyName();
    }
    
    public boolean isAutoGradingEnabled() {
        return autoGradingEnabled;
    }
    
    public int getGradesPublishedCount() {
        return gradesPublished.size();
    }
    
    public int getQueuedSubmissionsCount() {
        return submissionsToGrade.size();
    }
    
    @Override
    public String getDescription() {
        return String.format("GradingSystem: %s (Strategy: %s, Auto: %s, Graded: %d, Queued: %d)", 
                           getName(), getGradingStrategy(), autoGradingEnabled, 
                           getGradesPublishedCount(), getQueuedSubmissionsCount());
    }
}
