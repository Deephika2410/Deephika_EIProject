# Smart City Infrastructure Management System

## Overview

This project demonstrates the **Composite Design Pattern** through a comprehensive Smart City Infrastructure Management System. The system allows users to build and manage a city hierarchy dynamically at runtime, showcasing how the Composite pattern enables uniform treatment of individual objects and compositions of objects.

## 🏗️ Architecture & Design Patterns

### Primary Pattern: Composite Design Pattern
- **Purpose**: Compose objects into tree structures to represent part-whole hierarchies
- **Implementation**: Unified interface for both leaf and composite objects
- **Benefits**: Clients can treat individual objects and compositions uniformly

### Supporting Patterns
1. **Singleton Pattern**: Logger for centralized logging
2. **Facade Pattern**: SmartCityManager for simplified management interface
3. **Template Method Pattern**: AbstractComposite and AbstractDevice for common behavior

## 🏙️ System Hierarchy

```
City
├── District 1
│   ├── Zone 1
│   │   ├── Building 1
│   │   │   ├── Floor 1
│   │   │   │   ├── Smart Light (LED)
│   │   │   │   ├── Air Conditioner (Central)
│   │   │   │   └── Sensor (Temperature)
│   │   │   └── Floor 2
│   │   │       └── Smart Light (Fluorescent)
│   │   └── Building 2
│   └── Zone 2
└── District 2
```

## 📁 Project Structure

```
src/
├── Main.java                          # Application entry point
├── composite/                         # Composite pattern core
│   ├── InfrastructureComponent.java   # Base component interface
│   ├── CompositeComponent.java        # Interface for composite components
│   └── AbstractComposite.java         # Abstract base for composites
├── components/                        # Concrete composite components
│   ├── City.java                      # Root composite
│   ├── District.java                  # City subdivisions
│   ├── Zone.java                      # District subdivisions
│   ├── Building.java                  # Zone subdivisions
│   └── Floor.java                     # Building subdivisions
├── devices/                           # Leaf components (devices)
│   ├── AbstractDevice.java            # Abstract base for devices
│   ├── SmartLight.java                # Smart lighting device
│   ├── AirConditioner.java            # HVAC device
│   └── Sensor.java                    # Monitoring device
├── exceptions/                        # Custom exceptions
│   └── InfrastructureException.java   # Domain-specific exceptions
├── management/                        # Infrastructure management
│   └── SmartCityManager.java          # Facade for city operations
├── ui/                                # User interface
│   └── ConsoleInterface.java          # Interactive console interface
└── utils/                             # Utility classes
    ├── Logger.java                    # Singleton logging utility
    └── ValidationUtils.java           # Input validation utilities
```

## 🎯 SOLID Principles Implementation

### 1. Single Responsibility Principle (SRP)
- **Logger**: Only handles logging operations
- **ValidationUtils**: Only handles input validation
- **ConsoleInterface**: Only handles user interaction
- **SmartCityManager**: Only manages infrastructure operations

### 2. Open/Closed Principle (OCP)
- **Device Types**: New device types can be added by extending `AbstractDevice`
- **Component Types**: New infrastructure levels can be added by implementing interfaces
- **Validation Rules**: New validators can be added without modifying existing code

### 3. Liskov Substitution Principle (LSP)
- **InfrastructureComponent**: All implementations can be substituted for the interface
- **Device Hierarchy**: Any device can replace `AbstractDevice` in operations
- **Composite Hierarchy**: All composites behave consistently through base interface

### 4. Interface Segregation Principle (ISP)
- **InfrastructureComponent**: Basic operations for all components
- **CompositeComponent**: Additional operations only for containers
- **Separated Concerns**: Devices don't implement composite-specific methods

### 5. Dependency Inversion Principle (DIP)
- **High-level modules**: Depend on abstractions (interfaces)
- **Concrete implementations**: Depend on abstractions, not concretions
- **Dependency injection**: Used throughout for loose coupling

##  Defensive Programming Features

### Input Validation
- **Null checks**: All inputs validated against null values
- **Empty string checks**: String inputs validated for meaningful content
- **Range validation**: Numeric inputs validated against acceptable ranges
- **Type validation**: Enum values validated against allowed types

