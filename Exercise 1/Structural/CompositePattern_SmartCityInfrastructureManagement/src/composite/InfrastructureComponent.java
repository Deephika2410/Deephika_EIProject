package composite;

import exceptions.InfrastructureException;
import java.util.List;

/**
 * Component interface in the Composite Design Pattern.
 * This interface defines the contract that all infrastructure components
 * (both leaf and composite) must implement.
 * 
 * Follows the Interface Segregation Principle by providing only
 * the essential operations needed by all components.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public interface InfrastructureComponent {
    
    /**
     * Gets the unique identifier of the component
     * @return String representing the component ID
     */
    String getId();
    
    /**
     * Gets the display name of the component
     * @return String representing the component name
     */
    String getName();
    
    /**
     * Gets the type of the component (City, District, Zone, etc.)
     * @return String representing the component type
     */
    String getType();
    
    /**
     * Displays the component information with proper indentation
     * for hierarchical representation
     * @param indentation String for formatting the display
     */
    void display(String indentation);
    
    /**
     * Calculates and returns the total energy consumption
     * of the component and all its children
     * @return double representing total energy consumption in kWh
     */
    double getEnergyConsumption();
    
    /**
     * Gets the operational status of the component
     * @return boolean true if operational, false otherwise
     */
    boolean isOperational();
    
    /**
     * Sets the operational status of the component
     * @param operational boolean status to set
     */
    void setOperational(boolean operational);
    
    /**
     * Performs maintenance operation on the component
     * This method should be implemented by all components
     * to handle their specific maintenance requirements
     */
    void performMaintenance();
    
    /**
     * Gets detailed information about the component
     * including its statistics and current state
     * @return String containing detailed component information
     */
    String getDetailedInfo();
    
    /**
     * Validates the component's current state and configuration
     * @throws InfrastructureException if validation fails
     */
    void validate() throws InfrastructureException;
}
