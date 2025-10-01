package mediator;

import models.*;
import participants.ClassroomParticipant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete implementation of the ClassroomMediator interface.
 * This class serves as the central coordination hub for all classroom activities.
 * 
 * Design Principles Applied:
 * - Single Responsibility: Manages only communication coordination
 * - Open/Closed: Open for extension, closed for modification
 * - Mediator Pattern: Encapsulates how objects interact
 */
public class SmartClassroomCoordinator implements ClassroomMediator {
    
    private final Map<String, ClassroomParticipant> participants;
    private final List<String> activityLog;
    private final DateTimeFormatter timeFormatter;
    
    public SmartClassroomCoordinator() {
        this.participants = new ConcurrentHashMap<>();
        this.activityLog = Collections.synchronizedList(new ArrayList<>());
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }
    
    @Override
    public void registerParticipant(ClassroomParticipant participant) {
        participants.put(participant.getId(), participant);
        participant.setMediator(this);
        
        String logMessage = String.format("[%s] %s '%s' joined the classroom",
            LocalDateTime.now().format(timeFormatter),
            participant.getParticipantType(),
            participant.getName());
        
        activityLog.add(logMessage);
        System.out.println("[+] " + logMessage);
        
        // Notify other participants about new member
        notifyParticipantsExcept(
            new Notification("SYSTEM", "New member joined: " + participant.getName(), 
                           "PARTICIPANT_JOINED", participant.getParticipantType()),
            participant.getId()
        );
    }
    
    @Override
    public void removeParticipant(ClassroomParticipant participant) {
        if (participants.remove(participant.getId()) != null) {
            String logMessage = String.format("[%s] %s '%s' left the classroom",
                LocalDateTime.now().format(timeFormatter),
                participant.getParticipantType(),
                participant.getName());
            
            activityLog.add(logMessage);
            System.out.println("[-] " + logMessage);
            
            // Notify remaining participants
            notifyParticipantsExcept(
                new Notification("SYSTEM", participant.getName() + " left the classroom", 
                               "PARTICIPANT_LEFT", "ALL"),
                participant.getId()
            );
        }
    }
    
    @Override
    public void postAssignment(Assignment assignment, String teacherId) {
        String logMessage = String.format("[%s] Assignment posted: '%s' by %s",
            LocalDateTime.now().format(timeFormatter),
            assignment.getTitle(),
            getParticipantName(teacherId));
        
        activityLog.add(logMessage);
        System.out.println("[ASSIGN] " + logMessage);
        
        // Notify all students about new assignment
        ClassroomParticipant[] students = getParticipantsByType("STUDENT");
        for (ClassroomParticipant student : students) {
            student.receiveNotification(new Notification(
                teacherId,
                "New assignment posted: " + assignment.getTitle(),
                "ASSIGNMENT_POSTED",
                "STUDENT"
            ));
        }
        
        // Notify notification system to schedule deadline reminders
        ClassroomParticipant[] notificationSystems = getParticipantsByType("NOTIFICATION_SYSTEM");
        for (ClassroomParticipant system : notificationSystems) {
            system.receiveNotification(new Notification(
                teacherId,
                "Schedule deadline reminder for: " + assignment.getTitle(),
                "SCHEDULE_REMINDER",
                "NOTIFICATION_SYSTEM"
            ));
        }
    }
    
    @Override
    public void submitAssignment(Submission submission, String studentId) {
        String logMessage = String.format("[%s] Assignment submitted: '%s' by %s",
            LocalDateTime.now().format(timeFormatter),
            submission.getAssignmentTitle(),
            getParticipantName(studentId));
        
        activityLog.add(logMessage);
        System.out.println("[SUBMIT] " + logMessage);
        
        // Send submission to grading system
        ClassroomParticipant[] gradingSystems = getParticipantsByType("GRADING_SYSTEM");
        for (ClassroomParticipant system : gradingSystems) {
            system.receiveNotification(new Notification(
                studentId,
                "New submission for grading: " + submission.getAssignmentTitle(),
                "SUBMISSION_RECEIVED",
                "GRADING_SYSTEM"
            ));
        }
        
        // Notify teacher about submission
        ClassroomParticipant[] teachers = getParticipantsByType("TEACHER");
        for (ClassroomParticipant teacher : teachers) {
            teacher.receiveNotification(new Notification(
                studentId,
                "Assignment submitted by " + getParticipantName(studentId) + ": " + submission.getAssignmentTitle(),
                "SUBMISSION_RECEIVED",
                "TEACHER"
            ));
        }
    }
    
