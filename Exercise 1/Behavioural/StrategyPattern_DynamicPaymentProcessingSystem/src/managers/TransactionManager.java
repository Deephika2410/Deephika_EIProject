package managers;

import context.PaymentProcessor;
import models.CustomerProfile;
import models.PaymentResult;
import models.Transaction;
import strategies.CreditCardPayment;
import strategies.PayPalPayment;
import strategies.BitcoinPayment;
import strategies.PaymentStrategy;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * TransactionManager - Handles payment processing and transaction tracking
 * Responsible for executing payments and maintaining transaction history
 */
public class TransactionManager {
    private PaymentProcessor processor;
    
    public TransactionManager(PaymentProcessor processor) {
        this.processor = processor;
    }
    
    public PaymentResult processPayment(CustomerProfile customerProfile, double amount, 
                                      String description, boolean isUrgent) {
        if (customerProfile == null) {
            throw new IllegalArgumentException("Customer profile is required");
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        
        if (description == null || description.trim().isEmpty()) {
            description = "Payment transaction";
        }
        
        PaymentResult result = processor.executePayment(amount, customerProfile.getPaymentDetails());
        
        // Create and store transaction record
        Transaction transaction = new Transaction(
            result.getTransactionId(),
            amount,
            result.getPaymentMethod(),
            description.trim(),
            result.isSuccess() ? "SUCCESS" : "FAILED",
            customerProfile.getPaymentDetails().getCurrency()
        );
        
        customerProfile.addTransaction(transaction);
        
        return result;
    }

    public Map<String, PaymentStrategy> getAvailableStrategies(String ccNumber, String paypalEmail, String btcWallet) {
        // Use default values if not provided
        if (ccNumber == null || ccNumber.length() < 16) ccNumber = "1234567890123456";
        if (paypalEmail == null || paypalEmail.isEmpty()) paypalEmail = "demo@example.com";
        if (btcWallet == null || btcWallet.length() < 26) btcWallet = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa";
        
        Map<String, PaymentStrategy> strategies = new HashMap<>();
        strategies.put("Credit Card", new CreditCardPayment(ccNumber, "12/26", "123"));
        strategies.put("PayPal", new PayPalPayment(paypalEmail, "securepassword"));
        strategies.put("Bitcoin", new BitcoinPayment(btcWallet, "demo_private_key"));
        
        return strategies;
    }

    public PaymentResult switchAndProcessPayment(CustomerProfile customerProfile, double amount, 
                                                String paymentMethod, Map<String, PaymentStrategy> availableStrategies,
                                                String description) {
        if (customerProfile == null) {
            throw new IllegalArgumentException("Customer profile is required");
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        
        if (!availableStrategies.containsKey(paymentMethod)) {
            throw new IllegalArgumentException("Payment method not available: " + paymentMethod);
        }
        
        System.out.println("\n" + "=".repeat(20) + " SWITCHING TO: " + paymentMethod + " " + "=".repeat(20));
        
        // Switch to the selected payment strategy
        processor.setPaymentStrategy(availableStrategies.get(paymentMethod));
        
        // Process the payment
        PaymentResult result = processor.executePayment(amount, customerProfile.getPaymentDetails());
        
        // Record transaction
        Transaction transaction = new Transaction(
            result.getTransactionId(),
            amount,
            result.getPaymentMethod(),
            description != null ? description : "Interactive switching payment",
            result.isSuccess() ? "SUCCESS" : "FAILED",
            customerProfile.getPaymentDetails().getCurrency()
        );
        customerProfile.addTransaction(transaction);
        
        System.out.println("\n[PAYMENT RESULT]:");
        System.out.println("Status: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
        System.out.println("Method: " + result.getPaymentMethod());
        System.out.println("Transaction ID: " + result.getTransactionId());
        System.out.println("Amount: " + amount + " " + customerProfile.getPaymentDetails().getCurrency());
        
        return result;
    }
    
    public void displayTransactionHistory(CustomerProfile customerProfile) {
        if (customerProfile == null) {
            throw new IllegalArgumentException("Customer profile is required");
        }
        
        System.out.println("\n📈 TRANSACTION HISTORY");
        System.out.println("Customer: " + customerProfile.getPaymentDetails().getCustomerName());
        System.out.println("=" + "=".repeat(60));
        
        List<Transaction> transactions = customerProfile.getTransactionHistory();
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this customer.");
            return;
        }
        
        System.out.println("Total Transactions: " + transactions.size());
        System.out.println("Successful Transactions: " + customerProfile.getSuccessfulTransactions().size());
        System.out.println("Failed Transactions: " + customerProfile.getFailedTransactions().size());
        System.out.println("Total Amount Spent: $" + String.format("%.2f", customerProfile.getTotalSpent()) + 
                         " " + customerProfile.getPaymentDetails().getCurrency());
        System.out.println();
        
        System.out.println("Transaction Details:");
        System.out.println("-".repeat(60));
        
        for (int i = transactions.size() - 1; i >= 0; i--) { // Show most recent first
            Transaction transaction = transactions.get(i);
            System.out.println((transactions.size() - i) + ". " + transaction.toString());
        }
        
        System.out.println("-".repeat(60));
        
        // Show summary by payment method
        Map<String, Long> paymentMethodCounts = transactions.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Transaction::getPaymentMethod,
                java.util.stream.Collectors.counting()
            ));
        
        if (!paymentMethodCounts.isEmpty()) {
            System.out.println("\nPayment Method Usage:");
            for (Map.Entry<String, Long> entry : paymentMethodCounts.entrySet()) {
                System.out.println("   " + entry.getKey() + ": " + entry.getValue() + " transactions");
            }
        }
    }
}
