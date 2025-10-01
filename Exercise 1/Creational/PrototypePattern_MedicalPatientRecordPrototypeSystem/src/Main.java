import ui.MedicalRecordConsoleUI;

/**
 * Main application class for the Medical Patient Record Prototype System.
 * 
 * This application demonstrates the implementation of the Prototype design pattern
 * in a medical records management context. The Prototype pattern allows for
 * efficient creation of patient record objects by cloning existing prototypes
 * rather than creating new instances from scratch.
 * 
 * Key Features Demonstrated:
 * - Prototype Interface: PatientRecordPrototype defines the cloning contract
 * - Concrete Prototypes: BasicPatientRecord and EmergencyPatientRecord
 * - Prototype Registry: Centralized management of prototype templates
 * - Deep Cloning: Proper cloning of complex objects with nested data
 * - Dynamic Field Updates: Runtime modification of patient record fields
 * - Validation: Comprehensive record validation for data integrity
 * 
 * Design Pattern Benefits Showcased:
 * - Performance: Cloning is faster than creating complex objects from scratch
 * - Flexibility: Easy creation of variations from existing records
 * - Encapsulation: Object creation details are hidden from clients
 * - Consistency: Templates ensure standardized record structures
 * 
 * System Architecture:
 * - models/: Patient record classes and supporting models
 * - prototype/: Prototype interface and registry implementation
 * - management/: High-level record management operations
 * - enums/: Type-safe enumeration classes for medical data
 * - ui/: Console-based user interface
 * 
 * Usage Instructions:
 * 1. Compile: javac -d . src\*.java src\**\*.java
 * 2. Run: java Main
 * 3. Follow the interactive menu to explore prototype pattern features
 * 
 * @author Medical Records System
 * @version 1.0
 * @since 2025-09-29
 */
public class Main {
    
    /**
     * Main entry point for the Medical Patient Record Prototype System.
     * 
     * This method initializes the console-based user interface and starts
     * the interactive demonstration of the Prototype pattern implementation.
     * 
     * The application provides a comprehensive example of how the Prototype
     * pattern can be effectively used in real-world medical software systems
     * where efficient object creation and template-based record management
     * are essential requirements.
     * 
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        try {
            // Display system startup information
            System.out.println("Initializing Medical Patient Record Prototype System...");
            System.out.println("Loading prototype templates and registry...");
            
            // Create and start the console UI
            MedicalRecordConsoleUI consoleUI = new MedicalRecordConsoleUI();
            consoleUI.start();
            
        } catch (Exception e) {
            // Handle any unexpected startup errors
            System.err.println("Critical Error: Failed to start the application");
            System.err.println("Error Details: " + e.getMessage());
            System.err.println("Please check your Java environment and try again.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Displays information about the Prototype pattern implementation.
     * This method can be called to show educational information about
     * the design pattern being demonstrated.
     */
    public static void displayPatternInfo() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    PROTOTYPE PATTERN OVERVIEW");
        System.out.println("=".repeat(70));
        System.out.println("Intent:");
        System.out.println("  Specify the kinds of objects to create using a prototypical");
        System.out.println("  instance, and create new objects by copying this prototype.");
        System.out.println();
        System.out.println("Benefits in Medical Records System:");
        System.out.println("  - Fast creation of patient records from templates");
        System.out.println("  - Consistent record structure across different record types");
        System.out.println("  - Easy customization of cloned records for specific needs");
        System.out.println("  - Reduced complexity in object creation");
        System.out.println("  - Support for different record types (Basic, Emergency, etc.)");
        System.out.println();
        System.out.println("Key Components Implemented:");
        System.out.println("  1. Prototype Interface (PatientRecordPrototype)");
        System.out.println("  2. Concrete Prototypes (BasicPatientRecord, EmergencyPatientRecord)");
        System.out.println("  3. Prototype Registry (PatientRecordPrototypeRegistry)");
        System.out.println("  4. Client Code (PatientRecordManager)");
        System.out.println("=".repeat(70));
    }
}
