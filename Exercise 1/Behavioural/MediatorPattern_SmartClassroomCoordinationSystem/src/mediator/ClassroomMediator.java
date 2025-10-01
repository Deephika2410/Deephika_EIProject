package mediator;

import models.*;
import participants.ClassroomParticipant;

/**
 * Mediator interface that defines the contract for classroom coordination.
 * This interface follows the Mediator Pattern by providing a central
 * communication hub for all classroom participants.
 * 
 * Design Principles Applied:
 * - Interface Segregation: Clean, focused interface
 * - Dependency Inversion: Depend on abstractions
 */
public interface ClassroomMediator {
    
    /**
     * Registers a participant in the classroom system
     * Enables dynamic addition of participants at runtime
     * 
     * @param participant The participant to register
     */
    void registerParticipant(ClassroomParticipant participant);
    
    /**
     * Removes a participant from the classroom system
     * Enables dynamic removal of participants at runtime
     * 
     * @param participant The participant to remove
     */
    void removeParticipant(ClassroomParticipant participant);
    
    /**
     * Handles assignment posting from teachers
     * Routes assignment to relevant students and systems
     * 
     * @param assignment The assignment to post
     * @param teacherId The ID of the teacher posting
     */
    void postAssignment(Assignment assignment, String teacherId);
    
    /**
     * Handles submission of assignments from students
     * Routes submission to grading system and teacher
     * 
     * @param submission The student submission
     * @param studentId The ID of the submitting student
     */
    void submitAssignment(Submission submission, String studentId);
    
    /**
     * Handles questions from students
     * Routes questions to teachers and relevant systems
     * 
     * @param question The question from student
     * @param studentId The ID of the asking student
     */
    void askQuestion(Question question, String studentId);
    
    /**
     * Handles grading completion from grading system
     * Routes grades to students and teachers
     * 
     * @param grade The completed grade
     * @param gradingSystemId The ID of the grading system
     */
    void publishGrade(Grade grade, String gradingSystemId);
    
    /**
     * Sends notifications through the notification system
     * Routes notifications to relevant participants
     * 
     * @param notification The notification to send
     * @param senderId The ID of the sender
     */
    void sendNotification(Notification notification, String senderId);
    
    /**
     * Gets all registered participants of a specific type
     * Supports dynamic participant management
     * 
     * @param participantType The type of participants to retrieve
     * @return Array of matching participants
     */
    ClassroomParticipant[] getParticipantsByType(String participantType);
}
