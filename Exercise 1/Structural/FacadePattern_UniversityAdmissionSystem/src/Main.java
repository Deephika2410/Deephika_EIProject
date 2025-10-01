import ui.UniversityAdmissionConsoleUI;

/**
 * Main Application Class for University Admission System
 * 
 * This class demonstrates the Facade Pattern in a real-world university admission scenario.
 * 
 * FACADE PATTERN DEMONSTRATION:
 * ============================
 * 
 * PROBLEM:
 * --------
 * University admission involves multiple complex subsystems:
 * - Document Verification (checking academic certificates, identity proofs, etc.)
 * - Fee Payment Processing (calculating fees, processing payments)
 * - Course Allocation (checking availability, seat allocation)
 * - Student ID Generation (creating unique IDs, ID cards)
 * 
 * Without Facade Pattern, clients would need to:
 * - Understand each subsystem's interface
 * - Handle complex interactions between subsystems
 * - Manage the proper sequence of operations
 * - Handle errors from multiple systems
 * 
 * SOLUTION - FACADE PATTERN:
 * -------------------------
 * The AdmissionFacade provides a single, simplified interface that:
 * - Hides the complexity of multiple subsystems
 * - Coordinates all subsystems automatically
 * - Provides a single method: enrollStudent()
 * - Handles all error scenarios gracefully
 * - Manages the complete workflow internally
 * 
 * PATTERN PARTICIPANTS:
 * --------------------
 * 1. Facade (AdmissionFacade): 
 *    - Provides unified interface to admission process
 *    - Delegates work to appropriate subsystems
 *    - Manages workflow and error handling
 * 
 * 2. Subsystems:
 *    - DocumentVerificationSystem: Handles document verification
 *    - FeePaymentSystem: Processes fee calculation and payment
 *    - CourseAllocationSystem: Manages course allocation
 *    - IDGenerationSystem: Generates student IDs
 * 
 * 3. Client (Console UI):
 *    - Uses simple facade interface
 *    - No direct interaction with subsystems
 *    - Single method call for complete admission
 * 
 * BENEFITS DEMONSTRATED:
 * ---------------------
 * - Simplified Interface: One method vs multiple subsystem calls
 * - Loose Coupling: Client doesn't depend on subsystem details
 * - Better Maintainability: Changes in subsystems don't affect client
 * - Improved Error Handling: Centralized error management
 * - Consistent Workflow: Facade ensures proper sequence
 * 
 * CONSOLE FLOW:
 * ------------
 * 1. User enters student details (name, email, phone, etc.)
 * 2. User selects course from available options
 * 3. User submits required documents
 * 4. User selects payment method
 * 5. Single facade call: admissionFacade.enrollStudent()
 * 6. Facade internally handles:
 *    - Document verification
 *    - Fee calculation and payment
 *    - Course allocation
 *    - Student ID generation
 * 7. User receives comprehensive admission result
 * 
 * @author University Admission System Team
 * @version 1.0
 * @since 2024
 */
public class Main {
    
    /**
     * Main method - Entry point of the application
     * Initializes the console UI and starts the admission system
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Display pattern information
            displayPatternInfo();
            
            // Initialize and start the console UI
            UniversityAdmissionConsoleUI consoleUI = new UniversityAdmissionConsoleUI();
            consoleUI.start();
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to start the application");
            System.err.println("Error Details: " + e.getMessage());
            System.err.println("Please check your system configuration and try again.");
            
            // Print stack trace for debugging (in development)
            if (Boolean.parseBoolean(System.getProperty("debug", "false"))) {
                e.printStackTrace();
            }
            
            System.exit(1);
        }
    }
    
    /**
     * Displays information about the Facade Pattern implementation
     */
    private static void displayPatternInfo() {
        System.out.println("+" + "=".repeat(78) + "+");
        System.out.println("|" + " ".repeat(25) + "FACADE PATTERN DEMONSTRATION" + " ".repeat(25) + "|");
        System.out.println("|" + " ".repeat(23) + "University Admission System" + " ".repeat(27) + "|");
        System.out.println("+" + "=".repeat(78) + "+");
        System.out.println("| PURPOSE: Demonstrate how Facade Pattern simplifies complex systems      |");
        System.out.println("|                                                                            |");
        System.out.println("| ARCHITECTURE:                                                          |");
        System.out.println("|    Client (Console UI) -> Facade -> Multiple Subsystems                    |");
        System.out.println("|                                                                            |");
        System.out.println("| SUBSYSTEMS:                                                             |");
        System.out.println("|    * DocumentVerificationSystem - Verifies academic documents            |");
        System.out.println("|    * FeePaymentSystem - Handles fee calculation and payment              |");
        System.out.println("|    * CourseAllocationSystem - Manages course enrollment                  |");
        System.out.println("|    * IDGenerationSystem - Generates unique student IDs                   |");
        System.out.println("|                                                                            |");
        System.out.println("| FACADE BENEFITS:                                                        |");
        System.out.println("|    * Single method call instead of multiple subsystem interactions       |");
        System.out.println("|    * Automatic workflow management and error handling                     |");
        System.out.println("|    * Client code remains simple and maintainable                         |");
        System.out.println("|                                                                            |");
        System.out.println("| READY TO START: Press Enter to begin the admission process...         |");
        System.out.println("+" + "=".repeat(78) + "+");
        System.out.println();
        
        // Wait for user to press Enter
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore exception and continue
        }
        
        // Clear screen for better user experience
        clearScreen();
    }
    
    /**
     * Clears the console screen for better user experience
     */
    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // For Unix/Linux/Mac systems
                System.out.print("\033[2J\033[H");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: print empty lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
