package participants;

import models.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Student participant in the classroom system.
 * Responsible for submitting assignments, asking questions, and receiving notifications.
 * 
 * Design Principles Applied:
 * - Single Responsibility: Manages student-specific operations
 * - Encapsulation: Protects internal state
 */
public class Student extends ClassroomParticipant {
    
    private final String studentNumber;
    private final String major;
    private final Map<String, Submission> submissionsMap;
    private final List<String> questionsAsked;
    private final List<Notification> receivedNotifications;
    private int submissionCounter;
    private int questionCounter;
    
    public Student(String id, String name, String studentNumber, String major) {
        super(id, name);
        this.studentNumber = studentNumber != null ? studentNumber : "STU" + id;
        this.major = major != null ? major : "Undeclared";
        this.submissionsMap = new ConcurrentHashMap<>();
        this.questionsAsked = Collections.synchronizedList(new ArrayList<>());
        this.receivedNotifications = Collections.synchronizedList(new ArrayList<>());
        this.submissionCounter = 1;
        this.questionCounter = 1;
    }
    
    @Override
    public String getParticipantType() {
        return "STUDENT";
    }
    
    /**
     * Submits an assignment through the mediator
     * @param assignmentId ID of the assignment being submitted
     * @param assignmentTitle Title of the assignment
     * @param content The submission content
     * @param filePath Optional file attachment path
     */
    public void submitAssignment(String assignmentId, String assignmentTitle, 
                               String content, String filePath) {
        validateMediatorSet();
        
        String submissionId = generateSubmissionId();
        Submission submission = new Submission(submissionId, assignmentId, assignmentTitle, 
                                             getId(), content, filePath);
        
        submissionsMap.put(submissionId, submission);
        
        System.out.println("[STUDENT] " + getName() + " is submitting assignment: " + assignmentTitle);
        mediator.submitAssignment(submission, getId());
    }
    
    /**
     * Submits an assignment without file attachment
     * @param assignmentId ID of the assignment
     * @param assignmentTitle Title of the assignment
     * @param content The submission content
     */
    public void submitAssignment(String assignmentId, String assignmentTitle, String content) {
        submitAssignment(assignmentId, assignmentTitle, content, null);
    }
    
    /**
     * Asks a question through the mediator
     * @param content The question content
     * @param subject The subject/topic of the question
     * @param priority Priority level of the question
     * @param isAnonymous Whether the question should be anonymous
     */
    public void askQuestion(String content, String subject, Question.QuestionPriority priority, 
                          boolean isAnonymous) {
        validateMediatorSet();
        
        String questionId = generateQuestionId();
        Question question = new Question(questionId, getId(), content, subject, priority, isAnonymous);
        
        questionsAsked.add(questionId);
        
        String displayName = isAnonymous ? "Anonymous Student" : getName();
        System.out.println("[STUDENT] " + displayName + " is asking: " + content);
        mediator.askQuestion(question, getId());
    }
    
    /**
     * Asks a simple question with default settings
     * @param content The question content
     * @param subject The subject of the question
     */
    public void askQuestion(String content, String subject) {
        askQuestion(content, subject, Question.QuestionPriority.MEDIUM, false);
    }
    
    /**
     * Participates in class discussion by sending a message
     * @param message The discussion message
     */
    public void participateInDiscussion(String message) {
        validateMediatorSet();
        
        Notification discussionMessage = new Notification(
            getId(),
            getName() + " says: " + message,
            "CLASS_DISCUSSION",
            "ALL"
        );
        
        System.out.println("[STUDENT] " + getName() + " participating in discussion: " + message);
        mediator.sendNotification(discussionMessage, getId());
    }
    
    /**
     * Requests help from teacher
     * @param topic The topic needing help with
     */
    public void requestHelp(String topic) {
        askQuestion("I need help with " + topic + ". Could you please explain this concept?", 
                   topic, Question.QuestionPriority.HIGH, false);
    }
    
