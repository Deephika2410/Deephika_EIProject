package ui;

import management.PatientRecordManager;
import prototype.PatientRecordPrototype;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for the Medical Patient Record Prototype System.
 * 
 * This class provides an interactive menu system that demonstrates
 * the Prototype pattern implementation for medical patient records.
 * It allows users to create, clone, and manage patient records dynamically.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public class MedicalRecordConsoleUI {
    private PatientRecordManager recordManager;
    private Scanner scanner;
    private boolean running;
    
    /**
     * Constructor for MedicalRecordConsoleUI.
     */
    public MedicalRecordConsoleUI() {
        this.recordManager = new PatientRecordManager();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    /**
     * Starts the console user interface.
     */
    public void start() {
        displayWelcomeMessage();
        
        while (running) {
            try {
                displayMainMenu();
                int choice = getUserChoice();
                processMenuChoice(choice);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
        
        displayGoodbyeMessage();
    }
    
    /**
     * Displays the welcome message and system information.
     */
    private void displayWelcomeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    MEDICAL PATIENT RECORD PROTOTYPE SYSTEM");
        System.out.println("           Demonstrating Prototype Pattern");
        System.out.println("=".repeat(60));
        System.out.println("This system demonstrates the Prototype design pattern");
        System.out.println("for efficiently creating and managing patient records.");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Displays the main menu options.
     */
    private void displayMainMenu() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("              MAIN MENU");
        System.out.println("-".repeat(50));
        System.out.println("1. Create Basic Patient Record (from template)");
        System.out.println("2. Create Emergency Patient Record (from template)");
        System.out.println("3. Create Custom Basic Patient Record");
        System.out.println("4. Create Custom Emergency Patient Record");
        System.out.println("5. Clone Existing Record");
        System.out.println("6. View All Active Records");
        System.out.println("7. View Registry Information");
        System.out.println("8. Validate All Records");
        System.out.println("9. Clear All Active Records");
        System.out.println("0. Exit System");
        System.out.println("-".repeat(50));
        System.out.print("Please select an option (0-9): ");
    }
    
    /**
     * Gets the user's menu choice.
     * 
     * @return The selected menu option
     */
    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }
    
    /**
     * Processes the user's menu selection.
     * 
     * @param choice The menu option selected by the user
     */
    private void processMenuChoice(int choice) {
        switch (choice) {
            case 1 -> createBasicRecordFromTemplate();
            case 2 -> createEmergencyRecordFromTemplate();
            case 3 -> createCustomBasicRecord();
            case 4 -> createCustomEmergencyRecord();
            case 5 -> cloneExistingRecord();
            case 6 -> viewAllActiveRecords();
            case 7 -> viewRegistryInformation();
            case 8 -> validateAllRecords();
            case 9 -> clearAllActiveRecords();
            case 0 -> exitSystem();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * Creates a basic patient record from template.
     */
    private void createBasicRecordFromTemplate() {
        System.out.println("\n=== Creating Basic Patient Record from Template ===");
        PatientRecordPrototype record = recordManager.createPatientRecord("BASIC_TEMPLATE");
        
        if (record != null) {
            System.out.println("Basic patient record created successfully!");
            System.out.println("\nRecord Summary:");
            System.out.println(record.getRecordSummary());
        }
        
        waitForUserInput();
    }
    
    /**
     * Creates an emergency patient record from template.
     */
    private void createEmergencyRecordFromTemplate() {
        System.out.println("\n=== Creating Emergency Patient Record from Template ===");
        PatientRecordPrototype record = recordManager.createPatientRecord("EMERGENCY_TEMPLATE");
        
        if (record != null) {
            System.out.println("Emergency patient record created successfully!");
            System.out.println("\nRecord Summary:");
            System.out.println(record.getRecordSummary());
        }
        
        waitForUserInput();
    }
    
    /**
     * Creates a custom basic patient record with user input.
     */
    private void createCustomBasicRecord() {
        PatientRecordPrototype record = recordManager.createCustomBasicRecord();
        
        if (record != null) {
            System.out.println("\nRecord Summary:");
            System.out.println(record.getRecordSummary());
        }
        
        waitForUserInput();
    }
    
    /**
     * Creates a custom emergency patient record with user input.
     */
    private void createCustomEmergencyRecord() {
        PatientRecordPrototype record = recordManager.createCustomEmergencyRecord();
        
        if (record != null) {
            System.out.println("\nRecord Summary:");
            System.out.println(record.getRecordSummary());
        }
        
        waitForUserInput();
    }
    
    /**
     * Clones an existing patient record with intelligent duplicate handling.
     */
    private void cloneExistingRecord() {
        System.out.println("\n=== Clone Existing Record ===");
        
        if (recordManager.getActiveRecordCount() == 0) {
            System.out.println("No active records available to clone.");
            waitForUserInput();
            return;
        }
        
        // Display available records
        System.out.println("Available Records to Clone:");
        for (int i = 0; i < recordManager.getActiveRecordCount(); i++) {
            PatientRecordPrototype record = recordManager.getActiveRecord(i);
            String patientName = "Unknown";
            String recordType = record.getRecordType();
            
            if (record instanceof models.BasicPatientRecord basicRecord) {
                patientName = basicRecord.getFullName();
            } else if (record instanceof models.EmergencyPatientRecord emergencyRecord) {
                patientName = emergencyRecord.getFullName();
            }
            
            System.out.printf("%d. %s - %s%n", i + 1, recordType, patientName);
        }
        
        System.out.print("Select record to clone (1-" + recordManager.getActiveRecordCount() + "): ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            PatientRecordPrototype sourceRecord = recordManager.getActiveRecord(index);
            
            if (sourceRecord != null) {
                System.out.println("\nChoose cloning option:");
                System.out.println("1. Smart clone (checks for existing duplicates)");
                System.out.println("2. Clone with custom name (prevents duplicates)");
                System.out.println("3. Clone and edit specific fields");
                System.out.print("Choice (1-3): ");
                
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1 -> {
                        PatientRecordPrototype clonedRecord = recordManager.cloneExistingRecord(sourceRecord);
                        if (clonedRecord != null) {
                            System.out.println("\nCloned Record Summary:");
                            System.out.println(clonedRecord.getRecordSummary());
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter new first name: ");
                        String firstName = scanner.nextLine().trim();
                        System.out.print("Enter new last name: ");
                        String lastName = scanner.nextLine().trim();
                        
                        PatientRecordPrototype clonedRecord = recordManager.createOrUpdateClone(
                            sourceRecord, firstName, lastName);
                        if (clonedRecord != null) {
                            System.out.println("\nCloned Record Summary:");
                            System.out.println(clonedRecord.getRecordSummary());
                        }
                    }
                    case 3 -> {
                        PatientRecordPrototype clonedRecord = recordManager.cloneExistingRecord(sourceRecord);
                        if (clonedRecord != null) {
                            System.out.println("\n=== Record Cloned Successfully! ===");
                            System.out.println("Now you can selectively edit only the fields you want to change.");
                            
                            editClonedRecord(clonedRecord);
                            
                            System.out.println("\n=== Final Cloned Record Summary ===");
                            System.out.println(clonedRecord.getRecordSummary());
                            
                            System.out.println("\n=== Prototype Pattern Benefits Demonstrated ===");
                            System.out.println(" Inherited: Complete record structure from original");
                            System.out.println("Modified: Only selected fields were updated");
                            System.out.println("Preserved: Original record remains unchanged");
                            System.out.println("Efficient: No duplicate creation, smart duplicate detection");
                        }
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        
        waitForUserInput();
    }
    
    /**
     * Allows selective editing of a cloned record.
     * 
     * @param clonedRecord The cloned record to edit
     */
    private void editClonedRecord(PatientRecordPrototype clonedRecord) {
        boolean continuingEditing = true;
        
        while (continuingEditing) {
            System.out.println("\n=== Selective Field Editing ===");
            System.out.println("Choose which field to modify (or skip to keep inherited value):");
            System.out.println("1. First Name");
            System.out.println("2. Last Name");
            System.out.println("3. Phone Number");
            System.out.println("4. Email");
            System.out.println("5. Blood Type");
            System.out.println("6. Status");
            
            if (clonedRecord instanceof models.EmergencyPatientRecord) {
                System.out.println("7. Triage Level");
                System.out.println("8. Chief Complaint");
                System.out.println("9. Emergency Contact Name");
                System.out.println("10. Emergency Contact Phone");
            }
            
            System.out.println("0. Finish editing");
            System.out.print("Select field to edit (0-" + (clonedRecord instanceof models.EmergencyPatientRecord ? "10" : "6") + "): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 0:
                        continuingEditing = false;
                        break;
                    case 1:
                        System.out.print("Enter new first name: ");
                        String firstName = scanner.nextLine().trim();
                        if (!firstName.isEmpty()) {
                            boolean updated = clonedRecord.updateField("firstName", firstName);
                            System.out.println(updated ? "✓ First name updated" : "✗ Failed to update first name");
                        }
                        break;
                    case 2:
                        System.out.print("Enter new last name: ");
                        String lastName = scanner.nextLine().trim();
                        if (!lastName.isEmpty()) {
                            boolean updated = clonedRecord.updateField("lastName", lastName);
                            System.out.println(updated ? "✓ Last name updated" : "✗ Failed to update last name");
                        }
                        break;
                    case 3:
                        System.out.print("Enter new phone number: ");
                        String phone = scanner.nextLine().trim();
                        if (!phone.isEmpty()) {
                            boolean updated = clonedRecord.updateField("phoneNumber", phone);
                            System.out.println(updated ? "✓ Phone number updated" : "✗ Failed to update phone number");
                        }
                        break;
                    case 4:
                        System.out.print("Enter new email: ");
                        String email = scanner.nextLine().trim();
                        if (!email.isEmpty()) {
                            boolean updated = clonedRecord.updateField("email", email);
                            System.out.println(updated ? "✓ Email updated" : "✗ Failed to update email");
                        }
                        break;
                    case 5:
                        System.out.println("Select Blood Type:");
                        System.out.println("1. A+  2. A-  3. B+  4. B-");
                        System.out.println("5. AB+ 6. AB- 7. O+  8. O-");
                        System.out.print("Choice (1-8): ");
                        int bloodChoice = Integer.parseInt(scanner.nextLine().trim());
                        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
                        if (bloodChoice >= 1 && bloodChoice <= 8) {
                            boolean updated = clonedRecord.updateField("bloodType", bloodTypes[bloodChoice - 1]);
                            System.out.println(updated ? "✓ Blood type updated" : "✗ Failed to update blood type");
                        }
                        break;
                    case 6:
                        System.out.println("Select Status:");
                        System.out.println("1. Active  2. Inactive  3. Discharged  4. Pending");
                        System.out.print("Choice (1-4): ");
                        int statusChoice = Integer.parseInt(scanner.nextLine().trim());
                        String[] statuses = {"Active", "Inactive", "Discharged", "Pending"};
                        if (statusChoice >= 1 && statusChoice <= 4) {
                            boolean updated = clonedRecord.updateField("status", statuses[statusChoice - 1]);
                            System.out.println(updated ? "✓ Status updated" : "✗ Failed to update status");
                        }
                        break;
                    case 7:
                        if (clonedRecord instanceof models.EmergencyPatientRecord) {
                            System.out.println("Select Triage Level:");
                            System.out.println("1. CRITICAL  2. URGENT  3. LESS_URGENT  4. NON_URGENT");
                            System.out.print("Choice (1-4): ");
                            int triageChoice = Integer.parseInt(scanner.nextLine().trim());
                            String[] triagelevels = {"CRITICAL", "URGENT", "LESS_URGENT", "NON_URGENT"};
                            if (triageChoice >= 1 && triageChoice <= 4) {
                                boolean updated = clonedRecord.updateField("triageLevel", triagelevels[triageChoice - 1]);
                                System.out.println(updated ? "✓ Triage level updated" : "✗ Failed to update triage level");
                            }
                        }
                        break;
                    case 8:
                        if (clonedRecord instanceof models.EmergencyPatientRecord) {
                            System.out.print("Enter new chief complaint: ");
                            String complaint = scanner.nextLine().trim();
                            if (!complaint.isEmpty()) {
                                boolean updated = clonedRecord.updateField("chiefComplaint", complaint);
                                System.out.println(updated ? "✓ Chief complaint updated" : "✗ Failed to update chief complaint");
                            }
                        }
                        break;
                    case 9:
                        if (clonedRecord instanceof models.EmergencyPatientRecord) {
                            System.out.print("Enter emergency contact name: ");
                            String contactName = scanner.nextLine().trim();
                            if (!contactName.isEmpty()) {
                                boolean updated = clonedRecord.updateField("emergencyContactName", contactName);
                                System.out.println(updated ? "✓ Emergency contact name updated" : "✗ Failed to update emergency contact name");
                            }
                        }
                        break;
                    case 10:
                        if (clonedRecord instanceof models.EmergencyPatientRecord) {
                            System.out.print("Enter emergency contact phone: ");
                            String contactPhone = scanner.nextLine().trim();
                            if (!contactPhone.isEmpty()) {
                                boolean updated = clonedRecord.updateField("emergencyContactPhone", contactPhone);
                                System.out.println(updated ? "✓ Emergency contact phone updated" : "✗ Failed to update emergency contact phone");
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Displays all active records.
     */
    private void viewAllActiveRecords() {
        System.out.println("\n=== All Active Records ===");
        String summary = recordManager.getAllRecordsSummary();
        System.out.println(summary);
        waitForUserInput();
    }
    
    /**
     * Displays registry information.
     */
    private void viewRegistryInformation() {
        System.out.println("\n=== Registry Information ===");
        String registryInfo = recordManager.getRegistryInfo();
        System.out.println(registryInfo);
        waitForUserInput();
    }
    
    /**
     * Validates all active records.
     */
    private void validateAllRecords() {
        System.out.println("\n=== Record Validation Results ===");
        List<String> validationResults = recordManager.validateAllRecords();
        
        if (validationResults.isEmpty()) {
            System.out.println("No records to validate.");
        } else {
            for (String result : validationResults) {
                System.out.println(result);
            }
        }
        
        waitForUserInput();
    }
    
    /**
     * Clears all active records.
     */
    private void clearAllActiveRecords() {
        System.out.println("\n=== Clear All Active Records ===");
        System.out.print("Are you sure you want to clear all active records? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.startsWith("y")) {
            recordManager.clearActiveRecords();
            System.out.println("All active records have been cleared.");
        } else {
            System.out.println("Operation cancelled.");
        }
        
        waitForUserInput();
    }
    
    /**
     * Exits the system.
     */
    private void exitSystem() {
        System.out.println("\nExiting Medical Patient Record Prototype System...");
        running = false;
    }
    
    /**
     * Displays goodbye message.
     */
    private void displayGoodbyeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    Thank you for using the Medical Patient Record");
        System.out.println("              Prototype System!");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Waits for user input before continuing.
     */
    private void waitForUserInput() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
