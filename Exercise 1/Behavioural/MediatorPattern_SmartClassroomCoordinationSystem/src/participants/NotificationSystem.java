package participants;

import models.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Notification System participant that handles various types of notifications
 * and reminders in the classroom system.
 * 
 * Demonstrates Strategy pattern for different notification strategies.
 */
public class NotificationSystem extends ClassroomParticipant {
    
    private final Map<String, LocalDateTime> scheduledReminders;
    private final List<Notification> sentNotifications;
    private final NotificationStrategy strategy;
    
    // Strategy interface for different notification approaches
    public interface NotificationStrategy {
        void sendNotification(String message, String targetType);
        String getStrategyName();
    }
    
    // Concrete strategy implementations
    public static class EmailNotificationStrategy implements NotificationStrategy {
        public void sendNotification(String message, String targetType) {
            System.out.println("[EMAIL] " + message + " (sent to " + targetType + ")");
        }
        public String getStrategyName() { return "Email"; }
    }
    
    public static class PushNotificationStrategy implements NotificationStrategy {
        public void sendNotification(String message, String targetType) {
            System.out.println("[PUSH] " + message + " (sent to " + targetType + ")");
        }
        public String getStrategyName() { return "Push"; }
    }
    
    public static class SMSNotificationStrategy implements NotificationStrategy {
        public void sendNotification(String message, String targetType) {
            System.out.println("[SMS] " + message + " (sent to " + targetType + ")");
        }
        public String getStrategyName() { return "SMS"; }
    }
    
    public NotificationSystem(String id, String name, NotificationStrategy strategy) {
        super(id, name);
        this.strategy = strategy != null ? strategy : new EmailNotificationStrategy();
        this.scheduledReminders = Collections.synchronizedMap(new HashMap<>());
        this.sentNotifications = Collections.synchronizedList(new ArrayList<>());
    }
    
    // Constructor with default email strategy
    public NotificationSystem(String id, String name) {
        this(id, name, new EmailNotificationStrategy());
    }
    
    @Override
    public String getParticipantType() {
        return "NOTIFICATION_SYSTEM";
    }
    
    /**
     * Sends immediate notification to specified targets
     * @param message The notification message
     * @param targetType Target participant type
     * @param notificationType Type of notification
     */
    public void sendImmediateNotification(String message, String targetType, String notificationType) {
        validateMediatorSet();
        
        Notification notification = new Notification(
            getId(),
            message,
            notificationType,
            targetType
        );
        
        sentNotifications.add(notification);
        strategy.sendNotification(message, targetType);
        
        System.out.println("[NOTIFY] " + getName() + " (" + strategy.getStrategyName() + ") sending: " + message);
        mediator.sendNotification(notification, getId());
    }
    
    /**
     * Schedules a deadline reminder for an assignment
     * @param assignmentTitle The assignment title
     * @param dueDate The due date
     * @param hoursBeforeReminder Hours before due date to send reminder
     */
    public void scheduleDeadlineReminder(String assignmentTitle, LocalDateTime dueDate, int hoursBeforeReminder) {
        LocalDateTime reminderTime = dueDate.minusHours(hoursBeforeReminder);
        String reminderId = "REMINDER_" + assignmentTitle.replaceAll("\\s+", "_") + "_" + System.currentTimeMillis();
        
        scheduledReminders.put(reminderId, reminderTime);
        
        System.out.println("[REMINDER] " + getName() + " scheduled reminder for '" + assignmentTitle + "' at " + reminderTime);
        
        // Simulate sending reminder (in real system, this would be scheduled)
        if (shouldSendReminder(reminderTime)) {
            sendDeadlineReminder(assignmentTitle, dueDate);
        }
    }
    
    /**
     * Sends deadline reminder notification
     * @param assignmentTitle The assignment title
     * @param dueDate The due date
     */
    public void sendDeadlineReminder(String assignmentTitle, LocalDateTime dueDate) {
        String message = "[WARNING] DEADLINE REMINDER: Assignment '" + assignmentTitle + 
                        "' is due on " + dueDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"));
        
        sendImmediateNotification(message, "STUDENT", "DEADLINE_REMINDER");
    }
    
    /**
     * Sends system maintenance notification
     * @param maintenanceTime When maintenance will occur
     * @param expectedDuration Expected duration in hours
     */
    public void sendMaintenanceNotification(LocalDateTime maintenanceTime, int expectedDuration) {
        String message = "[MAINTENANCE] SYSTEM MAINTENANCE: The classroom system will be unavailable on " +
                        maintenanceTime.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm")) +
                        " for approximately " + expectedDuration + " hour(s).";
        
        sendImmediateNotification(message, "ALL", "SYSTEM_MAINTENANCE");
    }
    
