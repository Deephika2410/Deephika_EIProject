package ui;

import managers.CustomerManager;
import managers.PaymentSetupManager;
import managers.TransactionManager;
import models.CustomerProfile;
import models.PaymentResult;
import strategies.PaymentStrategy;
import java.util.Scanner;
import java.util.Map;

/**
 * PaymentUI - Handles all payment-related user interactions
 * Responsible for payment setup and processing UI
 */
public class PaymentUI {
    private Scanner scanner;
    private CustomerManager customerManager;
    private PaymentSetupManager paymentSetupManager;
    private TransactionManager transactionManager;
    
    public PaymentUI(Scanner scanner, CustomerManager customerManager, 
                    PaymentSetupManager paymentSetupManager, TransactionManager transactionManager) {
        this.scanner = scanner;
        this.customerManager = customerManager;
        this.paymentSetupManager = paymentSetupManager;
        this.transactionManager = transactionManager;
    }
    
    public void setupCreditCard() {
        if (!customerManager.hasCurrentCustomer()) {
            System.out.println("[ERROR] Please select a customer first (Option 1 -> Customer Management)");
            return;
        }
        
        CustomerProfile currentCustomer = customerManager.getCurrentCustomer();
        System.out.println("\n💳 CREDIT CARD SETUP");
        System.out.println("-".repeat(25));
        
        // Check if already configured
        if (paymentSetupManager.isPaymentAlreadyConfigured(currentCustomer, "Credit Card")) {
            System.out.println("[INFO] Credit Card is already configured for " + 
                             currentCustomer.getPaymentDetails().getCustomerName());
            System.out.print("Do you want to reconfigure? (y/n): ");
            String reconfigure = scanner.nextLine().trim().toLowerCase();
            if (!reconfigure.equals("y") && !reconfigure.equals("yes")) {
                System.out.println("[INFO] Using existing Credit Card configuration.");
                return;
            }
        }
        
        System.out.print("Enter Card Number (16 digits): ");
        String cardNumber = scanner.nextLine().trim();
        
        System.out.print("Enter Expiry Date (MM/YY): ");
        String expiry = scanner.nextLine().trim();
        
        System.out.print("Enter CVV (3-4 digits): ");
        String cvv = scanner.nextLine().trim();
        
        System.out.print("Enter Cardholder Name: ");
        String cardholderName = scanner.nextLine().trim();
        
        try {
            paymentSetupManager.setupCreditCard(currentCustomer, cardNumber, expiry, cvv, cardholderName);
            System.out.println("[SUCCESS] Credit Card payment method configured!");
            System.out.println("Cardholder: " + cardholderName);
            System.out.println("Card: **** **** **** " + cardNumber.substring(cardNumber.length() - 4));
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
    
    public void setupPayPal() {
        if (!customerManager.hasCurrentCustomer()) {
            System.out.println("[ERROR] Please select a customer first (Option 1 -> Customer Management)");
            return;
        }
        
        CustomerProfile currentCustomer = customerManager.getCurrentCustomer();
        System.out.println("\n💰 PAYPAL SETUP");
        System.out.println("-".repeat(20));
        
        // Check if already configured
        if (paymentSetupManager.isPaymentAlreadyConfigured(currentCustomer, "PayPal")) {
            System.out.println("[INFO] PayPal is already configured for " + 
                             currentCustomer.getPaymentDetails().getCustomerName());
            System.out.print("Do you want to reconfigure? (y/n): ");
            String reconfigure = scanner.nextLine().trim().toLowerCase();
            if (!reconfigure.equals("y") && !reconfigure.equals("yes")) {
                System.out.println("[INFO] Using existing PayPal configuration.");
                return;
            }
        }
        
        System.out.print("Enter PayPal Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Enter PayPal Password: ");
        String password = scanner.nextLine().trim();
        
        System.out.print("Enable Two-Factor Authentication? (y/n): ");
        String twoFactor = scanner.nextLine().trim().toLowerCase();
        
        try {
            paymentSetupManager.setupPayPal(currentCustomer, email, password);
            System.out.println("[SUCCESS] PayPal payment method configured!");
            System.out.println("Account: " + email);
            System.out.println("2FA: " + (twoFactor.equals("y") ? "Enabled" : "Disabled"));
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
    
    public void setupBitcoin() {
        if (!customerManager.hasCurrentCustomer()) {
            System.out.println("[ERROR] Please select a customer first (Option 1 -> Customer Management)");
            return;
        }
        
        CustomerProfile currentCustomer = customerManager.getCurrentCustomer();
        System.out.println("\n₿ BITCOIN SETUP");
        System.out.println("-".repeat(20));
        
        // Check if already configured
        if (paymentSetupManager.isPaymentAlreadyConfigured(currentCustomer, "Bitcoin")) {
            System.out.println("[INFO] Bitcoin is already configured for " + 
                             currentCustomer.getPaymentDetails().getCustomerName());
            System.out.print("Do you want to reconfigure? (y/n): ");
            String reconfigure = scanner.nextLine().trim().toLowerCase();
            if (!reconfigure.equals("y") && !reconfigure.equals("yes")) {
                System.out.println("[INFO] Using existing Bitcoin configuration.");
                return;
            }
        }
        
        System.out.print("Enter Bitcoin Wallet Address: ");
        String walletAddress = scanner.nextLine().trim();
        
        System.out.print("Enter Private Key (for demo purposes): ");
        String privateKey = scanner.nextLine().trim();
        
        System.out.print("Select Network (mainnet/testnet): ");
        String network = scanner.nextLine().trim().toLowerCase();
        
        System.out.print("Enter Transaction Fee (High/Medium/Low): ");
        String feeLevel = scanner.nextLine().trim();
        
        try {
            paymentSetupManager.setupBitcoin(currentCustomer, walletAddress, privateKey);
            System.out.println("[SUCCESS] Bitcoin payment method configured!");
            System.out.println("Wallet: " + walletAddress.substring(0, 6) + "..." + walletAddress.substring(walletAddress.length() - 6));
            System.out.println("Network: " + (network.equals("mainnet") ? "Mainnet" : "Testnet"));
            System.out.println("Fee Level: " + feeLevel);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
    
    public void processPayment() {
        if (!customerManager.hasCurrentCustomer()) {
            System.out.println("[ERROR] Please select a customer first (Option 1 -> Customer Management)");
            return;
        }
        
        CustomerProfile currentCustomer = customerManager.getCurrentCustomer();
        System.out.println("\n💵 PAYMENT PROCESSING");
        System.out.println("-".repeat(25));
        System.out.println("Customer: " + currentCustomer.getPaymentDetails().getCustomerName());
        System.out.println("Currency: " + currentCustomer.getPaymentDetails().getCurrency());
        System.out.println("Previous Transactions: " + currentCustomer.getTransactionCount());
        
        System.out.print("Enter payment amount: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Enter payment description: ");
            String description = scanner.nextLine().trim();
            
            System.out.print("Is this an urgent payment? (y/n): ");
            String urgent = scanner.nextLine().trim().toLowerCase();
            boolean isUrgent = urgent.equals("y") || urgent.equals("yes");
            
            System.out.println("\n🔄 Processing payment...");
            System.out.println("Description: " + (description.isEmpty() ? "Payment transaction" : description));
            System.out.println("Priority: " + (isUrgent ? "Urgent" : "Normal"));
            
            PaymentResult result = transactionManager.processPayment(currentCustomer, amount, description, isUrgent);
            
            System.out.println("\n[PAYMENT RESULT]:");
            System.out.println("Success: " + (result.isSuccess() ? "YES" : "NO"));
            System.out.println("Message: " + result.getMessage());
            System.out.println("Amount: " + amount + " " + currentCustomer.getPaymentDetails().getCurrency());
            if (result.getTransactionId() != null) {
                System.out.println("Transaction ID: " + result.getTransactionId());
            }
            System.out.println("Total Transactions for Customer: " + currentCustomer.getTransactionCount());
            
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid amount entered. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
    
    public void demonstrateRuntimeSwitching() {
        if (!customerManager.hasCurrentCustomer()) {
            System.out.println("[ERROR] Please select a customer first (Option 1 -> Customer Management)");
            return;
        }
        
        CustomerProfile currentCustomer = customerManager.getCurrentCustomer();
        System.out.println("\n🔄 INTERACTIVE RUNTIME STRATEGY SWITCHING");
        System.out.println("Choose payment methods one at a time to see runtime switching in action");
        System.out.println("=" + "=".repeat(65));
        
        // Get runtime payment details for demonstration
        System.out.println("\nEnter sample payment details for demonstration:");
        
        System.out.print("Credit Card Number (16 digits): ");
        String ccNumber = scanner.nextLine().trim();
        
        System.out.print("PayPal Email: ");
        String paypalEmail = scanner.nextLine().trim();
        
        System.out.print("Bitcoin Wallet Address: ");
        String btcWallet = scanner.nextLine().trim();
        
        // Get available payment strategies
        Map<String, PaymentStrategy> availableStrategies = transactionManager.getAvailableStrategies(ccNumber, paypalEmail, btcWallet);
        
        System.out.println("\n📋 Available Payment Methods:");
        String[] methods = availableStrategies.keySet().toArray(new String[0]);
        for (int i = 0; i < methods.length; i++) {
            System.out.println((i + 1) + ". " + methods[i]);
        }
        
        boolean continueSwitching = true;
        int transactionCount = 0;
        
        while (continueSwitching) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🎯 TRANSACTION #" + (transactionCount + 1));
            System.out.println("Current customer: " + currentCustomer.getPaymentDetails().getCustomerName());
            System.out.println("Total customer transactions: " + currentCustomer.getTransactionCount());
            
            System.out.print("\nEnter payment amount: ");
            double amount;
            try {
                amount = Double.parseDouble(scanner.nextLine().trim());
                if (amount <= 0) {
                    System.out.println("[ERROR] Amount must be greater than 0");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid amount entered.");
                continue;
            }
            
            System.out.print("Enter payment description: ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = "Interactive switching payment #" + (transactionCount + 1);
            }
            
            System.out.println("\n💳 Choose Payment Method:");
            for (int i = 0; i < methods.length; i++) {
                System.out.println((i + 1) + ". " + methods[i]);
            }
            
            System.out.print("Select payment method (1-" + methods.length + "): ");
            try {
                int methodChoice = Integer.parseInt(scanner.nextLine().trim());
                if (methodChoice >= 1 && methodChoice <= methods.length) {
                    String selectedMethod = methods[methodChoice - 1];
                    
                    // Process payment with selected method
                    PaymentResult result = transactionManager.switchAndProcessPayment(
                        currentCustomer, amount, selectedMethod, availableStrategies, description);
                    
                    transactionCount++;
                    
                    System.out.println("\n✅ Strategy Pattern Demonstrated:");
                    System.out.println("   * Context (PaymentProcessor) switched algorithms dynamically");
                    System.out.println("   * Client code remained unchanged");
                    System.out.println("   * " + selectedMethod + " strategy executed successfully");
                    
                } else {
                    System.out.println("[ERROR] Invalid selection. Please try again.");
                    continue;
                }
                
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid input. Please enter a number.");
                continue;
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
                continue;
            }
            
            System.out.print("\nDo you want to make another payment with a different strategy? (y/n): ");
            String continueChoice = scanner.nextLine().trim().toLowerCase();
            
            if (!continueChoice.equals("y") && !continueChoice.equals("yes")) {
                continueSwitching = false;
            }
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🎉 INTERACTIVE RUNTIME SWITCHING SESSION COMPLETED!");
        System.out.println("📊 Session Summary:");
        System.out.println("   * Total payments processed: " + transactionCount);
        System.out.println("   * Customer: " + currentCustomer.getPaymentDetails().getCustomerName());
        System.out.println("   * Customer's total transactions now: " + currentCustomer.getTransactionCount());
        System.out.println("\n🔑 Key Strategy Pattern Benefits Demonstrated:");
        System.out.println("   * Same PaymentProcessor context used different algorithms");
        System.out.println("   * Runtime strategy switching without code changes");
        System.out.println("   * Each strategy handled payments independently");
        System.out.println("   * User controlled which algorithm to use per transaction");
        System.out.println("   * Algorithms were completely interchangeable");
        System.out.println("=" + "=".repeat(70));
    }
    
    public void viewTransactionHistory() {
        if (!customerManager.hasCurrentCustomer()) {
            System.out.println("[ERROR] Please select a customer first (Option 1 -> Customer Management)");
            return;
        }
        
        try {
            transactionManager.displayTransactionHistory(customerManager.getCurrentCustomer());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}
