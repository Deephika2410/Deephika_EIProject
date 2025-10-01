package prototype;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Registry for managing patient record prototypes.
 * 
 * This class implements the Prototype Registry pattern, which provides
 * a centralized location for storing and retrieving prototype instances.
 * It allows the system to manage multiple prototype templates and
 * create new instances efficiently through cloning.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public class PatientRecordPrototypeRegistry {
    private static PatientRecordPrototypeRegistry instance;
    private Map<String, PatientRecordPrototype> prototypes;
    
    /**
     * Private constructor for singleton pattern.
     */
    private PatientRecordPrototypeRegistry() {
        this.prototypes = new HashMap<>();
    }
    
    /**
     * Gets the singleton instance of the prototype registry.
     * 
     * @return The singleton PatientRecordPrototypeRegistry instance
     */
    public static synchronized PatientRecordPrototypeRegistry getInstance() {
        if (instance == null) {
            instance = new PatientRecordPrototypeRegistry();
        }
        return instance;
    }
    
    /**
     * Registers a prototype in the registry with a unique identifier.
     * 
     * @param id Unique identifier for the prototype
     * @param prototype The prototype instance to register
     * @throws IllegalArgumentException if id is null or prototype is null
     */
    public void registerPrototype(String id, PatientRecordPrototype prototype) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Prototype ID cannot be null or empty");
        }
        if (prototype == null) {
            throw new IllegalArgumentException("Prototype cannot be null");
        }
        
        prototypes.put(id.toUpperCase(), prototype);
        System.out.println("Prototype registered successfully: " + id + " -> " + prototype.getRecordType());
    }
    
    /**
     * Creates a new patient record by cloning the specified prototype.
     * 
     * @param prototypeId The identifier of the prototype to clone
     * @return A new PatientRecordPrototype instance cloned from the specified prototype
     * @throws CloneNotSupportedException if cloning is not supported
     * @throws IllegalArgumentException if prototype ID is not found
     */
    public PatientRecordPrototype createRecord(String prototypeId) throws CloneNotSupportedException {
        if (prototypeId == null || prototypeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Prototype ID cannot be null or empty");
        }
        
        String upperCaseId = prototypeId.toUpperCase();
        PatientRecordPrototype prototype = prototypes.get(upperCaseId);
        
        if (prototype == null) {
            throw new IllegalArgumentException("Prototype not found for ID: " + prototypeId);
        }
        
        PatientRecordPrototype clonedRecord = prototype.cloneRecord();
        System.out.println("Record created from prototype: " + prototypeId + " -> " + clonedRecord.getRecordType());
        return clonedRecord;
    }
    
    /**
     * Removes a prototype from the registry.
     * 
     * @param prototypeId The identifier of the prototype to remove
     * @return true if the prototype was removed, false if it was not found
     */
    public boolean removePrototype(String prototypeId) {
        if (prototypeId == null || prototypeId.trim().isEmpty()) {
            return false;
        }
        
        String upperCaseId = prototypeId.toUpperCase();
        PatientRecordPrototype removed = prototypes.remove(upperCaseId);
        
        if (removed != null) {
            System.out.println("Prototype removed: " + prototypeId);
            return true;
        }
        return false;
    }
    
    /**
     * Checks if a prototype with the specified ID exists in the registry.
     * 
     * @param prototypeId The identifier to check
     * @return true if the prototype exists, false otherwise
     */
    public boolean hasPrototype(String prototypeId) {
        if (prototypeId == null || prototypeId.trim().isEmpty()) {
            return false;
        }
        return prototypes.containsKey(prototypeId.toUpperCase());
    }
    
    /**
     * Gets a set of all registered prototype IDs.
     * 
     * @return Set containing all registered prototype identifiers
     */
    public Set<String> getRegisteredPrototypeIds() {
        return prototypes.keySet();
    }
    
    /**
     * Gets the number of registered prototypes.
     * 
     * @return Integer representing the count of registered prototypes
     */
    public int getPrototypeCount() {
        return prototypes.size();
    }
    
    /**
     * Clears all prototypes from the registry.
     */
    public void clearRegistry() {
        int count = prototypes.size();
        prototypes.clear();
        System.out.println("Registry cleared. Removed " + count + " prototypes.");
    }
    
    /**
     * Gets a formatted list of all registered prototypes.
     * 
     * @return String containing information about all registered prototypes
     */
    public String getRegistryInfo() {
        StringBuilder info = new StringBuilder();
        info.append("========== PROTOTYPE REGISTRY INFO ==========\n");
        info.append("Total Prototypes: ").append(prototypes.size()).append("\n");
        info.append("Registered Prototypes:\n");
        
        if (prototypes.isEmpty()) {
            info.append("  No prototypes registered.\n");
        } else {
            for (Map.Entry<String, PatientRecordPrototype> entry : prototypes.entrySet()) {
                info.append("  ID: ").append(entry.getKey())
                    .append(" -> Type: ").append(entry.getValue().getRecordType()).append("\n");
            }
        }
        
        info.append("============================================");
        return info.toString();
    }
    
    /**
     * Creates a prototype with sample data for testing purposes.
     * 
     * @param prototypeId The ID for the sample prototype
     * @param recordType The type of record to create
     * @return The created prototype with sample data
     */
    public PatientRecordPrototype createSamplePrototype(String prototypeId, String recordType) {
        try {
            PatientRecordPrototype prototype = null;
            
            // This would typically use a factory or reflection
            // For demonstration, we'll handle the basic types
            switch (recordType.toUpperCase()) {
                case "BASIC":
                case "BASIC_PATIENT_RECORD":
                    // Note: This would typically use dynamic class loading
                    // For now, we'll return null and let the calling code handle instantiation
                    break;
                case "EMERGENCY":
                case "EMERGENCY_PATIENT_RECORD":
                    // Note: Similar to above
                    break;
                default:
                    throw new IllegalArgumentException("Unknown record type: " + recordType);
            }
            
            return prototype;
        } catch (Exception e) {
            System.err.println("Error creating sample prototype: " + e.getMessage());
            return null;
        }
    }
}
