package models;

// PaymentDetails.java - Data Transfer Object
public class PaymentDetails {
    private String customerEmail;
    private String customerName;
    private String address;
    private String currency;
    
    public PaymentDetails(String customerEmail, String customerName, String address, String currency) {
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.address = address;
        this.currency = currency;
    }
    
    // Getters
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerName() { return customerName; }
    public String getAddress() { return address; }
    public String getCurrency() { return currency; }
}
