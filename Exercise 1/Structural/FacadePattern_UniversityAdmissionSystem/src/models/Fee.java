package models;

import java.time.LocalDateTime;

/**
 * Fee model class representing fee payment information
 * Handles different types of fees and payment tracking
 */
public class Fee {
    private String feeId;
    private String studentId;
    private String courseCode;
    private double amount;
    private FeeType feeType;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private LocalDateTime dueDate;
    private LocalDateTime paidDate;
    private String transactionId;
    private String description;
    
    /**
     * Enum for different types of fees
     */
    public enum FeeType {
        ADMISSION("Admission Fee"),
        TUITION("Tuition Fee"),
        REGISTRATION("Registration Fee"),
        EXAMINATION("Examination Fee"),
        LIBRARY("Library Fee"),
        LABORATORY("Laboratory Fee"),
        HOSTEL("Hostel Fee"),
        TRANSPORT("Transport Fee"),
        MISCELLANEOUS("Miscellaneous Fee");
        
        private final String displayName;
        
        FeeType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Enum for payment status tracking
     */
    public enum PaymentStatus {
        PENDING("Pending"),
        PAID("Paid"),
        OVERDUE("Overdue"),
        PARTIAL("Partially Paid"),
        REFUNDED("Refunded"),
        CANCELLED("Cancelled");
        
        private final String displayName;
        
        PaymentStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Enum for payment methods
     */
    public enum PaymentMethod {
        CASH("Cash"),
        CREDIT_CARD("Credit Card"),
        DEBIT_CARD("Debit Card"),
        NET_BANKING("Net Banking"),
        UPI("UPI"),
        CHEQUE("Cheque"),
        DEMAND_DRAFT("Demand Draft"),
        ONLINE_TRANSFER("Online Transfer");
        
        private final String displayName;
        
        PaymentMethod(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Constructor to create a new Fee
     * @param feeId Unique fee identifier
     * @param studentId Student identifier
     * @param courseCode Course code
     * @param amount Fee amount
     * @param feeType Type of fee
     * @param dueDate Payment due date
     */
    public Fee(String feeId, String studentId, String courseCode, 
              double amount, FeeType feeType, LocalDateTime dueDate) {
        this.feeId = feeId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.amount = amount;
        this.feeType = feeType;
        this.dueDate = dueDate;
        this.paymentStatus = PaymentStatus.PENDING;
    }
    
    // Getters and Setters
    public String getFeeId() { return feeId; }
    public void setFeeId(String feeId) { this.feeId = feeId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public FeeType getFeeType() { return feeType; }
    public void setFeeType(FeeType feeType) { this.feeType = feeType; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public LocalDateTime getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDateTime paidDate) { this.paidDate = paidDate; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    /**
     * Marks the fee as paid
     * @param paymentMethod Method used for payment
     * @param transactionId Transaction reference
     */
    public void markAsPaid(PaymentMethod paymentMethod, String transactionId) {
        this.paymentStatus = PaymentStatus.PAID;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.paidDate = LocalDateTime.now();
    }
    
    /**
     * Checks if payment is overdue
     * @return true if current date is past due date and not paid
     */
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && 
               paymentStatus != PaymentStatus.PAID;
    }
    
    @Override
    public String toString() {
        return String.format("Fee{id='%s', type=%s, amount=%.2f, status=%s, due=%s}",
                           feeId, feeType.getDisplayName(), amount, 
                           paymentStatus.getDisplayName(), dueDate.toLocalDate());
    }
}
