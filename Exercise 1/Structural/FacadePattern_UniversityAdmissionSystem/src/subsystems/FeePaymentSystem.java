package subsystems;

import models.Fee;
import models.Student;
import models.Course;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Fee Payment System - Handles all fee-related operations
 * This subsystem manages fee calculation, payment processing, and fee tracking
 * Part of the Facade Pattern implementation for University Admission System
 */
public class FeePaymentSystem {
    private Map<String, List<Fee>> studentFees;
    private Map<String, Double> courseFees;
    private int transactionCounter;
    
    /**
     * Constructor initializes the fee payment system
     */
    public FeePaymentSystem() {
        this.studentFees = new HashMap<>();
        this.courseFees = new HashMap<>();
        this.transactionCounter = 1000;
        initializeCourseFees();
    }
    
    /**
     * Initializes course fees for different programs
     */
    private void initializeCourseFees() {
        courseFees.put("CS101", 50000.0);  // Computer Science
        courseFees.put("EE101", 45000.0);  // Electrical Engineering
        courseFees.put("ME101", 48000.0);  // Mechanical Engineering
        courseFees.put("CE101", 46000.0);  // Civil Engineering
        courseFees.put("MBA101", 75000.0); // MBA
        courseFees.put("MCA101", 55000.0); // MCA
        courseFees.put("BBA101", 40000.0); // BBA
        courseFees.put("BCom101", 35000.0);// B.Com
    }
    
    /**
     * Calculates total fees for a student's course
     * @param student Student for fee calculation
     * @param course Course selected by student
     * @return FeeCalculationResult containing fee breakdown
     */
    public FeeCalculationResult calculateFees(Student student, Course course) {
        System.out.println("\nFEE PAYMENT SYSTEM");
        System.out.println("===================");
        System.out.println("Calculating fees for: " + student.getName());
        System.out.println("Course: " + course.getCourseName() + " (" + course.getCourseCode() + ")");
        
        String studentId = student.getEmail();
        String courseCode = course.getCourseCode();
        
        // Base tuition fee
        double baseFee = courseFees.getOrDefault(courseCode, course.getFees());
        
        // Additional fees
        double admissionFee = 5000.0;
        double registrationFee = 2000.0;
        double libraryFee = 3000.0;
        double laboratoryFee = course.getDepartment().toLowerCase().contains("engineering") ? 8000.0 : 0.0;
        double examFee = 1500.0;
        
        // Calculate total
        double totalFee = baseFee + admissionFee + registrationFee + libraryFee + laboratoryFee + examFee;
        
        // Create fee breakdown
        List<Fee> fees = new ArrayList<>();
        LocalDateTime dueDate = LocalDateTime.now().plusDays(30);
        
        fees.add(new Fee(generateFeeId(), studentId, courseCode, baseFee, 
                        Fee.FeeType.TUITION, dueDate));
        fees.add(new Fee(generateFeeId(), studentId, courseCode, admissionFee, 
                        Fee.FeeType.ADMISSION, dueDate));
        fees.add(new Fee(generateFeeId(), studentId, courseCode, registrationFee, 
                        Fee.FeeType.REGISTRATION, dueDate));
        fees.add(new Fee(generateFeeId(), studentId, courseCode, libraryFee, 
                        Fee.FeeType.LIBRARY, dueDate));
        
        if (laboratoryFee > 0) {
            fees.add(new Fee(generateFeeId(), studentId, courseCode, laboratoryFee, 
                           Fee.FeeType.LABORATORY, dueDate));
        }
        
        fees.add(new Fee(generateFeeId(), studentId, courseCode, examFee, 
                        Fee.FeeType.EXAMINATION, dueDate));
        
        // Store fees for the student
        studentFees.put(studentId, fees);
        
        // Display fee breakdown
        System.out.println("\nFee Breakdown:");
        System.out.println("================");
        for (Fee fee : fees) {
            System.out.printf("* %-20s: Rs.%,.2f%n", fee.getFeeType().getDisplayName(), fee.getAmount());
        }
        System.out.println("   " + "=".repeat(35));
        System.out.printf("* %-20s: Rs.%,.2f%n", "TOTAL AMOUNT", totalFee);
        System.out.println("Payment Due Date: " + dueDate.toLocalDate());
        
        return new FeeCalculationResult(true, totalFee, fees, "Fee calculation completed successfully");
    }
    
