package devices;

import exceptions.InfrastructureException;
import utils.ValidationUtils;

/**
 * AirConditioner class representing an intelligent HVAC device.
 * This is a leaf component in the Composite Design Pattern that
 * provides smart climate control functionality with temperature regulation,
 * energy monitoring, and automated scheduling capabilities.
 * 
 * Smart air conditioners can automatically adjust based on occupancy,
 * ambient conditions, and energy efficiency requirements.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class AirConditioner extends AbstractDevice {
    
    private static final String DEVICE_TYPE = "AirConditioner";
    
    private final double capacity; // BTU/hour
    private final int targetTemperature; // Celsius
    private final String mode; // Cool, Heat, Auto, Fan, Dry
    private final int fanSpeed; // 1-5 scale
    private final boolean hasWiFi;
    private final boolean hasScheduling;
    private final String refrigerant; // R-32, R-410A, etc.
    private final double seer; // Seasonal Energy Efficiency Ratio
    private final boolean hasInverterTechnology;
    
    /**
     * Constructor for creating an Air Conditioner device
     * @param id Unique identifier for the AC unit
     * @param name Display name for the AC unit
     * @param manufacturer Manufacturer of the AC unit
     * @param model Model of the AC unit
     * @param serialNumber Serial number of the AC unit
     * @param energyConsumption Energy consumption in kWh
     * @param capacity Cooling/heating capacity in BTU/hour
     * @param targetTemperature Target temperature in Celsius
     * @param mode Operating mode
     * @param fanSpeed Fan speed setting (1-5)
     * @param hasWiFi Whether the unit has WiFi connectivity
     * @param hasScheduling Whether the unit supports scheduling
     * @param refrigerant Type of refrigerant used
     * @param seer Seasonal Energy Efficiency Ratio
     * @param hasInverterTechnology Whether unit has inverter technology
     * @throws InfrastructureException if parameters are invalid
     */
    public AirConditioner(String id, String name, String manufacturer, String model,
                         String serialNumber, double energyConsumption, double capacity,
                         int targetTemperature, String mode, int fanSpeed, boolean hasWiFi,
                         boolean hasScheduling, String refrigerant, double seer,
                         boolean hasInverterTechnology) throws InfrastructureException {
        super(id, name, DEVICE_TYPE, manufacturer, model, serialNumber, energyConsumption);
        
        // Validate AC-specific parameters
        ValidationUtils.validatePositive(capacity, "Capacity");
        ValidationUtils.validateRange(targetTemperature, 10, 35, "Target temperature");
        ValidationUtils.validateNotNullOrEmpty(mode, "Mode");
        ValidationUtils.validateRange(fanSpeed, 1, 5, "Fan speed");
        ValidationUtils.validateNotNullOrEmpty(refrigerant, "Refrigerant");
        ValidationUtils.validatePositive(seer, "SEER");
        
        // Validate mode
        String[] validModes = {"Cool", "Heat", "Auto", "Fan", "Dry", "Off"};
        ValidationUtils.validateComponentType(mode, validModes, "Mode");
        
        // Validate refrigerant type
        String[] validRefrigerants = {"R-32", "R-410A", "R-22", "R-134a", "R-407C", "R-290"};
        ValidationUtils.validateComponentType(refrigerant, validRefrigerants, "Refrigerant");
        
        this.capacity = capacity;
        this.targetTemperature = targetTemperature;
        this.mode = mode.trim();
        this.fanSpeed = fanSpeed;
        this.hasWiFi = hasWiFi;
        this.hasScheduling = hasScheduling;
        this.refrigerant = refrigerant.trim();
        this.seer = seer;
        this.hasInverterTechnology = hasInverterTechnology;
        
        logger.info("Air Conditioner configured: " + name + " (" + capacity + " BTU/hr, " + 
                   "Target: " + targetTemperature + "°C, Mode: " + mode + 
                   ", SEER: " + seer + ")");
    }
    
    /**
     * Gets the cooling/heating capacity
     * @return double representing capacity in BTU/hour
     */
    public double getCapacity() {
        return capacity;
    }
    
    /**
     * Gets the target temperature
     * @return int representing target temperature in Celsius
     */
    public int getTargetTemperature() {
        return targetTemperature;
    }
    
    /**
     * Gets the operating mode
     * @return String representing the current mode
     */
    public String getMode() {
        return mode;
    }
    
    /**
     * Gets the fan speed setting
     * @return int representing fan speed (1-5)
     */
    public int getFanSpeed() {
        return fanSpeed;
    }
    
    /**
     * Checks if the unit has WiFi connectivity
     * @return boolean true if has WiFi, false otherwise
     */
    public boolean hasWiFi() {
        return hasWiFi;
    }
    
    /**
     * Checks if the unit supports scheduling
     * @return boolean true if supports scheduling, false otherwise
     */
    public boolean hasScheduling() {
        return hasScheduling;
    }
    
    /**
     * Gets the refrigerant type
     * @return String representing the refrigerant type
     */
    public String getRefrigerant() {
        return refrigerant;
    }
    
    /**
     * Gets the SEER rating
     * @return double representing the SEER value
     */
    public double getSeer() {
        return seer;
    }
    
    /**
     * Checks if the unit has inverter technology
     * @return boolean true if has inverter technology, false otherwise
     */
    public boolean hasInverterTechnology() {
        return hasInverterTechnology;
    }
    
    @Override
    protected void displayDeviceSpecificInfo(String indentation) {
        System.out.println(indentation + "Capacity: " + String.format("%.0f", getCapacity()) + " BTU/hr");
        System.out.println(indentation + "Target Temperature: " + getTargetTemperature() + "°C");
        System.out.println(indentation + "Mode: " + getMode());
        System.out.println(indentation + "Fan Speed: " + getFanSpeed() + "/5");
        System.out.println(indentation + "WiFi: " + (hasWiFi() ? "Yes" : "No"));
        System.out.println(indentation + "Scheduling: " + (hasScheduling() ? "Yes" : "No"));
        System.out.println(indentation + "Refrigerant: " + getRefrigerant());
        System.out.println(indentation + "SEER: " + String.format("%.1f", getSeer()));
        System.out.println(indentation + "Inverter Technology: " + (hasInverterTechnology() ? "Yes" : "No"));
        
        // Calculate and display additional metrics
        String efficiencyRating = calculateEfficiencyRating();
        System.out.println(indentation + "Efficiency Rating: " + efficiencyRating);
        
        double tonCapacity = calculateTonCapacity();
        System.out.println(indentation + "Capacity: " + String.format("%.2f", tonCapacity) + " tons");
    }
    
    @Override
    protected String getDeviceSpecificInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Capacity: ").append(String.format("%.0f", getCapacity())).append(" BTU/hr\n");
        info.append("Capacity: ").append(String.format("%.2f", calculateTonCapacity())).append(" tons\n");
        info.append("Target Temperature: ").append(getTargetTemperature()).append("°C\n");
        info.append("Mode: ").append(getMode()).append("\n");
        info.append("Fan Speed: ").append(getFanSpeed()).append("/5\n");
        info.append("WiFi: ").append(hasWiFi() ? "Yes" : "No").append("\n");
        info.append("Scheduling: ").append(hasScheduling() ? "Yes" : "No").append("\n");
        info.append("Refrigerant: ").append(getRefrigerant()).append("\n");
        info.append("SEER: ").append(String.format("%.1f", getSeer())).append("\n");
        info.append("Inverter Technology: ").append(hasInverterTechnology() ? "Yes" : "No").append("\n");
        info.append("Efficiency Rating: ").append(calculateEfficiencyRating()).append("\n");
        info.append("Environmental Impact: ").append(assessEnvironmentalImpact()).append("\n");
        
        return info.toString();
    }
    
    @Override
    protected void performDeviceSpecificMaintenance() {
        logger.info("Performing Air Conditioner specific maintenance:");
        
        // Filter maintenance
        logger.info("Checking and replacing air filters");
        logger.info("Cleaning evaporator and condenser coils");
        
        // Refrigerant system maintenance
        logger.info("Checking refrigerant levels and pressure");
        logger.info("Inspecting refrigerant lines for leaks");
        logger.info("Testing compressor operation");
        
        // Electrical system maintenance
        logger.info("Checking electrical connections and controls");
        logger.info("Testing thermostat calibration");
        
        // Fan and motor maintenance
        logger.info("Lubricating fan motors and checking belt tension");
        logger.info("Testing fan operation at all speed levels");
        
        // Drainage system maintenance
        logger.info("Cleaning condensate drain and drain pan");
        logger.info("Checking for proper drainage and water leaks");
        
        // Smart features maintenance
        if (hasWiFi) {
            logger.info("Testing WiFi connectivity and smart controls");
        }
        
        if (hasScheduling) {
            logger.info("Verifying scheduling and timer functions");
        }
        
        // Inverter technology maintenance
        if (hasInverterTechnology) {
            logger.info("Testing inverter operation and variable speed control");
        }
        
        // Energy efficiency check
        if (seer < 13) {
            logger.warning("AC unit has low SEER rating: " + seer + " - Consider upgrade");
        }
        
        // Refrigerant environmental check
        if ("R-22".equals(refrigerant)) {
            logger.warning("Unit uses R-22 refrigerant (being phased out for environmental reasons)");
        }
        
        logger.info("Air Conditioner maintenance completed");
    }
    
    @Override
    protected void validateDeviceSpecific() throws InfrastructureException {
        // Validate capacity range
        if (capacity < 5000 || capacity > 60000) { // Typical range for room/small commercial units
            logger.warning("AC capacity outside typical range: " + capacity + " BTU/hr");
        }
        
        // Validate temperature range
        if (targetTemperature < 10 || targetTemperature > 35) {
            throw new InfrastructureException("Target temperature must be between 10°C and 35°C", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate fan speed
        if (fanSpeed < 1 || fanSpeed > 5) {
            throw new InfrastructureException("Fan speed must be between 1 and 5", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate SEER rating
        if (seer < 8 || seer > 30) {
            throw new InfrastructureException("SEER rating must be between 8 and 30", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate mode
        if (mode == null || mode.trim().isEmpty()) {
            throw new InfrastructureException("AC mode cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate refrigerant
        if (refrigerant == null || refrigerant.trim().isEmpty()) {
            throw new InfrastructureException("Refrigerant type cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        // Energy consumption validation based on capacity
        double expectedEnergyRange = capacity / 1000; // Rough estimate: 1 kWh per 1000 BTU/hr
        if (energyConsumption > expectedEnergyRange * 2) {
            logger.warning("AC " + getName() + " has high energy consumption relative to capacity");
        }
    }
    
    /**
     * Calculates the cooling capacity in tons
     * @return double representing capacity in tons (1 ton = 12,000 BTU/hr)
     */
    private double calculateTonCapacity() {
        return capacity / 12000.0;
    }
    
    /**
     * Calculates energy efficiency rating based on SEER
     * @return String representing the efficiency rating
     */
    private String calculateEfficiencyRating() {
        if (seer >= 21) {
            return "A+ (Excellent)";
        } else if (seer >= 18) {
            return "A (Very Good)";
        } else if (seer >= 16) {
            return "B (Good)";
        } else if (seer >= 14) {
            return "C (Fair)";
        } else if (seer >= 13) {
            return "D (Minimum Standard)";
        } else {
            return "F (Below Standard)";
        }
    }
    
    /**
     * Assesses the environmental impact based on refrigerant and efficiency
     * @return String representing the environmental impact assessment
     */
    private String assessEnvironmentalImpact() {
        StringBuilder impact = new StringBuilder();
        
        // Refrigerant environmental impact
        switch (refrigerant) {
            case "R-32":
                impact.append("Low GWP refrigerant (Good)");
                break;
            case "R-410A":
                impact.append("Medium GWP refrigerant (Moderate)");
                break;
            case "R-22":
                impact.append("High GWP refrigerant (Poor - Being Phased Out)");
                break;
            case "R-290":
                impact.append("Natural refrigerant (Excellent)");
                break;
            default:
                impact.append("Standard refrigerant impact");
        }
        
        // Energy efficiency impact
        if (seer >= 16) {
            impact.append(", High efficiency (Low energy impact)");
        } else if (seer >= 13) {
            impact.append(", Standard efficiency (Moderate energy impact)");
        } else {
            impact.append(", Low efficiency (High energy impact)");
        }
        
        return impact.toString();
    }
    
    /**
     * Calculates estimated operating cost per hour
     * @param electricityRate Cost per kWh
     * @return double representing cost per hour of operation
     */
    public double calculateOperatingCostPerHour(double electricityRate) {
        return getEnergyConsumption() * electricityRate;
    }
    
    /**
     * Estimates cooling/heating coverage area
     * @return int representing estimated square footage coverage
     */
    public int estimateCoverageArea() {
        // Rule of thumb: 20 BTU per square foot for residential, 25 for commercial
        return (int) (capacity / 22); // Average between residential and commercial
    }
    
    /**
     * Gets AC statistics summary
     * @return String containing key statistics
     */
    public String getAirConditionerStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("Air Conditioner Statistics for ").append(getName()).append(":\n");
        stats.append("  Capacity: ").append(String.format("%.0f", getCapacity())).append(" BTU/hr (")
             .append(String.format("%.2f", calculateTonCapacity())).append(" tons)\n");
        stats.append("  Target Temperature: ").append(getTargetTemperature()).append("°C\n");
        stats.append("  Current Mode: ").append(getMode()).append("\n");
        stats.append("  SEER Rating: ").append(String.format("%.1f", getSeer())).append("\n");
        stats.append("  Efficiency Rating: ").append(calculateEfficiencyRating()).append("\n");
        stats.append("  Refrigerant: ").append(getRefrigerant()).append("\n");
        stats.append("  Coverage Area: ~").append(estimateCoverageArea()).append(" sq ft\n");
        stats.append("  Smart Features: ");
        
        StringBuilder smartFeatures = new StringBuilder();
        if (hasWiFi) smartFeatures.append("WiFi");
        if (hasScheduling) {
            if (smartFeatures.length() > 0) smartFeatures.append(", ");
            smartFeatures.append("Scheduling");
        }
        if (hasInverterTechnology) {
            if (smartFeatures.length() > 0) smartFeatures.append(", ");
            smartFeatures.append("Inverter");
        }
        if (smartFeatures.length() == 0) smartFeatures.append("None");
        
        stats.append(smartFeatures.toString()).append("\n");
        stats.append("  Environmental Impact: ").append(assessEnvironmentalImpact()).append("\n");
        stats.append("  Status: ").append(operational ? "Operational" : "Offline").append("\n");
        
        return stats.toString();
    }
}
