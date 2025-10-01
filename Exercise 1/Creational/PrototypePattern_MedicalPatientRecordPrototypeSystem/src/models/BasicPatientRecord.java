package models;

import prototype.PatientRecordPrototype;
import enums.Gender;
import enums.BloodType;
import enums.RecordStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Concrete implementation of PatientRecordPrototype for basic patient records.
 * 
 * This class represents a comprehensive patient record with all essential
 * medical information. It implements the Prototype pattern to allow
 * efficient cloning of patient records for templates and bulk operations.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public class BasicPatientRecord implements PatientRecordPrototype {
    // Basic Information
    private String recordId;
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private BloodType bloodType;
    private String phoneNumber;
    private String email;
    private String address;
    private RecordStatus status;
    
    // Medical Information
    private List<MedicalCondition> medicalConditions;
    private List<Medication> medications;
    private List<String> allergies;
    private List<EmergencyContact> emergencyContacts;
    
    // Record Metadata
    private LocalDate createdDate;
    private LocalDate lastModified;
    private String createdBy;
    private String lastModifiedBy;
    private String notes;
    
    /**
     * Default constructor for BasicPatientRecord.
     * Initializes collections and sets default values.
     */
    public BasicPatientRecord() {
        this.recordId = "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.patientId = "PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.createdDate = LocalDate.now();
        this.lastModified = LocalDate.now();
        this.status = RecordStatus.PENDING;
        this.bloodType = BloodType.UNKNOWN;
        this.gender = Gender.PREFER_NOT_TO_SAY;
        
        // Initialize collections
        this.medicalConditions = new ArrayList<>();
        this.medications = new ArrayList<>();
        this.allergies = new ArrayList<>();
        this.emergencyContacts = new ArrayList<>();
    }
    
    /**
     * Parameterized constructor for BasicPatientRecord.
     * 
     * @param firstName Patient's first name
     * @param lastName Patient's last name
     * @param dateOfBirth Patient's date of birth
     * @param gender Patient's gender
     */
    public BasicPatientRecord(String firstName, String lastName, LocalDate dateOfBirth, Gender gender) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = RecordStatus.ACTIVE;
    }
    
    /**
     * Creates a deep copy of this patient record.
     * 
     * @return A new BasicPatientRecord instance that is a copy of this record
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Override
    public PatientRecordPrototype cloneRecord() throws CloneNotSupportedException {
        BasicPatientRecord cloned = new BasicPatientRecord();
        
        // Copy basic information
        cloned.recordId = "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        cloned.patientId = this.patientId + "-COPY";
        cloned.firstName = this.firstName;
        cloned.lastName = this.lastName;
        cloned.dateOfBirth = this.dateOfBirth != null ? LocalDate.from(this.dateOfBirth) : null;
        cloned.gender = this.gender;
        cloned.bloodType = this.bloodType;
        cloned.phoneNumber = this.phoneNumber;
        cloned.email = this.email;
        cloned.address = this.address;
        cloned.status = RecordStatus.PENDING; // New records start as pending
        
        // Deep copy collections
        cloned.medicalConditions = new ArrayList<>();
        for (MedicalCondition condition : this.medicalConditions) {
            cloned.medicalConditions.add(condition.clone());
        }
        
        cloned.medications = new ArrayList<>();
        for (Medication medication : this.medications) {
            cloned.medications.add(medication.clone());
        }
        
        cloned.allergies = new ArrayList<>(this.allergies);
        
        cloned.emergencyContacts = new ArrayList<>();
        for (EmergencyContact contact : this.emergencyContacts) {
            cloned.emergencyContacts.add(contact.clone());
        }
        
        // Set metadata for cloned record
        cloned.createdDate = LocalDate.now();
        cloned.lastModified = LocalDate.now();
        cloned.createdBy = "CLONED_FROM_" + this.recordId;
        cloned.notes = "Cloned from record: " + this.recordId;
        
        return cloned;
    }
    
    /**
     * Gets the record type identifier.
     * 
     * @return String representing the record type
     */
    @Override
    public String getRecordType() {
        return "BASIC_PATIENT_RECORD";
    }
    
    /**
     * Gets a formatted summary of the patient record.
     * 
     * @return String containing formatted record summary
     */
    @Override
    public String getRecordSummary() {
        StringBuilder summary = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        summary.append("========== PATIENT RECORD SUMMARY ==========\n");
        summary.append("Record ID: ").append(recordId).append("\n");
        summary.append("Patient ID: ").append(patientId).append("\n");
        summary.append("Name: ").append(getFullName()).append("\n");
        summary.append("Date of Birth: ").append(dateOfBirth != null ? dateOfBirth.format(formatter) : "Not specified").append("\n");
        summary.append("Age: ").append(calculateAge()).append(" years\n");
        summary.append("Gender: ").append(gender != null ? gender.getDisplayName() : "Not specified").append("\n");
        summary.append("Blood Type: ").append(bloodType != null ? bloodType.getSymbol() : "Unknown").append("\n");
        summary.append("Status: ").append(status != null ? status.getDisplayName() : "Unknown").append("\n");
        summary.append("Phone: ").append(phoneNumber != null ? phoneNumber : "Not provided").append("\n");
        summary.append("Email: ").append(email != null ? email : "Not provided").append("\n");
        
        summary.append("\nMedical Conditions: ").append(medicalConditions.size()).append("\n");
        summary.append("Current Medications: ").append(medications.size()).append("\n");
        summary.append("Known Allergies: ").append(allergies.size()).append("\n");
        summary.append("Emergency Contacts: ").append(emergencyContacts.size()).append("\n");
        
        summary.append("\nLast Modified: ").append(lastModified != null ? lastModified.format(formatter) : "Unknown").append("\n");
        summary.append("==========================================");
        
        return summary.toString();
    }
    
    /**
     * Validates the completeness and correctness of the patient record.
     * 
     * @return true if the record is valid, false otherwise
     */
    @Override
    public boolean validateRecord() {
        // Check required fields
        if (firstName == null || firstName.trim().isEmpty()) return false;
        if (lastName == null || lastName.trim().isEmpty()) return false;
        if (dateOfBirth == null) return false;
        if (gender == null) return false;
        
        // Check date constraints
        if (dateOfBirth.isAfter(LocalDate.now())) return false;
        
        // Validate age (must be reasonable)
        int age = calculateAge();
        if (age < 0 || age > 150) return false;
        
        // Validate collections are not null
        if (medicalConditions == null || medications == null || 
            allergies == null || emergencyContacts == null) return false;
        
        return true;
    }
    
    /**
     * Updates specific fields in the patient record dynamically.
     * 
     * @param fieldName The name of the field to update
     * @param value The new value for the field
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateField(String fieldName, Object value) {
        if (!status.isModifiable()) {
            return false; // Record is not in a modifiable state
        }
        
        try {
            switch (fieldName.toLowerCase()) {
                case "firstname":
                    this.firstName = (String) value;
                    break;
                case "lastname":
                    this.lastName = (String) value;
                    break;
                case "phonenumber":
                    this.phoneNumber = (String) value;
                    break;
                case "email":
                    this.email = (String) value;
                    break;
                case "address":
                    this.address = (String) value;
                    break;
                case "bloodtype":
                    this.bloodType = (value instanceof BloodType) ? (BloodType) value : BloodType.fromString((String) value);
                    break;
                case "gender":
                    this.gender = (value instanceof Gender) ? (Gender) value : Gender.fromString((String) value);
                    break;
                case "status":
                    this.status = (value instanceof RecordStatus) ? (RecordStatus) value : RecordStatus.fromString((String) value);
                    break;
                case "notes":
                    this.notes = (String) value;
                    break;
                default:
                    return false; // Unknown field
            }
            this.lastModified = LocalDate.now();
            return true;
        } catch (Exception e) {
            return false; // Update failed
        }
    }
    
    /**
     * Calculates the patient's current age based on date of birth.
     * 
     * @return Integer representing the patient's age in years
     */
    public int calculateAge() {
        if (dateOfBirth == null) return 0;
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
    
    /**
     * Gets the patient's full name.
     * 
     * @return String containing the full name
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null && !firstName.trim().isEmpty()) {
            fullName.append(firstName.trim());
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(lastName.trim());
        }
        return fullName.toString();
    }
    
    // Getters and Setters
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    
    public BloodType getBloodType() { return bloodType; }
    public void setBloodType(BloodType bloodType) { this.bloodType = bloodType; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public RecordStatus getStatus() { return status; }
    public void setStatus(RecordStatus status) { this.status = status; }
    
    public List<MedicalCondition> getMedicalConditions() { return medicalConditions; }
    public void setMedicalConditions(List<MedicalCondition> medicalConditions) { this.medicalConditions = medicalConditions; }
    
    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }
    
    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }
    
    public List<EmergencyContact> getEmergencyContacts() { return emergencyContacts; }
    public void setEmergencyContacts(List<EmergencyContact> emergencyContacts) { this.emergencyContacts = emergencyContacts; }
    
    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
    
    public LocalDate getLastModified() { return lastModified; }
    public void setLastModified(LocalDate lastModified) { this.lastModified = lastModified; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
