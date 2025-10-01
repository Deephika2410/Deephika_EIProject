package models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Model class representing a medical condition.
 * 
 * This class encapsulates information about a patient's medical condition,
 * including diagnosis details, severity, and treatment notes.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public class MedicalCondition implements Cloneable {
    private String conditionId;
    private String name;
    private String description;
    private String severity;
    private LocalDate diagnosisDate;
    private String treatmentNotes;
    private boolean isActive;
    
    /**
     * Default constructor for MedicalCondition.
     */
    public MedicalCondition() {
        this.isActive = true;
        this.diagnosisDate = LocalDate.now();
    }
    
    /**
     * Parameterized constructor for MedicalCondition.
     * 
     * @param conditionId Unique identifier for the condition
     * @param name Name of the medical condition
     * @param description Detailed description of the condition
     * @param severity Severity level of the condition
     */
    public MedicalCondition(String conditionId, String name, String description, String severity) {
        this();
        this.conditionId = conditionId;
        this.name = name;
        this.description = description;
        this.severity = severity;
    }
    
    /**
     * Creates a deep copy of this medical condition.
     * 
     * @return A new MedicalCondition instance that is a copy of this condition
     */
    @Override
    public MedicalCondition clone() {
        try {
            MedicalCondition cloned = (MedicalCondition) super.clone();
            // Deep copy for date object
            cloned.diagnosisDate = this.diagnosisDate != null ? LocalDate.from(this.diagnosisDate) : null;
            return cloned;
        } catch (CloneNotSupportedException e) {
            // This should never happen since we implement Cloneable
            throw new AssertionError("Clone not supported", e);
        }
    }
    
    // Getters and Setters
    public String getConditionId() {
        return conditionId;
    }
    
    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }
    
    public void setDiagnosisDate(LocalDate diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }
    
    public String getTreatmentNotes() {
        return treatmentNotes;
    }
    
    public void setTreatmentNotes(String treatmentNotes) {
        this.treatmentNotes = treatmentNotes;
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
        MedicalCondition that = (MedicalCondition) obj;
        return Objects.equals(conditionId, that.conditionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(conditionId);
    }
    
    @Override
    public String toString() {
        return String.format("MedicalCondition{id='%s', name='%s', severity='%s', active=%b}", 
                           conditionId, name, severity, isActive);
    }
}
