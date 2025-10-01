package composite;

import exceptions.InfrastructureException;
import utils.Logger;
import java.util.List;

/**
 * Composite interface that extends the base component interface.
 * This interface defines operations specific to composite components
 * that can contain other components.
 * 
 * Follows the Interface Segregation Principle by separating
 * composite-specific operations from the base component operations.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public interface CompositeComponent extends InfrastructureComponent {
    
    /**
     * Adds a child component to this composite
     * @param component The component to add
     * @throws InfrastructureException if the component cannot be added
     */
    void addComponent(InfrastructureComponent component) throws InfrastructureException;
    
    /**
     * Removes a child component from this composite
     * @param componentId The ID of the component to remove
     * @return boolean true if component was removed, false if not found
     * @throws InfrastructureException if removal operation fails
     */
    boolean removeComponent(String componentId) throws InfrastructureException;
    
    /**
     * Gets a child component by its ID
     * @param componentId The ID of the component to retrieve
     * @return InfrastructureComponent if found, null otherwise
     */
    InfrastructureComponent getComponent(String componentId);
    
    /**
     * Gets all child components
     * @return List of all child components (defensive copy)
     */
    List<InfrastructureComponent> getAllComponents();
    
    /**
     * Gets the number of direct child components
     * @return int representing the count of child components
     */
    int getComponentCount();
    
    /**
     * Checks if this composite has any child components
     * @return boolean true if has children, false otherwise
     */
    boolean hasComponents();
    
    /**
     * Gets the total count of all components in the hierarchy
     * (including nested components)
     * @return int representing total component count
     */
    int getTotalComponentCount();
    
    /**
     * Finds components by type within the hierarchy
     * @param type The type of components to find
     * @return List of components matching the specified type
     */
    List<InfrastructureComponent> findComponentsByType(String type);
    
    /**
     * Performs a deep validation of this composite and all its children
     * @throws InfrastructureException if any validation fails
     */
    void validateHierarchy() throws InfrastructureException;
    
    /**
     * Gets the maximum depth of the component hierarchy
     * @return int representing the maximum depth
     */
    int getMaxDepth();
}
