package enums;

/**
 * Enumeration for blood type classification.
 * 
 * This enum represents the standard ABO blood group system with Rh factor,
 * essential for medical procedures and emergency care.
 * 
 * @author Medical Records System
 * @version 1.0
 */
public enum BloodType {
    A_POSITIVE("A+", "Type A Positive"),
    A_NEGATIVE("A-", "Type A Negative"),
    B_POSITIVE("B+", "Type B Positive"),
    B_NEGATIVE("B-", "Type B Negative"),
    AB_POSITIVE("AB+", "Type AB Positive"),
    AB_NEGATIVE("AB-", "Type AB Negative"),
    O_POSITIVE("O+", "Type O Positive"),
    O_NEGATIVE("O-", "Type O Negative"),
    UNKNOWN("Unknown", "Blood type not determined");
    
    private final String symbol;
    private final String description;
    
    /**
     * Constructor for BloodType enum.
     * 
     * @param symbol The standard symbol for the blood type
     * @param description The full description of the blood type
     */
    BloodType(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }
    
    /**
     * Gets the symbol for the blood type.
     * 
     * @return String containing the blood type symbol
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Gets the description of the blood type.
     * 
     * @return String containing the blood type description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Creates a BloodType enum from a string value.
     * 
     * @param value The string value to convert
     * @return BloodType enum corresponding to the value
     */
    public static BloodType fromString(String value) {
        for (BloodType bloodType : BloodType.values()) {
            if (bloodType.symbol.equalsIgnoreCase(value) || 
                bloodType.description.equalsIgnoreCase(value) || 
                bloodType.name().equalsIgnoreCase(value)) {
                return bloodType;
            }
        }
        return UNKNOWN; // Default fallback
    }
}
