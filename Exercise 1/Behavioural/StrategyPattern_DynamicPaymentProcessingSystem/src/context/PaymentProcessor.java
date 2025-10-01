package context;

import strategies.PaymentStrategy;
import models.PaymentDetails;
import models.PaymentResult;

// PaymentProcessor.java - Context Class (Single Responsibility Principle)
public class PaymentProcessor {
    private PaymentStrategy paymentStrategy;
    
    public PaymentProcessor() {
        // Default strategy can be null - must be set before processing
    }
    
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
        System.out.println("🔄 Payment method switched to: " + paymentStrategy.getPaymentMethodName());
    }
    
    public PaymentResult executePayment(double amount, PaymentDetails details) {
        if (paymentStrategy == null) {
            return new PaymentResult(false, null, "No payment method selected", amount, "None");
        }
        
        if (!paymentStrategy.isAvailable()) {
            return new PaymentResult(false, null, 
                paymentStrategy.getPaymentMethodName() + " is currently unavailable", amount, 
                paymentStrategy.getPaymentMethodName());
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println(" PROCESSING PAYMENT");
        System.out.println("Method: " + paymentStrategy.getPaymentMethodName());
        System.out.println("Customer: " + details.getCustomerName());
        System.out.println("=".repeat(50));
        
        PaymentResult result = paymentStrategy.processPayment(amount, details);
        
        System.out.println("=".repeat(50));
        System.out.println("Status: " + (result.isSuccess() ? "SUCCESS" : "FAILED "));
        System.out.println("=".repeat(50));
        
        return result;
    }
    
    public String getCurrentPaymentMethod() {
        return paymentStrategy != null ? paymentStrategy.getPaymentMethodName() : "None";
    }
}
