package subsystems;

import models.Student;
import models.Course;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ID Generation System - Handles student ID generation and management
 * This subsystem generates unique student IDs and manages student registration
 * Part of the Facade Pattern implementation for University Admission System
 */
public class IDGenerationSystem {
    private Map<String, String> studentIdMapping;
    private Set<String> generatedIds;
    private Map<String, LocalDateTime> idGenerationHistory;
    private int currentIdSequence;
    
    /**
     * Constructor initializes the ID generation system
     */
    public IDGenerationSystem() {
        this.studentIdMapping = new HashMap<>();
        this.generatedIds = new HashSet<>();
        this.idGenerationHistory = new HashMap<>();
        this.currentIdSequence = 2024001; // Starting sequence for 2024
    }
    
    /**
     * Generates a unique student ID based on course and admission year
     * @param student Student for whom ID is being generated
     * @param course Course enrolled by the student
     * @return IDGenerationResult containing the generated ID and details
     */
    public IDGenerationResult generateStudentId(Student student, Course course) {
        System.out.println("\nID GENERATION SYSTEM");
        System.out.println("======================");
        System.out.println("Generating Student ID...");
        
        String studentEmail = student.getEmail();
        
        // Check if ID already exists for this student
        if (studentIdMapping.containsKey(studentEmail)) {
            String existingId = studentIdMapping.get(studentEmail);
            System.out.println("WARNING: Student ID already exists: " + existingId);
            return new IDGenerationResult(false, existingId, "Student ID already generated", null);
        }
        
        // Generate ID components
        String academicYear = String.valueOf(LocalDateTime.now().getYear());
        String departmentCode = getDepartmentCode(course.getDepartment());
        String courseTypeCode = getCourseTypeCode(course.getCourseType());
        
        System.out.println("ID Generation Details:");
        System.out.println("   Student: " + student.getName());
        System.out.println("   Course: " + course.getCourseName());
        System.out.println("   Department: " + course.getDepartment());
        System.out.println("   Course Type: " + course.getCourseType().getDisplayName());
        
        // Generate unique student ID
        String studentId = null;
        int attempts = 0;
        int maxAttempts = 10;
        
        while (studentId == null && attempts < maxAttempts) {
            attempts++;
            String candidateId = generateIdCandidate(academicYear, departmentCode, courseTypeCode);
            
            if (!generatedIds.contains(candidateId)) {
                studentId = candidateId;
                generatedIds.add(studentId);
                break;
            }
        }
        
        if (studentId == null) {
            System.out.println("ERROR: Failed to generate unique ID after " + maxAttempts + " attempts");
            return new IDGenerationResult(false, null, "Failed to generate unique ID", null);
        }
        
        // Store mapping and history
        studentIdMapping.put(studentEmail, studentId);
        idGenerationHistory.put(studentId, LocalDateTime.now());
        
        // Update student object
        student.setStudentId(studentId);
        
        // Create ID card details
        IDCardDetails idCard = createIDCardDetails(student, course, studentId);
        
        System.out.println("\nStudent ID Generated Successfully!");
        System.out.println("Student ID: " + studentId);
        System.out.println("Generated On: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")));
        
        // Display ID breakdown
        System.out.println("\nID Breakdown:");
        System.out.println("   Academic Year: " + academicYear);
        System.out.println("   Department Code: " + departmentCode + " (" + course.getDepartment() + ")");
        System.out.println("   Course Type: " + courseTypeCode + " (" + course.getCourseType().getDisplayName() + ")");
        System.out.println("   Sequence Number: " + studentId.substring(studentId.length() - 3));
        
        return new IDGenerationResult(true, studentId, "Student ID generated successfully", idCard);
    }
    
    /**
     * Generates department code based on department name
     * @param department Department name
     * @return Two-character department code
     */
    private String getDepartmentCode(String department) {
        switch (department.toLowerCase()) {
            case "computer science":
            case "computer applications":
                return "CS";
            case "engineering":
                return "EN";
            case "electrical":
            case "electronics":
                return "EE";
            case "mechanical":
                return "ME";
            case "civil":
                return "CE";
            case "management":
                return "MG";
            case "commerce":
                return "CM";
            case "science":
                return "SC";
            case "arts":
                return "AR";
            default:
                return "GN"; // General
        }
    }
    
    /**
     * Generates course type code
     * @param courseType Course type
     * @return Single character course type code
     */
    private String getCourseTypeCode(Course.CourseType courseType) {
        switch (courseType) {
            case UNDERGRADUATE:
                return "U";
            case POSTGRADUATE:
                return "P";
            case DIPLOMA:
                return "D";
            case CERTIFICATE:
                return "C";
            case RESEARCH:
                return "R";
            default:
                return "G"; // General
        }
    }
    
    /**
     * Generates ID candidate
     * @param year Academic year
     * @param deptCode Department code
     * @param typeCode Course type code
     * @return Generated ID candidate
     */
    private String generateIdCandidate(String year, String deptCode, String typeCode) {
        String sequence = String.format("%03d", currentIdSequence++);
        return year + deptCode + typeCode + sequence;
    }
    
    /**
     * Creates ID card details
     * @param student Student information
     * @param course Course information
     * @param studentId Generated student ID
     * @return IDCardDetails object
     */
    private IDCardDetails createIDCardDetails(Student student, Course course, String studentId) {
        LocalDateTime issueDate = LocalDateTime.now();
        LocalDateTime expiryDate = issueDate.plusYears(course.getDuration() + 1); // Extra year for buffer
        
        return new IDCardDetails(
            studentId,
            student.getName(),
            course.getCourseName(),
            course.getDepartment(),
            issueDate,
            expiryDate,
            "ACTIVE"
        );
    }
    
    /**
     * Validates if a student ID exists and is valid
     * @param studentId Student ID to validate
     * @return true if valid and exists
     */
    public boolean validateStudentId(String studentId) {
        return generatedIds.contains(studentId);
    }
    
    /**
     * Gets student ID for a given email
     * @param studentEmail Student email
     * @return Student ID if exists, null otherwise
     */
    public String getStudentId(String studentEmail) {
        return studentIdMapping.get(studentEmail);
    }
    
    /**
     * Gets ID generation history
     * @return Map of student IDs to generation timestamps
     */
    public Map<String, LocalDateTime> getIdGenerationHistory() {
        return new HashMap<>(idGenerationHistory);
    }
    
    /**
     * Gets system statistics
     * @return IDSystemStats containing generation statistics
     */
    public IDSystemStats getSystemStats() {
        int totalGenerated = generatedIds.size();
        int currentYear = LocalDateTime.now().getYear();
        
        long todayGenerated = idGenerationHistory.values().stream()
            .filter(date -> date.toLocalDate().equals(LocalDateTime.now().toLocalDate()))
            .count();
        
        long thisYearGenerated = idGenerationHistory.values().stream()
            .filter(date -> date.getYear() == currentYear)
            .count();
        
        return new IDSystemStats(totalGenerated, (int)todayGenerated, (int)thisYearGenerated, currentIdSequence - 1);
    }
    
    /**
     * Inner class for ID generation result
     */
    public static class IDGenerationResult {
        private boolean success;
        private String studentId;
        private String message;
        private IDCardDetails idCardDetails;
        
        public IDGenerationResult(boolean success, String studentId, String message, IDCardDetails idCardDetails) {
            this.success = success;
            this.studentId = studentId;
            this.message = message;
            this.idCardDetails = idCardDetails;
        }
        
        public boolean isSuccess() { return success; }
        public String getStudentId() { return studentId; }
        public String getMessage() { return message; }
        public IDCardDetails getIdCardDetails() { return idCardDetails; }
    }
    
    /**
     * Inner class for ID card details
     */
    public static class IDCardDetails {
        private String studentId;
        private String studentName;
        private String courseName;
        private String department;
        private LocalDateTime issueDate;
        private LocalDateTime expiryDate;
        private String status;
        
        public IDCardDetails(String studentId, String studentName, String courseName, String department,
                           LocalDateTime issueDate, LocalDateTime expiryDate, String status) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.courseName = courseName;
            this.department = department;
            this.issueDate = issueDate;
            this.expiryDate = expiryDate;
            this.status = status;
        }
        
        public String getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public String getCourseName() { return courseName; }
        public String getDepartment() { return department; }
        public LocalDateTime getIssueDate() { return issueDate; }
        public LocalDateTime getExpiryDate() { return expiryDate; }
        public String getStatus() { return status; }
        
        public void displayIdCard() {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           UNIVERSITY ID CARD           ");
            System.out.println("=".repeat(50));
            System.out.println("Student ID: " + studentId);
            System.out.println("Name: " + studentName);
            System.out.println("Course: " + courseName);
            System.out.println("Department: " + department);
            System.out.println("Issue Date: " + issueDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
            System.out.println("Valid Until: " + expiryDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
            System.out.println("Status: " + status);
            System.out.println("=".repeat(50));
        }
    }
    
    /**
     * Inner class for ID system statistics
     */
    public static class IDSystemStats {
        private int totalGenerated;
        private int todayGenerated;
        private int thisYearGenerated;
        private int lastSequenceNumber;
        
        public IDSystemStats(int totalGenerated, int todayGenerated, int thisYearGenerated, int lastSequenceNumber) {
            this.totalGenerated = totalGenerated;
            this.todayGenerated = todayGenerated;
            this.thisYearGenerated = thisYearGenerated;
            this.lastSequenceNumber = lastSequenceNumber;
        }
        
        public int getTotalGenerated() { return totalGenerated; }
        public int getTodayGenerated() { return todayGenerated; }
        public int getThisYearGenerated() { return thisYearGenerated; }
        public int getLastSequenceNumber() { return lastSequenceNumber; }
    }
}