### Exception Handling
- **Custom exceptions**: Domain-specific `InfrastructureException`
- **Graceful degradation**: System continues operation after recoverable errors
- **Error logging**: All exceptions logged with context information
- **User-friendly messages**: Clear error messages for end users

### Thread Safety
- **CopyOnWriteArrayList**: Thread-safe collections for component storage
- **Immutable objects**: Where possible, objects are designed to be immutable
- **Synchronized logging**: Thread-safe logging implementation

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Windows command prompt or PowerShell

### Compilation
```batch
# Using batch file
compile.bat

# Or manually
javac -d bin -cp src src\Main.java src\composite\*.java src\components\*.java src\devices\*.java src\exceptions\*.java src\management\*.java src\ui\*.java src\utils\*.java
```

### Execution
```batch
# Using batch file
run.bat

# Using PowerShell
.\run.ps1

# Or manually
java -cp bin Main
```

## 💡 Usage Instructions

### Menu Options
The system provides 18 interactive menu options:

1. **Create City** - Create a new city (root component)
2. **Create District** - Add districts to cities
3. **Create Zone** - Add zones to districts
4. **Create Building** - Add buildings to zones
5. **Create Floor** - Add floors to buildings
6. **Add Smart Light** - Add lighting devices to floors
7. **Add Air Conditioner** - Add HVAC devices to floors
8. **Add Sensor** - Add monitoring devices to floors
9. **Display City Hierarchy** - Show complete tree structure
10. **Display Component Details** - Show detailed component information
11. **Search Component** - Find components by ID
12. **Calculate Total Energy Consumption** - Aggregate energy usage
13. **List All Devices** - Show all leaf components
14. **Perform Maintenance** - Execute maintenance operations
15. **Get Infrastructure Statistics** - Display system statistics
16. **Remove Component** - Delete components from hierarchy
17. **Update Component Status** - Modify component operational status
18. **Exit** - Terminate application

### Sample Workflow
1. Start by creating a city
2. Add districts to the city
3. Add zones to districts
4. Add buildings to zones
5. Add floors to buildings
6. Add various devices to floors
7. Use management operations to monitor and maintain the infrastructure

##  Key Features

### Dynamic Runtime Creation
- No hardcoded data - all components created through user interaction
- Flexible hierarchy building with validation
- Real-time component management and modification

### Comprehensive Logging
- Singleton logger with different log levels
- Operation tracking and audit trail
- Error logging with context information

### Energy Management
- Aggregate energy consumption calculation
- Device-specific energy monitoring
- Maintenance scheduling based on usage

### Statistics and Reporting
- Component count by type
- Operational status summaries
- Maintenance requirement tracking

##  Testing the Composite Pattern

### Uniform Treatment Demonstration
```java
// Both individual devices and composite structures can be treated uniformly
InfrastructureComponent component = city; // or any device
component.display(0);
component.getEnergyConsumption();
component.performMaintenance();
```

### Tree Traversal
The system demonstrates classic composite tree operations:
- **Display**: Recursive tree printing with indentation
- **Energy Calculation**: Aggregating values across the hierarchy
- **Maintenance**: Propagating operations through the tree
- **Search**: Finding components at any level

##  Extensibility

### Adding New Device Types
1. Extend `AbstractDevice`
2. Implement device-specific behavior
3. Add to factory methods in `SmartCityManager`
4. Update UI options in `ConsoleInterface`

### Adding New Infrastructure Levels
1. Implement `CompositeComponent` interface
2. Extend `AbstractComposite` for common functionality
3. Add validation rules for parent-child relationships
4. Update management and UI layers

### Adding New Features
1. **Monitoring**: Add real-time monitoring capabilities
2. **Automation**: Implement automated device control
3. **Analytics**: Add data analysis and prediction features
4. **Integration**: Connect with external IoT systems

## 📊 Benefits Demonstrated

### Composite Pattern Benefits
- **Uniform Interface**: Same operations work on individuals and groups
- **Recursive Composition**: Natural tree structure handling
- **Flexibility**: Easy to add new component types
- **Client Simplification**: Clients don't need to distinguish between leaves and composites

### SOLID Principles Benefits
- **Maintainability**: Easy to modify and extend
- **Testability**: Isolated components can be tested independently
- **Reusability**: Components can be reused in different contexts
- **Flexibility**: System adapts to new requirements easily

---


