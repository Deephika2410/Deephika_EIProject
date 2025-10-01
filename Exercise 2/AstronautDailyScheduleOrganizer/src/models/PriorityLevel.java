package models;

/**
 * Enumeration representing different priority levels for tasks
 * Following SOLID principles with clear separation of concerns
 */
public enum PriorityLevel {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4);

    private final String displayName;
    private final int priority;

    /**
     * Constructor for PriorityLevel enum
     * @param displayName Human-readable name
     * @param priority Numeric priority for sorting
     */
    PriorityLevel(String displayName, int priority) {
        this.displayName = displayName;
        this.priority = priority;
    }

    /**
     * Gets the display name of the priority level
     * @return String representation of priority level
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the numeric priority value
     * @return Integer priority value for comparison
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Parses a string to PriorityLevel enum
     * @param priorityStr String representation of priority
     * @return PriorityLevel enum value
     * @throws IllegalArgumentException if invalid priority string
     */
    public static PriorityLevel fromString(String priorityStr) {
        if (priorityStr == null || priorityStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Priority level cannot be null or empty");
        }

        String normalizedPriority = priorityStr.trim().toUpperCase();
        
        try {
            return PriorityLevel.valueOf(normalizedPriority);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid priority level: " + priorityStr + 
                ". Valid options are: LOW, MEDIUM, HIGH, CRITICAL");
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}