    @Override
    public void askQuestion(Question question, String studentId) {
        String logMessage = String.format("[%s] Question asked by %s: '%s'",
            LocalDateTime.now().format(timeFormatter),
            getParticipantName(studentId),
            question.getContent().substring(0, Math.min(50, question.getContent().length())) + "...");
        
        activityLog.add(logMessage);
        System.out.println("[QUESTION] " + logMessage);
        
        // Route question to all teachers
        ClassroomParticipant[] teachers = getParticipantsByType("TEACHER");
        for (ClassroomParticipant teacher : teachers) {
            teacher.receiveNotification(new Notification(
                studentId,
                "Question from " + getParticipantName(studentId) + ": " + question.getContent(),
                "QUESTION_ASKED",
                "TEACHER"
            ));
        }
    }
    
    @Override
    public void publishGrade(Grade grade, String gradingSystemId) {
        String logMessage = String.format("[%s] Grade published: %s for %s's submission",
            LocalDateTime.now().format(timeFormatter),
            grade.getScore(),
            getParticipantName(grade.getStudentId()));
        
        activityLog.add(logMessage);
        System.out.println("[GRADE] " + logMessage);
        
        // Send grade to student
        ClassroomParticipant student = participants.get(grade.getStudentId());
        if (student != null) {
            student.receiveNotification(new Notification(
                gradingSystemId,
                "Your assignment has been graded: " + grade.getScore() + "/100",
                "GRADE_PUBLISHED",
                "STUDENT"
            ));
        }
        
        // Notify teachers about completed grading
        ClassroomParticipant[] teachers = getParticipantsByType("TEACHER");
        for (ClassroomParticipant teacher : teachers) {
            teacher.receiveNotification(new Notification(
                gradingSystemId,
                "Grade published for " + getParticipantName(grade.getStudentId()) + ": " + grade.getScore(),
                "GRADE_PUBLISHED",
                "TEACHER"
            ));
        }
    }
    
    @Override
    public void sendNotification(Notification notification, String senderId) {
        String logMessage = String.format("[%s] Notification sent by %s: '%s'",
            LocalDateTime.now().format(timeFormatter),
            getParticipantName(senderId),
            notification.getMessage());
        
        activityLog.add(logMessage);
        System.out.println("[NOTIFY] " + logMessage);
        
        // Route notification based on target type
        if ("ALL".equals(notification.getTargetType())) {
            for (ClassroomParticipant participant : participants.values()) {
                if (!participant.getId().equals(senderId)) {
                    participant.receiveNotification(notification);
                }
            }
        } else {
            ClassroomParticipant[] targetParticipants = getParticipantsByType(notification.getTargetType());
            for (ClassroomParticipant participant : targetParticipants) {
                if (!participant.getId().equals(senderId)) {
                    participant.receiveNotification(notification);
                }
            }
        }
    }
    
    @Override
    public ClassroomParticipant[] getParticipantsByType(String participantType) {
        return participants.values().stream()
            .filter(p -> p.getParticipantType().equals(participantType))
            .toArray(ClassroomParticipant[]::new);
    }
    
    /**
     * Gets the name of a participant by ID
     * @param participantId The participant ID
     * @return The participant's name or "Unknown"
     */
    private String getParticipantName(String participantId) {
        ClassroomParticipant participant = participants.get(participantId);
        return participant != null ? participant.getName() : "Unknown";
    }
    
    /**
     * Notifies all participants except the specified one
     * @param notification The notification to send
     * @param exceptId The ID to exclude from notification
     */
    private void notifyParticipantsExcept(Notification notification, String exceptId) {
        participants.values().stream()
            .filter(p -> !p.getId().equals(exceptId))
            .forEach(p -> p.receiveNotification(notification));
    }
    
    /**
     * Displays the activity log for debugging and monitoring
     */
    public void displayActivityLog() {
        System.out.println("\n[LOG] === CLASSROOM ACTIVITY LOG ===");
        if (activityLog.isEmpty()) {
            System.out.println("No activities recorded yet.");
        } else {
            activityLog.forEach(System.out::println);
        }
        System.out.println("================================\n");
    }
    
    /**
     * Gets current participant statistics
     */
    public void displayParticipantStats() {
        System.out.println("\n[STATS] === PARTICIPANT STATISTICS ===");
        Map<String, Long> typeCount = new HashMap<>();
        
        for (ClassroomParticipant participant : participants.values()) {
            String type = participant.getParticipantType();
            typeCount.put(type, typeCount.getOrDefault(type, 0L) + 1);
        }
        
        typeCount.forEach((type, count) -> 
            System.out.println(type + ": " + count));
        System.out.println("Total Participants: " + participants.size());
        System.out.println("================================\n");
    }
}
