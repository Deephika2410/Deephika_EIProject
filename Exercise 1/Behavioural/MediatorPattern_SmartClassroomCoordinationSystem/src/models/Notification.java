package models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a notification in the classroom system.
 * Supports different types of notifications for flexible communication.
 */
public class Notification {
    private final String id;
    private final String senderId;
    private final String message;
    private final String notificationType;
    private final String targetType;
    private final NotificationPriority priority;
    private final LocalDateTime createdAt;
    
    public enum NotificationPriority {
        LOW, NORMAL, HIGH, CRITICAL
    }
    
    public Notification(String senderId, String message, String notificationType, String targetType) {
        this(generateId(), senderId, message, notificationType, targetType, NotificationPriority.NORMAL);
    }
    
    public Notification(String id, String senderId, String message, String notificationType, 
                       String targetType, NotificationPriority priority) {
        this.id = Objects.requireNonNull(id, "Notification ID cannot be null");
        this.senderId = Objects.requireNonNull(senderId, "Sender ID cannot be null");
        this.message = Objects.requireNonNull(message, "Message cannot be null");
        this.notificationType = Objects.requireNonNull(notificationType, "Notification type cannot be null");
        this.targetType = Objects.requireNonNull(targetType, "Target type cannot be null");
        this.priority = Objects.requireNonNull(priority, "Priority cannot be null");
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters
    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getMessage() { return message; }
    public String getNotificationType() { return notificationType; }
    public String getTargetType() { return targetType; }
    public NotificationPriority getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * Checks if notification is critical
     * @return true if critical priority, false otherwise
     */
    public boolean isCritical() {
        return priority == NotificationPriority.CRITICAL;
    }
    
    /**
     * Gets formatted message with timestamp
     * @return formatted notification message
     */
    public String getFormattedMessage() {
        return String.format("[%s] %s", 
            createdAt.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")), 
            message);
    }
    
    /**
     * Simple ID generator for notifications
     * @return generated ID
     */
    private static String generateId() {
        return "NOTIF_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Notification that = (Notification) obj;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Notification{type='%s', target='%s', priority=%s, message='%s'}", 
                           notificationType, targetType, priority, message);
    }
}
