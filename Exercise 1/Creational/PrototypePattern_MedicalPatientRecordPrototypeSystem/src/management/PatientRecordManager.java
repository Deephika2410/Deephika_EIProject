package management;

import prototype.PatientRecordPrototype;
import prototype.PatientRecordPrototypeRegistry;
import models.BasicPatientRecord;
import models.EmergencyPatientRecord;
import models.MedicalCondition;
import models.Medication;
import models.EmergencyContact;
import enums.Gender;
import enums.BloodType;
import enums.RecordStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import prototype.PatientRecordPrototype;
import prototype.PatientRecordPrototypeRegistry;
import models.BasicPatientRecord;
import models.EmergencyPatientRecord;
import enums.Gender;
import enums.BloodType;
import enums.RecordStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Manager class for handling patient record operations.
 * 
 * This class provides high-level operations for managing patient records
 * using the Prototype pattern. It handles record creation, cloning,
 * validation, and management through the prototype registry.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public class PatientRecordManager {
    private PatientRecordPrototypeRegistry registry;
    private List<PatientRecordPrototype> activeRecords;
    private Scanner scanner;
    
    /**
     * Constructor for PatientRecordManager.
     */
    public PatientRecordManager() {
        this.registry = PatientRecordPrototypeRegistry.getInstance();
        this.activeRecords = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        initializeDefaultPrototypes();
    }
    
    /**
     * Initializes default prototypes in the registry.
     */
    private void initializeDefaultPrototypes() {
        try {
            // Create comprehensive basic patient record template with rich data
            BasicPatientRecord basicTemplate = new BasicPatientRecord();
            basicTemplate.setFirstName("Template");
            basicTemplate.setLastName("Patient");
            basicTemplate.setDateOfBirth(LocalDate.of(1990, 1, 1)); // Set default DOB for validation
            basicTemplate.setGender(Gender.PREFER_NOT_TO_SAY);
            basicTemplate.setBloodType(BloodType.UNKNOWN);
            basicTemplate.setStatus(RecordStatus.PENDING);
            basicTemplate.setCreatedBy("SYSTEM_TEMPLATE");
            basicTemplate.setPhoneNumber("000-000-0000");
            basicTemplate.setEmail("template@hospital.com");
            basicTemplate.setAddress("Template Address, City, State");
            
            // Add template medical conditions
            basicTemplate.getMedicalConditions().add(
                new models.MedicalCondition("TEMP-001", "Template Condition", "Standard template condition", "MILD")
            );
            
            // Add template medications
            basicTemplate.getMedications().add(
                new models.Medication("MED-001", "Template Medication", "10mg", "Daily")
            );
            
            // Add template allergies
            basicTemplate.getAllergies().add("Template Allergy");
            
            // Add template emergency contact
            models.EmergencyContact tempContact = new models.EmergencyContact(
                "EC-001", "Template Contact", "Emergency Contact", "000-000-0000"
            );
            basicTemplate.getEmergencyContacts().add(tempContact);
            
            registry.registerPrototype("BASIC_TEMPLATE", basicTemplate);
            
            // Create comprehensive emergency patient record template
            EmergencyPatientRecord emergencyTemplate = new EmergencyPatientRecord();
            emergencyTemplate.setFirstName("Emergency");
            emergencyTemplate.setLastName("Patient");
            emergencyTemplate.setDateOfBirth(LocalDate.of(1985, 6, 15)); // Set default DOB
            emergencyTemplate.setTriageLevel("PENDING");
            emergencyTemplate.setChiefComplaint("To be assessed");
            emergencyTemplate.setArrivalMode("WALK_IN");
            emergencyTemplate.setGender(Gender.PREFER_NOT_TO_SAY);
            emergencyTemplate.setBloodType(BloodType.UNKNOWN);
            emergencyTemplate.setEmergencyContactName("Emergency Contact Person");
            emergencyTemplate.setEmergencyContactPhone("911-911-9911");
            emergencyTemplate.setEmergencyContactRelation("Family");
            emergencyTemplate.setVitalSigns("Pending Assessment");
            emergencyTemplate.setInitialAssessment("Waiting for physician");
            
            // Add template allergies and medications for emergency record
            emergencyTemplate.getAllergies().add("Unknown - Verify with patient");
            emergencyTemplate.getCurrentMedications().add("Unknown - Verify with patient");
            
            registry.registerPrototype("EMERGENCY_TEMPLATE", emergencyTemplate);
            
            // Create specialized templates for demonstration
            createSpecializedTemplates();
            
            System.out.println("Default prototypes with rich data initialized successfully.");
            
        } catch (Exception e) {
            System.err.println("Error initializing default prototypes: " + e.getMessage());
        }
    }
    
    /**
     * Creates specialized templates to demonstrate prototype pattern flexibility.
     */
    private void createSpecializedTemplates() {
        try {
            // ICU Patient Template - inherits from basic but specialized for ICU
            BasicPatientRecord icuTemplate = new BasicPatientRecord();
            icuTemplate.setFirstName("ICU");
            icuTemplate.setLastName("Patient");
            icuTemplate.setDateOfBirth(LocalDate.of(1975, 3, 20));
            icuTemplate.setStatus(RecordStatus.ACTIVE);
            icuTemplate.setBloodType(BloodType.O_NEGATIVE); // Universal donor for emergencies
            icuTemplate.setCreatedBy("ICU_SYSTEM");
            
            // ICU-specific conditions and medications
            icuTemplate.getMedicalConditions().add(
                new models.MedicalCondition("ICU-001", "Critical Care Monitoring", "Intensive care required", "CRITICAL")
            );
            icuTemplate.getMedications().add(
                new models.Medication("ICU-MED-001", "Monitoring Protocol", "Continuous", "24/7")
            );
            
            registry.registerPrototype("ICU_TEMPLATE", icuTemplate);
            
            // Pediatric Template - specialized for children
            BasicPatientRecord pediatricTemplate = new BasicPatientRecord();
            pediatricTemplate.setFirstName("Child");
            pediatricTemplate.setLastName("Patient");
            pediatricTemplate.setDateOfBirth(LocalDate.of(2015, 8, 10)); // Child age
            pediatricTemplate.setCreatedBy("PEDIATRIC_SYSTEM");
            
            // Pediatric-specific data
            models.EmergencyContact parentContact = new models.EmergencyContact(
                "PARENT-001", "Parent Guardian", "Parent", "555-PARENT"
            );
            parentContact.setPrimaryContact(true);
            pediatricTemplate.getEmergencyContacts().add(parentContact);
            
            registry.registerPrototype("PEDIATRIC_TEMPLATE", pediatricTemplate);
            
            System.out.println("Specialized templates created: ICU_TEMPLATE, PEDIATRIC_TEMPLATE");
            
        } catch (Exception e) {
            System.err.println("Error creating specialized templates: " + e.getMessage());
        }
    }
    
    /**
     * Creates a new patient record by cloning a prototype.
     * 
     * @param prototypeId The ID of the prototype to clone
     * @return The newly created patient record
     */
    public PatientRecordPrototype createPatientRecord(String prototypeId) {
        try {
            PatientRecordPrototype newRecord = registry.createRecord(prototypeId);
            activeRecords.add(newRecord);
            System.out.println("New patient record created successfully!");
            return newRecord;
        } catch (Exception e) {
            System.err.println("Error creating patient record: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Demonstrates selective modification after cloning - Core Prototype Pattern Feature.
     * This method shows how you can clone a record and modify only specific fields
     * while preserving all other inherited data from the prototype.
     * 
     * @param prototypeId The ID of the prototype to clone
     * @param modifications Map of field names to new values for selective updates
     * @return The cloned and selectively modified patient record
     */
    public PatientRecordPrototype createAndModifyRecord(String prototypeId, java.util.Map<String, Object> modifications) {
        try {
            System.out.println("\n=== Demonstrating Selective Modification After Cloning ===");
            
            // Step 1: Clone the prototype (inherits ALL existing data)
            PatientRecordPrototype clonedRecord = registry.createRecord(prototypeId);
            System.out.println("✓ Cloned prototype: " + prototypeId);
            System.out.println("  Inherited: All template data, structure, and collections");
            
            // Step 2: Selectively modify only specified fields
            System.out.println("\n✓ Applying selective modifications:");
            int successfulUpdates = 0;
            for (java.util.Map.Entry<String, Object> entry : modifications.entrySet()) {
                String fieldName = entry.getKey();
                Object newValue = entry.getValue();
                
                boolean updateSuccess = clonedRecord.updateField(fieldName, newValue);
                if (updateSuccess) {
                    successfulUpdates++;
                    System.out.println("  - Updated '" + fieldName + "' to: " + newValue);
                } else {
                    System.out.println("  - Failed to update '" + fieldName + "' (field protected or invalid)");
                }
            }
            
            System.out.println("✓ Selective modification complete: " + successfulUpdates + "/" + modifications.size() + " fields updated");
            System.out.println("✓ All other fields remain unchanged from prototype");
            
            activeRecords.add(clonedRecord);
            return clonedRecord;
            
        } catch (Exception e) {
            System.err.println("Error in selective modification: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a customized basic patient record with user input.
     * 
     * @return The newly created and customized patient record
     */
    public PatientRecordPrototype createCustomBasicRecord() {
        try {
            System.out.println("\n=== Creating Custom Basic Patient Record ===");
            
            // Get patient information
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine().trim();
            
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine().trim();
            
            System.out.print("Enter birth year (YYYY): ");
            int birthYear = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Enter birth month (1-12): ");
            int birthMonth = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Enter birth day (1-31): ");
            int birthDay = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.println("Select Gender:");
            System.out.println("1. Male");
            System.out.println("2. Female");
            System.out.println("3. Other");
            System.out.println("4. Prefer not to say");
            System.out.print("Choice (1-4): ");
            int genderChoice = Integer.parseInt(scanner.nextLine().trim());
            
            Gender gender = switch (genderChoice) {
                case 1 -> Gender.MALE;
                case 2 -> Gender.FEMALE;
                case 3 -> Gender.OTHER;
                default -> Gender.PREFER_NOT_TO_SAY;
            };
            
            // Create record using template
            PatientRecordPrototype newRecord = createPatientRecord("BASIC_TEMPLATE");
            if (newRecord != null) {
                // Customize the record
                newRecord.updateField("firstName", firstName);
                newRecord.updateField("lastName", lastName);
                newRecord.updateField("gender", gender);
                
                if (newRecord instanceof BasicPatientRecord basicRecord) {
                    basicRecord.setDateOfBirth(LocalDate.of(birthYear, birthMonth, birthDay));
                    basicRecord.setStatus(RecordStatus.ACTIVE);
                    basicRecord.setCreatedBy("USER_INPUT");
                }
                
                System.out.println("Custom basic patient record created successfully!");
                return newRecord;
            }
            
        } catch (Exception e) {
            System.err.println("Error creating custom basic record: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Creates a customized emergency patient record with user input.
     * 
     * @return The newly created and customized emergency record
     */
    public PatientRecordPrototype createCustomEmergencyRecord() {
        try {
            System.out.println("\n=== Creating Custom Emergency Patient Record ===");
            
            // Get patient information
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine().trim();
            
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine().trim();
            
            System.out.print("Enter chief complaint: ");
            String chiefComplaint = scanner.nextLine().trim();
            
            System.out.println("Select Triage Level:");
            System.out.println("1. CRITICAL - Immediate attention required");
            System.out.println("2. URGENT - Care needed within 15 minutes");
            System.out.println("3. LESS_URGENT - Care needed within 60 minutes");
            System.out.println("4. NON_URGENT - Care needed within 2 hours");
            System.out.print("Choice (1-4): ");
            int triageChoice = Integer.parseInt(scanner.nextLine().trim());
            
            String triageLevel = switch (triageChoice) {
                case 1 -> "CRITICAL";
                case 2 -> "URGENT";
                case 3 -> "LESS_URGENT";
                default -> "NON_URGENT";
            };
            
            System.out.println("Select Arrival Mode:");
            System.out.println("1. AMBULANCE");
            System.out.println("2. WALK_IN");
            System.out.println("3. HELICOPTER");
            System.out.println("4. POLICE");
            System.out.print("Choice (1-4): ");
            int arrivalChoice = Integer.parseInt(scanner.nextLine().trim());
            
            String arrivalMode = switch (arrivalChoice) {
                case 1 -> "AMBULANCE";
                case 2 -> "WALK_IN";
                case 3 -> "HELICOPTER";
                default -> "POLICE";
            };
            
            System.out.print("Is this a trauma case? (y/n): ");
            boolean isTrauma = scanner.nextLine().trim().toLowerCase().startsWith("y");
            
            // Create record using template
            PatientRecordPrototype newRecord = createPatientRecord("EMERGENCY_TEMPLATE");
            if (newRecord != null) {
                // Customize the record
                newRecord.updateField("firstName", firstName);
                newRecord.updateField("lastName", lastName);
                newRecord.updateField("chiefComplaint", chiefComplaint);
                newRecord.updateField("triageLevel", triageLevel);
                newRecord.updateField("arrivalMode", arrivalMode);
                newRecord.updateField("isTrauma", isTrauma);
                
                if (isTrauma) {
                    System.out.print("Enter trauma details: ");
                    String traumaDetails = scanner.nextLine().trim();
                    newRecord.updateField("traumaDetails", traumaDetails);
                }
                
                System.out.println("Custom emergency patient record created successfully!");
                return newRecord;
            }
            
        } catch (Exception e) {
            System.err.println("Error creating custom emergency record: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Clones an existing patient record with intelligent duplicate handling.
     * If a clone of this record already exists, it updates the existing clone instead of creating a new one.
     * 
     * @param sourceRecord The record to clone
     * @return The cloned or updated record
     */
    public PatientRecordPrototype cloneExistingRecord(PatientRecordPrototype sourceRecord) {
        try {
            // Check if we already have a clone of this record
            PatientRecordPrototype existingClone = findExistingClone(sourceRecord);
            
            if (existingClone != null) {
                System.out.println("Found existing clone. Updating existing record instead of creating duplicate.");
                return existingClone;
            }
            
            // Create new clone if none exists
            PatientRecordPrototype clonedRecord = sourceRecord.cloneRecord();
            activeRecords.add(clonedRecord);
            System.out.println("New clone created successfully!");
            return clonedRecord;
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning record: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Finds an existing clone of the given source record.
     * 
     * @param sourceRecord The source record to find clones of
     * @return The existing clone if found, null otherwise
     */
    private PatientRecordPrototype findExistingClone(PatientRecordPrototype sourceRecord) {
        String sourcePatientId = null;
        String sourceFullName = null;
        
        // Extract identifiers from source record
        if (sourceRecord instanceof BasicPatientRecord basicRecord) {
            sourcePatientId = basicRecord.getPatientId();
            sourceFullName = basicRecord.getFullName();
        } else if (sourceRecord instanceof EmergencyPatientRecord emergencyRecord) {
            sourcePatientId = emergencyRecord.getPatientId();
            sourceFullName = emergencyRecord.getFullName();
        }
        
        if (sourcePatientId == null || sourceFullName == null) {
            return null;
        }
        
        // Look for existing clones
        for (PatientRecordPrototype record : activeRecords) {
            String recordPatientId = null;
            String recordFullName = null;
            
            if (record instanceof BasicPatientRecord basicRecord) {
                recordPatientId = basicRecord.getPatientId();
                recordFullName = basicRecord.getFullName();
            } else if (record instanceof EmergencyPatientRecord emergencyRecord) {
                recordPatientId = emergencyRecord.getPatientId();
                recordFullName = emergencyRecord.getFullName();
            }
            
            // Check if this is a clone (contains "-COPY" and matches source name)
            if (recordPatientId != null && recordPatientId.contains("-COPY") && 
                recordFullName != null && recordFullName.equals(sourceFullName)) {
                return record;
            }
        }
        
        return null;
    }
    
    /**
     * Creates or updates a clone with intelligent name management.
     * 
     * @param sourceRecord The source record to clone
     * @param newFirstName The new first name for the clone
     * @param newLastName The new last name for the clone
     * @return The created or updated clone
     */
    public PatientRecordPrototype createOrUpdateClone(PatientRecordPrototype sourceRecord, 
                                                     String newFirstName, String newLastName) {
        try {
            // Check if a clone with this name already exists
            PatientRecordPrototype existingRecord = findRecordByName(newFirstName, newLastName);
            
            if (existingRecord != null) {
                System.out.println("Record with name '" + newFirstName + " " + newLastName + "' already exists.");
                System.out.print("Do you want to update the existing record? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                
                if (response.startsWith("y")) {
                    // Update existing record
                    updateRecordFromSource(existingRecord, sourceRecord);
                    System.out.println("Existing record updated successfully!");
                    return existingRecord;
                } else {
                    System.out.println("Operation cancelled.");
                    return null;
                }
            }
            
            // Create new clone
            PatientRecordPrototype clonedRecord = sourceRecord.cloneRecord();
            clonedRecord.updateField("firstName", newFirstName);
            clonedRecord.updateField("lastName", newLastName);
            
            activeRecords.add(clonedRecord);
            System.out.println("New clone created with name: " + newFirstName + " " + newLastName);
            return clonedRecord;
            
        } catch (CloneNotSupportedException e) {
            System.err.println("Error creating clone: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Finds a record by first and last name.
     * 
     * @param firstName The first name to search for
     * @param lastName The last name to search for
     * @return The matching record if found, null otherwise
     */
    private PatientRecordPrototype findRecordByName(String firstName, String lastName) {
        String searchName = (firstName + " " + lastName).trim();
        
        for (PatientRecordPrototype record : activeRecords) {
            String recordName = null;
            
            if (record instanceof BasicPatientRecord basicRecord) {
                recordName = basicRecord.getFullName();
            } else if (record instanceof EmergencyPatientRecord emergencyRecord) {
                recordName = emergencyRecord.getFullName();
            }
            
            if (recordName != null && recordName.equalsIgnoreCase(searchName)) {
                return record;
            }
        }
        
        return null;
    }
    
    /**
     * Updates an existing record with data from a source record.
     * 
     * @param targetRecord The record to update
     * @param sourceRecord The source record to copy data from
     */
    private void updateRecordFromSource(PatientRecordPrototype targetRecord, PatientRecordPrototype sourceRecord) {
        if (sourceRecord instanceof BasicPatientRecord sourceBasic && 
            targetRecord instanceof BasicPatientRecord targetBasic) {
            
            // Update key fields while preserving identity
            if (sourceBasic.getGender() != null) targetBasic.setGender(sourceBasic.getGender());
            if (sourceBasic.getBloodType() != null) targetBasic.setBloodType(sourceBasic.getBloodType());
            if (sourceBasic.getDateOfBirth() != null) targetBasic.setDateOfBirth(sourceBasic.getDateOfBirth());
            if (sourceBasic.getPhoneNumber() != null) targetBasic.setPhoneNumber(sourceBasic.getPhoneNumber());
            if (sourceBasic.getEmail() != null) targetBasic.setEmail(sourceBasic.getEmail());
            if (sourceBasic.getAddress() != null) targetBasic.setAddress(sourceBasic.getAddress());
            
            // Update collections
            targetBasic.getMedicalConditions().clear();
            for (MedicalCondition condition : sourceBasic.getMedicalConditions()) {
                targetBasic.getMedicalConditions().add(condition.clone());
            }
            
            targetBasic.getMedications().clear();
            for (Medication medication : sourceBasic.getMedications()) {
                targetBasic.getMedications().add(medication.clone());
            }
            
            targetBasic.getAllergies().clear();
            targetBasic.getAllergies().addAll(sourceBasic.getAllergies());
            
            targetBasic.getEmergencyContacts().clear();
            for (EmergencyContact contact : sourceBasic.getEmergencyContacts()) {
                targetBasic.getEmergencyContacts().add(contact.clone());
            }
            
            targetBasic.setLastModified(LocalDate.now());
        }
        // Similar logic can be added for Emergency records
    }
    
    /**
     * Validates all active records.
     * 
     * @return List of validation results
     */
    public List<String> validateAllRecords() {
        List<String> validationResults = new ArrayList<>();
        
        for (int i = 0; i < activeRecords.size(); i++) {
            PatientRecordPrototype record = activeRecords.get(i);
            boolean isValid = record.validateRecord();
            String result = String.format("Record %d (%s): %s", 
                i + 1, record.getRecordType(), isValid ? "VALID" : "INVALID");
            validationResults.add(result);
        }
        
        return validationResults;
    }
    
    /**
     * Gets a summary of all active records.
     * 
     * @return String containing summaries of all active records
     */
    public String getAllRecordsSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("========== ALL ACTIVE RECORDS SUMMARY ==========\n");
        summary.append("Total Active Records: ").append(activeRecords.size()).append("\n\n");
        
        if (activeRecords.isEmpty()) {
            summary.append("No active records found.\n");
        } else {
            for (int i = 0; i < activeRecords.size(); i++) {
                summary.append("Record ").append(i + 1).append(":\n");
                summary.append(activeRecords.get(i).getRecordSummary()).append("\n\n");
            }
        }
        
        summary.append("===============================================");
        return summary.toString();
    }
    
    /**
     * Gets the registry information.
     * 
     * @return String containing registry information
     */
    public String getRegistryInfo() {
        return registry.getRegistryInfo();
    }
    
    /**
     * Gets the count of active records.
     * 
     * @return Integer representing the number of active records
     */
    public int getActiveRecordCount() {
        return activeRecords.size();
    }
    
    /**
     * Clears all active records.
     */
    public void clearActiveRecords() {
        int count = activeRecords.size();
        activeRecords.clear();
        System.out.println("Cleared " + count + " active records.");
    }
    
    /**
     * Gets a specific active record by index.
     * 
     * @param index The index of the record to retrieve
     * @return The patient record at the specified index, or null if invalid index
     */
    public PatientRecordPrototype getActiveRecord(int index) {
        if (index >= 0 && index < activeRecords.size()) {
            return activeRecords.get(index);
        }
        return null;
    }
    
}
