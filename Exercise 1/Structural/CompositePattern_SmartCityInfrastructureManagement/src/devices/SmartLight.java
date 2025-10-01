package devices;

import exceptions.InfrastructureException;
import utils.ValidationUtils;

/**
 * SmartLight class representing an intelligent lighting device.
 * This is a leaf component in the Composite Design Pattern that
 * provides smart lighting functionality with dimming, color control,
 * and energy monitoring capabilities.
 * 
 * Smart lights can automatically adjust based on ambient conditions,
 * occupancy, and time of day to optimize energy usage and comfort.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class SmartLight extends AbstractDevice {
    
    private static final String DEVICE_TYPE = "SmartLight";
    
    private final int brightness; // 0-100 percentage
    private final String colorTemperature; // Warm, Cool, Daylight
    private final boolean isDimmable;
    private final boolean hasMotionSensor;
    private final boolean hasAmbientLightSensor;
    private final int lifespan; // in hours
    private final String protocol; // WiFi, Zigbee, Bluetooth, etc.
    
    /**
     * Constructor for creating a Smart Light device
     * @param id Unique identifier for the light
     * @param name Display name for the light
     * @param manufacturer Manufacturer of the light
     * @param model Model of the light
     * @param serialNumber Serial number of the light
     * @param energyConsumption Energy consumption in kWh
     * @param brightness Current brightness level (0-100)
     * @param colorTemperature Color temperature setting
     * @param isDimmable Whether the light supports dimming
     * @param hasMotionSensor Whether the light has built-in motion sensor
     * @param hasAmbientLightSensor Whether the light has ambient light sensor
     * @param lifespan Expected lifespan in hours
     * @param protocol Communication protocol used
     * @throws InfrastructureException if parameters are invalid
     */
    public SmartLight(String id, String name, String manufacturer, String model,
                     String serialNumber, double energyConsumption, int brightness,
                     String colorTemperature, boolean isDimmable, boolean hasMotionSensor,
                     boolean hasAmbientLightSensor, int lifespan, String protocol) 
                     throws InfrastructureException {
        super(id, name, DEVICE_TYPE, manufacturer, model, serialNumber, energyConsumption);
        
        // Validate smart light specific parameters
        ValidationUtils.validateRange(brightness, 0, 100, "Brightness");
        ValidationUtils.validateNotNullOrEmpty(colorTemperature, "Color temperature");
        ValidationUtils.validatePositive(lifespan, "Lifespan");
        ValidationUtils.validateNotNullOrEmpty(protocol, "Protocol");
        
        // Validate color temperature
        String[] validColorTemperatures = {"Warm", "Cool", "Daylight", "RGB", "Tunable"};
        ValidationUtils.validateComponentType(colorTemperature, validColorTemperatures, "Color temperature");
        
        // Validate protocol
        String[] validProtocols = {"WiFi", "Zigbee", "Bluetooth", "Z-Wave", "Thread", "Matter"};
        ValidationUtils.validateComponentType(protocol, validProtocols, "Protocol");
        
        this.brightness = brightness;
        this.colorTemperature = colorTemperature.trim();
        this.isDimmable = isDimmable;
        this.hasMotionSensor = hasMotionSensor;
        this.hasAmbientLightSensor = hasAmbientLightSensor;
        this.lifespan = lifespan;
        this.protocol = protocol.trim();
        
        logger.info("Smart Light configured: " + name + " (Brightness: " + brightness + 
                   "%, Color: " + colorTemperature + ", Protocol: " + protocol + ")");
    }
    
    /**
     * Gets the current brightness level
     * @return int representing brightness percentage (0-100)
     */
    public int getBrightness() {
        return brightness;
    }
    
    /**
     * Gets the color temperature setting
     * @return String representing the color temperature
     */
    public String getColorTemperature() {
        return colorTemperature;
    }
    
    /**
     * Checks if the light is dimmable
     * @return boolean true if dimmable, false otherwise
     */
    public boolean isDimmable() {
        return isDimmable;
    }
    
    /**
     * Checks if the light has a motion sensor
     * @return boolean true if has motion sensor, false otherwise
     */
    public boolean hasMotionSensor() {
        return hasMotionSensor;
    }
    
    /**
     * Checks if the light has an ambient light sensor
     * @return boolean true if has ambient light sensor, false otherwise
     */
    public boolean hasAmbientLightSensor() {
        return hasAmbientLightSensor;
    }
    
    /**
     * Gets the expected lifespan
     * @return int representing lifespan in hours
     */
    public int getLifespan() {
        return lifespan;
    }
    
    /**
     * Gets the communication protocol
     * @return String representing the protocol
     */
    public String getProtocol() {
        return protocol;
    }
    
    @Override
    protected void displayDeviceSpecificInfo(String indentation) {
        System.out.println(indentation + "Brightness: " + getBrightness() + "%");
        System.out.println(indentation + "Color Temperature: " + getColorTemperature());
        System.out.println(indentation + "Dimmable: " + (isDimmable() ? "Yes" : "No"));
        System.out.println(indentation + "Motion Sensor: " + (hasMotionSensor() ? "Yes" : "No"));
        System.out.println(indentation + "Ambient Light Sensor: " + (hasAmbientLightSensor() ? "Yes" : "No"));
        System.out.println(indentation + "Lifespan: " + String.format("%,d", getLifespan()) + " hours");
        System.out.println(indentation + "Protocol: " + getProtocol());
        
        // Calculate and display additional metrics
        double efficiency = calculateLuminousEfficacy();
        System.out.println(indentation + "Luminous Efficacy: " + String.format("%.1f", efficiency) + " lm/W");
        
        String smartFeatures = getSmartFeaturesDescription();
        System.out.println(indentation + "Smart Features: " + smartFeatures);
    }
    
    @Override
    protected String getDeviceSpecificInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Brightness: ").append(getBrightness()).append("%\n");
        info.append("Color Temperature: ").append(getColorTemperature()).append("\n");
        info.append("Dimmable: ").append(isDimmable() ? "Yes" : "No").append("\n");
        info.append("Motion Sensor: ").append(hasMotionSensor() ? "Yes" : "No").append("\n");
        info.append("Ambient Light Sensor: ").append(hasAmbientLightSensor() ? "Yes" : "No").append("\n");
        info.append("Lifespan: ").append(String.format("%,d", getLifespan())).append(" hours\n");
        info.append("Protocol: ").append(getProtocol()).append("\n");
        info.append("Luminous Efficacy: ").append(String.format("%.1f", calculateLuminousEfficacy())).append(" lm/W\n");
        info.append("Smart Features: ").append(getSmartFeaturesDescription()).append("\n");
        info.append("Energy Efficiency: ").append(calculateEnergyEfficiencyRating()).append("\n");
        
        return info.toString();
    }
    
    @Override
    protected void performDeviceSpecificMaintenance() {
        logger.info("Performing Smart Light specific maintenance:");
        
        // LED-specific maintenance
        logger.info("Checking LED integrity and color consistency");
        logger.info("Calibrating brightness and color temperature sensors");
        
        // Sensor maintenance
        if (hasMotionSensor) {
            logger.info("Testing motion sensor sensitivity and range");
        }
        
        if (hasAmbientLightSensor) {
            logger.info("Calibrating ambient light sensor");
        }
        
        // Protocol and connectivity maintenance
        switch (protocol.toLowerCase()) {
            case "wifi":
                logger.info("Checking WiFi signal strength and connectivity");
                break;
            case "zigbee":
                logger.info("Testing Zigbee mesh network connectivity");
                break;
            case "bluetooth":
                logger.info("Verifying Bluetooth pairing and range");
                break;
            case "z-wave":
                logger.info("Testing Z-Wave network connectivity");
                break;
            case "thread":
                logger.info("Checking Thread network mesh connectivity");
                break;
            case "matter":
                logger.info("Verifying Matter protocol compatibility");
                break;
        }
        
        // Dimming functionality test
        if (isDimmable) {
            logger.info("Testing dimming functionality across brightness range");
        }
        
        // Energy efficiency check
        double efficiency = calculateLuminousEfficacy();
        if (efficiency < 80) {
            logger.warning("Light efficiency below optimal level: " + 
                String.format("%.1f", efficiency) + " lm/W");
        }
        
        logger.info("Smart Light maintenance completed");
    }
    
    @Override
    protected void validateDeviceSpecific() throws InfrastructureException {
        // Validate brightness range
        if (brightness < 0 || brightness > 100) {
            throw new InfrastructureException("Brightness must be between 0 and 100", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate color temperature
        if (colorTemperature == null || colorTemperature.trim().isEmpty()) {
            throw new InfrastructureException("Color temperature cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate lifespan
        if (lifespan <= 0) {
            throw new InfrastructureException("Lifespan must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        if (lifespan > 100000) { // Unreasonably high lifespan
            logger.warning("Light " + getName() + " has unusually high lifespan: " + 
                String.format("%,d", lifespan) + " hours");
        }
        
        // Validate protocol
        if (protocol == null || protocol.trim().isEmpty()) {
            throw new InfrastructureException("Protocol cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate energy consumption for LED lights
        if (energyConsumption > 100) { // Very high for LED
            logger.warning("Light " + getName() + " has high energy consumption for LED: " + 
                energyConsumption + " kWh");
        }
    }
    
    /**
     * Calculates the luminous efficacy (lumens per watt)
     * This is an estimation based on energy consumption and typical LED performance
     * @return double representing luminous efficacy in lm/W
     */
    private double calculateLuminousEfficacy() {
        if (energyConsumption <= 0) {
            return 0;
        }
        
        // Estimate lumens based on brightness and typical LED output
        double estimatedLumens = (brightness / 100.0) * 1000; // Assume max 1000 lumens
        double watts = energyConsumption * 1000; // Convert kWh to Wh, then to watts (assuming 1 hour operation)
        
        return estimatedLumens / watts;
    }
    
    /**
     * Gets a description of available smart features
     * @return String describing smart features
     */
    private String getSmartFeaturesDescription() {
        StringBuilder features = new StringBuilder();
        
        if (isDimmable) {
            features.append("Dimming");
        }
        
        if (hasMotionSensor) {
            if (features.length() > 0) features.append(", ");
            features.append("Motion Detection");
        }
        
        if (hasAmbientLightSensor) {
            if (features.length() > 0) features.append(", ");
            features.append("Ambient Light Sensing");
        }
        
        if (colorTemperature.equalsIgnoreCase("RGB") || colorTemperature.equalsIgnoreCase("Tunable")) {
            if (features.length() > 0) features.append(", ");
            features.append("Color Changing");
        }
        
        if (features.length() == 0) {
            features.append("Basic On/Off");
        }
        
        return features.toString();
    }
    
    /**
     * Calculates energy efficiency rating
     * @return String representing the efficiency rating
     */
    private String calculateEnergyEfficiencyRating() {
        double efficacy = calculateLuminousEfficacy();
        
        if (efficacy >= 150) {
            return "A+ (Excellent)";
        } else if (efficacy >= 120) {
            return "A (Very Good)";
        } else if (efficacy >= 100) {
            return "B (Good)";
        } else if (efficacy >= 80) {
            return "C (Fair)";
        } else if (efficacy >= 60) {
            return "D (Poor)";
        } else {
            return "F (Very Poor)";
        }
    }
    
    /**
     * Estimates remaining lifespan based on current usage
     * @param hoursUsedPerDay Average hours used per day
     * @return int estimated remaining lifespan in years
     */
    public int estimateRemainingLifespanYears(double hoursUsedPerDay) {
        if (hoursUsedPerDay <= 0) {
            return Integer.MAX_VALUE; // Indefinite if not used
        }
        
        double remainingHours = lifespan * 0.8; // Assume 20% already used
        double daysRemaining = remainingHours / hoursUsedPerDay;
        return (int) (daysRemaining / 365);
    }
    
    /**
     * Gets smart light statistics summary
     * @return String containing key statistics
     */
    public String getSmartLightStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("Smart Light Statistics for ").append(getName()).append(":\n");
        stats.append("  Current Brightness: ").append(getBrightness()).append("%\n");
        stats.append("  Color Temperature: ").append(getColorTemperature()).append("\n");
        stats.append("  Luminous Efficacy: ").append(String.format("%.1f", calculateLuminousEfficacy())).append(" lm/W\n");
        stats.append("  Energy Efficiency: ").append(calculateEnergyEfficiencyRating()).append("\n");
        stats.append("  Smart Features: ").append(getSmartFeaturesDescription()).append("\n");
        stats.append("  Communication: ").append(getProtocol()).append("\n");
        stats.append("  Expected Lifespan: ").append(String.format("%,d", getLifespan())).append(" hours\n");
        stats.append("  Status: ").append(operational ? "Operational" : "Offline").append("\n");
        
        return stats.toString();
    }
}
