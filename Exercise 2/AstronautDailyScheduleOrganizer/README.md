# Astronaut Daily Schedule Organizer

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Design Patterns](https://img.shields.io/badge/Design%20Patterns-Singleton%20%7C%20Factory%20%7C%20Observer%20%7C%20Strategy-green.svg)](README.md#design-patterns)

A comprehensive console-based application designed to help astronauts organize their daily schedules efficiently. Built using SOLID principles, Object-Oriented Programming, and multiple design patterns for maximum maintainability and extensibility.

## Features

### Core Functionality
- **Task Management (CRUD)**: Add, remove, view, and update tasks with full validation
- **Conflict Detection**: Automatic detection and prevention of overlapping tasks
- **Priority Management**: Organize tasks by priority levels (LOW, MEDIUM, HIGH, CRITICAL)
- **Completion Tracking**: Mark tasks as completed or pending
- **Time-based Sorting**: View tasks sorted by start time for optimal scheduling

### Advanced Features
- **Productivity Analyzer**: AI-powered analysis providing schedule optimization recommendations
- **Export Functionality**: Export schedules to Text or CSV formats for sharing
- **Real-time Notifications**: Immediate feedback on all operations through Observer pattern
- **Input Validation**: Comprehensive validation at all levels with defensive programming
- **Logging System**: Complete operation logging for debugging and audit trails

### Technical Highlights
- **Thread-Safe Design**: Concurrent collections and synchronized operations
- **Exception Handling**: Comprehensive error handling with custom exceptions
- **Clean Architecture**: Modular design following SOLID principles
- **Event-Driven UI**: No infinite loops, proper event-driven menu system
- **Performance Optimized**: Efficient algorithms and data structures

## Design Patterns Implemented

### 1. Singleton Pattern
- **ScheduleManager**: Ensures single instance managing all tasks across the application
- **Thread-safe implementation** using double-checked locking

### 2. Factory Pattern
- **TaskFactory**: Creates validated Task objects with comprehensive input validation
- **ScheduleExporterFactory**: Creates appropriate exporters based on format requirements

### 3. Observer Pattern
- **TaskObserver Interface**: Defines notification contract for task events
- **ConsoleTaskObserver**: Provides real-time console notifications for user feedback
- **Event-driven notifications** for all schedule operations

### 4. Strategy Pattern
- **ProductivityAnalyzer**: Pluggable analysis strategies for different types of schedule optimization
- **ScheduleExporter**: Multiple export format strategies (Text, CSV)

## SOLID Principles Implementation

### Single Responsibility Principle (SRP)
- Each class has a single, well-defined responsibility
- **Task**: Represents task data only
- **ScheduleManager**: Manages schedule operations only
- **ValidationUtils**: Handles input validation only

### Open-Closed Principle (OCP)
- Open for extension, closed for modification
- **ProductivityAnalyzer**: New analysis strategies can be added without modifying existing code
- **ScheduleExporter**: New export formats can be added easily

### Liskov Substitution Principle (LSP)
- All implementations can be substituted for their interfaces
- **TaskObserver implementations** are fully interchangeable
- **ProductivityAnalyzer implementations** maintain contract consistency

### Interface Segregation Principle (ISP)
- Interfaces are specific and focused
- **TaskObserver**: Specific to task-related events only
- **ScheduleExporter**: Focused only on export functionality

### Dependency Inversion Principle (DIP)
- High-level modules don't depend on low-level modules
- **ScheduleManager** depends on abstractions (TaskObserver interface)
- **Application layer** depends on service abstractions

## Project Structure

```
src/
├── AstronautScheduleApp.java          # Main application class
├── models/
│   ├── Task.java                      # Task entity with immutable design
│   └── PriorityLevel.java             # Priority enumeration
├── factories/
│   └── TaskFactory.java               # Task creation with validation
├── services/
│   └── ScheduleManager.java           # Singleton schedule management
├── observers/
│   ├── TaskObserver.java              # Observer interface
│   └── ConsoleTaskObserver.java       # Console notification implementation
├── analyzers/
│   ├── ProductivityAnalyzer.java      # Analysis strategy interface
│   └── ProductivityAnalysisContext.java # Strategy context
├── exporters/
│   ├── ScheduleExporter.java          # Export strategy interface
│   └── ScheduleExporterFactory.java   # Export factory
├── utils/
│   ├── ValidationUtils.java           # Input validation utilities
│   └── Logger.java                    # Logging utility
└── exceptions/
    ├── TaskValidationException.java   # Task validation errors
    ├── TaskConflictException.java     # Conflict-specific errors
    └── TaskNotFoundException.java     # Not found errors
```

## Getting Started

### Prerequisites
- Java 8 or higher
- Command line access (Windows Command Prompt or PowerShell)

### Compilation
```bash
# Using batch file (recommended)
compile.bat

# Or manually
javac -d bin -cp src src\*.java src\models\*.java src\factories\*.java src\observers\*.java src\services\*.java src\utils\*.java src\analyzers\*.java src\exporters\*.java src\exceptions\*.java
```

### Running the Application
```bash
# Using batch file (recommended)
run.bat

# Using PowerShell
run.ps1

# Or manually
java -cp bin AstronautScheduleApp
```

## Usage Guide

### Main Menu Options

1. **Add New Task**
   - Enter task description (3-100 characters)
   - Specify start and end times (HH:mm format)
   - Set priority level (LOW, MEDIUM, HIGH, CRITICAL)
   - Automatic conflict detection and validation

2. **Remove Task**
   - Select from numbered list of current tasks
   - Confirmation required for deletion
   - Immediate notification of freed time slots

3. **Edit Task**
   - Select task to modify
   - Update any or all properties
   - Conflict detection for time changes
   - Keep existing values by pressing Enter

4. **View All Tasks**
   - Displays all tasks sorted by start time
   - Shows completion status and details
   - Clear formatting for easy reading

5. **View Tasks by Priority**
   - Filter tasks by specific priority level
   - Useful for focusing on urgent items
   - Sorted by start time within priority

6. **Mark Task Completed/Pending**
   - Toggle completion status
   - Visual feedback on status changes
   - Maintains task history

7. **Run Productivity Analyzer**
   - Comprehensive schedule analysis
   - Identifies optimization opportunities
   - Provides actionable recommendations
   - Analyzes work-life balance

8. **Export Schedule**
   - Multiple format options (Text, CSV)
   - Includes all task details
   - Shareable with team members
   - Timestamped exports

### Sample Usage Flow

```
1. Start application
2. Add morning exercise: "Morning Workout" (07:00-08:00, HIGH)
3. Add team meeting: "Daily Standup" (09:00-09:30, MEDIUM)
4. Try to add conflicting task: "Training Session" (09:15-10:15, HIGH)
   → System detects conflict and prevents addition
5. Add valid task: "Research Session" (10:00-12:00, HIGH)
6. Run productivity analyzer for recommendations
7. Export schedule to CSV for team sharing
```

## Sample Application Outputs

### Main Menu Interface
```
================================================================
           ASTRONAUT DAILY SCHEDULE ORGANIZER
================================================================

1. Add New Task
2. Remove Task
3. Edit Task
4. View All Tasks
5. View Tasks by Priority
6. Mark Task as Completed/Pending
7. Run Productivity Analyzer
8. Export Schedule
9. Exit

Please select an option [1-9]: 
```

### Adding a New Task
```
=== ADD NEW TASK ===

Enter task description (3-100 characters): Morning Workout
Enter start time (HH:mm): 07:00
Enter end time (HH:mm): 08:00
Select priority level:
1. LOW
2. MEDIUM
3. HIGH
4. CRITICAL
Enter priority [1-4]: 3

✅ SUCCESS: Task "Morning Workout" added successfully!
⏰ Scheduled: 07:00 - 08:00 (1.00 hours)
🔴 Priority: HIGH
📝 Status: Pending

Press Enter to continue...
```

### Conflict Detection Example
```
=== ADD NEW TASK ===

Enter task description (3-100 characters): Team Meeting
Enter start time (HH:mm): 07:30
Enter end time (HH:mm): 08:30

❌ ERROR: Task conflict detected!
🔍 Conflicting with: "Morning Workout" (07:00 - 08:00)
⚠️ Please choose a different time slot.

Press Enter to continue...
```

### Viewing All Tasks
```
=== ALL TASKS (Sorted by Start Time) ===

📅 Schedule for Today | Total Tasks: 3 | Completed: 1 | Pending: 2

┌────┬─────────────────────┬──────────┬────────┬──────────┬──────────┐
│ No │ Task Description    │ Start    │ End    │ Priority │ Status   │
├────┼─────────────────────┼──────────┼────────┼──────────┼──────────┤
│ 1  │ Morning Workout     │ 07:00    │ 08:00  │ HIGH     │ Pending  │
│ 2  │ Team Standup       │ 09:00    │ 09:30  │ MEDIUM   │ Completed│
│ 3  │ Research Session   │ 10:00    │ 12:00  │ HIGH     │ Pending  │
└────┴─────────────────────┴──────────┴────────┴──────────┴──────────┘

📊 Schedule Summary:
• Total scheduled time: 3.5 hours
• First task starts: 07:00
• Last task ends: 12:00
• Schedule span: 5 hours

Press Enter to continue...
```

### Task Completion Tracking
```
=== MARK TASK COMPLETION ===

Select task to update:
1. Morning Workout (07:00-08:00) - Pending
2. Research Session (10:00-12:00) - Pending

Enter task number [1-2]: 1

✅ SUCCESS: Task "Morning Workout" marked as COMPLETED!
🎉 Great job! Task completed successfully.
⏱️ Duration: 1.00 hours

Press Enter to continue...
```

### Priority-Based Task Filtering
```
=== TASKS BY PRIORITY ===

Select priority level to view:
1. LOW (0 tasks)
2. MEDIUM (1 task)
3. HIGH (2 tasks)
4. CRITICAL (0 tasks)

Enter priority [1-4]: 3

📋 HIGH PRIORITY TASKS:

┌────┬─────────────────────┬──────────┬────────┬──────────┐
│ No │ Task Description    │ Start    │ End    │ Status   │
├────┼─────────────────────┼──────────┼────────┼──────────┤
│ 1  │ Morning Workout     │ 07:00    │ 08:00  │ Completed│
│ 2  │ Research Session   │ 10:00    │ 12:00  │ Pending  │
└────┴─────────────────────┴──────────┴────────┴──────────┘

⚠️ Note: 2 high-priority tasks scheduled today
💡 Tip: Consider balancing priorities across the schedule

Press Enter to continue...
```

### Productivity Analysis Report
```
=== PRODUCTIVITY ANALYSIS REPORT ===

📊 SCHEDULE OVERVIEW:
• Total tasks: 3
• Completed tasks: 1 (33.3%)
• Pending tasks: 2 (66.7%)
• Total scheduled time: 3.5 hours
• Schedule efficiency: Good

🔴 PRIORITY DISTRIBUTION:
• HIGH priority: 2 tasks (66.7%) ⚠️
• MEDIUM priority: 1 task (33.3%)
• LOW priority: 0 tasks (0.0%)
• CRITICAL priority: 0 tasks (0.0%)

⏰ TIME MANAGEMENT:
• Average task duration: 1.17 hours
• Longest task: Research Session (2.0 hours)
• Shortest task: Team Standup (0.5 hours)
• Schedule span: 5.0 hours (07:00 - 12:00)

🎯 RECOMMENDATIONS:
1. ⚠️ High concentration of HIGH priority tasks detected
2. ✅ Good task duration balance maintained
3. 💡 Consider adding buffer time between tasks
4. 📈 Current completion rate: 33.3% - room for improvement

🌟 PRODUCTIVITY SCORE: 7.2/10

Press Enter to continue...
```

### Export Schedule - CSV Format
```
=== EXPORT SCHEDULE ===

Select export format:
1. Text (.txt)
2. CSV (.csv)

Enter format [1-2]: 2

Enter filename (without extension): DailySchedule_Oct01

✅ SUCCESS: Schedule exported successfully!
📄 File: DailySchedule_Oct01.csv
📍 Location: Current directory
📊 Records exported: 3 tasks

Export Preview:
Task No.,Description,Start Time,End Time,Duration,Priority,Status
1,Morning Workout,07:00,08:00,1.00,HIGH,Completed
2,Team Standup,09:00,09:30,0.50,MEDIUM,Completed
3,Research Session,10:00,12:00,2.00,HIGH,Pending

Press Enter to continue...
```

### Export Schedule - Text Format
```
=== ASTRONAUT DAILY SCHEDULE ===
Generated: 2025-10-01 at 14:30:25

SCHEDULE OVERVIEW
=================
Total Tasks: 3
Completed: 1
Pending: 2
Total Hours: 3.5

TASK DETAILS
============

Task #1: Morning Workout
├─ Time: 07:00 - 08:00 (1.0 hours)
├─ Priority: HIGH
├─ Status: ✅ COMPLETED


Task #2: Team Standup
├─ Time: 09:00 - 09:30 (0.5 hours)
├─ Priority: MEDIUM
├─ Status: ✅ COMPLETED


Task #3: Research Session
├─ Time: 10:00 - 12:00 (2.0 hours)
├─ Priority: HIGH
├─ Status: ⏳ PENDING


SCHEDULE STATISTICS
==================
• First task: 07:00
• Last task: 12:00
• Schedule span: 5.0 hours
• Completion rate: 33.3%
• Average task duration: 1.17 hours

Generated by Astronaut Schedule Organizer v1.0
```

### Error Handling Examples
```
=== INPUT VALIDATION EXAMPLES ===

Invalid Time Format:
Enter start time (HH:mm): 25:00
❌ ERROR: Invalid time format! Please use HH:mm (24-hour format)

Invalid Task Duration:
Enter start time (HH:mm): 10:00
Enter end time (HH:mm): 10:05
❌ ERROR: Task too short! Minimum duration is 15 minutes.

Empty Task Description:
Enter task description: 
❌ ERROR: Task description cannot be empty! (3-100 characters required)

Task Not Found:
Select task to remove [1-5]: 10
❌ ERROR: Invalid task number! Please select from 1-3.
```

## Input Validation Features

### Defensive Programming
- **Null checks** at all input points
- **Input sanitization** to prevent injection attacks
- **Range validation** for time and duration
- **Format validation** for time strings
- **Length validation** for descriptions

### Time Validation
- 24-hour format (HH:mm) enforcement
- Start time must be before end time
- Minimum task duration: 15 minutes
- Maximum task duration: 8 hours
- No overlapping tasks allowed

### Error Handling
- **Custom exceptions** for specific error types
- **Graceful degradation** on validation failures
- **Clear error messages** with correction guidance
- **Operation rollback** on failures

## Productivity Analyzer Features

### Analysis Categories
1. **Schedule Overview**
   - Total tasks and completion rates
   - Time distribution analysis
   - Daily workload assessment

2. **Priority Distribution**
   - Balance of priority levels
   - High-priority task clustering detection
   - Workload distribution recommendations

3. **Time Management**
   - Task duration optimization
   - Work period analysis
   - Schedule span evaluation

4. **Break Analysis**
   - Break frequency and duration
   - Continuous work period detection
   - Work-life balance recommendations

### Sample Analysis Output
```
=== PRODUCTIVITY ANALYSIS REPORT ===

SCHEDULE OVERVIEW:
- Total tasks: 5
- Completed tasks: 2
- Pending tasks: 3
- Total scheduled time: 6.5 hours

PRIORITY DISTRIBUTION ANALYSIS:
- High priority: 3 tasks (60.0%)
- Medium priority: 2 tasks (40.0%)

WARNING: High percentage of high-priority tasks detected!

RECOMMENDATIONS:
1. Consider redistributing high-priority tasks across multiple days
2. Add breaks between consecutive tasks for better productivity
3. Evaluate if all high-priority tasks are truly urgent
```

## Export Functionality

### Supported Formats

#### Text Format (.txt)
- Human-readable format
- Detailed task information
- Timestamped generation
- Perfect for printing or email

#### CSV Format (.csv)
- Spreadsheet compatible
- Structured data format
- Easy data manipulation
- Integration with other tools

### Export Features
- **Automatic file extension** addition
- **Sorted output** by start time
- **Complete task details** included
- **Generation timestamps** for tracking

## Logging System

### Log Levels
- **INFO**: Normal operations and successful actions
- **WARN**: Non-critical issues and warnings
- **ERROR**: Error conditions with stack traces
- **DEBUG**: Detailed debugging information

### Log Features
- **File and console** dual output
- **Automatic timestamping**
- **Method entry/exit tracking**
- **Exception stack traces**
- **Thread-safe operations**

### Log File Location
- `astronaut_schedule.log` in application directory
- Persistent across application runs
- Useful for debugging and audit trails

## Error Handling Strategy

### Exception Hierarchy
```
Exception
├── TaskValidationException    # Input validation failures
├── TaskConflictException     # Schedule conflict errors
└── TaskNotFoundException     # Missing task errors
```

### Error Recovery
- **Graceful error handling** with user-friendly messages
- **Operation continuation** after errors
- **State preservation** during failures
- **Clear error reporting** with suggested actions

---


