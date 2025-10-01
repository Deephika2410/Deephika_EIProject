package exceptions;

/**
 * Custom exception class for infrastructure-related errors.
 * This exception is thrown when operations on infrastructure components fail
 * due to validation errors, invalid operations, or system constraints.
 * 
 * Follows the principle of creating domain-specific exceptions for
 * better error handling and debugging.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class InfrastructureException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private final String componentId;
    private final String componentType;
    private final String operation;
    
    /**
     * Constructs a new infrastructure exception with the specified detail message.
     * @param message The detail message explaining the error
     */
    public InfrastructureException(String message) {
        super(message);
        this.componentId = null;
        this.componentType = null;
        this.operation = null;
    }
    
    /**
     * Constructs a new infrastructure exception with the specified detail message and cause.
     * @param message The detail message explaining the error
     * @param cause The cause of the exception
     */
    public InfrastructureException(String message, Throwable cause) {
        super(message, cause);
        this.componentId = null;
        this.componentType = null;
        this.operation = null;
    }
    
    /**
     * Constructs a new infrastructure exception with detailed component information.
     * @param message The detail message explaining the error
     * @param componentId The ID of the component that caused the error
     * @param componentType The type of the component that caused the error
     * @param operation The operation that was being performed when the error occurred
     */
    public InfrastructureException(String message, String componentId, String componentType, String operation) {
        super(message);
        this.componentId = componentId;
        this.componentType = componentType;
        this.operation = operation;
    }
    
    /**
     * Constructs a new infrastructure exception with detailed component information and cause.
     * @param message The detail message explaining the error
     * @param cause The cause of the exception
     * @param componentId The ID of the component that caused the error
     * @param componentType The type of the component that caused the error
     * @param operation The operation that was being performed when the error occurred
     */
    public InfrastructureException(String message, Throwable cause, String componentId, 
                                 String componentType, String operation) {
        super(message, cause);
        this.componentId = componentId;
        this.componentType = componentType;
        this.operation = operation;
    }
    
    /**
     * Gets the ID of the component that caused the error.
     * @return String representing the component ID, or null if not available
     */
    public String getComponentId() {
        return componentId;
    }
    
    /**
     * Gets the type of the component that caused the error.
     * @return String representing the component type, or null if not available
     */
    public String getComponentType() {
        return componentType;
    }
    
    /**
     * Gets the operation that was being performed when the error occurred.
     * @return String representing the operation, or null if not available
     */
    public String getOperation() {
        return operation;
    }
    
    /**
     * Returns a detailed error message including component information.
     * @return String containing the complete error information
     */
    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder(super.getMessage());
        
        if (componentId != null || componentType != null || operation != null) {
            message.append(" [");
            
            if (componentType != null) {
                message.append("Type: ").append(componentType);
            }
            
            if (componentId != null) {
                if (componentType != null) message.append(", ");
                message.append("ID: ").append(componentId);
            }
            
            if (operation != null) {
                if (componentType != null || componentId != null) message.append(", ");
                message.append("Operation: ").append(operation);
            }
            
            message.append("]");
        }
        
        return message.toString();
    }
    
    /**
     * Creates an infrastructure exception for validation errors.
     * @param fieldName The name of the field that failed validation
     * @param value The value that failed validation
     * @return InfrastructureException configured for validation error
     */
    public static InfrastructureException validationError(String fieldName, String value) {
        return new InfrastructureException("Validation failed for field '" + fieldName + 
            "' with value: " + (value != null ? value : "null"));
    }
    
    /**
     * Creates an infrastructure exception for component not found errors.
     * @param componentId The ID of the component that was not found
     * @param parentType The type of the parent component
     * @return InfrastructureException configured for component not found error
     */
    public static InfrastructureException componentNotFound(String componentId, String parentType) {
        return new InfrastructureException("Component with ID '" + componentId + 
            "' not found in " + parentType, componentId, parentType, "FIND");
    }
    
    /**
     * Creates an infrastructure exception for invalid hierarchy errors.
     * @param childType The type of the child component
     * @param parentType The type of the parent component
     * @return InfrastructureException configured for invalid hierarchy error
     */
    public static InfrastructureException invalidHierarchy(String childType, String parentType) {
        return new InfrastructureException("Cannot add " + childType + " to " + parentType + 
            ". Invalid component hierarchy.", null, parentType, "ADD_COMPONENT");
    }
}
