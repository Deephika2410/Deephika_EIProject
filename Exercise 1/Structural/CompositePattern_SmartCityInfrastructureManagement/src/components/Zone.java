package components;

import composite.AbstractComposite;
import exceptions.InfrastructureException;
import utils.ValidationUtils;

/**
 * Zone class representing a specific functional area within a district.
 * A Zone can contain multiple Buildings and represents the third level
 * in the smart city hierarchy.
 * 
 * Zones are specialized areas with specific purposes such as shopping centers,
 * office complexes, residential neighborhoods, or industrial parks.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class Zone extends AbstractComposite {
    
    private static final String COMPONENT_TYPE = "Zone";
    private static final String[] VALID_CHILD_TYPES = {"Building"};
    
    private final String zoneType;
    private final String function;
    private final double area; // in square meters
    private final String securityLevel;
    private final boolean accessControlRequired;
    
    /**
     * Constructor for creating a Zone component
     * @param id Unique identifier for the zone
     * @param name Name of the zone
     * @param zoneType Type of zone (Residential, Office, Retail, Manufacturing, etc.)
     * @param function Primary function of the zone
     * @param area Area of the zone in square meters
     * @param securityLevel Security level required (Low, Medium, High, Critical)
     * @param accessControlRequired Whether access control is required
     * @throws InfrastructureException if parameters are invalid
     */
    public Zone(String id, String name, String zoneType, String function, 
               double area, String securityLevel, boolean accessControlRequired) 
               throws InfrastructureException {
        super(id, name, COMPONENT_TYPE);
        
        // Validate zone-specific parameters
        ValidationUtils.validateNotNullOrEmpty(zoneType, "Zone type");
        ValidationUtils.validateNotNullOrEmpty(function, "Zone function");
        ValidationUtils.validatePositive(area, "Area");
        ValidationUtils.validateNotNullOrEmpty(securityLevel, "Security level");
        
        // Validate zone type
        String[] validZoneTypes = {"Residential", "Office", "Retail", "Manufacturing", 
                                  "Warehouse", "Educational", "Healthcare", "Entertainment", 
                                  "Transportation", "Utilities", "Mixed"};
        ValidationUtils.validateComponentType(zoneType, validZoneTypes, "Zone type");
        
        // Validate security level
        String[] validSecurityLevels = {"Low", "Medium", "High", "Critical"};
        ValidationUtils.validateComponentType(securityLevel, validSecurityLevels, "Security level");
        
        this.zoneType = zoneType.trim();
        this.function = function.trim();
        this.area = area;
        this.securityLevel = securityLevel.trim();
        this.accessControlRequired = accessControlRequired;
        
        logger.info("Created " + zoneType + " zone: " + name + 
                   " (Function: " + function + ", Area: " + area + " sq m, Security: " + securityLevel + ")");
    }
    
    /**
     * Gets the type of the zone
     * @return String representing the zone type
     */
    public String getZoneType() {
        return zoneType;
    }
    
    /**
     * Gets the primary function of the zone
     * @return String representing the zone function
     */
    public String getFunction() {
        return function;
    }
    
    /**
     * Gets the area of the zone
     * @return double representing the area in square meters
     */
    public double getArea() {
        return area;
    }
    
    /**
     * Gets the security level of the zone
     * @return String representing the security level
     */
    public String getSecurityLevel() {
        return securityLevel;
    }
    
    /**
     * Checks if access control is required for the zone
     * @return boolean true if access control is required, false otherwise
     */
    public boolean isAccessControlRequired() {
        return accessControlRequired;
    }
    
    @Override
    protected boolean isValidChildType(String childType) {
        if (childType == null) {
            return false;
        }
        
        for (String validType : VALID_CHILD_TYPES) {
            if (validType.equalsIgnoreCase(childType.trim())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void display(String indentation) {
        String status = operational ? "[OPERATIONAL]" : "[OFFLINE]";
        String accessControl = accessControlRequired ? "[ACCESS CONTROLLED]" : "[OPEN ACCESS]";
        
        System.out.println(indentation + getType() + ": " + getName() + 
            " (ID: " + getId() + ") " + status + " " + accessControl);
        System.out.println(indentation + "  Type: " + getZoneType());
        System.out.println(indentation + "  Function: " + getFunction());
        System.out.println(indentation + "  Area: " + String.format("%.2f", getArea()) + " sq m");
        System.out.println(indentation + "  Security Level: " + getSecurityLevel());
        System.out.println(indentation + "  Energy: " + String.format("%.2f", getEnergyConsumption()) + " kWh");
        System.out.println(indentation + "  Buildings: " + getComponentCount());
        
        // Display all child components (buildings)
        for (var component : getAllComponents()) {
            component.display(indentation + "  ");
        }
    }
    
    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("ZONE INFORMATION\n");
        info.append("-".repeat(20)).append("\n");
        info.append("Name: ").append(getName()).append("\n");
        info.append("ID: ").append(getId()).append("\n");
        info.append("Type: ").append(getZoneType()).append("\n");
        info.append("Function: ").append(getFunction()).append("\n");
        info.append("Area: ").append(String.format("%.2f", getArea())).append(" sq m\n");
        info.append("Security Level: ").append(getSecurityLevel()).append("\n");
        info.append("Access Control: ").append(accessControlRequired ? "Required" : "Not Required").append("\n");
        info.append("Status: ").append(operational ? "Operational" : "Offline").append("\n");
        info.append("Energy Consumption: ").append(String.format("%.2f", getEnergyConsumption())).append(" kWh\n");
        info.append("Direct Buildings: ").append(getComponentCount()).append("\n");
        info.append("Total Components: ").append(getTotalComponentCount()).append("\n");
        
        // Calculate zone-specific metrics
        double energyDensity = getArea() > 0 ? getEnergyConsumption() / getArea() : 0;
        info.append("Energy Density: ").append(String.format("%.4f", energyDensity)).append(" kWh/sq m\n");
        
        // Zone utilization metrics
        String utilizationStatus = calculateUtilizationStatus();
        info.append("Utilization Status: ").append(utilizationStatus).append("\n");
        
        return info.toString();
    }
    
    @Override
    public void performMaintenance() {
        logger.info("Starting zone maintenance for " + getName() + " (" + getZoneType() + ")");
        
        // Perform zone-specific maintenance based on type and security level
        switch (zoneType.toLowerCase()) {
            case "residential":
                logger.info("Checking residential zone: HVAC systems, lighting, security cameras");
                break;
            case "office":
                logger.info("Checking office zone: network infrastructure, elevators, climate control");
                break;
            case "retail":
                logger.info("Checking retail zone: POS systems, lighting, customer WiFi, security");
                break;
            case "manufacturing":
                logger.info("Checking manufacturing zone: industrial equipment, safety systems, ventilation");
                break;
            case "warehouse":
                logger.info("Checking warehouse zone: inventory systems, loading docks, climate control");
                break;
            case "educational":
                logger.info("Checking educational zone: AV systems, network connectivity, safety equipment");
                break;
            case "healthcare":
                logger.info("Checking healthcare zone: medical equipment, backup power, air filtration");
                break;
            case "entertainment":
                logger.info("Checking entertainment zone: sound systems, lighting, crowd management");
                break;
            case "transportation":
                logger.info("Checking transportation zone: traffic systems, communication equipment");
                break;
            case "utilities":
                logger.info("Checking utilities zone: power distribution, monitoring systems, safety equipment");
                break;
            default:
                logger.info("Checking general zone infrastructure");
        }
        
        // Security-specific maintenance
        if (accessControlRequired) {
            logger.info("Performing access control system maintenance");
            switch (securityLevel.toLowerCase()) {
                case "critical":
                    logger.info("Running critical security diagnostics and biometric system checks");
                    break;
                case "high":
                    logger.info("Checking card readers, cameras, and alarm systems");
                    break;
                case "medium":
                    logger.info("Checking basic access controls and monitoring systems");
                    break;
                case "low":
                    logger.info("Checking perimeter security and basic monitoring");
                    break;
            }
        }
        
        // Call parent implementation to handle child components
        super.performMaintenance();
        
        logger.info("Zone maintenance completed for " + getName());
    }
    
    @Override
    public void validate() throws InfrastructureException {
        // Call parent validation
        super.validate();
        
        // Zone-specific validation
        if (zoneType == null || zoneType.trim().isEmpty()) {
            throw new InfrastructureException("Zone type cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (function == null || function.trim().isEmpty()) {
            throw new InfrastructureException("Zone function cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (securityLevel == null || securityLevel.trim().isEmpty()) {
            throw new InfrastructureException("Zone security level cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (area <= 0) {
            throw new InfrastructureException("Zone area must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate security level consistency
        validateSecurityConsistency();
        
        // Validate reasonable area limits
        if (area > 1000000) { // 1 million square meters
            logger.warning("Zone " + getName() + " has very large area: " + 
                String.format("%.2f", area) + " sq m");
        }
    }
    
    /**
     * Validates that security level is consistent with zone type and access control requirements
     * @throws InfrastructureException if security configuration is inconsistent
     */
    private void validateSecurityConsistency() throws InfrastructureException {
        // Critical security zones should require access control
        if ("Critical".equalsIgnoreCase(securityLevel) && !accessControlRequired) {
            logger.warning("Critical security zone " + getName() + 
                " should require access control");
        }
        
        // Certain zone types should have appropriate security levels
        switch (zoneType.toLowerCase()) {
            case "healthcare":
            case "utilities":
                if ("Low".equalsIgnoreCase(securityLevel)) {
                    logger.warning(zoneType + " zone " + getName() + 
                        " has low security level, which may be insufficient");
                }
                break;
            case "manufacturing":
            case "warehouse":
                if (!accessControlRequired && !"Low".equalsIgnoreCase(securityLevel)) {
                    logger.warning(zoneType + " zone " + getName() + 
                        " with " + securityLevel + " security should require access control");
                }
                break;
        }
    }
    
    /**
     * Gets the energy density of the zone (energy per square meter)
     * @return double representing energy consumption per square meter
     */
    public double getEnergyDensity() {
        return area > 0 ? getEnergyConsumption() / area : 0;
    }
    
    /**
     * Calculates the utilization status based on the number of buildings and area
     * @return String representing the utilization status
     */
    private String calculateUtilizationStatus() {
        if (getComponentCount() == 0) {
            return "Empty";
        }
        
        // Simple utilization calculation based on buildings per area
        double buildingDensity = getComponentCount() / (area / 1000); // buildings per 1000 sq m
        
        if (buildingDensity < 0.1) {
            return "Under-utilized";
        } else if (buildingDensity < 0.5) {
            return "Low Utilization";
        } else if (buildingDensity < 1.0) {
            return "Moderate Utilization";
        } else if (buildingDensity < 2.0) {
            return "High Utilization";
        } else {
            return "Over-utilized";
        }
    }
    
    /**
     * Gets zone statistics summary
     * @return String containing key zone statistics
     */
    public String getZoneStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("Zone Statistics for ").append(getName()).append(":\n");
        stats.append("  Type: ").append(getZoneType()).append("\n");
        stats.append("  Function: ").append(getFunction()).append("\n");
        stats.append("  Energy Density: ").append(String.format("%.4f", getEnergyDensity())).append(" kWh/sq m\n");
        stats.append("  Utilization: ").append(calculateUtilizationStatus()).append("\n");
        stats.append("  Security Level: ").append(getSecurityLevel()).append("\n");
        stats.append("  Access Control: ").append(accessControlRequired ? "Required" : "Not Required").append("\n");
        stats.append("  Total Buildings: ").append(getComponentCount()).append("\n");
        stats.append("  Infrastructure Components: ").append(getTotalComponentCount()).append("\n");
        
        return stats.toString();
    }
}
