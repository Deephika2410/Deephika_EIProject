package components;

import composite.AbstractComposite;
import exceptions.InfrastructureException;
import utils.ValidationUtils;

/**
 * Floor class representing a single level within a building.
 * A Floor can contain multiple Devices and represents the fifth level
 * in the smart city hierarchy.
 * 
 * Floors are horizontal divisions of buildings that contain various
 * smart devices and systems.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class Floor extends AbstractComposite {
    
    private static final String COMPONENT_TYPE = "Floor";
    private static final String[] VALID_CHILD_TYPES = {"SmartLight", "AirConditioner", "Sensor", "Device"};
    
    private final int floorNumber;
    private final String floorType;
    private final double area; // in square meters
    private final int numberOfRooms;
    private final double ceilingHeight; // in meters
    private final String hvacZone;
    private final boolean hasFireSuppressionSystem;
    private final boolean hasEmergencyLighting;
    private final int occupancyLimit;
    
    /**
     * Constructor for creating a Floor component
     * @param id Unique identifier for the floor
     * @param name Name of the floor
     * @param floorNumber Floor number (0 for ground floor, negative for basement)
     * @param floorType Type of floor (Office, Residential, Retail, etc.)
     * @param area Area of the floor in square meters
     * @param numberOfRooms Number of rooms on the floor
     * @param ceilingHeight Height of the ceiling in meters
     * @param hvacZone HVAC zone identifier
     * @param hasFireSuppressionSystem Whether floor has fire suppression system
     * @param hasEmergencyLighting Whether floor has emergency lighting
     * @param occupancyLimit Maximum occupancy limit for the floor
     * @throws InfrastructureException if parameters are invalid
     */
    public Floor(String id, String name, int floorNumber, String floorType,
                double area, int numberOfRooms, double ceilingHeight, String hvacZone,
                boolean hasFireSuppressionSystem, boolean hasEmergencyLighting,
                int occupancyLimit) throws InfrastructureException {
        super(id, name, COMPONENT_TYPE);
        
        // Validate floor-specific parameters
        ValidationUtils.validateNotNullOrEmpty(floorType, "Floor type");
        ValidationUtils.validatePositive(area, "Area");
        ValidationUtils.validatePositive(numberOfRooms, "Number of rooms");
        ValidationUtils.validatePositive(ceilingHeight, "Ceiling height");
        ValidationUtils.validateNotNullOrEmpty(hvacZone, "HVAC zone");
        ValidationUtils.validatePositive(occupancyLimit, "Occupancy limit");
        
        // Validate floor type
        String[] validFloorTypes = {"Office", "Residential", "Retail", "Industrial", 
                                   "Storage", "Parking", "Mechanical", "Lobby", 
                                   "Restaurant", "Conference", "Laboratory", "Medical"};
        ValidationUtils.validateComponentType(floorType, validFloorTypes, "Floor type");
        
        // Validate reasonable values
        ValidationUtils.validateRange(ceilingHeight, 2.0, 10.0, "Ceiling height");
        ValidationUtils.validateRange(floorNumber, -10, 200, "Floor number");
        
        this.floorNumber = floorNumber;
        this.floorType = floorType.trim();
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.ceilingHeight = ceilingHeight;
        this.hvacZone = hvacZone.trim();
        this.hasFireSuppressionSystem = hasFireSuppressionSystem;
        this.hasEmergencyLighting = hasEmergencyLighting;
        this.occupancyLimit = occupancyLimit;
        
        logger.info("Created " + floorType + " floor: " + name + 
                   " (Floor " + floorNumber + ", " + area + " sq m, " + 
                   numberOfRooms + " rooms, HVAC Zone: " + hvacZone + ")");
    }
    
    /**
     * Gets the floor number
     * @return int representing the floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }
    
    /**
     * Gets the type of the floor
     * @return String representing the floor type
     */
    public String getFloorType() {
        return floorType;
    }
    
    /**
     * Gets the area of the floor
     * @return double representing the area in square meters
     */
    public double getArea() {
        return area;
    }
    
    /**
     * Gets the number of rooms on the floor
     * @return int representing the number of rooms
     */
    public int getNumberOfRooms() {
        return numberOfRooms;
    }
    
    /**
     * Gets the ceiling height
     * @return double representing the ceiling height in meters
     */
    public double getCeilingHeight() {
        return ceilingHeight;
    }
    
    /**
     * Gets the HVAC zone identifier
     * @return String representing the HVAC zone
     */
    public String getHvacZone() {
        return hvacZone;
    }
    
    /**
     * Checks if the floor has a fire suppression system
     * @return boolean true if has fire suppression, false otherwise
     */
    public boolean hasFireSuppressionSystem() {
        return hasFireSuppressionSystem;
    }
    
    /**
     * Checks if the floor has emergency lighting
     * @return boolean true if has emergency lighting, false otherwise
     */
    public boolean hasEmergencyLighting() {
        return hasEmergencyLighting;
    }
    
    /**
     * Gets the occupancy limit for the floor
     * @return int representing the maximum occupancy
     */
    public int getOccupancyLimit() {
        return occupancyLimit;
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
        String safety = (hasFireSuppressionSystem && hasEmergencyLighting) ? "[SAFE]" : "[SAFETY CONCERN]";
        
        System.out.println(indentation + getType() + ": " + getName() + 
            " (ID: " + getId() + ") " + status + " " + safety);
        System.out.println(indentation + "  Floor Number: " + getFloorNumber());
        System.out.println(indentation + "  Type: " + getFloorType());
        System.out.println(indentation + "  Area: " + String.format("%.2f", getArea()) + " sq m");
        System.out.println(indentation + "  Rooms: " + getNumberOfRooms());
        System.out.println(indentation + "  Ceiling Height: " + String.format("%.2f", getCeilingHeight()) + " m");
        System.out.println(indentation + "  HVAC Zone: " + getHvacZone());
        System.out.println(indentation + "  Occupancy Limit: " + getOccupancyLimit());
        System.out.println(indentation + "  Energy: " + String.format("%.2f", getEnergyConsumption()) + " kWh");
        System.out.println(indentation + "  Devices: " + getComponentCount());
        
        // Display all child components (devices)
        for (var component : getAllComponents()) {
            component.display(indentation + "  ");
        }
    }
    
    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("FLOOR INFORMATION\n");
        info.append("-".repeat(20)).append("\n");
        info.append("Name: ").append(getName()).append("\n");
        info.append("ID: ").append(getId()).append("\n");
        info.append("Floor Number: ").append(getFloorNumber()).append("\n");
        info.append("Type: ").append(getFloorType()).append("\n");
        info.append("Area: ").append(String.format("%.2f", getArea())).append(" sq m\n");
        info.append("Rooms: ").append(getNumberOfRooms()).append("\n");
        info.append("Ceiling Height: ").append(String.format("%.2f", getCeilingHeight())).append(" m\n");
        info.append("HVAC Zone: ").append(getHvacZone()).append("\n");
        info.append("Fire Suppression: ").append(hasFireSuppressionSystem ? "Available" : "Not Available").append("\n");
        info.append("Emergency Lighting: ").append(hasEmergencyLighting ? "Available" : "Not Available").append("\n");
        info.append("Occupancy Limit: ").append(getOccupancyLimit()).append(" people\n");
        info.append("Status: ").append(operational ? "Operational" : "Offline").append("\n");
        info.append("Energy Consumption: ").append(String.format("%.2f", getEnergyConsumption())).append(" kWh\n");
        info.append("Devices Installed: ").append(getComponentCount()).append("\n");
        info.append("Total Components: ").append(getTotalComponentCount()).append("\n");
        
        // Calculate floor-specific metrics
        double energyPerArea = getArea() > 0 ? getEnergyConsumption() / getArea() : 0;
        info.append("Energy Density: ").append(String.format("%.4f", energyPerArea)).append(" kWh/sq m\n");
        
        double areaPerRoom = getNumberOfRooms() > 0 ? getArea() / getNumberOfRooms() : 0;
        info.append("Average Room Size: ").append(String.format("%.2f", areaPerRoom)).append(" sq m\n");
        
        double volume = getArea() * getCeilingHeight();
        info.append("Floor Volume: ").append(String.format("%.2f", volume)).append(" cubic m\n");
        
        // Safety rating
        String safetyRating = calculateSafetyRating();
        info.append("Safety Rating: ").append(safetyRating).append("\n");
        
        return info.toString();
    }
    
    @Override
    public void performMaintenance() {
        logger.info("Starting floor maintenance for " + getName() + " (Floor " + floorNumber + ")");
        
        // Floor-specific maintenance
        logger.info("Checking floor surfaces and structural elements");
        logger.info("Inspecting HVAC systems in zone: " + hvacZone);
        
        // Safety system maintenance
        if (hasFireSuppressionSystem) {
            logger.info("Testing fire suppression system");
        } else {
            logger.warning("Floor " + getName() + " lacks fire suppression system");
        }
        
        if (hasEmergencyLighting) {
            logger.info("Testing emergency lighting system");
        } else {
            logger.warning("Floor " + getName() + " lacks emergency lighting");
        }
        
        // Floor type specific maintenance
        switch (floorType.toLowerCase()) {
            case "office":
                logger.info("Checking office infrastructure: workstation power, network connectivity");
                break;
            case "residential":
                logger.info("Checking residential infrastructure: utilities, safety systems");
                break;
            case "retail":
                logger.info("Checking retail infrastructure: customer lighting, POS connectivity");
                break;
            case "industrial":
                logger.info("Checking industrial infrastructure: power distribution, ventilation");
                break;
            case "storage":
                logger.info("Checking storage infrastructure: climate control, security systems");
                break;
            case "parking":
                logger.info("Checking parking infrastructure: lighting, ventilation, security");
                break;
            case "mechanical":
                logger.info("Checking mechanical infrastructure: equipment, monitoring systems");
                break;
            case "lobby":
                logger.info("Checking lobby infrastructure: lighting, security, climate control");
                break;
            case "restaurant":
                logger.info("Checking restaurant infrastructure: kitchen equipment, ventilation");
                break;
            case "conference":
                logger.info("Checking conference infrastructure: AV systems, climate control");
                break;
            case "laboratory":
                logger.info("Checking laboratory infrastructure: fume hoods, gas systems, safety equipment");
                break;
            case "medical":
                logger.info("Checking medical infrastructure: medical gases, isolation systems, backup power");
                break;
            default:
                logger.info("Checking general floor infrastructure");
        }
        
        // Room density checks
        double roomDensity = getNumberOfRooms() / getArea() * 100; // rooms per 100 sq m
        if (roomDensity > 10) {
            logger.warning("Floor " + getName() + " has high room density: " + 
                String.format("%.2f", roomDensity) + " rooms per 100 sq m");
        }
        
        // Call parent implementation to handle child components (devices)
        super.performMaintenance();
        
        logger.info("Floor maintenance completed for " + getName());
    }
    
    @Override
    public void validate() throws InfrastructureException {
        // Call parent validation
        super.validate();
        
        // Floor-specific validation
        if (floorType == null || floorType.trim().isEmpty()) {
            throw new InfrastructureException("Floor type cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (area <= 0) {
            throw new InfrastructureException("Floor area must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        if (numberOfRooms <= 0) {
            throw new InfrastructureException("Number of rooms must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        if (ceilingHeight <= 0) {
            throw new InfrastructureException("Ceiling height must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        if (hvacZone == null || hvacZone.trim().isEmpty()) {
            throw new InfrastructureException("HVAC zone cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        if (occupancyLimit <= 0) {
            throw new InfrastructureException("Occupancy limit must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        // Safety validation
        if (!hasFireSuppressionSystem && !floorType.equalsIgnoreCase("parking")) {
            logger.warning("SAFETY CONCERN: Floor " + getName() + " lacks fire suppression system");
        }
        
        if (!hasEmergencyLighting) {
            logger.warning("SAFETY CONCERN: Floor " + getName() + " lacks emergency lighting");
        }
        
        // Validate reasonable room size
        double averageRoomSize = area / numberOfRooms;
        if (averageRoomSize < 5) { // Very small rooms
            logger.warning("Floor " + getName() + " has very small average room size: " + 
                String.format("%.2f", averageRoomSize) + " sq m");
        } else if (averageRoomSize > 1000) { // Very large rooms
            logger.warning("Floor " + getName() + " has very large average room size: " + 
                String.format("%.2f", averageRoomSize) + " sq m");
        }
        
        // Validate occupancy density
        double occupancyDensity = occupancyLimit / area;
        if (occupancyDensity > 1.0) { // More than 1 person per square meter
            logger.warning("Floor " + getName() + " has very high occupancy density: " + 
                String.format("%.2f", occupancyDensity) + " people/sq m");
        }
    }
    
    /**
     * Gets the energy consumption per square meter
     * @return double representing energy consumption per square meter
     */
    public double getEnergyDensity() {
        return area > 0 ? getEnergyConsumption() / area : 0;
    }
    
    /**
     * Gets the average room size
     * @return double representing average area per room
     */
    public double getAverageRoomSize() {
        return numberOfRooms > 0 ? area / numberOfRooms : 0;
    }
    
    /**
     * Gets the floor volume
     * @return double representing the volume in cubic meters
     */
    public double getVolume() {
        return area * ceilingHeight;
    }
    
    /**
     * Gets the occupancy density
     * @return double representing people per square meter
     */
    public double getOccupancyDensity() {
        return area > 0 ? (double) occupancyLimit / area : 0;
    }
    
    /**
     * Calculates a safety rating based on available safety systems
     * @return String representing the safety rating
     */
    private String calculateSafetyRating() {
        int safetyScore = 0;
        
        if (hasFireSuppressionSystem) safetyScore += 5;
        if (hasEmergencyLighting) safetyScore += 3;
        
        // Floor type specific safety requirements
        switch (floorType.toLowerCase()) {
            case "laboratory":
            case "medical":
            case "industrial":
                // Higher safety requirements
                if (safetyScore >= 8) return "Excellent";
                else if (safetyScore >= 6) return "Good";
                else if (safetyScore >= 4) return "Adequate";
                else return "Insufficient";
            default:
                // Standard safety requirements
                if (safetyScore >= 6) return "Excellent";
                else if (safetyScore >= 4) return "Good";
                else if (safetyScore >= 2) return "Adequate";
                else return "Insufficient";
        }
    }
    
    /**
     * Gets the floor level description
     * @return String describing the floor level
     */
    public String getFloorLevelDescription() {
        if (floorNumber < 0) {
            return "Basement Level " + Math.abs(floorNumber);
        } else if (floorNumber == 0) {
            return "Ground Floor";
        } else if (floorNumber == 1) {
            return "First Floor";
        } else if (floorNumber == 2) {
            return "Second Floor";
        } else if (floorNumber == 3) {
            return "Third Floor";
        } else {
            return "Floor " + floorNumber;
        }
    }
    
    /**
     * Gets floor statistics summary
     * @return String containing key floor statistics
     */
    public String getFloorStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("Floor Statistics for ").append(getName()).append(":\n");
        stats.append("  Level: ").append(getFloorLevelDescription()).append("\n");
        stats.append("  Type: ").append(getFloorType()).append("\n");
        stats.append("  Energy Density: ").append(String.format("%.4f", getEnergyDensity())).append(" kWh/sq m\n");
        stats.append("  Average Room Size: ").append(String.format("%.2f", getAverageRoomSize())).append(" sq m\n");
        stats.append("  Occupancy Density: ").append(String.format("%.4f", getOccupancyDensity())).append(" people/sq m\n");
        stats.append("  Volume: ").append(String.format("%.2f", getVolume())).append(" cubic m\n");
        stats.append("  Safety Rating: ").append(calculateSafetyRating()).append("\n");
        stats.append("  HVAC Zone: ").append(getHvacZone()).append("\n");
        stats.append("  Devices Installed: ").append(getComponentCount()).append("\n");
        stats.append("  Infrastructure Components: ").append(getTotalComponentCount()).append("\n");
        
        return stats.toString();
    }
}
