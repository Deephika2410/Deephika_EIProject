# Strategy Pattern Implementation - Payment Processing System

This project demonstrates the **Strategy Pattern** using a comprehensive payment processing system that supports multiple payment methods.

## 📁 Project Structure

```
StrategyPattern/
├── src/
│   ├── Main.java                       # Main application with interactive menu
│   ├── context/
│   │   └── PaymentProcessor.java       # Context class that uses strategies
│   ├── models/
│   │   ├── PaymentDetails.java         # Data Transfer Object for customer info
│   │   └── PaymentResult.java          # Result object for payment operations
│   └── strategies/
│       ├── PaymentStrategy.java        # Strategy interface
│       ├── CreditCardPayment.java      # Concrete strategy for credit cards
│       ├── PayPalPayment.java          # Concrete strategy for PayPal
│       └── BitcoinPayment.java         # Concrete strategy for Bitcoin
└── README.md                           # This file
```

## 🎯 Strategy Pattern Components

### 1. **Strategy Interface** (`PaymentStrategy`)
- Defines the contract for all payment strategies
- Contains methods: `processPayment()`, `getPaymentMethodName()`, `isAvailable()`

### 2. **Concrete Strategies**
- **CreditCardPayment**: Handles traditional credit card processing
- **PayPalPayment**: Handles PayPal account-based payments
- **BitcoinPayment**: Handles cryptocurrency payments with conversion logic

### 3. **Context Class** (`PaymentProcessor`)
- Maintains a reference to a strategy object
- Delegates payment processing to the current strategy
- Allows runtime strategy switching

### 4. **Supporting Classes**
- **PaymentDetails**: Encapsulates customer and payment information
- **PaymentResult**: Encapsulates the result of payment operations

## 🚀 How to Run

### Prerequisites
- Java 8 or higher
- Command line terminal

### Compilation
```bash
# Navigate to the src directory
cd src

# Compile all Java files
javac *.java context/*.java models/*.java strategies/*.java

# Run the application
java Main
```

### Alternative (from project root)
```bash
# Compile
javac -d . src/*.java src/context/*.java src/models/*.java src/strategies/*.java

# Run
java -cp . Main
```

## 🎮 Application Features

### Interactive Menu System
1. **Setup Customer Details** - Configure customer information
2. **Setup Credit Card Payment** - Configure credit card processing
3. **Setup PayPal Payment** - Configure PayPal processing
4. **Setup Bitcoin Payment** - Configure cryptocurrency processing
5. **Process Payment** - Execute payment with selected strategy
6. **Demo Runtime Strategy Switching** - Showcase dynamic strategy changes
7. **View Current Configuration** - Display system status
8. **Exit** - Close application

### Key Demonstrations

#### Runtime Strategy Switching
The application demonstrates how the same `PaymentProcessor` can use different algorithms (payment strategies) without changing its code:

```java
// Switch strategies at runtime
processor.setPaymentStrategy(new CreditCardPayment("1234...", "12/26", "123"));
processor.executePayment(100.0, customerDetails);

processor.setPaymentStrategy(new PayPalPayment("user@example.com", "password"));
processor.executePayment(100.0, customerDetails);

processor.setPaymentStrategy(new BitcoinPayment("wallet123...", "privatekey"));
processor.executePayment(100.0, customerDetails);
```

## 🏗️ Design Principles Demonstrated

### 1. **Open/Closed Principle**
- Open for extension: New payment methods can be added easily
- Closed for modification: Existing code doesn't need changes

### 2. **Single Responsibility Principle**
- Each strategy has one reason to change (its specific payment logic)
- PaymentProcessor focuses only on delegating to strategies

### 3. **Dependency Inversion Principle**
- PaymentProcessor depends on PaymentStrategy abstraction
- Not dependent on concrete payment implementations

### 4. **Strategy Pattern Benefits**
- **Runtime Algorithm Selection**: Change payment methods dynamically
- **Elimination of Conditionals**: No large if-else or switch statements
- **Easier Testing**: Each strategy can be tested independently
- **Better Maintainability**: Changes to one payment method don't affect others

## 🎯 Strategy Pattern vs Other Patterns

### Strategy vs State Pattern
- **Strategy**: Different algorithms for the same problem
- **State**: Different behaviors based on object's internal state

### Strategy vs Template Method
- **Strategy**: Uses composition and delegation
- **Template Method**: Uses inheritance and method overriding

## 💡 Real-world Applications

1. **Payment Processing** (as demonstrated)
2. **Sorting Algorithms** (QuickSort, MergeSort, BubbleSort)
3. **Compression Algorithms** (ZIP, RAR, GZIP)
4. **Authentication Methods** (OAuth, LDAP, Database)
5. **File Export Formats** (PDF, Excel, CSV, XML)
6. **Route Planning** (Fastest, Shortest, Scenic)

## 🔧 Extension Ideas

To further explore the pattern, consider adding:

1. **New Payment Strategies**:
   - Apple Pay
   - Google Pay
   - Bank Transfer
   - Cryptocurrency (Ethereum, Litecoin)

2. **Enhanced Features**:
   - Payment validation strategies
   - Currency conversion strategies
   - Transaction fee calculation strategies
   - Fraud detection strategies

3. **Strategy Factories**:
   - Factory pattern to create appropriate strategies
   - Configuration-based strategy selection

## 📚 Learning Outcomes

After running this demonstration, you should understand:

- How to implement the Strategy pattern in Java
- When to use Strategy pattern vs other behavioral patterns
- How strategy pattern promotes code flexibility and maintainability
- The relationship between strategy pattern and SOLID principles
- Real-world applications of the strategy pattern

## 🎓 Educational Value

This implementation showcases:
- **Clean Architecture**: Separation of concerns with proper package structure
- **Interactive Learning**: Menu-driven system for hands-on exploration
- **Real-world Simulation**: Realistic payment processing scenarios
- **Pattern Variations**: Multiple concrete strategies with different behaviors
- **Runtime Flexibility**: Dynamic strategy switching demonstration

---

*This project serves as a comprehensive example of the Strategy Pattern implementation, demonstrating both its technical aspects and practical applications in software development.*
