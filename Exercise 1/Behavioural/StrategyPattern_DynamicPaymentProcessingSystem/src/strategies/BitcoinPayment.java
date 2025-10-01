package strategies;

import models.PaymentDetails;
import models.PaymentResult;

// BitcoinPayment.java - Concrete Strategy
public class BitcoinPayment implements PaymentStrategy {
    private String walletAddress;
    private String privateKey;
    
    public BitcoinPayment(String walletAddress, String privateKey) {
        this.walletAddress = walletAddress;
        this.privateKey = privateKey;
    }
    
    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        System.out.println("₿ Processing Bitcoin Payment...");
        System.out.println("Wallet Address: " + maskWalletAddress(walletAddress));
        System.out.println("Amount: $" + amount + " USD (Converting to BTC...)");
        
        double btcAmount = convertToBTC(amount);
        System.out.println("BTC Amount: " + btcAmount + " BTC");
        
        // Simulate Bitcoin transaction
        if (validateWallet()) {
            String transactionId = "BTC_" + System.currentTimeMillis();
            System.out.println("✅ Bitcoin Payment Successful!");
            System.out.println("Transaction ID: " + transactionId);
            System.out.println("Blockchain confirmation pending...");
            
            return new PaymentResult(true, transactionId, 
                "Payment processed successfully via Bitcoin", amount, getPaymentMethodName());
        } else {
            return new PaymentResult(false, null, 
                "Bitcoin wallet validation failed", amount, getPaymentMethodName());
        }
    }
    
    private boolean validateWallet() {
        return walletAddress.length() >= 26 && !privateKey.isEmpty();
    }
    
    private String maskWalletAddress(String address) {
        return address.substring(0, 6) + "..." + address.substring(Math.max(0, address.length() - 6));
    }
    
    private double convertToBTC(double usdAmount) {
        // Simulate USD to BTC conversion (example rate)
        return usdAmount / 45000.0; // Assuming 1 BTC = $45,000
    }
    
    @Override
    public String getPaymentMethodName() {
        return "Bitcoin";
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}
