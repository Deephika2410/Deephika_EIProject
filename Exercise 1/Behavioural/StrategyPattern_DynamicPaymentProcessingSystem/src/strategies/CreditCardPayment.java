package strategies;

import models.PaymentDetails;
import models.PaymentResult;

// CreditCardPayment.java - Concrete Strategy
public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    
    public CreditCardPayment(String cardNumber, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        System.out.println("Processing Credit Card Payment...");
        System.out.println("Card Number: " + maskCardNumber(cardNumber));
        System.out.println("Amount: $" + amount + " " + details.getCurrency());
        
        // Simulate credit card processing
        if (validateCard()) {
            String transactionId = "CC_" + System.currentTimeMillis();
            System.out.println("Credit Card Payment Successful!");
            System.out.println("Transaction ID: " + transactionId);
            
            return new PaymentResult(true, transactionId, 
                "Payment processed successfully via Credit Card", amount, getPaymentMethodName());
        } else {
            return new PaymentResult(false, null, 
                "Credit Card validation failed", amount, getPaymentMethodName());
        }
    }
    
    private boolean validateCard() {
        // Simulate card validation logic
        return cardNumber.length() >= 16 && !cvv.isEmpty();
    }
    
    private String maskCardNumber(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(Math.max(0, cardNumber.length() - 4));
    }
    
    @Override
    public String getPaymentMethodName() {
        return "Credit Card";
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}
