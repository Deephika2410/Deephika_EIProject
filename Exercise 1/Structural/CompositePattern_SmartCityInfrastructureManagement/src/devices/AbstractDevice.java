package devices;

import composite.InfrastructureComponent;
import exceptions.InfrastructureException;
import utils.Logger;
import utils.ValidationUtils;

/**
 * Abstract base class for all smart devices in the infrastructure.
 * This class provides common functionality for leaf components in the
 * Composite Design Pattern.
 * 
 * Devices are the leaf nodes in the hierarchy that cannot contain
 * other components but provide specific smart city functionality.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public abstract class AbstractDevice implements InfrastructureComponent {
    
    protected final String id;
    protected final String name;
    protected final String type;
    protected final String manufacturer;
    protected final String model;
    protected final String serialNumber;
    protected final double energyConsumption; // kWh
    protected boolean operational;
    protected final Logger logger;
    
    /**
     * Constructor for creating a device
     * @param id Unique identifier for the device
     * @param name Display name for the device
     * @param type Type of the device
     * @param manufacturer Manufacturer of the device
     * @param model Model of the device
     * @param serialNumber Serial number of the device
     * @param energyConsumption Energy consumption in kWh
     * @throws InfrastructureException if parameters are invalid
     */
    protected AbstractDevice(String id, String name, String type, String manufacturer,
                           String model, String serialNumber, double energyConsumption) 
                           throws InfrastructureException {
        // Defensive programming - validate inputs
        ValidationUtils.validateId(id, "Device ID");
        ValidationUtils.validateName(name, "Device name");
        ValidationUtils.validateNotNullOrEmpty(type, "Device type");
        ValidationUtils.validateNotNullOrEmpty(manufacturer, "Manufacturer");
        ValidationUtils.validateNotNullOrEmpty(model, "Model");
        ValidationUtils.validateNotNullOrEmpty(serialNumber, "Serial number");
        ValidationUtils.validatePositive(energyConsumption, "Energy consumption");
        
        this.id = id.trim();
        this.name = name.trim();
        this.type = type.trim();
        this.manufacturer = manufacturer.trim();
        this.model = model.trim();
        this.serialNumber = serialNumber.trim();
        this.energyConsumption = energyConsumption;
        this.operational = true;
        this.logger = Logger.getInstance();
        
        logger.info("Created " + type + " device: " + name + " (Model: " + model + 
                   ", S/N: " + serialNumber + ", Energy: " + energyConsumption + " kWh)");
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
    
    /**
     * Gets the manufacturer of the device
     * @return String representing the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }
    
    /**
     * Gets the model of the device
     * @return String representing the model
     */
    public String getModel() {
        return model;
    }
    
    /**
     * Gets the serial number of the device
     * @return String representing the serial number
     */
    public String getSerialNumber() {
        return serialNumber;
    }
    
    @Override
    public double getEnergyConsumption() {
        return operational ? energyConsumption : 0.0;
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
    public void display(String indentation) {
        String status = operational ? "[OPERATIONAL]" : "[OFFLINE]";
        System.out.println(indentation + getType() + ": " + getName() + 
            " (ID: " + getId() + ") " + status);
        System.out.println(indentation + "  Manufacturer: " + getManufacturer());
        System.out.println(indentation + "  Model: " + getModel());
        System.out.println(indentation + "  Serial Number: " + getSerialNumber());
        System.out.println(indentation + "  Energy: " + String.format("%.2f", getEnergyConsumption()) + " kWh");
        
        // Display device-specific information
        displayDeviceSpecificInfo(indentation + "  ");
    }
    
    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("DEVICE INFORMATION\n");
        info.append("-".repeat(15)).append("\n");
        info.append("Name: ").append(getName()).append("\n");
        info.append("ID: ").append(getId()).append("\n");
        info.append("Type: ").append(getType()).append("\n");
        info.append("Manufacturer: ").append(getManufacturer()).append("\n");
        info.append("Model: ").append(getModel()).append("\n");
        info.append("Serial Number: ").append(getSerialNumber()).append("\n");
        info.append("Status: ").append(operational ? "Operational" : "Offline").append("\n");
        info.append("Energy Consumption: ").append(String.format("%.2f", getEnergyConsumption())).append(" kWh\n");
        
        // Add device-specific detailed information
        info.append(getDeviceSpecificInfo());
        
        return info.toString();
    }
    
    @Override
    public void performMaintenance() {
        logger.info("Performing maintenance on " + getType() + " '" + getName() + "'");
        
        // Common device maintenance
        logger.info("Checking device connectivity and communication");
        logger.info("Updating device firmware if available");
        logger.info("Cleaning device sensors and components");
        logger.info("Verifying device calibration");
        
        // Device-specific maintenance
        performDeviceSpecificMaintenance();
        
        logger.info("Maintenance completed for " + getType() + " '" + getName() + "'");
    }
    
    @Override
    public void validate() throws InfrastructureException {
        // Basic device validation
        if (id == null || id.trim().isEmpty()) {
            throw new InfrastructureException("Device ID cannot be null or empty");
        }
        
        if (name == null || name.trim().isEmpty()) {
            throw new InfrastructureException("Device name cannot be null or empty");
        }
        
        if (type == null || type.trim().isEmpty()) {
            throw new InfrastructureException("Device type cannot be null or empty");
        }
        
        if (manufacturer == null || manufacturer.trim().isEmpty()) {
            throw new InfrastructureException("Device manufacturer cannot be null or empty");
        }
        
        if (model == null || model.trim().isEmpty()) {
            throw new InfrastructureException("Device model cannot be null or empty");
        }
        
        if (serialNumber == null || serialNumber.trim().isEmpty()) {
            throw new InfrastructureException("Device serial number cannot be null or empty");
        }
        
        if (energyConsumption < 0) {
            throw new InfrastructureException("Device energy consumption cannot be negative");
        }
        
        // Device-specific validation
        validateDeviceSpecific();
    }
    
    /**
     * Displays device-specific information
     * @param indentation String for formatting the display
     */
    protected abstract void displayDeviceSpecificInfo(String indentation);
    
    /**
     * Gets device-specific detailed information
     * @return String containing device-specific details
     */
    protected abstract String getDeviceSpecificInfo();
    
    /**
     * Performs device-specific maintenance tasks
     */
    protected abstract void performDeviceSpecificMaintenance();
    
    /**
     * Performs device-specific validation
     * @throws InfrastructureException if device-specific validation fails
     */
    protected abstract void validateDeviceSpecific() throws InfrastructureException;
    
    /**
     * Gets the device identifier string (manufacturer + model + serial)
     * @return String representing the device identifier
     */
    public String getDeviceIdentifier() {
        return manufacturer + " " + model + " (S/N: " + serialNumber + ")";
    }
    
    /**
     * Checks if the device is energy efficient (consumption below threshold)
     * @param threshold Energy consumption threshold in kWh
     * @return boolean true if device is energy efficient, false otherwise
     */
    public boolean isEnergyEfficient(double threshold) {
        return energyConsumption <= threshold;
    }
    
    /**
     * Gets device status summary
     * @return String containing device status information
     */
    public String getStatusSummary() {
        StringBuilder status = new StringBuilder();
        status.append("Device: ").append(getName()).append(" (").append(getType()).append(")\n");
        status.append("Status: ").append(operational ? "Operational" : "Offline").append("\n");
        status.append("Energy: ").append(String.format("%.2f", getEnergyConsumption())).append(" kWh\n");
        status.append("Identifier: ").append(getDeviceIdentifier()).append("\n");
        
        return status.toString();
    }
    
    /**
     * Toggles the operational status of the device
     */
    public void toggleOperationalStatus() {
        setOperational(!operational);
    }
    
    /**
     * Resets the device (turns off and on)
     */
    public void reset() {
        logger.info("Resetting device: " + getName());
        setOperational(false);
        
        // Simulate reset delay
        try {
            Thread.sleep(100); // Short delay to simulate reset
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        setOperational(true);
        logger.info("Device reset completed: " + getName());
    }
}