    /**
     * Sends welcome notification to new participants
     * @param participantName Name of new participant
     * @param participantType Type of participant
     */
    public void sendWelcomeNotification(String participantName, String participantType) {
        String message = "[WELCOME] Welcome " + participantName + " to our smart classroom! " +
                        "We're excited to have you as a " + participantType.toLowerCase() + ".";
        
        sendImmediateNotification(message, "ALL", "WELCOME_MESSAGE");
    }
    
    @Override
    protected void processNotification(Notification notification) {
        String notificationType = notification.getNotificationType();
        String message = notification.getFormattedMessage();
        
        switch (notificationType) {
            case "SCHEDULE_REMINDER":
                handleScheduleReminderRequest(notification);
                break;
            case "PARTICIPANT_JOINED":
                handleNewParticipant(notification);
                break;
            case "ASSIGNMENT_POSTED":
                handleAssignmentPosted(notification);
                break;
            case "EMERGENCY_ALERT":
                handleEmergencyAlert(notification);
                break;
            default:
                System.out.println("[NOTIFY] " + getName() + " received: " + message);
        }
    }
    
    private void handleScheduleReminderRequest(Notification notification) {
        System.out.println("[NOTIFY] " + getName() + " processing reminder request: " + notification.getMessage());
        
        // Extract assignment info and schedule reminder
        String message = notification.getMessage();
        if (message.contains("Schedule deadline reminder for:")) {
            String assignmentTitle = message.substring(message.indexOf(":") + 1).trim();
            // In real system, would parse due date and schedule accordingly
            LocalDateTime dueDate = LocalDateTime.now().plusDays(7); // Mock due date
            scheduleDeadlineReminder(assignmentTitle, dueDate, 24); // 24 hours before
        }
    }
    
    private void handleNewParticipant(Notification notification) {
        System.out.println("[NOTIFY] " + getName() + " welcoming new participant");
        // Welcome notification would be sent here
    }
    
    private void handleAssignmentPosted(Notification notification) {
        System.out.println("[NOTIFY] " + getName() + " processing assignment notification");
        // Could send additional notifications about assignment formatting, resources, etc.
    }
    
    private void handleEmergencyAlert(Notification notification) {
        System.out.println("[EMERGENCY] " + getName() + " processing EMERGENCY: " + notification.getMessage());
        // Emergency alerts get highest priority
        sendImmediateNotification(
            "[URGENT] URGENT: " + notification.getMessage(),
            "ALL",
            "EMERGENCY_BROADCAST"
        );
    }
    
    /**
     * Determines if a reminder should be sent based on current time
     * @param reminderTime The scheduled reminder time
     * @return true if reminder should be sent
     */
    private boolean shouldSendReminder(LocalDateTime reminderTime) {
        // In real system, this would compare with actual current time
        // For demonstration, we'll simulate that some reminders are due
        return Math.random() > 0.5; // 50% chance for demo purposes
    }
    
    /**
     * Sends a custom notification with specified priority
     * @param message The message content
     * @param targetType Target participant type
     * @param priority Notification priority
     */
    public void sendPriorityNotification(String message, String targetType, 
                                       Notification.NotificationPriority priority) {
        validateMediatorSet();
        
        Notification notification = new Notification(
            "PRIORITY_" + System.currentTimeMillis(),
            getId(),
            message,
            "PRIORITY_MESSAGE",
            targetType,
            priority
        );
        
        sentNotifications.add(notification);
        
        String prioritySymbol = priority == Notification.NotificationPriority.CRITICAL ? "[CRITICAL]" : 
                               priority == Notification.NotificationPriority.HIGH ? "[HIGH]" : "[INFO]";
        
        System.out.println("[NOTIFY] " + getName() + " sending " + priority + " priority: " + prioritySymbol + " " + message);
        mediator.sendNotification(notification, getId());
    }
    
    // Getters for system information
    public String getNotificationStrategy() {
        return strategy.getStrategyName();
    }
    
    public int getScheduledRemindersCount() {
        return scheduledReminders.size();
    }
    
    public int getSentNotificationsCount() {
        return sentNotifications.size();
    }
    
    @Override
    public String getDescription() {
        return String.format("NotificationSystem: %s (Strategy: %s, Reminders: %d, Sent: %d)", 
                           getName(), getNotificationStrategy(), 
                           getScheduledRemindersCount(), getSentNotificationsCount());
    }
}