    /**
     * Processes payment for student fees
     * @param student Student making payment
     * @param paymentMethod Payment method selected
     * @param paymentAmount Amount being paid
     * @return PaymentResult containing payment status
     */
    public PaymentResult processPayment(Student student, Fee.PaymentMethod paymentMethod, double paymentAmount) {
        System.out.println("\nProcessing Payment...");
        System.out.println("=======================");
        
        String studentId = student.getEmail();
        List<Fee> fees = studentFees.get(studentId);
        
        if (fees == null || fees.isEmpty()) {
            System.out.println("ERROR: No fees found for payment!");
            return new PaymentResult(false, "No fees found for student", "", 0.0);
        }
        
        // Calculate total outstanding amount
        double totalOutstanding = fees.stream()
            .filter(fee -> fee.getPaymentStatus() != Fee.PaymentStatus.PAID)
            .mapToDouble(Fee::getAmount)
            .sum();
        
        System.out.printf("Payment Amount: Rs.%,.2f%n", paymentAmount);
        System.out.printf("Outstanding Amount: Rs.%,.2f%n", totalOutstanding);
        System.out.println("Payment Method: " + paymentMethod.getDisplayName());
        
        if (paymentAmount < totalOutstanding) {
            System.out.println("WARNING: Partial payment received. Full payment required for enrollment.");
            return new PaymentResult(false, "Partial payment - full payment required", "", paymentAmount);
        }
        
        if (paymentAmount > totalOutstanding) {
            System.out.println("WARNING: Payment amount exceeds outstanding fees!");
            return new PaymentResult(false, "Payment amount exceeds outstanding fees", "", paymentAmount);
        }
        
        // Simulate payment processing
        System.out.println("Processing payment...");
        try {
            Thread.sleep(1000); // Simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Generate transaction ID
        String transactionId = generateTransactionId();
        
        // Simulate payment gateway response (95% success rate)
        boolean paymentSuccess = Math.random() > 0.05;
        
        if (paymentSuccess) {
            // Mark all fees as paid
            for (Fee fee : fees) {
                if (fee.getPaymentStatus() != Fee.PaymentStatus.PAID) {
                    fee.markAsPaid(paymentMethod, transactionId);
                }
            }
            
            System.out.println("SUCCESS: Payment processed successfully!");
            System.out.println("Transaction ID: " + transactionId);
            System.out.println("Payment Date: " + LocalDateTime.now().toLocalDate());
            
            return new PaymentResult(true, "Payment successful", transactionId, paymentAmount);
        } else {
            System.out.println("ERROR: Payment failed! Please try again.");
            return new PaymentResult(false, "Payment gateway error - please retry", "", 0.0);
        }
    }
    
    /**
     * Checks payment status for a student
     * @param studentId Student identifier
     * @return true if all fees are paid
     */
    public boolean isPaymentComplete(String studentId) {
        List<Fee> fees = studentFees.get(studentId);
        if (fees == null || fees.isEmpty()) {
            return false;
        }
        
        return fees.stream().allMatch(fee -> fee.getPaymentStatus() == Fee.PaymentStatus.PAID);
    }
    
    /**
     * Gets fee summary for a student
     * @param studentId Student identifier
     * @return FeeSummary containing payment details
     */
    public FeeSummary getFeeSummary(String studentId) {
        List<Fee> fees = studentFees.get(studentId);
        if (fees == null || fees.isEmpty()) {
            return new FeeSummary(0.0, 0.0, 0.0, new ArrayList<>());
        }
        
        double totalFees = fees.stream().mapToDouble(Fee::getAmount).sum();
        double paidAmount = fees.stream()
            .filter(fee -> fee.getPaymentStatus() == Fee.PaymentStatus.PAID)
            .mapToDouble(Fee::getAmount)
            .sum();
        double outstanding = totalFees - paidAmount;
        
        return new FeeSummary(totalFees, paidAmount, outstanding, fees);
    }
    
    /**
     * Generates unique fee ID
     * @return Fee ID
     */
    private String generateFeeId() {
        return "FEE_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    /**
     * Generates unique transaction ID
     * @return Transaction ID
     */
    private String generateTransactionId() {
        return "TXN_" + (transactionCounter++) + "_" + System.currentTimeMillis();
    }
    
    /**
     * Inner class for fee calculation result
     */
    public static class FeeCalculationResult {
        private boolean success;
        private double totalAmount;
        private List<Fee> fees;
        private String message;
        
        public FeeCalculationResult(boolean success, double totalAmount, List<Fee> fees, String message) {
            this.success = success;
            this.totalAmount = totalAmount;
            this.fees = fees;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public double getTotalAmount() { return totalAmount; }
        public List<Fee> getFees() { return fees; }
        public String getMessage() { return message; }
    }
    
    /**
     * Inner class for payment result
     */
    public static class PaymentResult {
        private boolean success;
        private String message;
        private String transactionId;
        private double amountPaid;
        
        public PaymentResult(boolean success, String message, String transactionId, double amountPaid) {
            this.success = success;
            this.message = message;
            this.transactionId = transactionId;
            this.amountPaid = amountPaid;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getTransactionId() { return transactionId; }
        public double getAmountPaid() { return amountPaid; }
    }
    
    /**
     * Inner class for fee summary
     */
    public static class FeeSummary {
        private double totalFees;
        private double paidAmount;
        private double outstandingAmount;
        private List<Fee> fees;
        
        public FeeSummary(double totalFees, double paidAmount, double outstandingAmount, List<Fee> fees) {
            this.totalFees = totalFees;
            this.paidAmount = paidAmount;
            this.outstandingAmount = outstandingAmount;
            this.fees = fees;
        }
        
        public double getTotalFees() { return totalFees; }
        public double getPaidAmount() { return paidAmount; }
        public double getOutstandingAmount() { return outstandingAmount; }
        public List<Fee> getFees() { return fees; }
    }
}
