package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Transaction.java - Transaction record for customer history
public class Transaction {
    private String transactionId;
    private double amount;
    private String paymentMethod;
    private String description;
    private String status;
    private LocalDateTime timestamp;
    private String currency;
    
    public Transaction(String transactionId, double amount, String paymentMethod, 
                      String description, String status, String currency) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.status = status;
        this.currency = currency;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getCurrency() { return currency; }
    
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f %s via %s (%s)", 
            getFormattedTimestamp(), description, amount, currency, paymentMethod, status);
    }
}
