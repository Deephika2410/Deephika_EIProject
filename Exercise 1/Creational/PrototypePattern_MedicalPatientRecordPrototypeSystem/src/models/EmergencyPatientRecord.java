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
 * Specialized patient record for emergency department patients.
 * 
 * This class extends the basic patient record functionality with
 * emergency-specific fields and rapid access features. It demonstrates
 * the Prototype pattern by providing optimized cloning for emergency scenarios.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public class EmergencyPatientRecord implements PatientRecordPrototype {
    // Basic Information (inherited behavior)
    private String recordId;
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private BloodType bloodType;
    private String phoneNumber;
    private String emergencyContactPhone;
    private RecordStatus status;
    
    // Emergency-specific fields
    private String triageLevel;
    private String chiefComplaint;
    private String arrivalMode;
    private LocalDate admissionDate;
    private String admittingPhysician;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private List<String> allergies;
    private List<String> currentMedications;
    private String vitalSigns;
    private String initialAssessment;
    private boolean isTrauma;
    private String traumaDetails;
    
    /**
     * Default constructor for EmergencyPatientRecord.
     */
    public EmergencyPatientRecord() {
        this.recordId = "EMREC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.patientId = "EMPAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.admissionDate = LocalDate.now();
        this.status = RecordStatus.ACTIVE;
        this.bloodType = BloodType.UNKNOWN;
        this.gender = Gender.PREFER_NOT_TO_SAY;
        this.triageLevel = "PENDING";
        this.arrivalMode = "WALK_IN";
        this.isTrauma = false;
        
        // Initialize collections
        this.allergies = new ArrayList<>();
        this.currentMedications = new ArrayList<>();
    }
    
    /**
     * Parameterized constructor for EmergencyPatientRecord.
     * 
     * @param firstName Patient's first name
     * @param lastName Patient's last name
     * @param triageLevel Emergency triage level
     * @param chiefComplaint Primary complaint
     */
    public EmergencyPatientRecord(String firstName, String lastName, String triageLevel, String chiefComplaint) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.triageLevel = triageLevel;
        this.chiefComplaint = chiefComplaint;
    }
    
    /**
     * Creates a deep copy of this emergency patient record.
     * This is optimized for emergency scenarios where rapid record creation is needed.
     * 
     * @return A new EmergencyPatientRecord instance that is a copy of this record
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Override
    public PatientRecordPrototype cloneRecord() throws CloneNotSupportedException {
        EmergencyPatientRecord cloned = new EmergencyPatientRecord();
        
        // Copy basic information with new IDs
        cloned.recordId = "EMREC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        cloned.patientId = this.patientId + "-COPY";
        cloned.firstName = this.firstName;
        cloned.lastName = this.lastName;
        cloned.dateOfBirth = this.dateOfBirth != null ? LocalDate.from(this.dateOfBirth) : null;
        cloned.gender = this.gender;
        cloned.bloodType = this.bloodType;
        cloned.phoneNumber = this.phoneNumber;
        cloned.emergencyContactPhone = this.emergencyContactPhone;
        cloned.status = RecordStatus.PENDING; // New emergency records start as pending
        
        // Copy emergency-specific fields
        cloned.triageLevel = "PENDING"; // Reset triage for new patient
        cloned.chiefComplaint = this.chiefComplaint;
        cloned.arrivalMode = this.arrivalMode;
        cloned.admissionDate = LocalDate.now(); // New admission date
        cloned.admittingPhysician = this.admittingPhysician;
        cloned.emergencyContactName = this.emergencyContactName;
        cloned.emergencyContactRelation = this.emergencyContactRelation;
        cloned.vitalSigns = "PENDING"; // Reset vitals for new patient
        cloned.initialAssessment = "PENDING"; // Reset assessment
        cloned.isTrauma = false; // Reset trauma flag
        cloned.traumaDetails = null; // Clear trauma details
        
        // Deep copy collections
        cloned.allergies = new ArrayList<>(this.allergies);
        cloned.currentMedications = new ArrayList<>(this.currentMedications);
        
        return cloned;
    }
    
    /**
     * Gets the record type identifier.
     * 
     * @return String representing the emergency record type
     */
    @Override
    public String getRecordType() {
        return "EMERGENCY_PATIENT_RECORD";
    }
    
    /**
     * Gets a formatted summary of the emergency patient record.
     * 
     * @return String containing formatted emergency record summary
     */
    @Override
    public String getRecordSummary() {
        StringBuilder summary = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        summary.append("========== EMERGENCY PATIENT RECORD ==========\n");
        summary.append("Record ID: ").append(recordId).append("\n");
        summary.append("Patient ID: ").append(patientId).append("\n");
        summary.append("Name: ").append(getFullName()).append("\n");
        summary.append("Triage Level: ").append(triageLevel).append("\n");
        summary.append("Chief Complaint: ").append(chiefComplaint != null ? chiefComplaint : "Not specified").append("\n");
        summary.append("Arrival Mode: ").append(arrivalMode).append("\n");
        summary.append("Admission Date: ").append(admissionDate != null ? admissionDate.format(formatter) : "Unknown").append("\n");
        summary.append("Status: ").append(status != null ? status.getDisplayName() : "Unknown").append("\n");
        summary.append("Trauma Case: ").append(isTrauma ? "YES" : "NO").append("\n");
        
        if (dateOfBirth != null) {
            summary.append("Age: ").append(calculateAge()).append(" years\n");
        }
        summary.append("Gender: ").append(gender != null ? gender.getDisplayName() : "Not specified").append("\n");
        summary.append("Blood Type: ").append(bloodType != null ? bloodType.getSymbol() : "Unknown").append("\n");
        
        summary.append("\nEmergency Contact: ").append(emergencyContactName != null ? emergencyContactName : "Not provided").append("\n");
        summary.append("Contact Phone: ").append(emergencyContactPhone != null ? emergencyContactPhone : "Not provided").append("\n");
        summary.append("Relationship: ").append(emergencyContactRelation != null ? emergencyContactRelation : "Not specified").append("\n");
        
        summary.append("\nAllergies: ").append(allergies.size()).append("\n");
        summary.append("Current Medications: ").append(currentMedications.size()).append("\n");
        summary.append("Vital Signs: ").append(vitalSigns != null ? vitalSigns : "Pending").append("\n");
        summary.append("Initial Assessment: ").append(initialAssessment != null ? initialAssessment : "Pending").append("\n");
        
        if (isTrauma && traumaDetails != null) {
            summary.append("Trauma Details: ").append(traumaDetails).append("\n");
        }
        
        summary.append("Admitting Physician: ").append(admittingPhysician != null ? admittingPhysician : "Not assigned").append("\n");
        summary.append("===========================================");
        
        return summary.toString();
    }
    
    /**
     * Validates the emergency patient record.
     * 
     * @return true if the record is valid for emergency use, false otherwise
     */
    @Override
    public boolean validateRecord() {
        // Emergency records have more relaxed validation due to urgent nature
        if (firstName == null || firstName.trim().isEmpty()) return false;
        if (lastName == null || lastName.trim().isEmpty()) return false;
        if (triageLevel == null || triageLevel.trim().isEmpty()) return false;
        if (chiefComplaint == null || chiefComplaint.trim().isEmpty()) return false;
        
        // Validate collections are not null
        if (allergies == null || currentMedications == null) return false;
        
        return true;
    }
    
    /**
     * Updates specific fields in the emergency patient record dynamically.
     * 
     * @param fieldName The name of the field to update
     * @param value The new value for the field
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateField(String fieldName, Object value) {
        try {
            switch (fieldName.toLowerCase()) {
                case "firstname":
                    this.firstName = (String) value;
                    break;
                case "lastname":
                    this.lastName = (String) value;
                    break;
                case "triagelevel":
                    this.triageLevel = (String) value;
                    break;
                case "chiefcomplaint":
                    this.chiefComplaint = (String) value;
                    break;
                case "arrivalmode":
                    this.arrivalMode = (String) value;
                    break;
                case "vitalsigns":
                    this.vitalSigns = (String) value;
                    break;
                case "initialassessment":
                    this.initialAssessment = (String) value;
                    break;
                case "istrauma":
                    this.isTrauma = (Boolean) value;
                    break;
                case "traumadetails":
                    this.traumaDetails = (String) value;
                    break;
                case "admittingphysician":
                    this.admittingPhysician = (String) value;
                    break;
                case "emergencycontactname":
                    this.emergencyContactName = (String) value;
                    break;
                case "emergencycontactphone":
                    this.emergencyContactPhone = (String) value;
                    break;
                case "emergencycontactrelation":
                    this.emergencyContactRelation = (String) value;
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
                default:
                    return false; // Unknown field
            }
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
    
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    
    public RecordStatus getStatus() { return status; }
    public void setStatus(RecordStatus status) { this.status = status; }
    
    public String getTriageLevel() { return triageLevel; }
    public void setTriageLevel(String triageLevel) { this.triageLevel = triageLevel; }
    
    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
    
    public String getArrivalMode() { return arrivalMode; }
    public void setArrivalMode(String arrivalMode) { this.arrivalMode = arrivalMode; }
    
    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }
    
    public String getAdmittingPhysician() { return admittingPhysician; }
    public void setAdmittingPhysician(String admittingPhysician) { this.admittingPhysician = admittingPhysician; }
    
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    
    public String getEmergencyContactRelation() { return emergencyContactRelation; }
    public void setEmergencyContactRelation(String emergencyContactRelation) { this.emergencyContactRelation = emergencyContactRelation; }
    
    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }
    
    public List<String> getCurrentMedications() { return currentMedications; }
    public void setCurrentMedications(List<String> currentMedications) { this.currentMedications = currentMedications; }
    
    public String getVitalSigns() { return vitalSigns; }
    public void setVitalSigns(String vitalSigns) { this.vitalSigns = vitalSigns; }
    
    public String getInitialAssessment() { return initialAssessment; }
    public void setInitialAssessment(String initialAssessment) { this.initialAssessment = initialAssessment; }
    
    public boolean isTrauma() { return isTrauma; }
    public void setTrauma(boolean trauma) { isTrauma = trauma; }
    
    public String getTraumaDetails() { return traumaDetails; }
    public void setTraumaDetails(String traumaDetails) { this.traumaDetails = traumaDetails; }
}
