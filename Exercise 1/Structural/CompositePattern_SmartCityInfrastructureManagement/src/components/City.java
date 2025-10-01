package components;

import composite.AbstractComposite;
import exceptions.InfrastructureException;
import utils.ValidationUtils;

/**
 * City class representing the top-level component in the smart city hierarchy.
 * A City can contain multiple Districts and serves as the root composite
 * in the Composite Design Pattern.
 * 
 * Follows the Open/Closed Principle by extending AbstractComposite
 * and implementing city-specific behavior.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class City extends AbstractComposite {
    
    private static final String COMPONENT_TYPE = "City";
    private static final String[] VALID_CHILD_TYPES = {"District"};
    
    private final String country;
    private final String state;
    private final long population;
    private final double area; // in square kilometers
    
    /**
     * Constructor for creating a City component
     * @param id Unique identifier for the city
     * @param name Name of the city
     * @param country Country where the city is located
     * @param state State/Province where the city is located
     * @param population Population of the city
     * @param area Area of the city in square kilometers
     * @throws InfrastructureException if parameters are invalid
     */
    public City(String id, String name, String country, String state, 
                long population, double area) throws InfrastructureException {
        super(id, name, COMPONENT_TYPE);
        
        // Validate additional city-specific parameters
        ValidationUtils.validateNotNullOrEmpty(country, "Country");
        ValidationUtils.validateNotNullOrEmpty(state, "State");
        ValidationUtils.validatePositive(population, "Population");
        ValidationUtils.validatePositive(area, "Area");
        
        this.country = country.trim();
        this.state = state.trim();
        this.population = population;
        this.area = area;
        
        logger.info("Created city: " + name + " in " + state + ", " + country);
    }
    
    /**
     * Gets the country where the city is located
     * @return String representing the country name
     */
    public String getCountry() {
        return country;
    }
    
    /**
     * Gets the state/province where the city is located
     * @return String representing the state name
     */
    public String getState() {
        return state;
    }
    
    /**
     * Gets the population of the city
     * @return long representing the population count
     */
    public long getPopulation() {
        return population;
    }
    
    /**
     * Gets the area of the city
     * @return double representing the area in square kilometers
     */
    public double getArea() {
        return area;
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
        System.out.println(indentation + "  Location: " + getState() + ", " + getCountry());
        System.out.println(indentation + "  Population: " + String.format("%,d", getPopulation()));
        System.out.println(indentation + "  Area: " + String.format("%.2f", getArea()) + " sq km");
        System.out.println(indentation + "  Energy: " + String.format("%.2f", getEnergyConsumption()) + " kWh");
        System.out.println(indentation + "  Districts: " + getComponentCount());
        
        // Display all child components (districts)
        for (var component : getAllComponents()) {
            component.display(indentation + "  ");
        }
    }
    
    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=".repeat(50)).append("\n");
        info.append("CITY INFORMATION\n");
        info.append("=".repeat(50)).append("\n");
        info.append("Name: ").append(getName()).append("\n");
        info.append("ID: ").append(getId()).append("\n");
        info.append("Location: ").append(getState()).append(", ").append(getCountry()).append("\n");
        info.append("Population: ").append(String.format("%,d", getPopulation())).append("\n");
        info.append("Area: ").append(String.format("%.2f", getArea())).append(" sq km\n");
        info.append("Status: ").append(operational ? "Operational" : "Offline").append("\n");
        info.append("Energy Consumption: ").append(String.format("%.2f", getEnergyConsumption())).append(" kWh\n");
        info.append("Direct Districts: ").append(getComponentCount()).append("\n");
        info.append("Total Components: ").append(getTotalComponentCount()).append("\n");
        info.append("Hierarchy Depth: ").append(getMaxDepth()).append("\n");
        
        // Calculate population density
        double density = getPopulation() / getArea();
        info.append("Population Density: ").append(String.format("%.2f", density)).append(" people/sq km\n");
        
        // Calculate energy efficiency
        if (getPopulation() > 0) {
            double energyPerCapita = getEnergyConsumption() / getPopulation();
            info.append("Energy per Capita: ").append(String.format("%.4f", energyPerCapita)).append(" kWh/person\n");
        }
        
        info.append("=".repeat(50)).append("\n");
        
        return info.toString();
    }
    
    @Override
    public void performMaintenance() {
        logger.info("Starting city-wide maintenance for " + getName());
        
        // Perform city-specific maintenance tasks
        logger.info("Checking city infrastructure systems...");
        logger.info("Updating city databases...");
        logger.info("Synchronizing district communications...");
        
        // Call parent implementation to handle child components
        super.performMaintenance();
        
        logger.info("City-wide maintenance completed for " + getName());
    }
    
    @Override
    public void validate() throws InfrastructureException {
        // Call parent validation
        super.validate();
        
        // City-specific validation
        if (country == null || country.trim().isEmpty()) {
            throw new InfrastructureException("City country cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (state == null || state.trim().isEmpty()) {
            throw new InfrastructureException("City state cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (population <= 0) {
            throw new InfrastructureException("City population must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        if (area <= 0) {
            throw new InfrastructureException("City area must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate population density is reasonable (not too high or too low)
        double density = population / area;
        if (density > 50000) { // Very high density threshold
            logger.warning("City " + getName() + " has very high population density: " + 
                String.format("%.2f", density) + " people/sq km");
        } else if (density < 1) { // Very low density threshold
            logger.warning("City " + getName() + " has very low population density: " + 
                String.format("%.2f", density) + " people/sq km");
        }
    }
    
    /**
     * Gets the population density of the city
     * @return double representing population per square kilometer
     */
    public double getPopulationDensity() {
        return area > 0 ? population / area : 0;
    }
    
    /**
     * Gets the energy consumption per capita
     * @return double representing energy consumption per person
     */
    public double getEnergyConsumptionPerCapita() {
        return population > 0 ? getEnergyConsumption() / population : 0;
    }
    
    /**
     * Gets city statistics summary
     * @return String containing key city statistics
     */
    public String getCityStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("City Statistics for ").append(getName()).append(":\n");
        stats.append("  Population Density: ").append(String.format("%.2f", getPopulationDensity())).append(" people/sq km\n");
        stats.append("  Energy per Capita: ").append(String.format("%.4f", getEnergyConsumptionPerCapita())).append(" kWh/person\n");
        stats.append("  Total Districts: ").append(getComponentCount()).append("\n");
        stats.append("  Infrastructure Components: ").append(getTotalComponentCount()).append("\n");
        stats.append("  System Status: ").append(operational ? "Operational" : "Offline").append("\n");
        
        return stats.toString();
    }
}
