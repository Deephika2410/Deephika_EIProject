# Transport Ticketing System - Factory Method Pattern

A comprehensive Java implementation demonstrating the **Factory Method Design Pattern** through a dynamic transport ticketing system. This system allows users to book tickets for different modes of transportation (Bus, Train, Flight) with complete runtime user input and no hardcoded data.

## 🎯 Project Overview

This project implements a transport ticket booking system that showcases:
- **Factory Method Pattern** - Creating different types of tickets through specialized factories
- **SOLID Principles** - Well-structured, maintainable, and extensible code
- **Dynamic User Input** - All data entered at runtime, no fake/hardcoded data
- **Clean Architecture** - Properly organized packages and separation of concerns

## 🏗️ Architecture & Design Patterns

### Factory Method Pattern Implementation
```
Abstract Factory (TicketFactory)
├── BusTicketFactory
├── TrainTicketFactory
└── FlightTicketFactory

Abstract Product (Ticket)
├── BusTicket
├── TrainTicket
└── FlightTicket
```

### SOLID Principles Applied
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension (new transport types), closed for modification
- **Liskov Substitution**: All ticket types can be used interchangeably
- **Interface Segregation**: Clean interfaces without unnecessary dependencies
- **Dependency Inversion**: Depends on abstractions, not concrete classes

## 📁 Project Structure

```
FactoryMethod_TransportTicketingSystem/
├── src/
│   ├── enums/
│   │   ├── TicketClass.java          # Ticket classes (Economy, Business, First Class)
│   │   └── PaymentStatus.java        # Payment status enumeration
│   ├── models/
│   │   ├── PassengerInfo.java        # Passenger information model
│   │   ├── RouteInfo.java            # Route details with duration calculation
│   │   └── PricingInfo.java          # Pricing breakdown model
│   ├── ticket/
│   │   ├── Ticket.java               # Abstract Product - Base ticket interface
│   │   ├── BusTicket.java            # Concrete Product - Bus ticket implementation
│   │   ├── TrainTicket.java          # Concrete Product - Train ticket implementation
│   │   └── FlightTicket.java         # Concrete Product - Flight ticket implementation
│   ├── factory/
│   │   ├── TicketFactory.java        # Abstract Creator - Factory interface
│   │   ├── BusTicketFactory.java     # Concrete Creator - Bus ticket factory
│   │   ├── TrainTicketFactory.java   # Concrete Creator - Train ticket factory
│   │   └── FlightTicketFactory.java  # Concrete Creator - Flight ticket factory
│   ├── system/
│   │   └── TravelBookingSystem.java  # Client - Main booking system
│   ├── input/
│   │   └── UserInputHandler.java     # User input management
│   └── Main.java                     # Application entry point
├── compile.bat                       # Compilation script
├── run.bat                          # Execution script
└── README.md                        # This file
```

## ⚡ Features

### Multi-Transport Support
- **Bus Tickets**: Various bus types (Sleeper, AC, Volvo) with seat selection
- **Train Tickets**: Coach and berth selection with PNR generation
- **Flight Tickets**: Airline selection with gate and seat assignment

### Dynamic Pricing System
- Base fare calculation based on distance and transport type
- Class-based pricing multipliers (Economy: 1.0x, Business: 1.8x, First Class: 3.0x)
- Tax and service charge calculation (12% tax, 5% service charge)
- Discount application support

### Rich Ticket Features
- **Amenities**: Class-specific amenities for each transport type
- **Baggage Allowance**: Transport and class-specific baggage policies
- **Cancellation Policy**: Flexible cancellation terms based on transport and class
- **QR Code Generation**: Unique QR codes for each ticket
- **Detailed Receipt**: Complete ticket display with all information

### User Experience
- **Menu-Driven Interface**: Easy-to-use console interface
- **Input Validation**: Robust error handling for user inputs
- **Multiple Bookings**: Option to book multiple tickets in one session
- **Real-time Pricing**: Dynamic calculation based on user selections

## 🚀 How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Windows Command Prompt or PowerShell

### Compilation
1. Open Command Prompt or PowerShell
2. Navigate to the project directory:
   ```batch
   cd FactoryMethod_TransportTicketingSystem
   ```
3. Compile the project:
   ```batch
   compile.bat
   ```

### Execution
1. Run the application:
   ```batch
   run.bat
   ```
## 🔧 Extensibility

The system is designed for easy extension:

### Adding New Transport Types
1. Create a new concrete product class extending `Ticket`
2. Create a new concrete factory class extending `TicketFactory`
3. Register the new factory in `TravelBookingSystem`
4. Add input handling methods in `UserInputHandler`

### Adding New Features
- New ticket classes can be added to `TicketClass` enum
- Additional pricing models can be implemented in factories
- New amenities and policies can be added to concrete ticket classes

## 📋 Technical Specifications

- **Language**: Java 8+
- **Design Pattern**: Factory Method Pattern
- **Architecture**: Layered architecture with clear separation of concerns
- **Input Method**: Console-based user input (Scanner)
- **Date/Time Handling**: Java LocalDateTime
- **Error Handling**: Try-catch blocks with user-friendly messages
- **Code Style**: Clean, readable code following Java naming conventions

## 🎯 Learning Objectives Demonstrated

1. **Factory Method Pattern**: Proper implementation of creational design pattern
2. **SOLID Principles**: All five principles applied throughout the codebase  
3. **Polymorphism**: Abstract classes and method overriding
4. **Encapsulation**: Private fields with public accessors
5. **Abstraction**: Abstract factory and product hierarchies
6. **Error Handling**: Robust exception handling and validation
7. **User Interaction**: Dynamic console-based user input systems
8. **Code Organization**: Professional package structure and file organization
