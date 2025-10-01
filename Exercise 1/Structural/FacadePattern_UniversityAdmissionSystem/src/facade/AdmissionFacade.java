package facade;

import models.*;
import subsystems.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Admission Facade - Main facade class implementing the Facade Pattern
 * This class provides a simplified interface to the complex university admission system
 * It coordinates between Document Verification, Fee Payment, Course Allocation, and ID Generation subsystems
 * 
 * The Facade Pattern is demonstrated here by:
 * 1. Hiding the complexity of multiple subsystems from the client
 * 2. Providing a single, unified interface for admission process
 * 3. Managing the workflow and dependencies between subsystems
 * 4. Ensuring proper sequence of operations
 */
public class AdmissionFacade {
    // Subsystem instances - The facade coordinates these complex subsystems
    private DocumentVerificationSystem documentSystem;
    private FeePaymentSystem feeSystem;
    private CourseAllocationSystem courseSystem;
    private IDGenerationSystem idSystem;
    
    /**
     * Constructor initializes all subsystems
     * The facade acts as a single point of access to multiple subsystems
     */
    public AdmissionFacade() {
        System.out.println("Initializing University Admission System...");
        
        this.documentSystem = new DocumentVerificationSystem();
        this.feeSystem = new FeePaymentSystem();
        this.courseSystem = new CourseAllocationSystem();
        this.idSystem = new IDGenerationSystem();
        
        System.out.println("All subsystems initialized successfully!");
        System.out.println("Admission Facade ready for operations");
    }
    
    /**
     * Main facade method - Enrolls a student through the complete admission process
     * This single method coordinates all subsystems internally, hiding complexity from client
     * 
     * @param student Student applying for admission
     * @param courseCode Course code for enrollment
     * @param documentTypes List of document types submitted
     * @param paymentMethod Payment method for fees
     * @return AdmissionResult containing the complete admission status
     */
    public AdmissionResult enrollStudent(Student student, String courseCode, 
                                       List<Document.DocumentType> documentTypes, 
                                       Fee.PaymentMethod paymentMethod) {
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("UNIVERSITY ADMISSION PROCESS INITIATED");
        System.out.println("=".repeat(60));
        System.out.println("Student: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Requested Course: " + courseCode);
        System.out.println("Process Started: " + LocalDateTime.now());
        System.out.println("=".repeat(60));
        
        AdmissionResult result = new AdmissionResult();
        result.setStudent(student);
        result.setRequestedCourseCode(courseCode);
        result.setProcessStartTime(LocalDateTime.now());
        
        try {
            // STEP 1: Course Validation and Allocation (Tentative)
            Course selectedCourse = validateAndAllocateCourse(student, courseCode, result);
            if (selectedCourse == null) {
                // No need to cancel allocation as course allocation itself failed
                return result; // Early termination if course allocation fails
            }
            
            // STEP 2: Document Verification
            boolean documentsVerified = processDocumentVerification(student, documentTypes, result);
            if (!documentsVerified) {
                // Cancel tentative allocation since document verification failed
                courseSystem.cancelTentativeAllocation(student.getEmail());
                result.addProcessStep("ALLOCATION_CANCELLED", true, "Tentative allocation cancelled due to document failure");
                return result; // Early termination if document verification fails
            }
            
            // STEP 3: Fee Calculation and Payment
            boolean feesProcessed = processFeePayment(student, selectedCourse, paymentMethod, result);
            if (!feesProcessed) {
                // Cancel tentative allocation since fee payment failed
                courseSystem.cancelTentativeAllocation(student.getEmail());
                result.addProcessStep("ALLOCATION_CANCELLED", true, "Tentative allocation cancelled due to payment failure");
                return result; // Early termination if fee payment fails
            }
            
            // STEP 4: Student ID Generation
            boolean idGenerated = generateStudentId(student, selectedCourse, result);
            if (!idGenerated) {
                // Cancel tentative allocation since ID generation failed
                courseSystem.cancelTentativeAllocation(student.getEmail());
                result.addProcessStep("ALLOCATION_CANCELLED", true, "Tentative allocation cancelled due to ID generation failure");
                return result; // Early termination if ID generation fails
            }
            
            // STEP 5: Final Enrollment Completion - Confirm the allocation and increase capacity
            completeEnrollment(student, selectedCourse, result);
            
        } catch (Exception e) {
            System.out.println("ERROR: CRITICAL ERROR: " + e.getMessage());
            // Cancel tentative allocation in case of system error
            courseSystem.cancelTentativeAllocation(student.getEmail());
            result.setSuccess(false);
            result.setMessage("Admission process failed due to system error: " + e.getMessage());
            result.addProcessStep("SYSTEM_ERROR", false, "Critical system error occurred");
            result.addProcessStep("ALLOCATION_CANCELLED", true, "Tentative allocation cancelled due to system error");
        }
        
        result.setProcessEndTime(LocalDateTime.now());
        displayAdmissionSummary(result);
        
        return result;
    }
    
