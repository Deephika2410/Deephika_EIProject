package ui;

import managers.CustomerManager;
import models.CustomerProfile;
import java.util.Scanner;
import java.util.List;

/**
 * CustomerUI - Handles all customer-related user interactions
 * Responsible for customer management menu and input/output
 */
public class CustomerUI {
    private Scanner scanner;
    private CustomerManager customerManager;
    
    public CustomerUI(Scanner scanner, CustomerManager customerManager) {
        this.scanner = scanner;
        this.customerManager = customerManager;
    }
    
    public void showCustomerManagementMenu() {
        boolean continueManagement = true;
        
        while (continueManagement) {
            displayCustomerMenu();
            int choice = getChoiceFromUser();
            
            switch (choice) {
                case 1:
                    addNewCustomer();
                    break;
                case 2:
                    switchCustomer();
                    break;
                case 3:
                    listAllCustomers();
                    break;
                case 4:
                    editCurrentCustomer();
                    break;
                case 5:
                    removeCustomer();
                    break;
                case 6:
                    continueManagement = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please try again.");
            }
            
            if (continueManagement && choice != 6) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private void displayCustomerMenu() {
        System.out.println("\n CUSTOMER MANAGEMENT");
        System.out.println("-".repeat(30));
        System.out.println("Current Customers: " + customerManager.getCustomerCount());
        if (customerManager.hasCurrentCustomer()) {
            CustomerProfile current = customerManager.getCurrentCustomer();
            System.out.println("Active Customer: " + current.getPaymentDetails().getCustomerName());
            System.out.println("Setup Complete: " + (current.isPaymentSetupComplete() ? "YES" : "NO"));
        }
        System.out.println("-".repeat(30));
        System.out.println("1. Add New Customer");
        System.out.println("2. Switch Customer");
        System.out.println("3. List All Customers");
        System.out.println("4. Edit Current Customer");
        System.out.println("5. Remove Customer");
        System.out.println("6. Back to Main Menu");
        System.out.print("Choose an option (1-6): ");
    }
    
    private void addNewCustomer() {
        System.out.println("\n ADD NEW CUSTOMER");
        System.out.println("-".repeat(25));
        
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Customer Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Enter Billing Address: ");
        String address = scanner.nextLine().trim();
        
        System.out.print("Enter Currency (USD, EUR, GBP, etc.): ");
        String currency = scanner.nextLine().trim().toUpperCase();
        
        try {
            customerManager.addCustomer(email, name, address, currency);
            System.out.println("[SUCCESS] Customer added and activated successfully!");
            System.out.println("Customer: " + name + " (" + email + ")");
            System.out.println("Currency: " + currency);
            System.out.println("Total Customers: " + customerManager.getCustomerCount());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
    
    private void switchCustomer() {
        if (!customerManager.hasCustomers()) {
            System.out.println("[ERROR] No customers available. Please add a customer first.");
            return;
        }
        
        System.out.println("\n🔄 SWITCH CUSTOMER");
        System.out.println("-".repeat(20));
        System.out.println("Available Customers:");
        
        List<CustomerProfile> customers = customerManager.getAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            CustomerProfile profile = customers.get(i);
            String status = (i == customerManager.getCurrentCustomerIndex()) ? " [ACTIVE]" : "";
            String setupStatus = profile.isPaymentSetupComplete() ? " [SETUP DONE]" : " [NEEDS SETUP]";
            System.out.println((i + 1) + ". " + profile.getPaymentDetails().getCustomerName() + 
                             " (" + profile.getPaymentDetails().getCustomerEmail() + ")" + status + setupStatus +
                             " [Transactions: " + profile.getTransactionCount() + "]");
        }
        
        System.out.print("\nSelect customer number (1-" + customers.size() + "): ");
        try {
            int selection = Integer.parseInt(scanner.nextLine().trim());
            if (selection >= 1 && selection <= customers.size()) {
                if (customerManager.switchCustomer(selection - 1)) {
                    CustomerProfile switched = customerManager.getCurrentCustomer();
                    System.out.println("[SUCCESS] Switched to customer: " + switched.getPaymentDetails().getCustomerName());
                    if (switched.isPaymentSetupComplete()) {
                        System.out.println("[INFO] Payment method already configured: " + 
                                         switched.getPreferredPaymentMethod());
                    } else {
                        System.out.println("[INFO] Payment method setup required for this customer.");
                    }
                    System.out.println("[INFO] Transaction history: " + switched.getTransactionCount() + " transactions");
                } else {
                    System.out.println("[ERROR] Failed to switch customer.");
                }
            } else {
                System.out.println("[ERROR] Invalid selection. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input. Please enter a number.");
        }
    }
    
    private void listAllCustomers() {
        System.out.println("\n📋 ALL CUSTOMERS");
        System.out.println("-".repeat(20));
        
        if (!customerManager.hasCustomers()) {
            System.out.println("No customers registered yet.");
            return;
        }
        
        List<CustomerProfile> customers = customerManager.getAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            CustomerProfile profile = customers.get(i);
            String status = (i == customerManager.getCurrentCustomerIndex()) ? " [ACTIVE]" : "";
            System.out.println((i + 1) + ". " + profile.getPaymentDetails().getCustomerName() + status);
            System.out.println("    Email: " + profile.getPaymentDetails().getCustomerEmail());
            System.out.println("    Address: " + profile.getPaymentDetails().getAddress());
            System.out.println("    Currency: " + profile.getPaymentDetails().getCurrency());
            System.out.println("    Transactions: " + profile.getTransactionCount());
            System.out.println("    Setup Complete: " + (profile.isPaymentSetupComplete() ? "YES" : "NO"));
            System.out.println();
        }
        
        System.out.println("Total Customers: " + customers.size());
    }
    
    private void editCurrentCustomer() {
        if (!customerManager.hasCurrentCustomer()) {
            System.out.println("[ERROR] No customer selected. Please select a customer first.");
            return;
        }
        
        CustomerProfile current = customerManager.getCurrentCustomer();
        System.out.println("\n EDIT CUSTOMER");
        System.out.println("-".repeat(20));
        System.out.println("Current Details:");
        System.out.println("Name: " + current.getPaymentDetails().getCustomerName());
        System.out.println("Email: " + current.getPaymentDetails().getCustomerEmail() + " (cannot be changed)");
        System.out.println("Address: " + current.getPaymentDetails().getAddress());
        System.out.println("Currency: " + current.getPaymentDetails().getCurrency());
        
        System.out.print("\nEnter new Customer Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter new Billing Address: ");
        String address = scanner.nextLine().trim();
        
        System.out.print("Enter new Currency: ");
        String currency = scanner.nextLine().trim();
        
        try {
            customerManager.updateCurrentCustomer(name, address, currency);
            System.out.println("[SUCCESS] Customer details updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
    
    private void removeCustomer() {
        if (!customerManager.hasCustomers()) {
            System.out.println("[ERROR] No customers available to remove.");
            return;
        }
        
        System.out.println("\n REMOVE CUSTOMER");
        System.out.println("-".repeat(20));
        System.out.println("Available Customers:");
        
        List<CustomerProfile> customers = customerManager.getAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            CustomerProfile profile = customers.get(i);
            String status = (i == customerManager.getCurrentCustomerIndex()) ? " [ACTIVE]" : "";
            System.out.println((i + 1) + ". " + profile.getPaymentDetails().getCustomerName() + 
                             " (" + profile.getPaymentDetails().getCustomerEmail() + ")" + status);
        }
        
        System.out.print("\nSelect customer number to remove (1-" + customers.size() + "): ");
        try {
            int selection = Integer.parseInt(scanner.nextLine().trim());
            if (selection >= 1 && selection <= customers.size()) {
                CustomerProfile customerToRemove = customers.get(selection - 1);
                System.out.print("Are you sure you want to remove " + customerToRemove.getPaymentDetails().getCustomerName() + "? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                
                if (confirm.equals("y") || confirm.equals("yes")) {
                    if (customerManager.removeCustomer(selection - 1)) {
                        System.out.println("[SUCCESS] Customer removed successfully!");
                        System.out.println("Remaining customers: " + customerManager.getCustomerCount());
                    } else {
                        System.out.println("[ERROR] Failed to remove customer.");
                    }
                } else {
                    System.out.println("Customer removal cancelled.");
                }
            } else {
                System.out.println("[ERROR] Invalid selection. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input. Please enter a number.");
        }
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
