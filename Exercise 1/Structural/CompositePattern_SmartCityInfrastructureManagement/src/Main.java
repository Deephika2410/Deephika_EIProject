import ui.ConsoleInterface;
import utils.Logger;

/**
 * Main application class for the Smart City Infrastructure Management System.
 * This class serves as the entry point for the application and demonstrates
 * the Composite Design Pattern implementation.
 * 
 * The application allows users to build a hierarchical city infrastructure
 * dynamically at runtime, where:
 * - City contains Districts
 * - Districts contain Zones  
 * - Zones contain Buildings
 * - Buildings contain Floors
 * - Floors contain Devices (Smart Lights, ACs, Sensors)
 * 
 * Both individual Devices (leaf nodes) and composite structures
 * are treated uniformly using the Composite pattern.
 * 
 * Design Principles Demonstrated:
 * - Composite Design Pattern: Uniform treatment of individual and composite objects
 * - Single Responsibility Principle: Each class has one reason to change
 * - Open/Closed Principle: Open for extension, closed for modification
 * - Interface Segregation Principle: Specific interfaces for different concerns
 * - Dependency Inversion Principle: Depend on abstractions, not concretions
 * - Liskov Substitution Principle: Derived classes are substitutable for base classes
 * 
 * Features:
 * - Dynamic runtime infrastructure creation
 * - Comprehensive input validation and error handling
 * - Logging and monitoring capabilities
 * - Maintenance and validation operations
 * - Energy consumption tracking
 * - Statistical reporting
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 * @since 2024
 */
public class Main {
    
