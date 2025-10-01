import ui.ConsoleInterface;
import utils.Logger;

/**
 * Main entry point for the Astronaut Daily Schedule Organizer
 * Demonstrates SOLID principles, design patterns, and best practices
 * 
 * Design Patterns Used:
 * - Singleton: ScheduleManager
 * - Factory: TaskFactory and ScheduleExporterFactory
 * - Observer: TaskObserver and ConsoleTaskObserver
 * - Strategy: ProductivityAnalyzer with different analysis strategies
 * 
 * SOLID Principles Applied:
 * - Single Responsibility: Each class has one clear purpose
 * - Open/Closed: Easy to extend with new analyzers, exporters, observers
 * - Liskov Substitution: Interfaces can be substituted with implementations
 * - Interface Segregation: Focused interfaces (TaskObserver, ProductivityAnalyzer)
 * - Dependency Inversion: Dependencies on abstractions, not concretions
 */
public class AstronautScheduleApp {
    
    /**
     * Main method - application entry point
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            Logger.info("=".repeat(80));
            Logger.info("Starting Astronaut Daily Schedule Organizer Application");
            Logger.info("=".repeat(80));
            
            // Create and start the console interface
            ConsoleInterface consoleInterface = new ConsoleInterface();
            consoleInterface.start();
            
            Logger.info("Application terminated successfully");
            
        } catch (Exception e) {
            Logger.error("Fatal error in main application: " + e.getMessage(), e);
            
            // Display user-friendly error message
            System.err.println("=".repeat(80));
            System.err.println("FATAL ERROR: Application failed to start");
            System.err.println("Error: " + e.getMessage());
            System.err.println("Please check the log file for detailed error information.");
            System.err.println("=".repeat(80));
            
            // Exit with error code
            System.exit(1);
        }
    }
}
