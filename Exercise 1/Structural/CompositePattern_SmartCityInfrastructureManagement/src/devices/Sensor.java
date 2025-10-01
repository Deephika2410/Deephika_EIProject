package devices;

import exceptions.InfrastructureException;
import utils.ValidationUtils;

/**
 * Sensor class representing an intelligent monitoring device.
 * This is a leaf component in the Composite Design Pattern that
 * provides environmental monitoring, data collection, and
 * automated alerting capabilities.
 * 
 * Smart sensors can monitor various environmental parameters
 * and provide real-time data for building management systems.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class Sensor extends AbstractDevice {
    
    private static final String DEVICE_TYPE = "Sensor";
    
    private final String sensorType; // Temperature, Humidity, Motion, CO2, etc.
    private final String measurementUnit; // °C, %, ppm, etc.
    private final double minRange; // Minimum measurement range
    private final double maxRange; // Maximum measurement range
    private final double accuracy; // Accuracy percentage
    private final int samplingRate; // Samples per minute
    private final boolean hasWirelessConnectivity;
    private final boolean hasDataLogging;
    private final String alertThreshold; // Threshold for alerts
    private final int batteryLife; // Battery life in months (0 if hardwired)
    
    /**
     * Constructor for creating a Sensor device
     * @param id Unique identifier for the sensor
     * @param name Display name for the sensor
     * @param manufacturer Manufacturer of the sensor
     * @param model Model of the sensor
     * @param serialNumber Serial number of the sensor
     * @param energyConsumption Energy consumption in kWh
     * @param sensorType Type of sensor (Temperature, Humidity, etc.)
     * @param measurementUnit Unit of measurement
     * @param minRange Minimum measurement range
     * @param maxRange Maximum measurement range
     * @param accuracy Accuracy as a percentage
     * @param samplingRate Number of samples per minute
     * @param hasWirelessConnectivity Whether sensor has wireless connectivity
     * @param hasDataLogging Whether sensor supports data logging
     * @param alertThreshold Threshold value for generating alerts
     * @param batteryLife Battery life in months (0 for hardwired)
     * @throws InfrastructureException if parameters are invalid
     */
    public Sensor(String id, String name, String manufacturer, String model,
                 String serialNumber, double energyConsumption, String sensorType,
                 String measurementUnit, double minRange, double maxRange, double accuracy,
                 int samplingRate, boolean hasWirelessConnectivity, boolean hasDataLogging,
                 String alertThreshold, int batteryLife) throws InfrastructureException {
        super(id, name, DEVICE_TYPE, manufacturer, model, serialNumber, energyConsumption);
        
        // Validate sensor-specific parameters
        ValidationUtils.validateNotNullOrEmpty(sensorType, "Sensor type");
        ValidationUtils.validateNotNullOrEmpty(measurementUnit, "Measurement unit");
        ValidationUtils.validateRange(accuracy, 0.1, 100.0, "Accuracy");
        ValidationUtils.validatePositive(samplingRate, "Sampling rate");
        ValidationUtils.validateNotNullOrEmpty(alertThreshold, "Alert threshold");
        ValidationUtils.validatePositive(batteryLife, "Battery life");
        
        // Validate that max range is greater than min range
        if (maxRange <= minRange) {
            throw InfrastructureException.validationError("Max range", 
                "Max range (" + maxRange + ") must be greater than min range (" + minRange + ")");
        }
        
        // Validate sensor type
        String[] validSensorTypes = {"Temperature", "Humidity", "Motion", "CO2", "CO", "Air Quality", 
                                    "Noise", "Light", "Pressure", "Smoke", "Gas", "Water Level", 
                                    "Vibration", "Occupancy", "Door/Window", "Proximity"};
        ValidationUtils.validateComponentType(sensorType, validSensorTypes, "Sensor type");
        
        this.sensorType = sensorType.trim();
        this.measurementUnit = measurementUnit.trim();
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.accuracy = accuracy;
        this.samplingRate = samplingRate;
        this.hasWirelessConnectivity = hasWirelessConnectivity;
        this.hasDataLogging = hasDataLogging;
        this.alertThreshold = alertThreshold.trim();
        this.batteryLife = batteryLife;
        
        logger.info("Sensor configured: " + name + " (" + sensorType + " sensor, Range: " + 
                   minRange + "-" + maxRange + " " + measurementUnit + 
                   ", Accuracy: " + accuracy + "%)");
    }
    
    /**
     * Gets the sensor type
     * @return String representing the sensor type
     */
    public String getSensorType() {
        return sensorType;
    }
    
    /**
     * Gets the measurement unit
     * @return String representing the measurement unit
     */
    public String getMeasurementUnit() {
        return measurementUnit;
    }
    
    /**
     * Gets the minimum measurement range
     * @return double representing the minimum range
     */
    public double getMinRange() {
        return minRange;
    }
    
    /**
     * Gets the maximum measurement range
     * @return double representing the maximum range
     */
    public double getMaxRange() {
        return maxRange;
    }
    
    /**
     * Gets the sensor accuracy
     * @return double representing accuracy as a percentage
     */
    public double getAccuracy() {
        return accuracy;
    }
    
    /**
     * Gets the sampling rate
     * @return int representing samples per minute
     */
    public int getSamplingRate() {
        return samplingRate;
    }
    
    /**
     * Checks if the sensor has wireless connectivity
     * @return boolean true if has wireless connectivity, false otherwise
     */
    public boolean hasWirelessConnectivity() {
        return hasWirelessConnectivity;
    }
    
    /**
     * Checks if the sensor supports data logging
     * @return boolean true if supports data logging, false otherwise
     */
    public boolean hasDataLogging() {
        return hasDataLogging;
    }
    
    /**
     * Gets the alert threshold
     * @return String representing the alert threshold
     */
    public String getAlertThreshold() {
        return alertThreshold;
    }
    
    /**
     * Gets the battery life
     * @return int representing battery life in months (0 if hardwired)
     */
    public int getBatteryLife() {
        return batteryLife;
    }
    
    /**
     * Checks if the sensor is battery powered
     * @return boolean true if battery powered, false if hardwired
     */
    public boolean isBatteryPowered() {
        return batteryLife > 0;
    }
    
    @Override
    protected void displayDeviceSpecificInfo(String indentation) {
        System.out.println(indentation + "Sensor Type: " + getSensorType());
        System.out.println(indentation + "Measurement Unit: " + getMeasurementUnit());
        System.out.println(indentation + "Range: " + getMinRange() + " - " + getMaxRange() + " " + getMeasurementUnit());
        System.out.println(indentation + "Accuracy: " + String.format("%.1f", getAccuracy()) + "%");
        System.out.println(indentation + "Sampling Rate: " + getSamplingRate() + " samples/min");
        System.out.println(indentation + "Wireless: " + (hasWirelessConnectivity() ? "Yes" : "No"));
        System.out.println(indentation + "Data Logging: " + (hasDataLogging() ? "Yes" : "No"));
        System.out.println(indentation + "Alert Threshold: " + getAlertThreshold());
        
        if (isBatteryPowered()) {
            System.out.println(indentation + "Power: Battery (" + getBatteryLife() + " months)");
        } else {
            System.out.println(indentation + "Power: Hardwired");
        }
        
        // Display sensor capabilities
        String capabilities = getSensorCapabilities();
        System.out.println(indentation + "Capabilities: " + capabilities);
        
        // Display measurement range description
        String rangeDescription = getMeasurementRangeDescription();
        System.out.println(indentation + "Range Classification: " + rangeDescription);
    }
    
    @Override
    protected String getDeviceSpecificInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Sensor Type: ").append(getSensorType()).append("\n");
        info.append("Measurement Unit: ").append(getMeasurementUnit()).append("\n");
        info.append("Range: ").append(getMinRange()).append(" - ").append(getMaxRange())
            .append(" ").append(getMeasurementUnit()).append("\n");
        info.append("Accuracy: ").append(String.format("%.1f", getAccuracy())).append("%\n");
        info.append("Sampling Rate: ").append(getSamplingRate()).append(" samples/min\n");
        info.append("Wireless Connectivity: ").append(hasWirelessConnectivity() ? "Yes" : "No").append("\n");
        info.append("Data Logging: ").append(hasDataLogging() ? "Yes" : "No").append("\n");
        info.append("Alert Threshold: ").append(getAlertThreshold()).append("\n");
        info.append("Power Source: ").append(isBatteryPowered() ? 
            "Battery (" + getBatteryLife() + " months)" : "Hardwired").append("\n");
        info.append("Capabilities: ").append(getSensorCapabilities()).append("\n");
        info.append("Range Classification: ").append(getMeasurementRangeDescription()).append("\n");
        info.append("Data Collection Rate: ").append(calculateDataPointsPerDay()).append(" points/day\n");
        
        return info.toString();
    }
    
    @Override
    protected void performDeviceSpecificMaintenance() {
        logger.info("Performing Sensor specific maintenance:");
        
        // Sensor calibration
        logger.info("Performing sensor calibration and accuracy verification");
        logger.info("Checking measurement range and threshold settings");
        
        // Physical maintenance
        switch (sensorType.toLowerCase()) {
            case "temperature":
                logger.info("Checking temperature sensor element and thermal coupling");
                break;
            case "humidity":
                logger.info("Cleaning humidity sensor element and checking for contamination");
                break;
            case "motion":
                logger.info("Testing motion detection range and sensitivity");
                break;
            case "co2":
            case "co":
            case "gas":
                logger.info("Calibrating gas sensor and checking for sensor drift");
                break;
            case "air quality":
                logger.info("Cleaning air intake and checking particle sensor");
                break;
            case "noise":
                logger.info("Calibrating microphone and checking frequency response");
                break;
            case "light":
                logger.info("Cleaning light sensor and checking photodiode response");
                break;
            case "pressure":
                logger.info("Checking pressure sensor diaphragm and zero calibration");
                break;
            case "smoke":
                logger.info("Testing smoke chamber and cleaning optical components");
                break;
            case "water level":
                logger.info("Checking water level sensor probes and cleaning contacts");
                break;
            case "vibration":
                logger.info("Testing accelerometer calibration and mounting");
                break;
            case "occupancy":
                logger.info("Testing occupancy detection accuracy and coverage area");
                break;
            case "door/window":
                logger.info("Testing magnetic switch operation and contact alignment");
                break;
            case "proximity":
                logger.info("Testing proximity detection range and interference rejection");
                break;
            default:
                logger.info("Performing general sensor maintenance");
        }
        
        // Communication and connectivity maintenance
        if (hasWirelessConnectivity) {
            logger.info("Testing wireless communication and signal strength");
        } else {
            logger.info("Checking wired communication connections");
        }
        
        // Data logging maintenance
        if (hasDataLogging) {
            logger.info("Checking data storage capacity and retrieval functions");
        }
        
        // Power system maintenance
        if (isBatteryPowered()) {
            logger.info("Checking battery voltage and estimating remaining life");
            
            // Battery life warning
            if (batteryLife < 6) {
                logger.warning("Sensor " + getName() + " battery life is low: " + 
                    batteryLife + " months remaining");
            }
        } else {
            logger.info("Checking power supply connections and voltage levels");
        }
        
        // Accuracy verification
        if (accuracy < 90) {
            logger.warning("Sensor " + getName() + " has low accuracy: " + 
                String.format("%.1f", accuracy) + "%");
        }
        
        // Sampling rate optimization
        if (samplingRate > 60) { // Very high sampling rate
            logger.info("High sampling rate detected - verifying necessity for power optimization");
        }
        
        logger.info("Sensor maintenance completed");
    }
    
    @Override
    protected void validateDeviceSpecific() throws InfrastructureException {
        // Validate sensor type
        if (sensorType == null || sensorType.trim().isEmpty()) {
            throw new InfrastructureException("Sensor type cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate measurement unit
        if (measurementUnit == null || measurementUnit.trim().isEmpty()) {
            throw new InfrastructureException("Measurement unit cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate range
        if (maxRange <= minRange) {
            throw new InfrastructureException("Maximum range must be greater than minimum range", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate accuracy
        if (accuracy <= 0 || accuracy > 100) {
            throw new InfrastructureException("Accuracy must be between 0.1 and 100", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate sampling rate
        if (samplingRate <= 0) {
            throw new InfrastructureException("Sampling rate must be positive", 
                getId(), getType(), "VALIDATE");
        }
        
        if (samplingRate > 1000) { // Very high sampling rate
            logger.warning("Sensor " + getName() + " has very high sampling rate: " + 
                samplingRate + " samples/min");
        }
        
        // Validate alert threshold
        if (alertThreshold == null || alertThreshold.trim().isEmpty()) {
            throw new InfrastructureException("Alert threshold cannot be null or empty", 
                getId(), getType(), "VALIDATE");
        }
        
        // Validate battery life for battery-powered sensors
        if (batteryLife < 0) {
            throw new InfrastructureException("Battery life cannot be negative", 
                getId(), getType(), "VALIDATE");
        }
        
        // Sensor-specific validations
        validateSensorTypeSpecific();
    }
    
    /**
     * Performs sensor type specific validation
     * @throws InfrastructureException if validation fails
     */
    private void validateSensorTypeSpecific() throws InfrastructureException {
        switch (sensorType.toLowerCase()) {
            case "temperature":
                if (!measurementUnit.matches("(?i)(°C|°F|K)")) {
                    logger.warning("Unusual temperature unit: " + measurementUnit);
                }
                break;
            case "humidity":
                if (!measurementUnit.matches("(?i)(%)")) {
                    logger.warning("Unusual humidity unit: " + measurementUnit);
                }
                if (maxRange > 100) {
                    throw new InfrastructureException("Humidity range cannot exceed 100%", 
                        getId(), getType(), "VALIDATE");
                }
                break;
            case "co2":
                if (!measurementUnit.matches("(?i)(ppm|mg/m3)")) {
                    logger.warning("Unusual CO2 unit: " + measurementUnit);
                }
                break;
            case "pressure":
                if (!measurementUnit.matches("(?i)(pa|kpa|mpa|bar|psi|atm)")) {
                    logger.warning("Unusual pressure unit: " + measurementUnit);
                }
                break;
            case "noise":
                if (!measurementUnit.matches("(?i)(db|dba|dbc)")) {
                    logger.warning("Unusual noise unit: " + measurementUnit);
                }
                break;
        }
    }
    
    /**
     * Gets a description of sensor capabilities
     * @return String describing sensor capabilities
     */
    private String getSensorCapabilities() {
        StringBuilder capabilities = new StringBuilder();
        
        capabilities.append("Monitoring");
        
        if (hasWirelessConnectivity) {
            capabilities.append(", Wireless Communication");
        } else {
            capabilities.append(", Wired Communication");
        }
        
        if (hasDataLogging) {
            capabilities.append(", Data Logging");
        }
        
        capabilities.append(", Real-time Alerts");
        
        if (samplingRate >= 60) {
            capabilities.append(", High-frequency Sampling");
        }
        
        if (accuracy >= 95) {
            capabilities.append(", High Precision");
        }
        
        return capabilities.toString();
    }
    
    /**
     * Gets a description of the measurement range
     * @return String describing the range classification
     */
    private String getMeasurementRangeDescription() {
        double range = maxRange - minRange;
        
        switch (sensorType.toLowerCase()) {
            case "temperature":
                if (range >= 200) return "Wide Range";
                else if (range >= 100) return "Standard Range";
                else return "Narrow Range";
            case "humidity":
                if (range >= 80) return "Full Range";
                else if (range >= 50) return "Standard Range";
                else return "Limited Range";
            case "co2":
                if (range >= 10000) return "High Range";
                else if (range >= 5000) return "Standard Range";
                else return "Low Range";
            default:
                if (range >= 1000) return "Wide Range";
                else if (range >= 100) return "Standard Range";
                else return "Narrow Range";
        }
    }
    
    /**
     * Calculates the number of data points collected per day
     * @return int representing data points per day
     */
    private int calculateDataPointsPerDay() {
        return samplingRate * 60 * 24; // samples/min * min/hour * hours/day
    }
    
    /**
     * Simulates taking a sensor reading
     * @return double representing a simulated sensor reading
     */
    public double simulateReading() {
        if (!operational) {
            return Double.NaN; // Not a Number for offline sensor
        }
        
        // Generate a random reading within the sensor's range
        double range = maxRange - minRange;
        double reading = minRange + (Math.random() * range);
        
        // Add some accuracy variation
        double accuracyFactor = (100 - accuracy) / 100.0;
        double variation = reading * accuracyFactor * (Math.random() - 0.5) * 2;
        
        return reading + variation;
    }
    
    /**
     * Checks if a reading is within alert threshold
     * @param reading The sensor reading to check
     * @return boolean true if alert should be triggered, false otherwise
     */
    public boolean shouldTriggerAlert(double reading) {
        try {
            double threshold = Double.parseDouble(alertThreshold);
            return reading >= threshold;
        } catch (NumberFormatException e) {
            // Handle non-numeric thresholds (could be "Motion Detected", etc.)
            return false;
        }
    }
    
    /**
     * Gets sensor statistics summary
     * @return String containing key statistics
     */
    public String getSensorStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("Sensor Statistics for ").append(getName()).append(":\n");
        stats.append("  Type: ").append(getSensorType()).append("\n");
        stats.append("  Measurement Range: ").append(getMinRange()).append(" - ")
             .append(getMaxRange()).append(" ").append(getMeasurementUnit()).append("\n");
        stats.append("  Accuracy: ").append(String.format("%.1f", getAccuracy())).append("%\n");
        stats.append("  Sampling Rate: ").append(getSamplingRate()).append(" samples/min\n");
        stats.append("  Data Points/Day: ").append(String.format("%,d", calculateDataPointsPerDay())).append("\n");
        stats.append("  Alert Threshold: ").append(getAlertThreshold()).append("\n");
        stats.append("  Capabilities: ").append(getSensorCapabilities()).append("\n");
        stats.append("  Power Source: ").append(isBatteryPowered() ? 
            "Battery (" + getBatteryLife() + " months)" : "Hardwired").append("\n");
        stats.append("  Status: ").append(operational ? "Operational" : "Offline").append("\n");
        
        return stats.toString();
    }
}
