package components;

import composite.AbstractComposite;
import exceptions.InfrastructureException;
import utils.ValidationUtils;

/**
 * District class representing a major administrative division within a city.
 * A District can contain multiple Zones and represents the second level
 * in the smart city hierarchy.
 * 
 * Follows the Single Responsibility Principle by handling district-specific
 * operations and delegating common composite behavior to the parent class.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class District extends AbstractComposite {
    
    private static final String COMPONENT_TYPE = "District";
    private static final String[] VALID_CHILD_TYPES = {"Zone"};
    
    private final String districtType;
    private final long population;
    private final double area; // in square kilometers
    private final String administrator;
    
    /**
     * Constructor for creating a District component
     * @param id Unique identifier for the district
     * @param name Name of the district
     * @param districtType Type of district (Residential, Commercial, Industrial, Mixed)
     * @param population Population of the district
     * @param area Area of the district in square kilometers
     * @param administrator Name of the district administrator
     * @throws InfrastructureException if parameters are invalid
     */
    public District(String id, String name, String districtType, long population, 
                   double area, String administrator) throws InfrastructureException {
        super(id, name, COMPONENT_TYPE);
        
        // Validate district-specific parameters
        ValidationUtils.validateNotNullOrEmpty(districtType, "District type");
        ValidationUtils.validatePositive(population, "Population");
        ValidationUtils.validatePositive(area, "Area");
        ValidationUtils.validateNotNullOrEmpty(administrator, "Administrator");
        
        // Validate district type
        String[] validTypes = {"Residential", "Commercial", "Industrial", "Mixed", "Educational", "Healthcare"};
        ValidationUtils.validateComponentType(districtType, validTypes, "District type");
        
        this.districtType = districtType.trim();
        this.population = population;
        this.area = area;
        this.administrator = administrator.trim();
        
        logger.info("Created " + districtType + " district: " + name + 
                   " (Population: " + population + ", Area: " + area + " sq km)");
    }
    
    /**
     * Gets the type of the district
     * @return String representing the district type
     */
    public String getDistrictType() {
        return districtType;
    }
    
    /**
     * Gets the population of the district
     * @return long representing the population count
     */
    public long getPopulation() {
        return population;
    }
    
    /**
     * Gets the area of the district
     * @return double representing the area in square kilometers
     */
    public double getArea() {
        return area;
    }
    
    /**
     * Gets the administrator of the district
     * @return String representing the administrator name
     */
    public String getAdministrator() {
        return administrator;
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
        System.out.println(indentation + getType() + ": " + getName() + 
            " (ID: " + getId() + ") " + status);
        System.out.println(indentation + "  Type: " + getDistrictType());
        System.out.println(indentation + "  Population: " + String.format("%,d", getPopulation()));
        System.out.println(indentation + "  Area: " + String.format("%.2f", getArea()) + " sq km");
        System.out.println(indentation + "  Administrator: " + getAdministrator());
        System.out.println(indentation + "  Energy: " + String.format("%.2f", getEnergyConsumption()) + " kWh");
        System.out.println(indentation + "  Zones: " + getComponentCount());
        
        // Display all child components (zones)
        for (var component : getAllComponents()) {
            component.display(indentation + "  ");
        }
    }
    
    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("DISTRICT INFORMATION\n");
        info.append("-".repeat(30)).append("\n");
        info.append("Name: ").append(getName()).append("\n");
        info.append("ID: ").append(getId()).append("\n");
        info.append("Type: ").append(getDistrictType()).append("\n");
        info.append("Administrator: ").append(getAdministrator()).append("\n");
        info.append("Population: ").append(String.format("%,d", getPopulation())).append("\n");
        info.append("Area: ").append(String.format("%.2f", getArea())).append(" sq km\n");
        info.append("Status: ").append(operational ? "Operational" : "Offline").append("\n");
        info.append("Energy Consumption: ").append(String.format("%.2f", getEnergyConsumption())).append(" kWh\n");
        info.append("Direct Zones: ").append(getComponentCount()).append("\n");
        info.append("Total Components: ").append(getTotalComponentCount()).append("\n");
        
        // Calculate district-specific metrics
        double density = getPopulation() / getArea();
        info.append("Population Density: ").append(String.format("%.2f", density)).append(" people/sq km\n");
        
        if (getPopulation() > 0) {
            double energyPerCapita = getEnergyConsumption() / getPopulation();
            info.append("Energy per Capita: ").append(String.format("%.4f", energyPerCapita)).append(" kWh/person\n");
        }
        
        // District efficiency rating
        String efficiencyRating = calculateEfficiencyRating();
        info.append("Efficiency Rating: ").append(efficiencyRating).append("\n");
        
        return info.toString();
    }
    
    @Override
    public void performMaintenance() {
        logger.info("Starting district maintenance for " + getName() + " (" + getDistrictType() + ")");
        
        // Perform district-specific maintenance based on type
        switch (districtType.toLowerCase()) {
            case "residential":
                logger.info("Checking residential infrastructure: water, electricity, waste management");
                break;
            case "commercial":
                logger.info("Checking commercial infrastructure: traffic systems, parking, security");
                break;
            case "industrial":
                logger.info("Checking industrial infrastructure: power grid, waste treatment, safety systems");
                break;
            case "mixed":
                logger.info("Checking mixed-use infrastructure: integrated systems, zoning compliance");
                break;
            case "educational":
                logger.info("Checking educational infrastructure: network connectivity, safety systems");
                break;
            case "healthcare":
                logger.info("Checking healthcare infrastructure: emergency systems, backup power");
                break;
            default:
                logger.info("Checking general district infrastructure");
        }
        
        // Call parent implementation to handle child components
        super.performMaintenance();
        
        logger.info("District maintenance completed for " + getName());
    }
    
    @Override
    public void validate() throws InfrastructureException {
        // Call parent validation
        super.validate();
        
        // District-specific validation
        if (districtType == null || districtType.trim().isEmpty()) {
            throw new InfrastructureException("District type cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (administrator == null || administrator.trim().isEmpty()) {
            throw new InfrastructureException("District administrator cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (population <= 0) {
            throw new InfrastructureException("District population must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        if (area <= 0) {
            throw new InfrastructureException("District area must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate population density is reasonable for district type
        double density = population / area;
        validatePopulationDensityForType(density);
    }
    
    /**
     * Validates population density based on district type
     * @param density The population density to validate
     * @throws InfrastructureException if density is unreasonable for the district type
     */
    private void validatePopulationDensityForType(double density) throws InfrastructureException {
        switch (districtType.toLowerCase()) {
            case "residential":
                if (density > 20000) {
                    logger.warning("Residential district " + getName() + 
                        " has very high density: " + String.format("%.2f", density));
                }
                break;
            case "commercial":
                if (density > 5000) {
                    logger.warning("Commercial district " + getName() + 
                        " has high residential density: " + String.format("%.2f", density));
                }
                break;
            case "industrial":
                if (density > 1000) {
                    logger.warning("Industrial district " + getName() + 
                        " has high residential density: " + String.format("%.2f", density));
                }
                break;
        }
    }
    
    /**
     * Gets the population density of the district
     * @return double representing population per square kilometer
     */
    public double getPopulationDensity() {
        return area > 0 ? population / area : 0;
    }
    
    /**
     * Gets the energy consumption per capita for the district
     * @return double representing energy consumption per person
     */
    public double getEnergyConsumptionPerCapita() {
        return population > 0 ? getEnergyConsumption() / population : 0;
    }
    
    /**
     * Calculates an efficiency rating for the district
     * @return String representing the efficiency rating
     */
    private String calculateEfficiencyRating() {
        double energyPerCapita = getEnergyConsumptionPerCapita();
        
        if (energyPerCapita == 0) {
            return "No Data";
        } else if (energyPerCapita < 2.0) {
            return "Excellent";
        } else if (energyPerCapita < 5.0) {
            return "Good";
        } else if (energyPerCapita < 10.0) {
            return "Fair";
        } else {
            return "Needs Improvement";
        }
    }
    
    /**
     * Gets district statistics summary
     * @return String containing key district statistics
     */
    public String getDistrictStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("District Statistics for ").append(getName()).append(":\n");
        stats.append("  Type: ").append(getDistrictType()).append("\n");
        stats.append("  Population Density: ").append(String.format("%.2f", getPopulationDensity())).append(" people/sq km\n");
        stats.append("  Energy per Capita: ").append(String.format("%.4f", getEnergyConsumptionPerCapita())).append(" kWh/person\n");
        stats.append("  Efficiency Rating: ").append(calculateEfficiencyRating()).append("\n");
        stats.append("  Total Zones: ").append(getComponentCount()).append("\n");
        stats.append("  Infrastructure Components: ").append(getTotalComponentCount()).append("\n");
        stats.append("  Administrator: ").append(getAdministrator()).append("\n");
        
        return stats.toString();
    }
}
