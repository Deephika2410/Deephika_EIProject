package components;

import composite.AbstractComposite;
import exceptions.InfrastructureException;
import utils.ValidationUtils;

/**
 * Building class representing a physical structure within a zone.
 * A Building can contain multiple Floors and represents the fourth level
 * in the smart city hierarchy.
 * 
 * Buildings are physical structures that house various activities and
 * contain smart infrastructure components.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class Building extends AbstractComposite {
    
    private static final String COMPONENT_TYPE = "Building";
    private static final String[] VALID_CHILD_TYPES = {"Floor"};
    
    private final String buildingType;
    private final int numberOfFloors;
    private final double totalArea; // in square meters
    private final int yearBuilt;
    private final String architect;
    private final String energyRating;
    private final boolean hasElevator;
    private final boolean hasEmergencyExits;
    
    /**
     * Constructor for creating a Building component
     * @param id Unique identifier for the building
     * @param name Name of the building
     * @param buildingType Type of building (Office, Residential, Retail, etc.)
     * @param numberOfFloors Number of floors in the building
     * @param totalArea Total area of the building in square meters
     * @param yearBuilt Year when the building was constructed
     * @param architect Name of the architect or architectural firm
     * @param energyRating Energy efficiency rating (A+, A, B, C, D, E, F)
     * @param hasElevator Whether the building has elevator access
     * @param hasEmergencyExits Whether the building has proper emergency exits
     * @throws InfrastructureException if parameters are invalid
     */
    public Building(String id, String name, String buildingType, int numberOfFloors,
                   double totalArea, int yearBuilt, String architect, String energyRating,
                   boolean hasElevator, boolean hasEmergencyExits) throws InfrastructureException {
        super(id, name, COMPONENT_TYPE);
        
        // Validate building-specific parameters
        ValidationUtils.validateNotNullOrEmpty(buildingType, "Building type");
        ValidationUtils.validatePositive(numberOfFloors, "Number of floors");
        ValidationUtils.validatePositive(totalArea, "Total area");
        ValidationUtils.validateRange(yearBuilt, 1800, 2030, "Year built");
        ValidationUtils.validateNotNullOrEmpty(architect, "Architect");
        ValidationUtils.validateNotNullOrEmpty(energyRating, "Energy rating");
        
        // Validate building type
        String[] validBuildingTypes = {"Office", "Residential", "Retail", "Industrial", 
                                      "Warehouse", "Hospital", "School", "Hotel", 
                                      "Parking", "Mixed-Use", "Government"};
        ValidationUtils.validateComponentType(buildingType, validBuildingTypes, "Building type");
        
        // Validate energy rating
        String[] validEnergyRatings = {"A+", "A", "B", "C", "D", "E", "F"};
        ValidationUtils.validateComponentType(energyRating, validEnergyRatings, "Energy rating");
        
        this.buildingType = buildingType.trim();
        this.numberOfFloors = numberOfFloors;
        this.totalArea = totalArea;
        this.yearBuilt = yearBuilt;
        this.architect = architect.trim();
        this.energyRating = energyRating.trim();
        this.hasElevator = hasElevator;
        this.hasEmergencyExits = hasEmergencyExits;
        
        logger.info("Created " + buildingType + " building: " + name + 
                   " (" + numberOfFloors + " floors, " + totalArea + " sq m, Built: " + yearBuilt + ")");
    }
    
    /**
     * Gets the type of the building
     * @return String representing the building type
     */
    public String getBuildingType() {
        return buildingType;
    }
    
    /**
     * Gets the number of floors in the building
     * @return int representing the number of floors
     */
    public int getNumberOfFloors() {
        return numberOfFloors;
    }
    
    /**
     * Gets the total area of the building
     * @return double representing the total area in square meters
     */
    public double getTotalArea() {
        return totalArea;
    }
    
    /**
     * Gets the year the building was built
     * @return int representing the year built
     */
    public int getYearBuilt() {
        return yearBuilt;
    }
    
    /**
     * Gets the architect of the building
     * @return String representing the architect name
     */
    public String getArchitect() {
        return architect;
    }
    
    /**
     * Gets the energy rating of the building
     * @return String representing the energy rating
     */
    public String getEnergyRating() {
        return energyRating;
    }
    
    /**
     * Checks if the building has elevator access
     * @return boolean true if has elevator, false otherwise
     */
    public boolean hasElevator() {
        return hasElevator;
    }
    
    /**
     * Checks if the building has emergency exits
     * @return boolean true if has emergency exits, false otherwise
     */
    public boolean hasEmergencyExits() {
        return hasEmergencyExits;
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
        String accessibility = hasElevator ? "[ELEVATOR]" : "[NO ELEVATOR]";
        String safety = hasEmergencyExits ? "[SAFE]" : "[SAFETY ISSUE]";
        
        System.out.println(indentation + getType() + ": " + getName() + 
            " (ID: " + getId() + ") " + status + " " + accessibility + " " + safety);
        System.out.println(indentation + "  Type: " + getBuildingType());
        System.out.println(indentation + "  Floors: " + getNumberOfFloors());
        System.out.println(indentation + "  Total Area: " + String.format("%.2f", getTotalArea()) + " sq m");
        System.out.println(indentation + "  Built: " + getYearBuilt());
        System.out.println(indentation + "  Energy Rating: " + getEnergyRating());
        System.out.println(indentation + "  Architect: " + getArchitect());
        System.out.println(indentation + "  Energy: " + String.format("%.2f", getEnergyConsumption()) + " kWh");
        System.out.println(indentation + "  Floors Added: " + getComponentCount());
        
        // Display all child components (floors)
        for (var component : getAllComponents()) {
            component.display(indentation + "  ");
        }
    }
    
    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("BUILDING INFORMATION\n");
        info.append("-".repeat(25)).append("\n");
        info.append("Name: ").append(getName()).append("\n");
        info.append("ID: ").append(getId()).append("\n");
        info.append("Type: ").append(getBuildingType()).append("\n");
        info.append("Floors: ").append(getNumberOfFloors()).append("\n");
        info.append("Total Area: ").append(String.format("%.2f", getTotalArea())).append(" sq m\n");
        info.append("Year Built: ").append(getYearBuilt()).append("\n");
        info.append("Age: ").append(calculateBuildingAge()).append(" years\n");
        info.append("Architect: ").append(getArchitect()).append("\n");
        info.append("Energy Rating: ").append(getEnergyRating()).append("\n");
        info.append("Elevator: ").append(hasElevator ? "Available" : "Not Available").append("\n");
        info.append("Emergency Exits: ").append(hasEmergencyExits ? "Compliant" : "Non-Compliant").append("\n");
        info.append("Status: ").append(operational ? "Operational" : "Offline").append("\n");
        info.append("Energy Consumption: ").append(String.format("%.2f", getEnergyConsumption())).append(" kWh\n");
        info.append("Floors Configured: ").append(getComponentCount()).append("\n");
        info.append("Total Components: ").append(getTotalComponentCount()).append("\n");
        
        // Calculate building-specific metrics
        double energyPerArea = getTotalArea() > 0 ? getEnergyConsumption() / getTotalArea() : 0;
        info.append("Energy per Area: ").append(String.format("%.4f", energyPerArea)).append(" kWh/sq m\n");
        
        double averageFloorArea = getNumberOfFloors() > 0 ? getTotalArea() / getNumberOfFloors() : 0;
        info.append("Average Floor Area: ").append(String.format("%.2f", averageFloorArea)).append(" sq m\n");
        
        // Building condition assessment
        String conditionRating = calculateConditionRating();
        info.append("Condition Rating: ").append(conditionRating).append("\n");
        
        return info.toString();
    }
    
    @Override
    public void performMaintenance() {
        logger.info("Starting building maintenance for " + getName() + " (" + getBuildingType() + ")");
        
        // Perform building-specific maintenance
        logger.info("Checking building envelope and structural integrity");
        logger.info("Inspecting HVAC systems and energy efficiency");
        logger.info("Testing fire safety and emergency systems");
        
        // Elevator maintenance
        if (hasElevator) {
            logger.info("Performing elevator maintenance and safety checks");
        }
        
        // Emergency exit inspection
        if (hasEmergencyExits) {
            logger.info("Inspecting emergency exits and escape routes");
        } else {
            logger.warning("Building " + getName() + " lacks proper emergency exits - SAFETY CONCERN");
        }
        
        // Building type specific maintenance
        switch (buildingType.toLowerCase()) {
            case "office":
                logger.info("Checking office infrastructure: network, lighting, climate control");
                break;
            case "residential":
                logger.info("Checking residential infrastructure: plumbing, electrical, security");
                break;
            case "retail":
                logger.info("Checking retail infrastructure: POS connectivity, customer amenities");
                break;
            case "industrial":
                logger.info("Checking industrial infrastructure: power distribution, safety systems");
                break;
            case "hospital":
                logger.info("Checking hospital infrastructure: medical gases, backup power, isolation systems");
                break;
            case "school":
                logger.info("Checking educational infrastructure: AV systems, safety equipment, accessibility");
                break;
            case "hotel":
                logger.info("Checking hotel infrastructure: guest services, security, amenities");
                break;
            default:
                logger.info("Checking general building infrastructure");
        }
        
        // Age-based maintenance
        int age = calculateBuildingAge();
        if (age > 50) {
            logger.info("Performing extensive maintenance for historic building (Age: " + age + " years)");
        } else if (age > 20) {
            logger.info("Performing standard maintenance for mature building (Age: " + age + " years)");
        } else {
            logger.info("Performing routine maintenance for modern building (Age: " + age + " years)");
        }
        
        // Call parent implementation to handle child components
        super.performMaintenance();
        
        logger.info("Building maintenance completed for " + getName());
    }
    
    @Override
    public void validate() throws InfrastructureException {
        // Call parent validation
        super.validate();
        
        // Building-specific validation
        if (buildingType == null || buildingType.trim().isEmpty()) {
            throw new InfrastructureException("Building type cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (numberOfFloors <= 0) {
            throw new InfrastructureException("Number of floors must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        if (totalArea <= 0) {
            throw new InfrastructureException("Total area must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        if (yearBuilt < 1800 || yearBuilt > 2030) {
            throw new InfrastructureException("Year built must be between 1800 and 2030", 
                getId(), getType(), "VALIDATE");
        }
        
        if (architect == null || architect.trim().isEmpty()) {
            throw new InfrastructureException("Architect cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (energyRating == null || energyRating.trim().isEmpty()) {
            throw new InfrastructureException("Energy rating cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        // Safety validation
        if (!hasEmergencyExits) {
            logger.error("SAFETY VIOLATION: Building " + getName() + " lacks emergency exits");
            throw new InfrastructureException("Building must have emergency exits for safety compliance", 
                getId(), getType(), "SAFETY_VALIDATE");
        }
        
        // Accessibility validation for multi-story buildings
        if (numberOfFloors > 3 && !hasElevator) {
            logger.warning("ACCESSIBILITY CONCERN: Building " + getName() + 
                " has " + numberOfFloors + " floors but no elevator");
        }
        
        // Validate floor count matches configured floors
        if (getComponentCount() > numberOfFloors) {
            throw new InfrastructureException("Configured floors (" + getComponentCount() + 
                ") exceeds building floor count (" + numberOfFloors + ")", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate reasonable area per floor
        double avgFloorArea = totalArea / numberOfFloors;
        if (avgFloorArea < 10) { // Very small floor area
            logger.warning("Building " + getName() + " has very small average floor area: " + 
                String.format("%.2f", avgFloorArea) + " sq m");
        } else if (avgFloorArea > 10000) { // Very large floor area
            logger.warning("Building " + getName() + " has very large average floor area: " + 
                String.format("%.2f", avgFloorArea) + " sq m");
        }
    }
    
    /**
     * Calculates the age of the building
     * @return int representing the building age in years
     */
    public int calculateBuildingAge() {
        return java.time.Year.now().getValue() - yearBuilt;
    }
    
    /**
     * Gets the energy consumption per square meter
     * @return double representing energy consumption per square meter
     */
    public double getEnergyPerArea() {
        return totalArea > 0 ? getEnergyConsumption() / totalArea : 0;
    }
    
    /**
     * Gets the average floor area
     * @return double representing average area per floor
     */
    public double getAverageFloorArea() {
        return numberOfFloors > 0 ? totalArea / numberOfFloors : 0;
    }
    
    /**
     * Calculates a condition rating based on age, energy rating, and maintenance status
     * @return String representing the building condition
     */
    private String calculateConditionRating() {
        int age = calculateBuildingAge();
        
        // Base score from energy rating
        int energyScore = 0;
        switch (energyRating) {
            case "A+": energyScore = 10; break;
            case "A": energyScore = 9; break;
            case "B": energyScore = 7; break;
            case "C": energyScore = 5; break;
            case "D": energyScore = 3; break;
            case "E": energyScore = 2; break;
            case "F": energyScore = 1; break;
        }
        
        // Age penalty
        int ageScore = 10;
        if (age > 50) ageScore = 3;
        else if (age > 30) ageScore = 5;
        else if (age > 15) ageScore = 7;
        else if (age > 5) ageScore = 9;
        
        // Safety bonus/penalty
        int safetyScore = 5;
        if (hasEmergencyExits && hasElevator) safetyScore = 10;
        else if (hasEmergencyExits) safetyScore = 8;
        else safetyScore = 2;
        
        int totalScore = (energyScore + ageScore + safetyScore) / 3;
        
        if (totalScore >= 9) return "Excellent";
        else if (totalScore >= 7) return "Good";
        else if (totalScore >= 5) return "Fair";
        else if (totalScore >= 3) return "Poor";
        else return "Critical";
    }
    
    /**
     * Gets building statistics summary
     * @return String containing key building statistics
     */
    public String getBuildingStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("Building Statistics for ").append(getName()).append(":\n");
        stats.append("  Type: ").append(getBuildingType()).append("\n");
        stats.append("  Age: ").append(calculateBuildingAge()).append(" years\n");
        stats.append("  Energy Rating: ").append(getEnergyRating()).append("\n");
        stats.append("  Energy per Area: ").append(String.format("%.4f", getEnergyPerArea())).append(" kWh/sq m\n");
        stats.append("  Average Floor Area: ").append(String.format("%.2f", getAverageFloorArea())).append(" sq m\n");
        stats.append("  Condition Rating: ").append(calculateConditionRating()).append("\n");
        stats.append("  Accessibility: ").append(hasElevator ? "Elevator Available" : "No Elevator").append("\n");
        stats.append("  Safety: ").append(hasEmergencyExits ? "Emergency Exits Compliant" : "Safety Non-Compliant").append("\n");
        stats.append("  Floors Configured: ").append(getComponentCount()).append("/").append(getNumberOfFloors()).append("\n");
        stats.append("  Infrastructure Components: ").append(getTotalComponentCount()).append("\n");
        
        return stats.toString();
    }
}
