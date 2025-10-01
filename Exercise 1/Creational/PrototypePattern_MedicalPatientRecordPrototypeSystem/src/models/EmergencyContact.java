package models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Model class representing emergency contact information.
 * 
 * This class stores essential contact details for patient emergencies,
 * including relationship information and contact preferences.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public class EmergencyContact implements Cloneable {
    private String contactId;
    private String fullName;
    private String relationship;
    private String primaryPhone;
    private String secondaryPhone;
    private String email;
    private String address;
    private boolean isPrimaryContact;
    private LocalDate lastUpdated;
    
    /**
     * Default constructor for EmergencyContact.
     */
    public EmergencyContact() {
        this.lastUpdated = LocalDate.now();
        this.isPrimaryContact = false;
    }
    
    /**
     * Parameterized constructor for EmergencyContact.
     * 
     * @param contactId Unique identifier for the contact
     * @param fullName Full name of the emergency contact
     * @param relationship Relationship to the patient
     * @param primaryPhone Primary phone number
     */
    public EmergencyContact(String contactId, String fullName, String relationship, String primaryPhone) {
        this();
        this.contactId = contactId;
        this.fullName = fullName;
        this.relationship = relationship;
        this.primaryPhone = primaryPhone;
    }
    
    /**
     * Creates a deep copy of this emergency contact.
     * 
     * @return A new EmergencyContact instance that is a copy of this contact
     */
    @Override
    public EmergencyContact clone() {
        try {
            EmergencyContact cloned = (EmergencyContact) super.clone();
            // Deep copy for date object
            cloned.lastUpdated = this.lastUpdated != null ? LocalDate.from(this.lastUpdated) : null;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }
    
    // Getters and Setters
    public String getContactId() {
        return contactId;
    }
    
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getRelationship() {
        return relationship;
    }
    
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
    
    public String getPrimaryPhone() {
        return primaryPhone;
    }
    
    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }
    
    public String getSecondaryPhone() {
        return secondaryPhone;
    }
    
    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public boolean isPrimaryContact() {
        return isPrimaryContact;
    }
    
    public void setPrimaryContact(boolean primaryContact) {
        isPrimaryContact = primaryContact;
    }
    
    public LocalDate getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmergencyContact that = (EmergencyContact) obj;
        return Objects.equals(contactId, that.contactId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(contactId);
    }
    
    @Override
    public String toString() {
        return String.format("EmergencyContact{id='%s', name='%s', relationship='%s', phone='%s', primary=%b}", 
                           contactId, fullName, relationship, primaryPhone, isPrimaryContact);
    }
}