    @Override
    protected void processNotification(Notification notification) {
        receivedNotifications.add(notification);
        
        String notificationType = notification.getNotificationType();
        String message = notification.getFormattedMessage();
        
        switch (notificationType) {
            case "ASSIGNMENT_POSTED":
                handleAssignmentPosted(notification);
                break;
            case "GRADE_PUBLISHED":
                handleGradeReceived(notification);
                break;
            case "QUESTION_RESPONSE":
                handleQuestionResponse(notification);
                break;
            case "TEACHER_ANNOUNCEMENT":
                handleTeacherAnnouncement(notification);
                break;
            case "DEADLINE_REMINDER":
                handleDeadlineReminder(notification);
                break;
            case "CLASS_DISCUSSION":
                handleClassDiscussion(notification);
                break;
            case "PARTICIPANT_JOINED":
            case "PARTICIPANT_LEFT":
                handleParticipantUpdate(notification);
                break;
            default:
                System.out.println("[STUDENT] " + getName() + " received: " + message);
        }
    }
    
    private void handleAssignmentPosted(Notification notification) {
        System.out.println("[STUDENT] " + getName() + " sees new assignment: " + notification.getMessage());
        
        // Simulate student engagement - some students might ask questions about new assignments
        if (Math.random() > 0.7) { // 30% chance to ask a question
            String[] possibleQuestions = {
                "What is the due date for this assignment?",
                "Do we need to submit this in a specific format?",
                "Can we work in groups for this assignment?",
                "How many points is this assignment worth?"
            };
            String randomQuestion = possibleQuestions[(int)(Math.random() * possibleQuestions.length)];
            askQuestion(randomQuestion, "Assignment Guidelines");
        }
    }
    
    private void handleGradeReceived(Notification notification) {
        System.out.println("[STUDENT] " + getName() + " received grade notification: " + notification.getMessage());
        
        // Simulate student reactions to grades
        if (notification.getMessage().contains("A")) {
            System.out.println("[STUDENT] " + getName() + " is happy with the grade!");
        } else if (notification.getMessage().contains("F") || notification.getMessage().contains("D")) {
            System.out.println("[STUDENT] " + getName() + " is disappointed and might ask for help");
            // Might ask for help improving
            if (Math.random() > 0.5) {
                requestHelp("improving my performance on assignments");
            }
        }
    }
    
    private void handleQuestionResponse(Notification notification) {
        System.out.println("[STUDENT] " + getName() + " received response: " + notification.getMessage());
    }
    
    private void handleTeacherAnnouncement(Notification notification) {
        System.out.println("[STUDENT] " + getName() + " heard announcement: " + notification.getMessage());
    }
    
    private void handleDeadlineReminder(Notification notification) {
        System.out.println("[STUDENT] " + getName() + " received deadline reminder: " + notification.getMessage());
        
        // Simulate rush to submit if reminded about deadline
        if (Math.random() > 0.6) { // 40% chance to submit when reminded
            System.out.println("[STUDENT] " + getName() + " is rushing to submit assignment!");
        }
    }
    
    private void handleClassDiscussion(Notification notification) {
        System.out.println("[STUDENT] " + getName() + " sees discussion: " + notification.getMessage());
    }
    
    private void handleParticipantUpdate(Notification notification) {
        System.out.println("[STUDENT] " + getName() + " notified: " + notification.getMessage());
    }
    
    /**
     * Generates unique submission ID
     * @return generated submission ID
     */
    private String generateSubmissionId() {
        return "SUB_" + getId() + "_" + (submissionCounter++);
    }
    
    /**
     * Generates unique question ID
     * @return generated question ID
     */
    private String generateQuestionId() {
        return "Q_" + getId() + "_" + (questionCounter++);
    }
    
    // Getters for student-specific information
    public String getStudentNumber() {
        return studentNumber;
    }
    
    public String getMajor() {
        return major;
    }
    
    public int getSubmissionCount() {
        return submissionsMap.size();
    }
    
    public int getQuestionCount() {
        return questionsAsked.size();
    }
    
    public int getNotificationCount() {
        return receivedNotifications.size();
    }
    
    @Override
    public String getDescription() {
        return String.format("Student: %s (Major: %s, Submissions: %d, Questions: %d, Notifications: %d)", 
                           getName(), major, getSubmissionCount(), getQuestionCount(), getNotificationCount());
    }
}
