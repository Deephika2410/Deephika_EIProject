package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// CustomerProfile.java - Enhanced customer class with transaction history
public class CustomerProfile {
    private PaymentDetails paymentDetails;
    private List<Transaction> transactionHistory;
    private boolean isPaymentSetupComplete;
    private String preferredPaymentMethod;
    private Map<String, Object> paymentMethodConfigs; // Store payment method configurations
    
    public CustomerProfile(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
        this.transactionHistory = new ArrayList<>();
        this.isPaymentSetupComplete = false;
        this.preferredPaymentMethod = "None";
        this.paymentMethodConfigs = new HashMap<>();
    }
    
    // Getters and Setters
    public PaymentDetails getPaymentDetails() { return paymentDetails; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }
    public boolean isPaymentSetupComplete() { return isPaymentSetupComplete; }
    public void setPaymentSetupComplete(boolean complete) { this.isPaymentSetupComplete = complete; }
    public String getPreferredPaymentMethod() { return preferredPaymentMethod; }
    public void setPreferredPaymentMethod(String method) { this.preferredPaymentMethod = method; }
    public Map<String, Object> getPaymentMethodConfigs() { return paymentMethodConfigs; }
    
    // Payment method configuration management
    public void storePaymentMethodConfig(String method, Map<String, String> config) {
        paymentMethodConfigs.put(method, config);
    }
    
    public Map<String, String> getPaymentMethodConfig(String method) {
        Object config = paymentMethodConfigs.get(method);
        if (config instanceof Map) {
            return (Map<String, String>) config;
        }
        return new HashMap<>();
    }
    
    public boolean hasPaymentMethodConfig(String method) {
        return paymentMethodConfigs.containsKey(method);
    }
    
    // Transaction management
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
    
    public int getTransactionCount() {
        return transactionHistory.size();
    }
    
    public double getTotalSpent() {
        return transactionHistory.stream()
                .filter(t -> "SUCCESS".equals(t.getStatus()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    public List<Transaction> getSuccessfulTransactions() {
        return transactionHistory.stream()
                .filter(t -> "SUCCESS".equals(t.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public List<Transaction> getFailedTransactions() {
        return transactionHistory.stream()
                .filter(t -> "FAILED".equals(t.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }
}
