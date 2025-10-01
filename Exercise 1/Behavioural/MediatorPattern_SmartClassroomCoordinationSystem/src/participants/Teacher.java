package participants;

import models.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Teacher participant in the classroom system.
 * Responsible for posting assignments, answering questions, and managing grades.
 * 
 * Design Principles Applied:
 * - Single Responsibility: Manages teacher-specific operations
 * - Open/Closed: Extensible for new teacher behaviors
 */
public class Teacher extends ClassroomParticipant {
    
    private final String subject;
    private final Map<String, Assignment> assignmentsCreated;
    private final Set<String> questionsReceived;
    private int assignmentCounter;
    
    public Teacher(String id, String name, String subject) {
        super(id, name);
        this.subject = subject != null ? subject : "General";
        this.assignmentsCreated = new ConcurrentHashMap<>();
        this.questionsReceived = Collections.synchronizedSet(new HashSet<>());
        this.assignmentCounter = 1;
    }
    
    @Override
    public String getParticipantType() {
        return "TEACHER";
    }
    
    /**
     * Posts a new assignment through the mediator
     * @param title Assignment title
     * @param description Assignment description
     * @param dueDate Due date for the assignment
     * @param maxPoints Maximum points for the assignment
     */
    public void postAssignment(String title, String description, LocalDateTime dueDate, int maxPoints) {
        validateMediatorSet();
        
        String assignmentId = generateAssignmentId();
        Assignment assignment = new Assignment(assignmentId, title, description, getId(), dueDate, maxPoints);
        
        assignmentsCreated.put(assignmentId, assignment);
        
        System.out.println("[TEACHER] " + getName() + " is posting assignment: " + title);
        mediator.postAssignment(assignment, getId());
    }
    
    /**
     * Responds to a student question
     * @param originalQuestionId The ID of the original question
     * @param response The teacher's response
     */
    public void respondToQuestion(String originalQuestionId, String response) {
        validateMediatorSet();
        
        if (questionsReceived.contains(originalQuestionId)) {
            Notification responseNotification = new Notification(
                getId(),
                "Teacher " + getName() + " responded: " + response,
                "QUESTION_RESPONSE",
                "ALL"
            );
            
            System.out.println("[TEACHER] " + getName() + " is responding to question: " + response);
            mediator.sendNotification(responseNotification, getId());
        }
    }
    
    /**
     * Sends a general announcement to all students
     * @param message The announcement message
     */
    public void makeAnnouncement(String message) {
        validateMediatorSet();
        
        Notification announcement = new Notification(
            getId(),
            "📢 Announcement from " + getName() + ": " + message,
            "TEACHER_ANNOUNCEMENT",
            "STUDENT"
        );
        
        System.out.println("[TEACHER] " + getName() + " is making announcement: " + message);
        mediator.sendNotification(announcement, getId());
    }
    
    /**
     * Manually grades a submission (alternative to automated grading)
     * @param submissionId The submission to grade
     * @param score The score to assign
     * @param feedback Feedback for the student
     */
    public void gradeSubmission(String submissionId, String studentId, String assignmentId, 
                              int score, int maxScore, String feedback) {
        validateMediatorSet();
        
        String gradeId = "GRADE_" + submissionId + "_" + System.currentTimeMillis();
        Grade grade = new Grade(gradeId, submissionId, studentId, assignmentId, 
                              score, maxScore, feedback, getId());
        
        System.out.println("[TEACHER] " + getName() + " is manually grading submission: " + grade.getFormattedGrade());
        mediator.publishGrade(grade, getId());
    }
    
    @Override
    protected void processNotification(Notification notification) {
        String notificationType = notification.getNotificationType();
        String message = notification.getFormattedMessage();
        
        switch (notificationType) {
            case "QUESTION_ASKED":
                handleQuestionReceived(notification);
                break;
            case "SUBMISSION_RECEIVED":
                handleSubmissionReceived(notification);
                break;
            case "GRADE_PUBLISHED":
                handleGradePublished(notification);
                break;
            case "PARTICIPANT_JOINED":
            case "PARTICIPANT_LEFT":
                handleParticipantUpdate(notification);
                break;
            default:
                System.out.println("[TEACHER] " + getName() + " received: " + message);
        }
    }
    
    private void handleQuestionReceived(Notification notification) {
        questionsReceived.add(notification.getId());
        System.out.println("[TEACHER] " + getName() + " received question: " + notification.getMessage());
        
        // Auto-respond to simple questions (demonstration of dynamic behavior)
        if (notification.getMessage().toLowerCase().contains("due date")) {
            respondToQuestion(notification.getId(), 
                "Please check the assignment details for the due date information.");
        } else if (notification.getMessage().toLowerCase().contains("help")) {
            respondToQuestion(notification.getId(), 
                "I'm here to help! Please be more specific about what you need assistance with.");
        }
    }
    
    private void handleSubmissionReceived(Notification notification) {
        System.out.println("[TEACHER] " + getName() + " notified of submission: " + notification.getMessage());
    }
    
    private void handleGradePublished(Notification notification) {
        System.out.println("[TEACHER] " + getName() + " notified of published grade: " + notification.getMessage());
    }
    
    private void handleParticipantUpdate(Notification notification) {
        System.out.println("[TEACHER] " + getName() + " notified: " + notification.getMessage());
    }
    
    /**
     * Generates unique assignment ID
     * @return generated assignment ID
     */
    private String generateAssignmentId() {
        return "ASSIGN_" + getId() + "_" + (assignmentCounter++);
    }
    
    // Getters for teacher-specific information
    public String getSubject() {
        return subject;
    }
    
    public int getAssignmentsCreatedCount() {
        return assignmentsCreated.size();
    }
    
    public int getQuestionsReceivedCount() {
        return questionsReceived.size();
    }
    
    @Override
    public String getDescription() {
        return String.format("Teacher: %s (Subject: %s, Assignments: %d, Questions: %d)", 
                           getName(), subject, getAssignmentsCreatedCount(), getQuestionsReceivedCount());
    }
}
