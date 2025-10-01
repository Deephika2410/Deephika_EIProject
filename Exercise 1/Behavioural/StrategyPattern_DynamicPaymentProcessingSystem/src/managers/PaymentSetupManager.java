package managers;

import context.PaymentProcessor;
import models.CustomerProfile;
import strategies.CreditCardPayment;
import strategies.PayPalPayment;
import strategies.BitcoinPayment;

/**
 * PaymentSetupManager - Handles payment method configuration
 * Responsible for setting up different payment strategies
 */
public class PaymentSetupManager {
    private PaymentProcessor processor;
    
    public PaymentSetupManager(PaymentProcessor processor) {
        this.processor = processor;
    }
    
    public boolean setupCreditCard(CustomerProfile customerProfile, String cardNumber, 
                                  String expiry, String cvv, String cardholderName) {
        // Validation
        if (cardNumber == null || cardNumber.trim().length() < 16 ||
            expiry == null || expiry.trim().length() < 5 ||
            cvv == null || cvv.trim().length() < 3 ||
            cardholderName == null || cardholderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid card details");
        }
        
        processor.setPaymentStrategy(new CreditCardPayment(cardNumber.trim(), expiry.trim(), cvv.trim()));
        customerProfile.setPaymentSetupComplete(true);
        customerProfile.setPreferredPaymentMethod("Credit Card");
        
        return true;
    }
    
    public boolean setupPayPal(CustomerProfile customerProfile, String email, String password) {
        // Validation
        if (email == null || !email.trim().contains("@") ||
            password == null || password.trim().length() < 6) {
            throw new IllegalArgumentException("Invalid PayPal credentials. Email must be valid and password at least 6 characters");
        }
        
        processor.setPaymentStrategy(new PayPalPayment(email.trim(), password.trim()));
        customerProfile.setPaymentSetupComplete(true);
        customerProfile.setPreferredPaymentMethod("PayPal");
        
        return true;
    }
    
    public boolean setupBitcoin(CustomerProfile customerProfile, String walletAddress, String privateKey) {
        // Validation
        if (walletAddress == null || walletAddress.trim().length() < 26 ||
            privateKey == null || privateKey.trim().length() < 10) {
            throw new IllegalArgumentException("Invalid Bitcoin wallet details");
        }
        
        processor.setPaymentStrategy(new BitcoinPayment(walletAddress.trim(), privateKey.trim()));
        customerProfile.setPaymentSetupComplete(true);
        customerProfile.setPreferredPaymentMethod("Bitcoin");
        
        return true;
    }
    
    public boolean isPaymentAlreadyConfigured(CustomerProfile customerProfile, String paymentMethod) {
        return customerProfile.isPaymentSetupComplete() && 
               customerProfile.getPreferredPaymentMethod().equals(paymentMethod);
    }
    
    public void resetPaymentMethod(CustomerProfile customerProfile) {
        customerProfile.setPaymentSetupComplete(false);
        customerProfile.setPreferredPaymentMethod("None");
    }
}