    /**
     * Main method - entry point of the application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Initialize logger
            Logger logger = Logger.getInstance();
            logger.setLogLevel(Logger.LogLevel.INFO);
            logger.setConsoleOutput(true);
            logger.setFileOutput(true);
            
            // Log application startup
            logger.info("Starting Smart City Infrastructure Management System");
            logger.info("Composite Design Pattern Demonstration");
            logger.info("Author: Smart City Infrastructure Team");
            logger.info("Version: 1.0");
            
            // Create and start the console interface
            ConsoleInterface consoleInterface = new ConsoleInterface();
            consoleInterface.start();
            
            // Log application shutdown
            logger.info("Smart City Infrastructure Management System terminated normally");
            
        } catch (Exception e) {
            // Handle any unexpected errors during application startup
            System.err.println("CRITICAL ERROR: Failed to start the application");
            System.err.println("Error Details: " + e.getMessage());
            e.printStackTrace();
            
            // Log the critical error
            Logger logger = Logger.getInstance();
            logger.error("Critical application startup error", e);
            
            System.exit(1);
        }
    }
}

/*
 * ============================================================================
 *                    SMART CITY INFRASTRUCTURE HIERARCHY
 * ============================================================================
 * 
 * This application demonstrates the Composite Design Pattern through a
 * comprehensive smart city infrastructure management system with the
 * following hierarchy:
 * 
 * City (Root Composite)
 * └── Districts (Composite)
 *     └── Zones (Composite)
 *         └── Buildings (Composite)
 *             └── Floors (Composite)
 *                 ├── SmartLight (Leaf)
 *                 ├── AirConditioner (Leaf)
 *                 └── Sensor (Leaf)
 * 
 * ============================================================================
 *                           DESIGN PATTERNS USED
 * ============================================================================
 * 
 * 1. COMPOSITE PATTERN (Primary)
 *    - InfrastructureComponent: Component interface
 *    - CompositeComponent: Composite interface
 *    - AbstractComposite: Abstract composite implementation
 *    - City, District, Zone, Building, Floor: Concrete composites
 *    - SmartLight, AirConditioner, Sensor: Concrete leaves
 * 
 * 2. SINGLETON PATTERN
 *    - Logger: Ensures single logging instance
 * 
 * 3. FACADE PATTERN
 *    - SmartCityManager: Simplified interface for complex operations
 * 
 * 4. TEMPLATE METHOD PATTERN
 *    - AbstractComposite: Common composite operations template
 *    - AbstractDevice: Common device operations template
 * 
 * ============================================================================
 *                            SOLID PRINCIPLES
 * ============================================================================
 * 
 * S - Single Responsibility Principle
 *     Each class has a single, well-defined responsibility:
 *     - City: Manages city-level operations
 *     - SmartLight: Handles smart lighting functionality
 *     - Logger: Manages application logging
 *     - ValidationUtils: Handles input validation
 * 
 * O - Open/Closed Principle
 *     Classes are open for extension but closed for modification:
 *     - New device types can be added by extending AbstractDevice
 *     - New composite types can be added by extending AbstractComposite
 *     - Existing code doesn't need modification for new features
 * 
 * L - Liskov Substitution Principle
 *     Derived classes are substitutable for their base classes:
 *     - All InfrastructureComponent implementations can be used interchangeably
 *     - Any CompositeComponent can contain any InfrastructureComponent
 *     - Device-specific classes can be used wherever AbstractDevice is expected
 * 
 * I - Interface Segregation Principle
 *     Clients depend only on interfaces they use:
 *     - InfrastructureComponent: Core component operations
 *     - CompositeComponent: Composite-specific operations
 *     - Separated interfaces prevent unnecessary dependencies
 * 
 * D - Dependency Inversion Principle
 *     Depend on abstractions, not concretions:
 *     - High-level modules depend on interfaces (InfrastructureComponent)
 *     - Low-level modules implement these interfaces
 *     - SmartCityManager depends on component interfaces, not concrete classes
 * 
 * ============================================================================
 *                           DEFENSIVE PROGRAMMING
 * ============================================================================
 * 
 * 1. INPUT VALIDATION
 *    - All user inputs are validated using ValidationUtils
 *    - Comprehensive parameter checking in constructors
 *    - Range validation for numeric inputs
 *    - Format validation for IDs and names
 * 
 * 2. EXCEPTION HANDLING
 *    - Custom InfrastructureException for domain-specific errors
 *    - Try-catch blocks around critical operations
 *    - Graceful error recovery and user feedback
 *    - Detailed error logging
 * 
 * 3. NULL SAFETY
 *    - Null checks for all object parameters
 *    - Defensive copying of collections
 *    - Safe navigation patterns
 * 
 * 4. THREAD SAFETY
 *    - CopyOnWriteArrayList for component collections
 *    - Synchronized logging operations
 *    - Immutable value objects where possible
 * 
 * ============================================================================
 *                              LOGGING STRATEGY
 * ============================================================================
 * 
 * The application implements comprehensive logging at multiple levels:
 * 
 * - DEBUG: Detailed debugging information
 * - INFO: General application flow and operations
 * - WARNING: Potential issues that don't prevent operation
 * - ERROR: Error conditions that may affect functionality
 * 
 * Logging outputs to both console and file for different deployment scenarios.
 * 
 * ============================================================================
 *                            USAGE INSTRUCTIONS
 * ============================================================================
 * 
 * 1. COMPILATION
 *    Navigate to the project root directory and run:
 *    javac -d bin src/*.java src/composite/*.java src/components/*.java 
 *          src/devices/*.java src/exceptions/*.java src/management/*.java 
 *          src/ui/*.java src/utils/*.java
 * 
 * 2. EXECUTION
 *    java -cp bin Main
 * 
 * 3. OPERATION
 *    - Follow the interactive menu system
 *    - Create infrastructure components in hierarchical order
 *    - Use validation and maintenance features to ensure system integrity
 *    - View statistics and detailed information about components
 * 
 * 4. BEST PRACTICES
 *    - Always create a city before adding districts
 *    - Follow the hierarchy: City -> District -> Zone -> Building -> Floor -> Device
 *    - Use meaningful IDs and names for easy identification
 *    - Regularly perform system maintenance and validation
 * 
 * ============================================================================
 *                          EXTENSIBILITY NOTES
 * ============================================================================
 * 
 * The system is designed for easy extension:
 * 
 * 1. NEW DEVICE TYPES
 *    - Extend AbstractDevice
 *    - Implement device-specific methods
 *    - Add creation methods to SmartCityManager and ConsoleInterface
 * 
 * 2. NEW COMPOSITE TYPES
 *    - Extend AbstractComposite
 *    - Define valid child types
 *    - Add to the hierarchy as needed
 * 
 * 3. NEW FEATURES
 *    - Add new interfaces for specific capabilities
 *    - Implement in relevant classes
 *    - Maintain backward compatibility
 * 
 * ============================================================================
 */
