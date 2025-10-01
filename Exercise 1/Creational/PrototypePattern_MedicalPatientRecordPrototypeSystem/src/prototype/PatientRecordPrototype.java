package prototype;

/**
 * Prototype interface for cloning patient records.
 * 
 * The Prototype pattern allows creating new objects by cloning existing instances,
 * which is particularly useful for complex medical records with many fields.
 * This approach provides better performance than creating objects from scratch
 * and maintains consistency in record structure.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public interface PatientRecordPrototype extends Cloneable {
    
    /**
     * Creates a deep copy of the current patient record.
     * 
     * @return A new PatientRecordPrototype instance that is a copy of this record
     * @throws CloneNotSupportedException if cloning is not supported
     */
    PatientRecordPrototype cloneRecord() throws CloneNotSupportedException;
    
    /**
     * Gets the unique identifier for this record type.
     * 
     * @return String representing the record type identifier
     */
    String getRecordType();
    
    /**
     * Gets a summary of the patient record for display purposes.
     * 
     * @return String containing formatted record summary
     */
    String getRecordSummary();
    
    /**
     * Validates the completeness and correctness of the patient record.
     * 
     * @return true if the record is valid, false otherwise
     */
    boolean validateRecord();
    
    /**
     * Updates specific fields in the patient record dynamically.
     * 
     * @param fieldName The name of the field to update
     * @param value The new value for the field
     * @return true if update was successful, false otherwise
     */
    boolean updateField(String fieldName, Object value);
}
