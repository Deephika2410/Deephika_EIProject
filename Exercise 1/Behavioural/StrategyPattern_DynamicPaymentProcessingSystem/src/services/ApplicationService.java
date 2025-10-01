package services;

import context.PaymentProcessor;
import managers.CustomerManager;
import managers.PaymentSetupManager;
import managers.TransactionManager;
import ui.CustomerUI;
import ui.PaymentUI;
import models.CustomerProfile;
import java.util.Scanner;

/**
 * ApplicationService - Main application coordinator
 * Responsible for application flow and coordination between different components
 */
public class ApplicationService {
    private Scanner scanner;
    private CustomerManager customerManager;
    private PaymentSetupManager paymentSetupManager;
    private TransactionManager transactionManager;
    private PaymentProcessor paymentProcessor;
    private CustomerUI customerUI;
    private PaymentUI paymentUI;
    
    public ApplicationService() {
        this.scanner = new Scanner(System.in);
        this.customerManager = new CustomerManager();
        this.paymentProcessor = new PaymentProcessor();
        this.paymentSetupManager = new PaymentSetupManager(paymentProcessor);
        this.transactionManager = new TransactionManager(paymentProcessor);
        this.customerUI = new CustomerUI(scanner, customerManager);
        this.paymentUI = new PaymentUI(scanner, customerManager, paymentSetupManager, transactionManager);
    }
    
    public void run() {
        System.out.println("🎯 STRATEGY PATTERN DEMONSTRATION");
        System.out.println("Dynamic Payment Processing System");
        System.out.println("=" + "=".repeat(48));
        
        boolean continueProcessing = true;
        
        while (continueProcessing) {
            displayMainMenu();
            int choice = getChoiceFromUser();
            
            switch (choice) {
                case 1:
                    customerUI.showCustomerManagementMenu();
                    break;
                case 2:
                    paymentUI.setupCreditCard();
                    break;
                case 3:
                    paymentUI.setupPayPal();
                    break;
                case 4:
                    paymentUI.setupBitcoin();
                    break;
                case 5:
                    paymentUI.processPayment();
                    break;
                case 6:
                    paymentUI.demonstrateRuntimeSwitching();
                    break;
                case 7:
                    viewCurrentConfiguration();
                    break;
                case 8:
                    paymentUI.viewTransactionHistory();
                    break;
                case 9:
                    continueProcessing = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please try again.");
            }
            
            if (continueProcessing && choice != 6 && choice != 7) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        System.out.println("\n👋 Thank you for using the Payment Processing System!");
        scanner.close();
    }
    
    private void displayMainMenu() {
        System.out.println("\n📋 PAYMENT SYSTEM MENU");
        String customerInfo = "Not Set";
        if (customerManager.hasCurrentCustomer()) {
            CustomerProfile current = customerManager.getCurrentCustomer();
            customerInfo = current.getPaymentDetails().getCustomerName() + 
                          " (" + (customerManager.getCurrentCustomerIndex() + 1) + "/" + customerManager.getCustomerCount() + ")" +
                          " [Transactions: " + current.getTransactionCount() + "]";
        }
        System.out.println("Customer: " + customerInfo);
        System.out.println("Payment Method: " + paymentProcessor.getCurrentPaymentMethod());
        System.out.println("-".repeat(50));
        System.out.println("1. Customer Management");
        System.out.println("2. Setup Credit Card Payment");
        System.out.println("3. Setup PayPal Payment");
        System.out.println("4. Setup Bitcoin Payment");
        System.out.println("5. Process Payment");
        System.out.println("6. Interactive Runtime Strategy Switching");
        System.out.println("7. View Current Configuration");
        System.out.println("8. View Transaction History");
        System.out.println("9. Exit");
        System.out.print("Choose an option (1-9): ");
    }
    
    private void viewCurrentConfiguration() {
        System.out.println("\n📋 CURRENT SYSTEM CONFIGURATION");
        System.out.println("=" + "=".repeat(40));
        
        // Customer Details
        if (customerManager.hasCurrentCustomer()) {
            CustomerProfile current = customerManager.getCurrentCustomer();
            System.out.println("CUSTOMER INFORMATION:");
            System.out.println("   Name: " + current.getPaymentDetails().getCustomerName());
            System.out.println("   Email: " + current.getPaymentDetails().getCustomerEmail());
            System.out.println("   Address: " + current.getPaymentDetails().getAddress());
            System.out.println("   Currency: " + current.getPaymentDetails().getCurrency());
            System.out.println("   Total Transactions: " + current.getTransactionCount());
            System.out.println("   Total Spent: $" + String.format("%.2f", current.getTotalSpent()) + 
                             " " + current.getPaymentDetails().getCurrency());
        } else {
            System.out.println("CUSTOMER INFORMATION: Not Configured");
        }
        
        System.out.println();
        
        // Payment Method
        String paymentMethod = paymentProcessor.getCurrentPaymentMethod();
        if (!paymentMethod.equals("None")) {
            System.out.println("PAYMENT METHOD:");
            System.out.println("   Current Method: " + paymentMethod);
            System.out.println("   Status: Configured");
            if (customerManager.hasCurrentCustomer()) {
                CustomerProfile current = customerManager.getCurrentCustomer();
                System.out.println("   Setup Complete: " + (current.isPaymentSetupComplete() ? "YES" : "NO"));
                System.out.println("   Preferred Method: " + current.getPreferredPaymentMethod());
            }
        } else {
            System.out.println("PAYMENT METHOD: Not Configured");
        }
        
        System.out.println();
        
        // System Status
        System.out.println("SYSTEM STATUS:");
        boolean readyToProcess = (customerManager.hasCurrentCustomer() && !paymentMethod.equals("None"));
        System.out.println("   Ready to Process Payments: " + (readyToProcess ? "YES" : "NO"));
        System.out.println("   Total Customers: " + customerManager.getCustomerCount());
        
        if (!readyToProcess) {
            System.out.println("   TODO:");
            if (!customerManager.hasCurrentCustomer()) {
                System.out.println("      * Select/Add customer (Option 1)");
            }
            if (paymentMethod.equals("None")) {
                System.out.println("      * Configure payment method (Options 2-4)");
            }
        }
        
        System.out.println("=" + "=".repeat(40));
    }
    
    private int getChoiceFromUser() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return choice;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
