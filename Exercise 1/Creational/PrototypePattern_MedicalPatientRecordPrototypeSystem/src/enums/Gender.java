package enums;

/**
 * Enumeration for patient gender classification.
 * 
 * This enum provides standardized gender options for medical records,
 * following medical documentation standards.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public enum Gender {
    MALE("Male", "M"),
    FEMALE("Female", "F"),
    OTHER("Other", "O"),
    PREFER_NOT_TO_SAY("Prefer not to say", "N");
    
    private final String displayName;
    private final String code;
    
    /**
     * Constructor for Gender enum.
     * 
     * @param displayName The full display name for the gender
     * @param code The single character code for the gender
     */
    Gender(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }
    
    /**
     * Gets the display name for the gender.
     * 
     * @return String containing the full gender display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the single character code for the gender.
     * 
     * @return String containing the gender code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Creates a Gender enum from a string value.
     * 
     * @param value The string value to convert
     * @return Gender enum corresponding to the value
     */
    public static Gender fromString(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.displayName.equalsIgnoreCase(value) || 
                gender.code.equalsIgnoreCase(value) || 
                gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        return OTHER; // Default fallback
    }
}
