import mediator.*;
import participants.*;
import models.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Smart Classroom Coordination System - Completely Dynamic Runtime Application
 * 
 * This application demonstrates the Mediator Pattern through a fully interactive
 * smart classroom system that creates all data dynamically based on user input.
 * No hardcoded or fake data - everything happens at runtime!
 */
public class SmartClassroomDemoApp {
    
    private static final SmartClassroomCoordinator coordinator = new SmartClassroomCoordinator();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    
    // Dynamic storage for runtime-created participants
    private static List<Teacher> teachers = new ArrayList<>();
    private static List<Student> students = new ArrayList<>();
    private static List<NotificationSystem> notificationSystems = new ArrayList<>();
    private static List<GradingSystem> gradingSystems = new ArrayList<>();
    
    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println("   SMART CLASSROOM COORDINATION SYSTEM");
        System.out.println("   Demonstrating Mediator Design Pattern");
        System.out.println("   100% DYNAMIC - NO HARDCODED DATA");
        System.out.println("===============================================\n");
        
        displayPatternExplanation();
        runInteractiveClassroom();
        
        System.out.println("\nThank you for exploring the Smart Classroom System!");
        scanner.close();
    }
    
    private static void displayPatternExplanation() {
        System.out.println("MEDIATOR PATTERN EXPLANATION:");
        System.out.println("------------------------------");
        System.out.println("* Defines how objects communicate through a central mediator");
        System.out.println("* Participants don't know about each other directly");
        System.out.println("* Promotes loose coupling and high cohesion");
        System.out.println("* Enables dynamic addition/removal of participants");
        System.out.println("* Simplifies maintenance and testing\n");
        
        System.out.println("SYSTEM ARCHITECTURE:");
        System.out.println("---------------------");
        System.out.println("* SmartClassroomCoordinator: Central mediator hub");
        System.out.println("* Teacher: Posts assignments, answers questions");
        System.out.println("* Student: Submits assignments, asks questions");
        System.out.println("* NotificationSystem: Manages alerts and reminders");
        System.out.println("* GradingSystem: Automatically evaluates submissions\n");
        
        pauseForUser("Press Enter to continue...");
    }
    
    private static void runInteractiveClassroom() {
        System.out.println("WELCOME TO DYNAMIC CLASSROOM SETUP");
        System.out.println("===================================");
        System.out.println("Let's build your classroom from scratch!\n");
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    createDynamicTeacher();
                    break;
                case 2:
                    createDynamicStudent();
                    break;
                case 3:
                    createDynamicNotificationSystem();
                    break;
                case 4:
                    createDynamicGradingSystem();
                    break;
                case 5:
                    performClassroomActivity();
                    break;
                case 6:
                    showCurrentClassroom();
                    break;
                case 7:
                    showSystemStatistics();
                    break;
                case 8:
                    running = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please try again.");
            }
            
            if (running) {
                pauseForUser("\nPress Enter to continue...");
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n==================================================");
        System.out.println("         SMART CLASSROOM MAIN MENU");
        System.out.println("==================================================");
        System.out.println("1. [+] Create Teacher");
        System.out.println("2. [+] Create Student");
        System.out.println("3. [+] Create Notification System");
        System.out.println("4. [+] Create Grading System");
        System.out.println("5. [ACTION] Perform Classroom Activity");
        System.out.println("6. [VIEW] Show Current Classroom");
        System.out.println("7. [STATS] Show System Statistics");
        System.out.println("8. [EXIT] Exit System");
        System.out.println("==================================================");
        System.out.print("Enter your choice (1-8): ");
    }
    
    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void createDynamicTeacher() {
        System.out.println("\n----------------------------------------");
        System.out.println("    CREATING NEW TEACHER");
        System.out.println("----------------------------------------");
        
        String teacherId = generateUniqueId("TEACH_");
        
        System.out.print("Enter teacher's name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Teacher_" + System.currentTimeMillis();
        }
        
        System.out.print("Enter department/subject: ");
        String department = scanner.nextLine().trim();
        if (department.isEmpty()) {
            department = "General Education";
        }
        
        Teacher teacher = new Teacher(teacherId, name, department);
        teachers.add(teacher);
        coordinator.registerParticipant(teacher);
        
        System.out.println("[SUCCESS] Teacher created and registered!");
        System.out.println("ID: " + teacherId);
        System.out.println("Name: " + name);
        System.out.println("Department: " + department);
    }
    
    private static void createDynamicStudent() {
        System.out.println("\n----------------------------------------");
        System.out.println("    CREATING NEW STUDENT");
        System.out.println("----------------------------------------");
        
        String studentId = generateUniqueId("STU_");
        
        System.out.print("Enter student's name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Student_" + System.currentTimeMillis();
        }
        
        String enrollmentId = generateUniqueId("ENR_");
        
        System.out.print("Enter program/major: ");
        String program = scanner.nextLine().trim();
        if (program.isEmpty()) {
            program = "General Studies";
        }
        
        Student student = new Student(studentId, name, enrollmentId, program);
        students.add(student);
        coordinator.registerParticipant(student);
        
        System.out.println("[SUCCESS] Student created and registered!");
        System.out.println("ID: " + studentId);
        System.out.println("Name: " + name);
        System.out.println("Enrollment: " + enrollmentId);
        System.out.println("Program: " + program);
    }
    
    private static void createDynamicNotificationSystem() {
        System.out.println("\n----------------------------------------");
        System.out.println("  CREATING NOTIFICATION SYSTEM");
        System.out.println("----------------------------------------");
        
        String systemId = generateUniqueId("NOTIF_");
        
        System.out.print("Enter notification system name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Notification_System_" + System.currentTimeMillis();
        }
        
        System.out.println("Select notification strategy:");
        System.out.println("1. Email Notifications");
        System.out.println("2. Push Notifications");
        System.out.println("3. SMS Notifications");
        System.out.print("Enter choice (1-3): ");
        
        NotificationSystem.NotificationStrategy strategy;
        int choice = getUserChoice();
        switch (choice) {
            case 2:
                strategy = new NotificationSystem.PushNotificationStrategy();
                break;
            case 3:
                strategy = new NotificationSystem.SMSNotificationStrategy();
                break;
            default:
                strategy = new NotificationSystem.EmailNotificationStrategy();
        }
        
        NotificationSystem notificationSystem = new NotificationSystem(systemId, name, strategy);
        notificationSystems.add(notificationSystem);
        coordinator.registerParticipant(notificationSystem);
        
        System.out.println("[SUCCESS] Notification System created and registered!");
        System.out.println("ID: " + systemId);
        System.out.println("Name: " + name);
        System.out.println("Strategy: " + strategy.getStrategyName());
    }
    
    private static void createDynamicGradingSystem() {
        System.out.println("\n----------------------------------------");
        System.out.println("    CREATING GRADING SYSTEM");
        System.out.println("----------------------------------------");
        
        String systemId = generateUniqueId("GRADE_");
        
        System.out.print("Enter grading system name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Grading_System_" + System.currentTimeMillis();
        }
        
        System.out.println("Select grading strategy:");
        System.out.println("1. Automated Grading");
        System.out.println("2. Rubric-Based Grading");
        System.out.print("Enter choice (1-2): ");
        
        GradingSystem.GradingStrategy strategy;
        int choice = getUserChoice();
        if (choice == 2) {
            strategy = new GradingSystem.RubricGradingStrategy();
        } else {
            strategy = new GradingSystem.AutomatedGradingStrategy();
        }
        
        GradingSystem gradingSystem = new GradingSystem(systemId, name, strategy);
        gradingSystems.add(gradingSystem);
        coordinator.registerParticipant(gradingSystem);
        
        System.out.println("[SUCCESS] Grading System created and registered!");
        System.out.println("ID: " + systemId);
        System.out.println("Name: " + name);
        System.out.println("Strategy: " + strategy.getStrategyName());
    }
    
    private static void performClassroomActivity() {
        if (teachers.isEmpty() && students.isEmpty()) {
            System.out.println("[ERROR] No participants available. Please create teachers and students first.");
            return;
        }
        
        System.out.println("\n----------------------------------------");
        System.out.println("  SELECT CLASSROOM ACTIVITY");
        System.out.println("----------------------------------------");
        System.out.println("1. Teacher Posts Assignment");
        System.out.println("2. Student Submits Assignment");
        System.out.println("3. Student Asks Question");
        System.out.println("4. Teacher Answers Question");
        System.out.println("5. Trigger Auto-Grading");
        System.out.print("Enter choice (1-5): ");
        
        int choice = getUserChoice();
        switch (choice) {
            case 1:
                teacherPostsAssignment();
                break;
            case 2:
                studentSubmitsAssignment();
                break;
            case 3:
                studentAsksQuestion();
                break;
            case 4:
                teacherAnswersQuestion();
                break;
            case 5:
                triggerAutoGrading();
                break;
            default:
                System.out.println("[ERROR] Invalid activity choice.");
        }
    }
    
    private static void teacherPostsAssignment() {
        if (teachers.isEmpty()) {
            System.out.println("[ERROR] No teachers available.");
            return;
        }
        
        Teacher selectedTeacher = selectTeacher();
        if (selectedTeacher == null) return;
        
        System.out.println("\nCREATING NEW ASSIGNMENT");
        System.out.println("------------------------------");
        
        String assignmentId = generateUniqueId("ASSIGN_");
        
        System.out.print("Enter assignment title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            title = "Assignment_" + System.currentTimeMillis();
        }
        
        System.out.print("Enter assignment description: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = "Dynamic assignment created at runtime by " + selectedTeacher.getName();
        }
        
        System.out.print("Enter due date (or press Enter for 7 days from now): ");
        String dueDateStr = scanner.nextLine().trim();
        LocalDateTime dueDate = dueDateStr.isEmpty() ? 
            LocalDateTime.now().plusDays(7) : 
            LocalDateTime.now().plusHours(24); // Simplified for demo
        
        Assignment assignment = new Assignment(assignmentId, title, description, 
                                             selectedTeacher.getId(), dueDate, 100);
        
        coordinator.postAssignment(assignment, selectedTeacher.getId());
        System.out.println("[SUCCESS] Assignment posted dynamically!");
    }
    
    private static void studentSubmitsAssignment() {
        if (students.isEmpty()) {
            System.out.println("[ERROR] No students available.");
            return;
        }
        
        Student selectedStudent = selectStudent();
        if (selectedStudent == null) return;
        
        System.out.print("Enter assignment title to submit for: ");
        String assignmentTitle = scanner.nextLine().trim();
        if (assignmentTitle.isEmpty()) {
            assignmentTitle = "Runtime_Assignment_" + System.currentTimeMillis();
        }
        
        String submissionId = generateUniqueId("SUB_");
        
        System.out.print("Enter your submission content: ");
        String content = scanner.nextLine().trim();
        if (content.isEmpty()) {
            content = "Dynamic submission by " + selectedStudent.getName() + " at " + LocalDateTime.now();
        }
        
        // Create a dynamic assignment for this submission
        Assignment assignment = new Assignment(generateUniqueId("ASSIGN_"), 
                                             assignmentTitle, "Dynamic assignment created at runtime", 
                                             "DYNAMIC_TEACHER", LocalDateTime.now().plusDays(1), 100);
        
        Submission submission = new Submission(submissionId, assignment.getId(), 
                                             assignment.getTitle(), selectedStudent.getId(), 
                                             content);
        
        coordinator.submitAssignment(submission, selectedStudent.getId());
        System.out.println("[SUCCESS] Assignment submitted dynamically!");
    }
    
    private static void studentAsksQuestion() {
        if (students.isEmpty()) {
            System.out.println("[ERROR] No students available.");
            return;
        }
        
        Student selectedStudent = selectStudent();
        if (selectedStudent == null) return;
        
        String questionId = generateUniqueId("Q_");
        
        System.out.print("Enter your question: ");
        String questionText = scanner.nextLine().trim();
        if (questionText.isEmpty()) {
            questionText = "Dynamic question from " + selectedStudent.getName() + " at " + LocalDateTime.now();
        }
        
        Question question = new Question(questionId, selectedStudent.getId(), 
                                       questionText, "General Question", 
                                       Question.QuestionPriority.MEDIUM, false);
        
        coordinator.askQuestion(question, selectedStudent.getId());
        System.out.println("[SUCCESS] Question asked dynamically!");
    }
    
    private static void teacherAnswersQuestion() {
        if (teachers.isEmpty()) {
            System.out.println("[ERROR] No teachers available.");
            return;
        }
        
        Teacher selectedTeacher = selectTeacher();
        if (selectedTeacher == null) return;
        
        System.out.print("Enter the question ID to answer (or press Enter for demo): ");
        String questionId = scanner.nextLine().trim();
        if (questionId.isEmpty()) {
            questionId = generateUniqueId("Q_");
        }
        
        System.out.print("Enter your answer: ");
        String answerText = scanner.nextLine().trim();
        if (answerText.isEmpty()) {
            answerText = "Dynamic answer from " + selectedTeacher.getName() + " at " + LocalDateTime.now();
        }
        
        Notification answerNotification = new Notification(selectedTeacher.getId(),
            "ANSWER to " + questionId + ": " + answerText,
            "ANSWER",
            "ALL");
        
        coordinator.sendNotification(answerNotification, selectedTeacher.getId());
        System.out.println("[SUCCESS] Answer provided dynamically!");
    }
    
    private static void triggerAutoGrading() {
        if (gradingSystems.isEmpty()) {
            System.out.println("[ERROR] No grading systems available.");
            return;
        }
        
        GradingSystem selectedGrader = selectGradingSystem();
        if (selectedGrader == null) return;
        
        Notification gradingNotification = new Notification(selectedGrader.getId(),
            "TRIGGER_AUTO_GRADING",
            "GRADING_REQUEST",
            "GRADING_SYSTEM");
        
        coordinator.sendNotification(gradingNotification, selectedGrader.getId());
        System.out.println("[SUCCESS] Auto-grading triggered dynamically!");
    }
    
    private static Teacher selectTeacher() {
        if (teachers.size() == 1) {
            return teachers.get(0);
        }
        
        System.out.println("Available Teachers:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i).getName());
        }
        System.out.print("Select teacher (1-" + teachers.size() + "): ");
        
        int choice = getUserChoice() - 1;
        if (choice >= 0 && choice < teachers.size()) {
            return teachers.get(choice);
        }
        
        System.out.println("[ERROR] Invalid teacher selection.");
        return null;
    }
    
    private static Student selectStudent() {
        if (students.size() == 1) {
            return students.get(0);
        }
        
        System.out.println("Available Students:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getName());
        }
        System.out.print("Select student (1-" + students.size() + "): ");
        
        int choice = getUserChoice() - 1;
        if (choice >= 0 && choice < students.size()) {
            return students.get(choice);
        }
        
        System.out.println("[ERROR] Invalid student selection.");
        return null;
    }
    
    private static GradingSystem selectGradingSystem() {
        if (gradingSystems.size() == 1) {
            return gradingSystems.get(0);
        }
        
        System.out.println("Available Grading Systems:");
        for (int i = 0; i < gradingSystems.size(); i++) {
            System.out.println((i + 1) + ". " + gradingSystems.get(i).getName());
        }
        System.out.print("Select grading system (1-" + gradingSystems.size() + "): ");
        
        int choice = getUserChoice() - 1;
        if (choice >= 0 && choice < gradingSystems.size()) {
            return gradingSystems.get(choice);
        }
        
        System.out.println("[ERROR] Invalid grading system selection.");
        return null;
    }
    
    private static void showCurrentClassroom() {
        System.out.println("\n==================================================");
        System.out.println("         CURRENT CLASSROOM STATE");
        System.out.println("==================================================");
        
        System.out.println("TEACHERS (" + teachers.size() + "):");
        for (Teacher teacher : teachers) {
            System.out.println("  - " + teacher.getName() + " (" + teacher.getSubject() + ")");
        }
        
        System.out.println("\nSTUDENTS (" + students.size() + "):");
        for (Student student : students) {
            System.out.println("  - " + student.getName() + " (" + student.getMajor() + ")");
        }
        
        System.out.println("\nNOTIFICATION SYSTEMS (" + notificationSystems.size() + "):");
        for (NotificationSystem system : notificationSystems) {
            System.out.println("  - " + system.getName());
        }
        
        System.out.println("\nGRADING SYSTEMS (" + gradingSystems.size() + "):");
        for (GradingSystem system : gradingSystems) {
            System.out.println("  - " + system.getName());
        }
        
        if (teachers.isEmpty() && students.isEmpty()) {
            System.out.println("\n[NOTICE] Classroom is empty. Create participants to get started!");
        }
    }
    
    private static void showSystemStatistics() {
        System.out.println("\n==================================================");
        System.out.println("         SYSTEM STATISTICS");
        System.out.println("==================================================");
        
        coordinator.displayParticipantStats();
        coordinator.displayActivityLog();
        
        System.out.println("\nMEDIATOR PATTERN BENEFITS DEMONSTRATED:");
        System.out.println("• Decoupled communication between all participants");
        System.out.println("• Dynamic participant registration at runtime");
        System.out.println("• Centralized coordination through SmartClassroomCoordinator");
        System.out.println("• No direct dependencies between participant types");
        System.out.println("• Easy to extend with new participant types");
        
        System.out.println("\nSOLID PRINCIPLES APPLIED:");
        System.out.println("• Single Responsibility: Each participant has one clear role");
        System.out.println("• Open/Closed: Easy to add new participant types");
        System.out.println("• Interface Segregation: Clean, focused interfaces");
        System.out.println("• Dependency Inversion: Depends on abstractions, not concretions");
    }
    
    private static String generateUniqueId(String prefix) {
        return prefix + System.currentTimeMillis() + "_" + random.nextInt(1000);
    }
    
    private static void pauseForUser(String message) {
        System.out.print("\n" + message);
        scanner.nextLine();
    }
}