    /**
     * Step 1: Validates course and attempts allocation
     */
    private Course validateAndAllocateCourse(Student student, String courseCode, AdmissionResult result) {
        System.out.println("\nSTEP 1: COURSE VALIDATION & ALLOCATION");
        System.out.println("==========================================");
        
        Course course = courseSystem.getCourse(courseCode);
        if (course == null) {
            result.setSuccess(false);
            result.setMessage("Invalid course code: " + courseCode);
            result.addProcessStep("COURSE_VALIDATION", false, "Course not found");
            System.out.println("ERROR: Course validation failed!");
            return null;
        }
        
        CourseAllocationSystem.AllocationResult allocationResult = 
            courseSystem.allocateCourse(student, courseCode);
        
        if (allocationResult.isSuccess()) {
            result.setAllocatedCourse(allocationResult.getAllocatedCourse());
            result.addProcessStep("COURSE_ALLOCATION", true, "Course tentatively allocated - pending verification");
            System.out.println("SUCCESS: Course tentatively allocated!");
            System.out.println("NOTE: Capacity will increase only after successful completion of all steps");
            return allocationResult.getAllocatedCourse();
        } else if (allocationResult.isWaitlisted()) {
            result.setSuccess(false);
            result.setMessage(allocationResult.getMessage());
            result.addProcessStep("COURSE_ALLOCATION", false, "Course full - added to waitlist");
            System.out.println("WARNING: Course allocation failed - waitlisted!");
            return null;
        } else {
            result.setSuccess(false);
            result.setMessage(allocationResult.getMessage());
            result.addProcessStep("COURSE_ALLOCATION", false, "Course allocation failed");
            System.out.println("ERROR: Course allocation failed!");
            return null;
        }
    }
    
    /**
     * Step 2: Processes document verification
     */
    private boolean processDocumentVerification(Student student, List<Document.DocumentType> documentTypes, 
                                              AdmissionResult result) {
        System.out.println("\nSTEP 2: DOCUMENT VERIFICATION");
        System.out.println("=================================");
        
        // Submit documents
        boolean submitted = documentSystem.submitDocuments(student, documentTypes);
        if (!submitted) {
            result.addProcessStep("DOCUMENT_SUBMISSION", false, "Document submission failed");
            System.out.println("ERROR: Document submission failed!");
            return false;
        }
        
        result.addProcessStep("DOCUMENT_SUBMISSION", true, "Documents submitted successfully");
        
        // Verify documents
        DocumentVerificationSystem.VerificationResult verificationResult = 
            documentSystem.verifyDocuments(student);
        
        if (verificationResult.isSuccess()) {
            result.addProcessStep("DOCUMENT_VERIFICATION", true, verificationResult.getMessage());
            result.setDocumentDetails(verificationResult.getDetails());
            System.out.println("SUCCESS: Document verification successful!");
            return true;
        } else {
            result.setSuccess(false);
            result.setMessage("Document verification failed: " + verificationResult.getMessage());
            result.addProcessStep("DOCUMENT_VERIFICATION", false, verificationResult.getMessage());
            result.setDocumentDetails(verificationResult.getDetails());
            System.out.println("ERROR: Document verification failed!");
            return false;
        }
    }
    
    /**
     * Step 3: Processes fee calculation and payment
     */
    private boolean processFeePayment(Student student, Course course, Fee.PaymentMethod paymentMethod, 
                                    AdmissionResult result) {
        System.out.println("\nSTEP 3: FEE CALCULATION & PAYMENT");
        System.out.println("===================================");
        
        // Calculate fees
        FeePaymentSystem.FeeCalculationResult feeCalculation = 
            feeSystem.calculateFees(student, course);
        
        if (!feeCalculation.isSuccess()) {
            result.addProcessStep("FEE_CALCULATION", false, "Fee calculation failed");
            System.out.println("ERROR: Fee calculation failed!");
            return false;
        }
        
        result.addProcessStep("FEE_CALCULATION", true, "Fees calculated successfully");
        result.setTotalFees(feeCalculation.getTotalAmount());
        result.setFeeBreakdown(feeCalculation.getFees());
        
        // Process payment
        FeePaymentSystem.PaymentResult paymentResult = 
            feeSystem.processPayment(student, paymentMethod, feeCalculation.getTotalAmount());
        
        if (paymentResult.isSuccess()) {
            result.addProcessStep("FEE_PAYMENT", true, "Payment processed successfully");
            result.setTransactionId(paymentResult.getTransactionId());
            result.setPaymentMethod(paymentMethod);
            System.out.println("SUCCESS: Fee payment successful!");
            return true;
        } else {
            result.setSuccess(false);
            result.setMessage("Payment failed: " + paymentResult.getMessage());
            result.addProcessStep("FEE_PAYMENT", false, paymentResult.getMessage());
            System.out.println("ERROR: Fee payment failed!");
            return false;
        }
    }
    
