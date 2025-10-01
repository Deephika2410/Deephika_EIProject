package composite;

import exceptions.InfrastructureException;
import utils.Logger;
import utils.ValidationUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract base class for all composite infrastructure components.
 * This class provides common functionality for components that can
 * contain other components (City, District, Zone, Building, Floor).
 * 
 * Follows the Template Method pattern for common operations and
 * implements defensive programming practices.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public abstract class AbstractComposite implements CompositeComponent {
    
    protected final String id;
    protected final String name;
    protected final String type;
    protected boolean operational;
    protected final List<InfrastructureComponent> components;
    protected final Logger logger;
    
    /**
     * Constructor for creating a composite component
     * @param id Unique identifier for the component
     * @param name Display name for the component
     * @param type Type of the component
     * @throws InfrastructureException if parameters are invalid
     */
    protected AbstractComposite(String id, String name, String type) throws InfrastructureException {
        // Defensive programming - validate inputs
        ValidationUtils.validateNotNullOrEmpty(id, "Component ID");
        ValidationUtils.validateNotNullOrEmpty(name, "Component name");
        ValidationUtils.validateNotNullOrEmpty(type, "Component type");
        
        this.id = id.trim();
        this.name = name.trim();
        this.type = type.trim();
        this.operational = true;
        this.components = new CopyOnWriteArrayList<>(); // Thread-safe list
        this.logger = Logger.getInstance();
        
        logger.info("Created " + type + " component: " + name + " (ID: " + id + ")");
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getType() {
        return type;
    }
    
    @Override
    public boolean isOperational() {
        return operational;
    }
    
    @Override
    public void setOperational(boolean operational) {
        this.operational = operational;
        logger.info(type + " " + name + " operational status changed to: " + operational);
    }
    
    @Override
    public void addComponent(InfrastructureComponent component) throws InfrastructureException {
        if (component == null) {
            throw new InfrastructureException("Cannot add null component to " + getName());
        }
        
        // Prevent circular references
        if (component.getId().equals(this.getId())) {
            throw new InfrastructureException("Cannot add component to itself");
        }
        
        // Check if component already exists
        if (getComponent(component.getId()) != null) {
            throw new InfrastructureException("Component with ID " + component.getId() + 
                " already exists in " + getName());
        }
        
        // Validate component type compatibility
        if (!isValidChildType(component.getType())) {
            throw new InfrastructureException("Cannot add " + component.getType() + 
                " to " + getType() + ". Invalid hierarchy.");
        }
        
        components.add(component);
        logger.info("Added " + component.getType() + " '" + component.getName() + 
            "' to " + getType() + " '" + getName() + "'");
    }
    
    @Override
    public boolean removeComponent(String componentId) throws InfrastructureException {
        ValidationUtils.validateNotNullOrEmpty(componentId, "Component ID");
        
        boolean removed = components.removeIf(component -> component.getId().equals(componentId.trim()));
        
        if (removed) {
            logger.info("Removed component with ID '" + componentId + "' from " + 
                getType() + " '" + getName() + "'");
        } else {
            logger.warning("Component with ID '" + componentId + "' not found in " + 
                getType() + " '" + getName() + "'");
        }
        
        return removed;
    }
    
    @Override
    public InfrastructureComponent getComponent(String componentId) {
        if (componentId == null || componentId.trim().isEmpty()) {
            return null;
        }
        
        return components.stream()
            .filter(component -> component.getId().equals(componentId.trim()))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public List<InfrastructureComponent> getAllComponents() {
        // Return defensive copy to prevent external modification
        return new ArrayList<>(components);
    }
    
    @Override
    public int getComponentCount() {
        return components.size();
    }
    
    @Override
    public boolean hasComponents() {
        return !components.isEmpty();
    }
    
    @Override
    public int getTotalComponentCount() {
        int total = components.size();
        for (InfrastructureComponent component : components) {
            if (component instanceof CompositeComponent) {
                total += ((CompositeComponent) component).getTotalComponentCount();
            }
        }
        return total;
    }
    
    @Override
    public List<InfrastructureComponent> findComponentsByType(String type) {
        List<InfrastructureComponent> result = new ArrayList<>();
        
        if (type == null || type.trim().isEmpty()) {
            return result;
        }
        
        String searchType = type.trim();
        
        for (InfrastructureComponent component : components) {
            if (component.getType().equalsIgnoreCase(searchType)) {
                result.add(component);
            }
            
            // Recursively search in composite components
            if (component instanceof CompositeComponent) {
                result.addAll(((CompositeComponent) component).findComponentsByType(searchType));
            }
        }
        
        return result;
    }
    
    @Override
    public double getEnergyConsumption() {
        double totalConsumption = 0.0;
        
        for (InfrastructureComponent component : components) {
            totalConsumption += component.getEnergyConsumption();
        }
        
        return totalConsumption;
    }
    
    @Override
    public void display(String indentation) {
        String status = operational ? "[OPERATIONAL]" : "[OFFLINE]";
        System.out.println(indentation + getType() + ": " + getName() + 
            " (ID: " + getId() + ") " + status);
        System.out.println(indentation + "  Energy: " + String.format("%.2f", getEnergyConsumption()) + " kWh");
        System.out.println(indentation + "  Components: " + getComponentCount());
        
        // Display all child components
        for (InfrastructureComponent component : components) {
            component.display(indentation + "  ");
        }
    }
    
    @Override
    public void performMaintenance() {
        logger.info("Performing maintenance on " + getType() + " '" + getName() + "'");
        
        // Perform maintenance on all child components
        for (InfrastructureComponent component : components) {
            try {
                component.performMaintenance();
            } catch (Exception e) {
                logger.error("Maintenance failed for component " + component.getId() + ": " + e.getMessage());
            }
        }
        
        logger.info("Maintenance completed for " + getType() + " '" + getName() + "'");
    }
    
    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append(getType()).append(": ").append(getName())
            .append(" (ID: ").append(getId()).append(")\n")
            .append("Status: ").append(operational ? "Operational" : "Offline").append("\n")
            .append("Energy Consumption: ").append(String.format("%.2f", getEnergyConsumption())).append(" kWh\n")
            .append("Direct Components: ").append(getComponentCount()).append("\n")
            .append("Total Components: ").append(getTotalComponentCount()).append("\n")
            .append("Max Depth: ").append(getMaxDepth()).append("\n");
        
        return info.toString();
    }
    
    @Override
    public void validate() throws InfrastructureException {
        // Validate this component
        if (id == null || id.trim().isEmpty()) {
            throw new InfrastructureException("Component ID cannot be null or empty");
        }
        
        if (name == null || name.trim().isEmpty()) {
            throw new InfrastructureException("Component name cannot be null or empty");
        }
        
        if (type == null || type.trim().isEmpty()) {
            throw new InfrastructureException("Component type cannot be null or empty");
        }
    }
    
    @Override
    public void validateHierarchy() throws InfrastructureException {
        // Validate this component
        validate();
        
        // Validate all child components
        for (InfrastructureComponent component : components) {
            component.validate();
            
            if (component instanceof CompositeComponent) {
                ((CompositeComponent) component).validateHierarchy();
            }
        }
    }
    
    @Override
    public int getMaxDepth() {
        if (components.isEmpty()) {
            return 1;
        }
        
        int maxChildDepth = 0;
        for (InfrastructureComponent component : components) {
            int childDepth;
            if (component instanceof CompositeComponent) {
                childDepth = ((CompositeComponent) component).getMaxDepth();
            } else {
                childDepth = 1;
            }
            maxChildDepth = Math.max(maxChildDepth, childDepth);
        }
        
        return 1 + maxChildDepth;
    }
    
    /**
     * Abstract method to validate if a child component type is valid for this composite
     * @param childType The type of the child component
     * @return boolean true if valid, false otherwise
     */
    protected abstract boolean isValidChildType(String childType);
}
