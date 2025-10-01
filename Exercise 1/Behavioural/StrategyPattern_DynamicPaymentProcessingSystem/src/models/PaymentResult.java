package models;

// PaymentResult.java - Result Object
public class PaymentResult {
    private boolean success;
    private String transactionId;
    private String message;
    private double amount;
    private String paymentMethod;
    
    public PaymentResult(boolean success, String transactionId, String message, double amount, String paymentMethod) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getTransactionId() { return transactionId; }
    public String getMessage() { return message; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
}
