package managers;

import models.CustomerProfile;
import models.PaymentDetails;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerManager - Handles all customer-related operations
 * Responsible for customer CRUD operations and customer switching
 */
public class CustomerManager {
    private List<CustomerProfile> customerProfiles;
    private int currentCustomerIndex;
    private CustomerProfile currentCustomerProfile;
    
    public CustomerManager() {
        this.customerProfiles = new ArrayList<>();
        this.currentCustomerIndex = -1;
        this.currentCustomerProfile = null;
    }
    
    // Customer CRUD Operations
    public boolean addCustomer(String email, String name, String address, String currency) {
        // Validation
        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            address == null || address.trim().isEmpty() || 
            currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }
        
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Check for duplicate email
        for (CustomerProfile profile : customerProfiles) {
            if (profile.getPaymentDetails().getCustomerEmail().equalsIgnoreCase(email.trim())) {
                throw new IllegalArgumentException("Customer with this email already exists");
            }
        }
        
        PaymentDetails paymentDetails = new PaymentDetails(email.trim(), name.trim(), address.trim(), currency.trim().toUpperCase());
        CustomerProfile newProfile = new CustomerProfile(paymentDetails);
        customerProfiles.add(newProfile);
        
        // Auto-select the new customer
        currentCustomerIndex = customerProfiles.size() - 1;
        currentCustomerProfile = newProfile;
        
        return true;
    }
    
    public boolean switchCustomer(int customerIndex) {
        if (customerIndex < 0 || customerIndex >= customerProfiles.size()) {
            return false;
        }
        
        currentCustomerIndex = customerIndex;
        currentCustomerProfile = customerProfiles.get(customerIndex);
        return true;
    }
    
    public boolean updateCurrentCustomer(String name, String address, String currency) {
        if (currentCustomerProfile == null) {
            return false;
        }
        
        // Validation
        if (name == null || name.trim().isEmpty() || 
            address == null || address.trim().isEmpty() || 
            currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }
        
        PaymentDetails currentDetails = currentCustomerProfile.getPaymentDetails();
        PaymentDetails updatedDetails = new PaymentDetails(
            currentDetails.getCustomerEmail(),
            name.trim(),
            address.trim(),
            currency.trim().toUpperCase()
        );
        
        // Create new profile with updated details but keep transaction history
        CustomerProfile updatedProfile = new CustomerProfile(updatedDetails);
        updatedProfile.setPaymentSetupComplete(currentCustomerProfile.isPaymentSetupComplete());
        updatedProfile.setPreferredPaymentMethod(currentCustomerProfile.getPreferredPaymentMethod());
        
        // Copy transaction history
        for (var transaction : currentCustomerProfile.getTransactionHistory()) {
            updatedProfile.addTransaction(transaction);
        }
        
        customerProfiles.set(currentCustomerIndex, updatedProfile);
        currentCustomerProfile = updatedProfile;
        
        return true;
    }
    
    public boolean removeCustomer(int customerIndex) {
        if (customerIndex < 0 || customerIndex >= customerProfiles.size()) {
            return false;
        }
        
        customerProfiles.remove(customerIndex);
        
        // Update current customer reference
        if (customerIndex == currentCustomerIndex) {
            if (customerProfiles.isEmpty()) {
                currentCustomerProfile = null;
                currentCustomerIndex = -1;
            } else {
                currentCustomerIndex = Math.min(currentCustomerIndex, customerProfiles.size() - 1);
                currentCustomerProfile = customerProfiles.get(currentCustomerIndex);
            }
        } else if (customerIndex < currentCustomerIndex) {
            currentCustomerIndex--;
        }
        
        return true;
    }
    
    // Getters
    public List<CustomerProfile> getAllCustomers() {
        return new ArrayList<>(customerProfiles);
    }
    
    public CustomerProfile getCurrentCustomer() {
        return currentCustomerProfile;
    }
    
    public int getCurrentCustomerIndex() {
        return currentCustomerIndex;
    }
    
    public int getCustomerCount() {
        return customerProfiles.size();
    }
    
    public boolean hasCustomers() {
        return !customerProfiles.isEmpty();
    }
    
    public boolean hasCurrentCustomer() {
        return currentCustomerProfile != null;
    }
    
    // Get customer by index
    public CustomerProfile getCustomer(int index) {
        if (index < 0 || index >= customerProfiles.size()) {
            return null;
        }
        return customerProfiles.get(index);
    }
}
