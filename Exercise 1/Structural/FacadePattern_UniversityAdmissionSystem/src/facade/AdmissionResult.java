package facade;

import models.*;
import subsystems.IDGenerationSystem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Admission Result - Encapsulates the complete result of the admission process
 * This class contains all information about the admission process outcome
 */
public class AdmissionResult {
    private boolean success;
    private String message;
    private Student student;
    private String requestedCourseCode;
    private Course allocatedCourse;
    private String generatedStudentId;
    private String transactionId;
    private Fee.PaymentMethod paymentMethod;
    private double totalFees;
    private List<Fee> feeBreakdown;
    private List<String> documentDetails;
    private IDGenerationSystem.IDCardDetails idCardDetails;
    private LocalDateTime processStartTime;
    private LocalDateTime processEndTime;
    private List<ProcessStep> processSteps;
    
    /**
     * Constructor initializes the admission result
     */
    public AdmissionResult() {
        this.success = false;
        this.processSteps = new ArrayList<>();
        this.documentDetails = new ArrayList<>();
        this.feeBreakdown = new ArrayList<>();
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public String getRequestedCourseCode() { return requestedCourseCode; }
    public void setRequestedCourseCode(String requestedCourseCode) { this.requestedCourseCode = requestedCourseCode; }
    
    public Course getAllocatedCourse() { return allocatedCourse; }
    public void setAllocatedCourse(Course allocatedCourse) { this.allocatedCourse = allocatedCourse; }
    
    public String getGeneratedStudentId() { return generatedStudentId; }
    public void setGeneratedStudentId(String generatedStudentId) { this.generatedStudentId = generatedStudentId; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public Fee.PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(Fee.PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public double getTotalFees() { return totalFees; }
    public void setTotalFees(double totalFees) { this.totalFees = totalFees; }
    
    public List<Fee> getFeeBreakdown() { return feeBreakdown; }
    public void setFeeBreakdown(List<Fee> feeBreakdown) { this.feeBreakdown = feeBreakdown; }
    
    public List<String> getDocumentDetails() { return documentDetails; }
    public void setDocumentDetails(List<String> documentDetails) { this.documentDetails = documentDetails; }
    
    public IDGenerationSystem.IDCardDetails getIdCardDetails() { return idCardDetails; }
    public void setIdCardDetails(IDGenerationSystem.IDCardDetails idCardDetails) { this.idCardDetails = idCardDetails; }
    
    public LocalDateTime getProcessStartTime() { return processStartTime; }
    public void setProcessStartTime(LocalDateTime processStartTime) { this.processStartTime = processStartTime; }
    
    public LocalDateTime getProcessEndTime() { return processEndTime; }
    public void setProcessEndTime(LocalDateTime processEndTime) { this.processEndTime = processEndTime; }
    
    public List<ProcessStep> getProcessSteps() { return processSteps; }
    
    /**
     * Adds a process step to the result
     * @param stepName Name of the step
     * @param success Whether the step was successful
     * @param description Description of the step result
     */
    public void addProcessStep(String stepName, boolean success, String description) {
        processSteps.add(new ProcessStep(stepName, success, description, LocalDateTime.now()));
    }
    
    /**
     * Inner class representing a process step
     */
    public static class ProcessStep {
        private String stepName;
        private boolean success;
        private String description;
        private LocalDateTime timestamp;
        
        public ProcessStep(String stepName, boolean success, String description, LocalDateTime timestamp) {
            this.stepName = stepName;
            this.success = success;
            this.description = description;
            this.timestamp = timestamp;
        }
        
        public String getStepName() { return stepName; }
        public boolean isSuccess() { return success; }
        public String getDescription() { return description; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
}
