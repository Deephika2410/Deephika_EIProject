# EI Coding Exercises


[![Design Patterns](https://img.shields.io/badge/Design%20Patterns-7%20Implemented-green.svg)](#design-patterns)
[![SOLID Principles](https://img.shields.io/badge/SOLID-Principles%20Applied-blue.svg)](#solid-principles)


A comprehensive portfolio demonstrating advanced Java development skills through practical design pattern implementations and enterprise-grade applications. This repository showcases professional software engineering practices applied to real-world business scenarios.

## Project Structure

```
EICodingExercises/
├── Exercise 1/                    # Design Patterns Showcase
│   ├── Behavioural/               # Mediator, Strategy patterns
│   ├── Creational/                # Factory Method, Prototype patterns  
│   └── Structural/                # Composite, Facade patterns
└── Exercise 2/                    # Advanced Application
    └── AstronautDailyScheduleOrganizer/
```

## Project Overview



##  Exercise 1 - Design Patterns Mastery

###  Behavioral Patterns

#### 1.  Mediator Pattern - Smart Classroom Coordination System
**Business Context**: Educational technology platform managing classroom interactions

```java
public class SmartClassroomMediator implements ClassroomMediator {
    private Map<String, ClassroomParticipant> participants;
    private List<String> activityLog;
    
    @Override
    public void coordinateActivity(String activity, String initiator) {
        notifyAllParticipants(activity, initiator);
        logActivity(activity, initiator);
        updateClassroomState();
    }
}
```

**Key Features:**
- Dynamic participant registration and communication
- Centralized coordination without tight coupling
- Real-time activity logging and monitoring
- Thread-safe operations for concurrent access

#### 2.  Strategy Pattern - Dynamic Payment Processing System
**Business Context**: E-commerce platform supporting multiple payment gateways

```java
public class PaymentProcessor {
    private PaymentStrategy strategy;
    
    public PaymentResult processPayment(PaymentDetails details) {
        validateInput(details);
        return strategy.processPayment(details);
    }
    
    public void switchPaymentMethod(PaymentStrategy newStrategy) {
        this.strategy = newStrategy;
    }
}
```

**Key Features:**
- Runtime payment method switching (Credit Card, PayPal, Bitcoin)
- Comprehensive validation and security measures
- Extensible architecture for new payment types
- Complete transaction lifecycle management

###  Creational Patterns

#### 3.  Factory Method Pattern - Transport Ticketing System
**Business Context**: Multi-modal transportation booking platform

```java
public abstract class TicketFactory {
    public abstract Ticket createTicket(PassengerInfo passenger, RouteInfo route);
    
    public BookingResult processBooking(BookingRequest request) {
        Ticket ticket = createTicket(request.getPassenger(), request.getRoute());
        return executeBookingWorkflow(ticket, request);
    }
}
```

**Key Features:**
- Dynamic ticket creation for Bus, Train, and Flight
- Flexible pricing and availability algorithms
- Complete booking workflow with validation
- No hardcoded data - full runtime configuration

#### 4.  Prototype Pattern - Medical Patient Record System
**Business Context**: Healthcare management for efficient patient record handling

```java
public class PatientRecord implements Cloneable {
    @Override
    public PatientRecord clone() throws CloneNotSupportedException {
        PatientRecord cloned = (PatientRecord) super.clone();
        // Deep copy implementation for complex medical data
        cloned.medicalHistory = deepCopyMedicalHistory();
        cloned.medications = deepCopyMedications();
        return cloned;
    }
}
```

**Key Features:**
- Deep cloning of complex medical records
- Template-based record creation for efficiency
- Registry pattern for prototype management
- Healthcare compliance and data validation

### Structural Patterns

#### 5. Composite Pattern - Smart City Infrastructure Management
**Business Context**: Municipal infrastructure monitoring and control system

```java
public abstract class InfrastructureComponent {
    protected String name;
    protected List<InfrastructureComponent> children;
    
    public abstract void displayStatus();
    public abstract double calculatePowerConsumption();
    public abstract void performMaintenance();
}
```

**Key Features:**
- Hierarchical city infrastructure modeling (City → District → Zone → Device)
- Unified operations across component types
- Real-time monitoring and status reporting
- Scalable tree structure for large metropolitan areas

#### 6. Facade Pattern - University Admission System
**Business Context**: Streamlined university application processing

```java
public class AdmissionFacade {
    private DocumentService documentService;
    private EligibilityService eligibilityService;
    private PaymentService paymentService;
    
    public AdmissionResult processApplication(Application application) {
        return executeAdmissionWorkflow(application);
    }
}
```

**Key Features:**
- Simplified interface to complex admission workflows
- Integration of multiple verification subsystems
- Comprehensive application processing pipeline
- User-friendly interaction experience

---

## Exercise 2 - Advanced Application Development

### 🌌 Astronaut Daily Schedule Organizer

*A sophisticated scheduling application demonstrating multiple design patterns working in harmony*

**Business Context**: NASA-grade mission scheduling system for astronaut daily activities with conflict detection, priority management, and productivity analytics.

#### Core Features

**📅 Advanced Schedule Management**
- Complete CRUD operations with conflict detection
- Priority-based task organization (LOW → CRITICAL)
- Time-based sorting and optimization
- Real-time completion tracking

**🤖 Smart Analytics Engine**
```java
public class ProductivityAnalyzer {
    public AnalysisResult analyzeSchedule(List<Task> tasks) {
        return AnalysisResult.builder()
            .workloadDistribution(calculateWorkload(tasks))
            .priorityBalance(analyzePriorities(tasks))
            .recommendations(generateRecommendations(tasks))
            .build();
    }
}
```

**📊 Export Capabilities**
- Multiple format support (CSV, Text)
- Professional report generation
- Data visualization ready

#### Architecture Excellence

**Design Patterns Implemented:**
- **Singleton**: Thread-safe ScheduleManager
- **Factory**: Validated Task creation
- **Observer**: Real-time notifications
- **Strategy**: Pluggable analysis algorithms

##  Technical Architecture

###  Design Principles

**SOLID Principles Implementation:**
```java
// Single Responsibility Principle
public class TaskValidator {
    public ValidationResult validate(Task task) {
        return ValidationResult.of(validateTimeSlot(task), validatePriority(task));
    }
}

// Open/Closed Principle  
public interface PaymentStrategy {
    PaymentResult processPayment(PaymentDetails details);
}

// Dependency Inversion Principle
public class ScheduleManager {
    private final TaskObserver observer;  // Depends on abstraction
    
    public ScheduleManager(TaskObserver observer) {
        this.observer = observer;
    }
}
```

###  Quality Assurance

**Error Handling Strategy:**
- Custom exception hierarchy for domain-specific errors
- Graceful degradation with user-friendly messages
- Comprehensive input validation at all layers
- Defensive programming practices throughout

**Performance Optimization:**
- Efficient algorithms for conflict detection
- Concurrent collections for thread safety
- Stream API for optimal data processing
- Memory-efficient data structures

---

##  SOLID Principles Implementation

| Principle | Implementation Example | Benefits |
|-----------|----------------------|----------|
| **Single Responsibility** | Each class has one clear purpose | High cohesion, easy maintenance |
| **Open/Closed** | Strategy pattern for extensibility | Add features without modification |
| **Liskov Substitution** | Interface contracts maintained | Reliable polymorphism |
| **Interface Segregation** | Focused, specific interfaces | No forced dependencies |
| **Dependency Inversion** | Abstractions over concretions | Flexible, testable design |

---

## Quick Start Guide

### Prerequisites
```bash
Java 17+ required
Any IDE (IntelliJ IDEA, Eclipse, VS Code recommended)
```

### Running the Applications

**Design Pattern Examples:**
```bash
# Navigate to any pattern implementation
cd "Exercise 1/Behavioural/MediatorPattern_SmartClassroomCoordinationSystem"
./compile.bat
./run.bat
```

**Advanced Application:**
```bash
# Astronaut Schedule Organizer
cd "Exercise 2/AstronautDailyScheduleOrganizer"
./compile.bat
./run.bat
```

## Contact

For any questions or suggestions, please contact me at [deephika50@gmail.com].