    /**
     * Step 4: Generates student ID
     */
    private boolean generateStudentId(Student student, Course course, AdmissionResult result) {
        System.out.println("\nSTEP 4: STUDENT ID GENERATION");
        System.out.println("===============================");
        
        IDGenerationSystem.IDGenerationResult idResult = 
            idSystem.generateStudentId(student, course);
        
        if (idResult.isSuccess()) {
            result.addProcessStep("ID_GENERATION", true, "Student ID generated successfully");
            result.setGeneratedStudentId(idResult.getStudentId());
            result.setIdCardDetails(idResult.getIdCardDetails());
            System.out.println("SUCCESS: Student ID generation successful!");
            return true;
        } else {
            result.setSuccess(false);
            result.setMessage("ID generation failed: " + idResult.getMessage());
            result.addProcessStep("ID_GENERATION", false, idResult.getMessage());
            System.out.println("ERROR: Student ID generation failed!");
            return false;
        }
    }
    
    /**
     * Step 5: Completes the enrollment process
     */
    private void completeEnrollment(Student student, Course course, AdmissionResult result) {
        System.out.println("\nSTEP 5: ENROLLMENT COMPLETION");
        System.out.println("===============================");
        
        // Confirm the tentative allocation - NOW increase the capacity!
        boolean enrolled = courseSystem.confirmEnrollment(student.getEmail());
        if (enrolled) {
            // Mark student as enrolled
            student.setEnrolled(true);
            
            // Final success confirmation
            result.setSuccess(true);
            result.setMessage("Admission process completed successfully! Welcome to " + course.getCourseName());
            result.addProcessStep("ENROLLMENT_COMPLETION", true, "Student successfully enrolled and capacity updated");
            
            System.out.println("SUCCESS: Enrollment completed successfully!");
            System.out.println("Welcome to " + course.getCourseName() + "!");
            System.out.println("Course capacity has been officially increased!");
        } else {
            // This should not happen, but handle it gracefully
            result.setSuccess(false);
            result.setMessage("Failed to confirm enrollment - please contact administration");
            result.addProcessStep("ENROLLMENT_COMPLETION", false, "Failed to confirm enrollment");
            System.out.println("ERROR: Failed to confirm enrollment!");
        }
    }
    
    /**
     * Displays comprehensive admission summary
     */
    private void displayAdmissionSummary(AdmissionResult result) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ADMISSION PROCESS SUMMARY");
        System.out.println("=".repeat(60));
        
        System.out.println("Student: " + result.getStudent().getName());
        System.out.println("Email: " + result.getStudent().getEmail());
        
        if (result.getAllocatedCourse() != null) {
            System.out.println("Course: " + result.getAllocatedCourse().getCourseName());
        }
        
        if (result.getGeneratedStudentId() != null) {
            System.out.println("Student ID: " + result.getGeneratedStudentId());
        }
        
        if (result.getTransactionId() != null) {
            System.out.println("Transaction ID: " + result.getTransactionId());
        }
        
        System.out.println("Duration: " + 
            java.time.Duration.between(result.getProcessStartTime(), result.getProcessEndTime()).toSeconds() + " seconds");
        
        System.out.println("\nProcess Steps:");
        for (AdmissionResult.ProcessStep step : result.getProcessSteps()) {
            String status = step.isSuccess() ? "SUCCESS" : "ERROR";
            System.out.println("   " + status + " " + step.getStepName() + ": " + step.getDescription());
        }
        
        System.out.println("\nFinal Status: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
        System.out.println("Message: " + result.getMessage());
        
        // Display ID Card if successful
        if (result.isSuccess() && result.getIdCardDetails() != null) {
            result.getIdCardDetails().displayIdCard();
        }
        
        System.out.println("=".repeat(60));
    }
    
    /**
     * Displays available courses (delegates to course allocation system)
     */
    public void displayAvailableCourses() {
        courseSystem.displayAvailableCourses();
    }
    
    /**
     * Gets system statistics
     */
    public SystemStats getSystemStats() {
        CourseAllocationSystem.EnrollmentStats enrollmentStats = courseSystem.getEnrollmentStats();
        IDGenerationSystem.IDSystemStats idStats = idSystem.getSystemStats();
        
        return new SystemStats(enrollmentStats, idStats);
    }
    
