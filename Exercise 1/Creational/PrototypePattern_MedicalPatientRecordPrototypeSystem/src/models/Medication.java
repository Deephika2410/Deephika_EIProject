package models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model class representing a medication entry.
 * 
 * This class encapsulates information about medications prescribed to a patient,
 * including dosage, frequency, and administration details.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public class Medication implements Cloneable {
    private String medicationId;
    private String name;
    private String dosage;
    private String frequency;
    private String instructions;
    private LocalDateTime prescribedDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String prescribingPhysician;
    private boolean isActive;
    
    /**
     * Default constructor for Medication.
     */
    public Medication() {
        this.prescribedDate = LocalDateTime.now();
        this.isActive = true;
    }
    
    /**
     * Parameterized constructor for Medication.
     * 
     * @param medicationId Unique identifier for the medication
     * @param name Name of the medication
     * @param dosage Dosage information
     * @param frequency Frequency of administration
     */
    public Medication(String medicationId, String name, String dosage, String frequency) {
        this();
        this.medicationId = medicationId;
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
    }
    
    /**
     * Creates a deep copy of this medication.
     * 
     * @return A new Medication instance that is a copy of this medication
     */
    @Override
    public Medication clone() {
        try {
            Medication cloned = (Medication) super.clone();
            // Deep copy for date objects
            cloned.prescribedDate = this.prescribedDate != null ? LocalDateTime.from(this.prescribedDate) : null;
            cloned.startDate = this.startDate != null ? LocalDateTime.from(this.startDate) : null;
            cloned.endDate = this.endDate != null ? LocalDateTime.from(this.endDate) : null;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }
    
    // Getters and Setters
    public String getMedicationId() {
        return medicationId;
    }
    
    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDosage() {
        return dosage;
    }
    
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public String getFrequency() {
        return frequency;
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public LocalDateTime getPrescribedDate() {
        return prescribedDate;
    }
    
    public void setPrescribedDate(LocalDateTime prescribedDate) {
        this.prescribedDate = prescribedDate;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public String getPrescribingPhysician() {
        return prescribingPhysician;
    }
    
    public void setPrescribingPhysician(String prescribingPhysician) {
        this.prescribingPhysician = prescribingPhysician;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Medication that = (Medication) obj;
        return Objects.equals(medicationId, that.medicationId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(medicationId);
    }
    
    @Override
    public String toString() {
        return String.format("Medication{id='%s', name='%s', dosage='%s', frequency='%s', active=%b}", 
                           medicationId, name, dosage, frequency, isActive);
    }
}
