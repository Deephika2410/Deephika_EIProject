# Medical Patient Record Prototype System

## Overview

This project demonstrates the **Prototype Design Pattern** through a comprehensive medical patient record management system. The system allows efficient creation and management of patient records by cloning existing prototypes rather than creating new instances from scratch.

## Design Pattern: Prototype

### Intent
The Prototype pattern specifies the kinds of objects to create using a prototypical instance, and creates new objects by copying this prototype.

### Benefits in This Implementation
- **Performance**: Cloning complex patient records is faster than creating from scratch
- **Flexibility**: Easy creation of record variations from existing templates
- **Consistency**: Ensures standardized record structures across the system
- **Encapsulation**: Hides object creation complexity from client code
- **Extensibility**: New record types can be easily added

## System Architecture

### Package Structure
```
src/
├── Main.java                          # Application entry point
├── enums/                             # Type-safe enumerations
│   ├── Gender.java                    # Patient gender classification
│   ├── BloodType.java                 # Blood type enumeration
│   └── RecordStatus.java              # Record status management
├── models/                            # Domain models
│   ├── MedicalCondition.java          # Medical condition data
│   ├── Medication.java                # Medication information
│   ├── EmergencyContact.java          # Emergency contact details
│   ├── BasicPatientRecord.java        # Basic patient record implementation
│   └── EmergencyPatientRecord.java    # Emergency patient record specialization
├── prototype/                         # Prototype pattern implementation
│   ├── PatientRecordPrototype.java    # Prototype interface
│   └── PatientRecordPrototypeRegistry.java # Prototype registry
├── management/                        # Business logic layer
│   └── PatientRecordManager.java      # High-level record operations
└── ui/                               # User interface layer
    └── MedicalRecordConsoleUI.java    # Console-based interface
```

## Key Features

### 1. Prototype Interface
- Defines the cloning contract for all patient records
- Supports deep cloning of complex nested objects
- Provides validation and dynamic field updates

### 2. Concrete Prototypes
- **BasicPatientRecord**: Comprehensive patient information
- **EmergencyPatientRecord**: Specialized for emergency department needs
- Both support efficient cloning and customization

### 3. Prototype Registry
- Centralized management of prototype templates
- Singleton pattern implementation for global access
- Dynamic prototype registration and retrieval

### 4. Deep Cloning Support
- Proper cloning of nested objects (dates, collections, custom objects)
- Maintains data integrity during cloning operations
- Generates unique identifiers for cloned records

### 5. Dynamic Field Updates
- Runtime modification of patient record fields
- Type-safe field updates with validation
- Support for enum and complex object fields

## Core Classes

### PatientRecordPrototype (Interface)
```java
public interface PatientRecordPrototype extends Cloneable {
    PatientRecordPrototype cloneRecord() throws CloneNotSupportedException;
    String getRecordType();
    String getRecordSummary();
    boolean validateRecord();
    boolean updateField(String fieldName, Object value);
}
```

### BasicPatientRecord (Concrete Prototype)
- Complete patient demographics and medical history
- Support for medical conditions, medications, allergies
- Emergency contact management
- Comprehensive validation logic

### EmergencyPatientRecord (Specialized Prototype)
- Optimized for emergency department workflows
- Triage level and chief complaint tracking
- Trauma case identification
- Rapid assessment fields

### PatientRecordPrototypeRegistry (Registry)
- Singleton pattern for global prototype management
- Template registration and retrieval
- Prototype cloning services

## Usage Examples

### 1. Creating Records from Templates
```java
PatientRecordManager manager = new PatientRecordManager();
PatientRecordPrototype basicRecord = manager.createPatientRecord("BASIC_TEMPLATE");
PatientRecordPrototype emergencyRecord = manager.createPatientRecord("EMERGENCY_TEMPLATE");
```

### 2. Customizing Records
```java
basicRecord.updateField("firstName", "John");
basicRecord.updateField("lastName", "Smith");
basicRecord.updateField("bloodType", BloodType.A_POSITIVE);
```

### 3. Cloning Existing Records
```java
PatientRecordPrototype clonedRecord = manager.cloneExistingRecord(existingRecord);
```

## Features Demonstrated

### 1. No Hardcoding
- All data is dynamically configurable
- User input drives record creation
- Flexible field updates at runtime

### 2. Unicode Support
- System uses standard ASCII characters only
- No special Unicode characters in source code
- Compatible with standard text editors

### 3. Comprehensive Documentation
- Extensive JavaDoc comments
- Clear class and method descriptions
- Usage examples and design rationale

### 4. Error Handling
- Robust exception handling throughout
- Graceful degradation for invalid inputs
- User-friendly error messages

## How to Run

### Prerequisites
- Java 17 or higher
- Windows command prompt or PowerShell

### Compilation
```bash
# Using batch file
compile.bat

# Or manually
javac -cp src -d src src\Main.java src\enums\*.java src\models\*.java src\prototype\*.java src\management\*.java src\ui\*.java
```

### Execution
```bash
# Using batch file
run.bat

# Using PowerShell
.\run.ps1

# Or manually
java -cp src Main
```

## Interactive Menu Options

1. **Create Basic Patient Record** - From basic template
2. **Create Emergency Patient Record** - From emergency template
3. **Create Custom Basic Record** - With user input
4. **Create Custom Emergency Record** - With user input
5. **Clone Existing Record** - Duplicate existing records
6. **View All Active Records** - Display all created records
7. **View Registry Information** - Show prototype templates
8. **Validate All Records** - Check record integrity
9. **Demonstration Mode** - Automated pattern demonstration
10. **Clear All Records** - Reset the system

## Design Patterns Used

### Primary Pattern: Prototype
- **Intent**: Create objects by cloning existing instances
- **Implementation**: PatientRecordPrototype interface with concrete implementations
- **Benefits**: Efficient object creation, consistency, flexibility

### Supporting Patterns
- **Singleton**: PatientRecordPrototypeRegistry for global access
- **Template Method**: Common validation logic in base classes
- **Strategy**: Different validation strategies for different record types

This system serves as a comprehensive example of the Prototype design pattern implementation in a real-world scenario, providing both educational value and practical demonstration of object cloning techniques in Java.
