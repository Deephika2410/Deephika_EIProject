package participants;

import mediator.ClassroomMediator;
import models.Notification;

/**
 * Abstract base class for all classroom participants.
 * Implements common functionality following the Template Method pattern.
 * 
 * Design Principles Applied:
 * - Single Responsibility: Manages participant basics
 * - Open/Closed: Open for extension via inheritance
 * - Template Method Pattern: Defines common structure
 */
public abstract class ClassroomParticipant {
    
    protected final String id;
    protected final String name;
    protected ClassroomMediator mediator;
    protected boolean isActive;
    
    protected ClassroomParticipant(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Participant ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Participant name cannot be null or empty");
        }
        
        this.id = id.trim();
        this.name = name.trim();
        this.isActive = true;
    }
    
    /**
     * Gets the unique identifier for this participant
     * @return participant ID
     */
    public final String getId() {
        return id;
    }
    
    /**
     * Gets the display name of this participant
     * @return participant name
     */
    public final String getName() {
        return name;
    }
    
    /**
     * Gets the type of participant (implemented by subclasses)
     * @return participant type string
     */
    public abstract String getParticipantType();
    
    /**
     * Sets the mediator for this participant
     * @param mediator the classroom mediator
     */
    public final void setMediator(ClassroomMediator mediator) {
        this.mediator = mediator;
    }
    
    /**
     * Gets the mediator for this participant
     * @return the classroom mediator
     */
    protected final ClassroomMediator getMediator() {
        return mediator;
    }
    
    /**
     * Receives a notification from the mediator
     * Template method that can be overridden by subclasses
     * @param notification the notification to process
     */
    public void receiveNotification(Notification notification) {
        if (!isActive) {
            return; // Ignore notifications if participant is inactive
        }
        
        // Default implementation - can be overridden
        processNotification(notification);
    }
    
    /**
     * Template method for processing notifications
     * Must be implemented by subclasses
     * @param notification the notification to process
     */
    protected abstract void processNotification(Notification notification);
    
    /**
     * Activates the participant
     */
    public final void activate() {
        this.isActive = true;
        System.out.println("[+] " + getParticipantType() + " '" + name + "' is now active");
    }
    
    /**
     * Deactivates the participant
     */
    public final void deactivate() {
        this.isActive = false;
        System.out.println("[-] " + getParticipantType() + " '" + name + "' is now inactive");
    }
    
    /**
     * Checks if participant is active
     * @return true if active, false otherwise
     */
    public final boolean isActive() {
        return isActive;
    }
    
    /**
     * Validates that mediator is set before operations
     * @throws IllegalStateException if mediator is not set
     */
    protected final void validateMediatorSet() {
        if (mediator == null) {
            throw new IllegalStateException("Mediator must be set before performing operations");
        }
    }
    
    /**
     * Gets a display-friendly description of this participant
     * @return formatted description
     */
    public String getDescription() {
        return String.format("%s: %s (ID: %s, Active: %s)", 
                           getParticipantType(), name, id, isActive);
    }
    
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClassroomParticipant that = (ClassroomParticipant) obj;
        return id.equals(that.id);
    }
    
    @Override
    public final int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return getDescription();
    }
}
