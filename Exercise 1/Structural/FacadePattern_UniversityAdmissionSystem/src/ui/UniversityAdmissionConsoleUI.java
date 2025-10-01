package ui;

import facade.AdmissionFacade;
import facade.AdmissionResult;
import models.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * University Admission Console UI - Interactive user interface for the admission system
 * This class provides a user-friendly console interface to interact with the Facade Pattern
 * Demonstrates how clients interact with the simplified facade interface
 */
public class UniversityAdmissionConsoleUI {
    private Scanner scanner;
    private AdmissionFacade admissionFacade;
    private static final String SEPARATOR = "=".repeat(60);
    
    /**
     * Constructor initializes the console UI
     */
    public UniversityAdmissionConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.admissionFacade = new AdmissionFacade();
    }
    
    /**
     * Starts the console application
     */
    public void start() {
        displayWelcomeMessage();
        
        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int choice = getValidChoice(1, 7);
                
                switch (choice) {
                    case 1:
                        processNewAdmission();
                        break;
                    case 2:
                        displayAvailableCourses();
                        break;
                    case 3:
                        displayRealTimeCapacityStatus();
                        break;
                    case 4:
                        displaySystemStatistics();
                        break;
                    case 5:
                        processStudentWithdrawal();
                        break;
                    case 6:
                        displayHelpInformation();
                        break;
                    case 7:
                        running = false;
                        displayExitMessage();
                        break;
                }
                
                if (running) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    clearScreen();
                }
                
            } catch (Exception e) {
                System.out.println("[ERROR] An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
                scanner.nextLine(); // Clear any remaining input
            }
        }
    }
    
    /**
     * Displays welcome message and system information
     */
    private void displayWelcomeMessage() {
        clearScreen();
        System.out.println(SEPARATOR);
        System.out.println("    UNIVERSITY ADMISSION SYSTEM");
        System.out.println("    Powered by Facade Pattern Implementation");
        System.out.println(SEPARATOR);
        System.out.println("Welcome to our comprehensive admission portal!");
        System.out.println("This system demonstrates the Facade Pattern by providing");
        System.out.println("   a simple interface to complex admission subsystems:");
        System.out.println("   * Document Verification System");
        System.out.println("   * Fee Payment System");
        System.out.println("   * Course Allocation System");
        System.out.println("   * ID Generation System");
        System.out.println(SEPARATOR);
        System.out.println();
    }
    
    /**
     * Displays main menu options
     */
    private void displayMainMenu() {
        System.out.println("MAIN MENU");
        System.out.println("============");
        System.out.println("1. Apply for New Admission");
        System.out.println("2. View Available Courses");
        System.out.println("3. Real-Time Capacity Status");
        System.out.println("4. System Statistics");
        System.out.println("5. Student Withdrawal");
        System.out.println("6. Help & Information");
        System.out.println("7. Exit System");
        System.out.println();
        System.out.print("Please select an option (1-7): ");
    }
    
    /**
     * Processes new admission application
     */
    private void processNewAdmission() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("NEW ADMISSION APPLICATION");
        System.out.println(SEPARATOR);
        System.out.println("Please provide your information for admission:");
        System.out.println();
        
        try {
            // Collect student information
            Student student = collectStudentInformation();
            if (student == null) return; // User cancelled
            
            // Display available courses and get selection
            String selectedCourseCode = selectCourse();
            if (selectedCourseCode == null) return; // User cancelled
            
            // Collect document information
            List<Document.DocumentType> documentTypes = collectDocumentInformation();
            if (documentTypes == null || documentTypes.isEmpty()) return; // User cancelled
            
            // Select payment method
            Fee.PaymentMethod paymentMethod = selectPaymentMethod();
            if (paymentMethod == null) return; // User cancelled
            
            // Confirm application details
            if (!confirmApplicationDetails(student, selectedCourseCode, documentTypes, paymentMethod)) {
                System.out.println("ERROR: Application cancelled by user.");
                return;
            }
            
            // Process admission through facade
            System.out.println("\n Processing your admission application...");
            System.out.println(" This may take a few moments...");
            
            AdmissionResult result = admissionFacade.enrollStudent(student, selectedCourseCode, 
                                                                 documentTypes, paymentMethod);
            
            // Display final result
            displayAdmissionResult(result);
            
        } catch (Exception e) {
            System.out.println("ERROR: Error processing admission: " + e.getMessage());
            System.out.println(" Please try again or contact support.");
        }
    }
    
    /**
     * Collects student information from user input
     */
    private Student collectStudentInformation() {
        System.out.println(" STUDENT INFORMATION");
        System.out.println("=====================");
        
        try {
            System.out.print("Full Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("ERROR: Name cannot be empty!");
                return null;
            }
            
            System.out.print("Email Address: ");
            String email = scanner.nextLine().trim().toLowerCase();
            if (!isValidEmail(email)) {
                System.out.println("ERROR: Invalid email format!");
                return null;
            }
            
            System.out.print("Phone Number: ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) {
                System.out.println("ERROR: Phone number cannot be empty!");
                return null;
            }
            
            System.out.print("Date of Birth (DD-MM-YYYY): ");
            String dobInput = scanner.nextLine().trim();
            LocalDate dateOfBirth = parseDate(dobInput);
            if (dateOfBirth == null) {
                System.out.println("ERROR: Invalid date format! Please use DD-MM-YYYY");
                return null;
            }
            
            System.out.print("Address: ");
            String address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("ERROR: Address cannot be empty!");
                return null;
            }
            
            System.out.print("Emergency Contact: ");
            String emergencyContact = scanner.nextLine().trim();
            if (emergencyContact.isEmpty()) {
                System.out.println("ERROR: Emergency contact cannot be empty!");
                return null;
            }
            
            Student student = new Student(name, email, phone, dateOfBirth, address, emergencyContact);
            
            if (!student.isValidForAdmission()) {
                System.out.println("ERROR: Invalid student information provided!");
                return null;
            }
            
            System.out.println("SUCCESS: Student information collected successfully!");
            return student;
            
        } catch (Exception e) {
            System.out.println("ERROR: Error collecting student information: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Allows user to select a course
     */
    private String selectCourse() {
        System.out.println("\nCOURSE SELECTION");
        System.out.println("==================");
        
        // Display available courses with real-time capacity
        admissionFacade.displayAvailableCourses();
        
        System.out.println("Available Course Codes with Real-Time Capacity:");
        System.out.println("   CS101  - Computer Science & Engineering - " + admissionFacade.getCourseCapacityStatus("CS101"));
        System.out.println("   EE101  - Electrical & Electronics Engineering - " + admissionFacade.getCourseCapacityStatus("EE101"));
        System.out.println("   ME101  - Mechanical Engineering - " + admissionFacade.getCourseCapacityStatus("ME101"));
        System.out.println("   CE101  - Civil Engineering - " + admissionFacade.getCourseCapacityStatus("CE101"));
        System.out.println("   MBA101 - Master of Business Administration - " + admissionFacade.getCourseCapacityStatus("MBA101"));
        System.out.println("   BBA101 - Bachelor of Business Administration - " + admissionFacade.getCourseCapacityStatus("BBA101"));
        System.out.println("   MCA101 - Master of Computer Applications - " + admissionFacade.getCourseCapacityStatus("MCA101"));
        System.out.println("   BCom101- Bachelor of Commerce - " + admissionFacade.getCourseCapacityStatus("BCom101"));
        System.out.println();
        
        System.out.print("Enter Course Code (or 'capacity' for detailed view, 'cancel' to go back): ");
        String input = scanner.nextLine().trim().toUpperCase();
        
        if ("CANCEL".equals(input)) {
            return null;
        }
        
        if ("CAPACITY".equals(input)) {
            displayRealTimeCapacityStatus();
            return selectCourse(); // Show selection again after capacity view
        }
        
        if (input.isEmpty()) {
            System.out.println("ERROR: Course code cannot be empty!");
            return null;
        }
        
        // Check if course is available for enrollment
        if (admissionFacade.isCourseAvailable(input)) {
            System.out.println("Course available! Current status: " + admissionFacade.getCourseCapacityStatus(input));
        } else {
            System.out.println("WARNING: Course may be full or unavailable. Proceeding anyway (you may be waitlisted).");
        }
        
        return input;
    }
    
    /**
     * Collects document information from user
     */
    private List<Document.DocumentType> collectDocumentInformation() {
        System.out.println("\nDOCUMENT SUBMISSION");
        System.out.println("=====================");
        System.out.println("Please select the documents you are submitting:");
        System.out.println("   (Select multiple options by entering numbers separated by commas)");
        System.out.println();
        
        // Display document options
        Document.DocumentType[] docTypes = Document.DocumentType.values();
        for (int i = 0; i < docTypes.length; i++) {
            String mandatory = isMandatoryDocument(docTypes[i]) ? " (MANDATORY)" : " (Optional)";
            System.out.printf("%2d. %s%s%n", i + 1, docTypes[i].getDisplayName(), mandatory);
        }
        System.out.println();
        
        System.out.print("Enter document numbers (e.g., 1,2,3) or 'cancel': ");
        String input = scanner.nextLine().trim();
        
        if ("cancel".equalsIgnoreCase(input)) {
            return null;
        }
        
        List<Document.DocumentType> selectedDocuments = new ArrayList<>();
        
        try {
            String[] selections = input.split(",");
            for (String selection : selections) {
                int docIndex = Integer.parseInt(selection.trim()) - 1;
                if (docIndex >= 0 && docIndex < docTypes.length) {
                    selectedDocuments.add(docTypes[docIndex]);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid input format! Please enter numbers separated by commas.");
            return null;
        }
        
        if (selectedDocuments.isEmpty()) {
            System.out.println("ERROR: No documents selected!");
            return null;
        }
        
        System.out.println("\n Selected Documents:");
        for (Document.DocumentType docType : selectedDocuments) {
            System.out.println("   " + docType.getDisplayName());
        }
        
        return selectedDocuments;
    }
    
    /**
     * Allows user to select payment method
     */
    private Fee.PaymentMethod selectPaymentMethod() {
        System.out.println("\n PAYMENT METHOD SELECTION");
        System.out.println("===========================");
        System.out.println("Please select your preferred payment method:");
        System.out.println();
        
        Fee.PaymentMethod[] paymentMethods = Fee.PaymentMethod.values();
        for (int i = 0; i < paymentMethods.length; i++) {
            System.out.printf("%d. %s%n", i + 1, paymentMethods[i].getDisplayName());
        }
        System.out.println();
        
        int choice = getValidChoice(1, paymentMethods.length, " Select payment method (1-" + paymentMethods.length + ") or 0 to cancel: ");
        
        if (choice == 0) {
            return null;
        }
        
        return paymentMethods[choice - 1];
    }
    
    /**
     * Confirms application details with user
     */
    private boolean confirmApplicationDetails(Student student, String courseCode, 
                                            List<Document.DocumentType> documentTypes, 
                                            Fee.PaymentMethod paymentMethod) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("APPLICATION CONFIRMATION");
        System.out.println(SEPARATOR);
        System.out.println(" Student Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Course Code: " + courseCode);
        System.out.println(" Payment Method: " + paymentMethod.getDisplayName());
        System.out.println("Documents: " + documentTypes.size() + " document(s) selected");
        System.out.println(SEPARATOR);
        
        System.out.print("Confirm application submission? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        return "yes".equals(confirmation) || "y".equals(confirmation);
    }
    
    /**
     * Displays admission result to user
     */
    private void displayAdmissionResult(AdmissionResult result) {
        System.out.println("\n" + SEPARATOR);
        if (result.isSuccess()) {
            System.out.println(" ADMISSION SUCCESSFUL! ");
            System.out.println(" Congratulations! You have been successfully admitted!");
            
            if (result.getIdCardDetails() != null) {
                System.out.println("\nYour admission details:");
                System.out.println(" Student ID: " + result.getGeneratedStudentId());
                System.out.println("Course: " + result.getAllocatedCourse().getCourseName());
                System.out.println(" Transaction ID: " + result.getTransactionId());
                System.out.println("Total Fees Paid: Rs." + String.format("%,.2f", result.getTotalFees()));
            }
        } else {
            System.out.println("ADMISSION FAILED");
            System.out.println(" Unfortunately, your admission could not be processed.");
            System.out.println(" Reason: " + result.getMessage());
            
            System.out.println("\nProcess Details:");
            for (AdmissionResult.ProcessStep step : result.getProcessSteps()) {
                String status = step.isSuccess() ? "SUCCESS" : "FAILED";
                System.out.println("   " + status + " " + step.getStepName().replace("_", " "));
            }
            
            System.out.println("\nYou may try again or contact our admissions office for assistance.");
        }
        System.out.println(SEPARATOR);
    }
    
    /**
     * Displays available courses
     */
    private void displayAvailableCourses() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("AVAILABLE COURSES");
        System.out.println(SEPARATOR);
        admissionFacade.displayAvailableCourses();
    }
    
    /**
     * Displays system statistics
     */
    private void displaySystemStatistics() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("SYSTEM STATISTICS");
        System.out.println(SEPARATOR);
        
        AdmissionFacade.SystemStats stats = admissionFacade.getSystemStats();
        stats.displayStats();
        
        System.out.println("\nThe Facade Pattern simplifies access to these complex subsystems!");
    }
    
    /**
     * Displays help and information
     */
    private void displayHelpInformation() {
        System.out.println("\n" + SEPARATOR);
        System.out.println(" HELP & INFORMATION");
        System.out.println(SEPARATOR);
        System.out.println("FACADE PATTERN DEMONSTRATION");
        System.out.println("===============================");
        System.out.println("This system demonstrates the Facade Pattern by:");
        System.out.println();
        System.out.println(" COMPLEX SUBSYSTEMS (Hidden from you):");
        System.out.println("   * DocumentVerificationSystem - Handles document verification");
        System.out.println("   * FeePaymentSystem - Processes payments and fee calculations");
        System.out.println("   * CourseAllocationSystem - Manages course enrollment");
        System.out.println("   * IDGenerationSystem - Generates unique student IDs");
        System.out.println();
        System.out.println(" FACADE INTERFACE (What you interact with):");
        System.out.println("   * AdmissionFacade.enrollStudent() - Single method call");
        System.out.println("   * Automatically coordinates ALL subsystems");
        System.out.println("   * Handles the complete admission workflow");
        System.out.println("   * Provides simplified error handling");
        System.out.println();
        System.out.println(" BENEFITS:");
        System.out.println("   * Simple interface hides complexity");
        System.out.println("   * One method call handles entire process");
        System.out.println("   * Automatic error handling and rollback");
        System.out.println("   * Consistent workflow management");
        System.out.println();
        System.out.println("SUPPORT:");
        System.out.println("   * Email: admissions@university.edu");
        System.out.println("   * Phone: +91-XXX-XXX-XXXX");
        System.out.println("   * Office Hours: 9 AM - 5 PM (Mon-Fri)");
        System.out.println(SEPARATOR);
    }
    
    /**
     * Displays exit message
     */
    private void displayExitMessage() {
        System.out.println("\n" + SEPARATOR);
        System.out.println(" Thank you for using University Admission System!");
        System.out.println("This Facade Pattern demonstration showed how a single");
        System.out.println("   interface can simplify complex subsystem interactions.");
        System.out.println("We hope you learned something valuable today!");
        System.out.println(" Goodbye and good luck with your studies!");
        System.out.println(SEPARATOR);
    }
    
    // Utility Methods
    
    /**
     * Gets valid choice from user within specified range
     */
    private int getValidChoice(int min, int max) {
        return getValidChoice(min, max, " Enter your choice (" + min + "-" + max + "): ");
    }
    
    /**
     * Gets valid choice from user with custom prompt
     */
    private int getValidChoice(int min, int max, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                if (choice >= min && choice <= max) {
                    return choice;
                } else if (choice == 0 && prompt.contains("cancel")) {
                    return 0; // Allow cancel option
                } else {
                    System.out.println("ERROR: Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a valid number!");
            }
        }
    }
    
    /**
     * Validates email format
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    /**
     * Parses date string to LocalDate
     */
    private LocalDate parseDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Checks if document type is mandatory
     */
    private boolean isMandatoryDocument(Document.DocumentType docType) {
        return docType == Document.DocumentType.BIRTH_CERTIFICATE ||
               docType == Document.DocumentType.ACADEMIC_TRANSCRIPT ||
               docType == Document.DocumentType.IDENTITY_PROOF ||
               docType == Document.DocumentType.ADDRESS_PROOF ||
               docType == Document.DocumentType.PASSPORT_PHOTO ||
               docType == Document.DocumentType.CHARACTER_CERTIFICATE;
    }
    
    /**
     * Displays real-time capacity status for all courses
     */
    private void displayRealTimeCapacityStatus() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("REAL-TIME CAPACITY MONITORING");
        System.out.println(SEPARATOR);
        
        // Use facade to get real-time capacity information
        admissionFacade.displayRealTimeCapacityStatus();
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Processes student withdrawal from the system
     */
    private void processStudentWithdrawal() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("STUDENT WITHDRAWAL PROCESS");
        System.out.println(SEPARATOR);
        
        System.out.print("Enter student email address: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println("ERROR: Email address cannot be empty");
            return;
        }
        
        // Validate email format
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("ERROR: Invalid email format");
            return;
        }
        
        System.out.println("\nProcessing withdrawal for: " + email);
        System.out.print("Are you sure you want to proceed? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("yes") || confirmation.equals("y")) {
            boolean success = admissionFacade.withdrawStudent(email);
            if (success) {
                System.out.println("\nSUCCESS: Student withdrawal completed successfully!");
                System.out.println("Capacity has been updated and waitlist processed automatically.");
            } else {
                System.out.println("\nERROR: Withdrawal failed. Please check student details.");
            }
        } else {
            System.out.println("Withdrawal cancelled.");
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Clears the console screen
     */
    private void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            // Fallback: print new lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
