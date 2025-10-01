package enums;

/**
 * Enumeration for patient record status classification.
 * 
 * This enum tracks the current state of patient records in the medical system,
 * enabling proper workflow management and access control.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public enum RecordStatus {
    ACTIVE("Active", "Patient is currently receiving care"),
    INACTIVE("Inactive", "Patient is not currently receiving care"),
    DISCHARGED("Discharged", "Patient has been discharged from care"),
    TRANSFERRED("Transferred", "Patient has been transferred to another facility"),
    DECEASED("Deceased", "Patient is deceased"),
    PENDING("Pending", "Record is awaiting completion or verification"),
    ARCHIVED("Archived", "Record has been archived for long-term storage");
    
    private final String displayName;
    private final String description;
    
    /**
     * Constructor for RecordStatus enum.
     * 
     * @param displayName The display name for the status
     * @param description The detailed description of the status
     */
    RecordStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Gets the display name for the status.
     * 
     * @return String containing the status display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the description of the status.
     * 
     * @return String containing the status description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Creates a RecordStatus enum from a string value.
     * 
     * @param value The string value to convert
     * @return RecordStatus enum corresponding to the value
     */
    public static RecordStatus fromString(String value) {
        for (RecordStatus status : RecordStatus.values()) {
            if (status.displayName.equalsIgnoreCase(value) || 
                status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return PENDING; // Default fallback
    }
    
    /**
     * Checks if the status allows record modifications.
     * 
     * @return true if the record can be modified, false otherwise
     */
    public boolean isModifiable() {
        return this == ACTIVE || this == PENDING;
    }
}
