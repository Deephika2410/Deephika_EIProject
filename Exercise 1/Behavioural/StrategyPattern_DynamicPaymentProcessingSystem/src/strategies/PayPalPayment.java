package strategies;

import models.PaymentDetails;
import models.PaymentResult;

// PayPalPayment.java - Concrete Strategy
public class PayPalPayment implements PaymentStrategy {
    private String email;
    private String password;
    
    public PayPalPayment(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        System.out.println("💰 Processing PayPal Payment...");
        System.out.println("PayPal Account: " + email);
        System.out.println("Amount: $" + amount + " " + details.getCurrency());
        
        // Simulate PayPal processing
        if (authenticatePayPal()) {
            String transactionId = "PP_" + System.currentTimeMillis();
            System.out.println("✅ PayPal Payment Successful!");
            System.out.println("Transaction ID: " + transactionId);
            
            return new PaymentResult(true, transactionId, 
                "Payment processed successfully via PayPal", amount, getPaymentMethodName());
        } else {
            return new PaymentResult(false, null, 
                "PayPal authentication failed", amount, getPaymentMethodName());
        }
    }
    
    private boolean authenticatePayPal() {
        // Simulate PayPal authentication
        return email.contains("@") && password.length() >= 6;
    }
    
    @Override
    public String getPaymentMethodName() {
        return "PayPal";
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}