    /**
     * Inner class for system statistics
     */
    public static class SystemStats {
        private CourseAllocationSystem.EnrollmentStats enrollmentStats;
        private IDGenerationSystem.IDSystemStats idStats;
        
        public SystemStats(CourseAllocationSystem.EnrollmentStats enrollmentStats, 
                          IDGenerationSystem.IDSystemStats idStats) {
            this.enrollmentStats = enrollmentStats;
            this.idStats = idStats;
        }
        
        public CourseAllocationSystem.EnrollmentStats getEnrollmentStats() { return enrollmentStats; }
        public IDGenerationSystem.IDSystemStats getIdStats() { return idStats; }
        
        public void displayStats() {
            System.out.println("\nSYSTEM STATISTICS");
            System.out.println("==================");
            System.out.println("Enrollment Statistics:");
            System.out.println("   Total Capacity: " + enrollmentStats.getTotalCapacity());
            System.out.println("   Total Enrolled: " + enrollmentStats.getTotalEnrolled());
            System.out.println("   Available Seats: " + enrollmentStats.getTotalAvailable());
            System.out.println("   Waitlisted: " + enrollmentStats.getTotalWaitlisted());
            
            System.out.println("\n ID Generation Statistics:");
            System.out.println("   Total IDs Generated: " + idStats.getTotalGenerated());
            System.out.println("   Generated Today: " + idStats.getTodayGenerated());
            System.out.println("   Generated This Year: " + idStats.getThisYearGenerated());
        }
    }
    
    /**
     * Gets real-time system capacity information
     * This method demonstrates the facade pattern by providing unified access to capacity data
     */
    public void displayRealTimeCapacityStatus() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("REAL-TIME SYSTEM CAPACITY STATUS");
        System.out.println("=".repeat(60));
        
        // Display system-wide statistics
        courseSystem.displaySystemCapacityStats();
        
        System.out.println("COURSE-WISE CAPACITY DETAILS");
        System.out.println("==============================");
        
        // Display individual course capacities
        for (Course course : courseSystem.getAllCourses().values()) {
            System.out.printf("%-8s | %-35s | %3d/%3d | %6.1f%% | %s%n",
                            course.getCourseCode(),
                            course.getCourseName().length() > 35 ? 
                                course.getCourseName().substring(0, 32) + "..." : course.getCourseName(),
                            course.getCurrentEnrollment(),
                            course.getMaxCapacity(),
                            course.getUtilizationPercentage(),
                            course.getCapacityStatus());
        }
        
        System.out.println("=".repeat(60));
    }
    
    /**
     * Withdraws a student from the system
     * Demonstrates facade pattern by coordinating withdrawal across all subsystems
     */
    public boolean withdrawStudent(String studentEmail) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("STUDENT WITHDRAWAL PROCESS");
        System.out.println("=".repeat(50));
        System.out.println("Processing withdrawal for: " + studentEmail);
        
        // Check if student is enrolled
        if (!courseSystem.hasAllocatedCourse(studentEmail)) {
            System.out.println("ERROR: Student not found in enrollment records");
            return false;
        }
        
        // Get current course allocation
        Course allocatedCourse = courseSystem.getAllocatedCourse(studentEmail);
        System.out.println("Current enrollment: " + allocatedCourse.getCourseName());
        
        // Withdraw from course allocation system
        boolean courseWithdrawal = courseSystem.withdrawStudent(studentEmail);
        if (!courseWithdrawal) {
            System.out.println("ERROR: Failed to withdraw from course system");
            return false;
        }
        
        // Additional cleanup could be added here for other subsystems
        // For example: refund processing, document cleanup, etc.
        
        System.out.println("SUCCESS: Student withdrawal completed");
        System.out.println("Updated capacity: " + courseSystem.getRealTimeCapacityInfo(allocatedCourse.getCourseCode()));
        System.out.println("=".repeat(50));
        
        return true;
    }
    
    /**
     * Gets real-time capacity information for a specific course
     */
    public String getCourseCapacityStatus(String courseCode) {
        return courseSystem.getRealTimeCapacityInfo(courseCode);
    }
    
    /**
     * Checks if a course is available for enrollment
     */
    public boolean isCourseAvailable(String courseCode) {
        Course course = courseSystem.getCourse(courseCode);
        return course != null && course.hasAvailableSeats() && course.isActive();
    }
    
    /**
     * Gets system enrollment statistics
     */
    public CourseAllocationSystem.EnrollmentStats getSystemEnrollmentStats() {
        return courseSystem.getEnrollmentStats();
    }
}
