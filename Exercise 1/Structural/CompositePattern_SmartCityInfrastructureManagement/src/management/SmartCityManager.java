package management;

import composite.InfrastructureComponent;
import composite.CompositeComponent;
import components.*;
import devices.*;
import exceptions.InfrastructureException;
import utils.Logger;
import utils.ValidationUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SmartCityManager class responsible for managing the entire smart city infrastructure.
 * This class implements the Facade pattern to provide a simplified interface
 * for managing complex infrastructure operations.
 * 
 * Follows the Single Responsibility Principle by focusing on
 * infrastructure management operations.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class SmartCityManager {
    
    private final Map<String, InfrastructureComponent> allComponents;
    private final Map<String, City> cities;
    private final Logger logger;
    private City currentCity;
    
    /**
     * Constructor for SmartCityManager
     */
    public SmartCityManager() {
        this.allComponents = new HashMap<>();
        this.cities = new HashMap<>();
        this.logger = Logger.getInstance();
        this.currentCity = null;
        
        logger.info("Smart City Manager initialized");
    }
    
    /**
     * Creates a new city and sets it as the current city
     * @param id Unique identifier for the city
     * @param name Name of the city
     * @param country Country where the city is located
     * @param state State/Province where the city is located
     * @param population Population of the city
     * @param area Area of the city in square kilometers
     * @return City the created city instance
     * @throws InfrastructureException if city creation fails
     */
    public City createCity(String id, String name, String country, String state,
                          long population, double area) throws InfrastructureException {
        
        ValidationUtils.validateId(id, "City ID");
        
        if (cities.containsKey(id)) {
            throw new InfrastructureException("City with ID '" + id + "' already exists");
        }
        
        City city = new City(id, name, country, state, population, area);
        cities.put(id, city);
        allComponents.put(id, city);
        currentCity = city;
        
        logger.info("Created and set current city: " + name + " (ID: " + id + ")");
        return city;
    }
    
    /**
     * Sets the current city for operations
     * @param cityId ID of the city to set as current
     * @throws InfrastructureException if city not found
     */
    public void setCurrentCity(String cityId) throws InfrastructureException {
        ValidationUtils.validateNotNullOrEmpty(cityId, "City ID");
        
        City city = cities.get(cityId);
        if (city == null) {
            throw InfrastructureException.componentNotFound(cityId, "SmartCityManager");
        }
        
        currentCity = city;
        logger.info("Current city set to: " + city.getName() + " (ID: " + cityId + ")");
    }
    
    /**
     * Gets the current city
     * @return City the current city, or null if none set
     */
    public City getCurrentCity() {
        return currentCity;
    }
    
    /**
     * Creates a new district in the current city
     * @param id Unique identifier for the district
     * @param name Name of the district
     * @param districtType Type of district
     * @param population Population of the district
     * @param area Area of the district
     * @param administrator Administrator of the district
     * @return District the created district instance
     * @throws InfrastructureException if district creation fails
     */
    public District createDistrict(String id, String name, String districtType,
                                  long population, double area, String administrator) 
                                  throws InfrastructureException {
        
        ensureCurrentCityExists();
        
        // Validate that ID doesn't already exist in the system
        if (allComponents.containsKey(id)) {
            throw new InfrastructureException("Component with ID '" + id + "' already exists in the system");
        }
        
        District district = new District(id, name, districtType, population, area, administrator);
        currentCity.addComponent(district);
        allComponents.put(id, district);
        
        logger.info("Created district: " + name + " in city: " + currentCity.getName());
        return district;
    }
    
    /**
     * Creates a new zone in a specified district
     * @param districtId ID of the parent district
     * @param id Unique identifier for the zone
     * @param name Name of the zone
     * @param zoneType Type of zone
     * @param function Primary function of the zone
     * @param area Area of the zone
     * @param securityLevel Security level required
     * @param accessControlRequired Whether access control is required
     * @return Zone the created zone instance
     * @throws InfrastructureException if zone creation fails
     */
    public Zone createZone(String districtId, String id, String name, String zoneType,
                          String function, double area, String securityLevel,
                          boolean accessControlRequired) throws InfrastructureException {
        
        District district = getDistrictById(districtId);
        
        // Validate that ID doesn't already exist in the system
        if (allComponents.containsKey(id)) {
            throw new InfrastructureException("Component with ID '" + id + "' already exists in the system");
        }
        
        Zone zone = new Zone(id, name, zoneType, function, area, securityLevel, accessControlRequired);
        district.addComponent(zone);
        allComponents.put(id, zone);
        
        logger.info("Created zone: " + name + " in district: " + district.getName());
        return zone;
    }
    
    /**
     * Creates a new building in a specified zone
     * @param zoneId ID of the parent zone
     * @param id Unique identifier for the building
     * @param name Name of the building
     * @param buildingType Type of building
     * @param numberOfFloors Number of floors
     * @param totalArea Total area of the building
     * @param yearBuilt Year when built
     * @param architect Architect name
     * @param energyRating Energy rating
     * @param hasElevator Whether has elevator
     * @param hasEmergencyExits Whether has emergency exits
     * @return Building the created building instance
     * @throws InfrastructureException if building creation fails
     */
    public Building createBuilding(String zoneId, String id, String name, String buildingType,
                                  int numberOfFloors, double totalArea, int yearBuilt,
                                  String architect, String energyRating, boolean hasElevator,
                                  boolean hasEmergencyExits) throws InfrastructureException {
        
        Zone zone = getZoneById(zoneId);
        
        // Validate that ID doesn't already exist in the system
        if (allComponents.containsKey(id)) {
            throw new InfrastructureException("Component with ID '" + id + "' already exists in the system");
        }
        
        Building building = new Building(id, name, buildingType, numberOfFloors, totalArea,
                                       yearBuilt, architect, energyRating, hasElevator, hasEmergencyExits);
        zone.addComponent(building);
        allComponents.put(id, building);
        
        logger.info("Created building: " + name + " in zone: " + zone.getName());
        return building;
    }
    
    /**
     * Creates a new floor in a specified building
     * @param buildingId ID of the parent building
     * @param id Unique identifier for the floor
     * @param name Name of the floor
     * @param floorNumber Floor number
     * @param floorType Type of floor
     * @param area Area of the floor
     * @param numberOfRooms Number of rooms
     * @param ceilingHeight Ceiling height
     * @param hvacZone HVAC zone
     * @param hasFireSuppression Whether has fire suppression
     * @param hasEmergencyLighting Whether has emergency lighting
     * @param occupancyLimit Occupancy limit
     * @return Floor the created floor instance
     * @throws InfrastructureException if floor creation fails
     */
    public Floor createFloor(String buildingId, String id, String name, int floorNumber,
                            String floorType, double area, int numberOfRooms, double ceilingHeight,
                            String hvacZone, boolean hasFireSuppression, boolean hasEmergencyLighting,
                            int occupancyLimit) throws InfrastructureException {
        
        Building building = getBuildingById(buildingId);
        
        // Validate that ID doesn't already exist in the system
        if (allComponents.containsKey(id)) {
            throw new InfrastructureException("Component with ID '" + id + "' already exists in the system");
        }
        
        Floor floor = new Floor(id, name, floorNumber, floorType, area, numberOfRooms,
                               ceilingHeight, hvacZone, hasFireSuppression, hasEmergencyLighting,
                               occupancyLimit);
        building.addComponent(floor);
        allComponents.put(id, floor);
        
        logger.info("Created floor: " + name + " in building: " + building.getName());
        return floor;
    }
    
    /**
     * Creates a new smart light device in a specified floor
     * @param floorId ID of the parent floor
     * @param id Unique identifier for the light
     * @param name Name of the light
     * @param manufacturer Manufacturer
     * @param model Model
     * @param serialNumber Serial number
     * @param energyConsumption Energy consumption
     * @param brightness Brightness level
     * @param colorTemperature Color temperature
     * @param isDimmable Whether dimmable
     * @param hasMotionSensor Whether has motion sensor
     * @param hasAmbientLightSensor Whether has ambient light sensor
     * @param lifespan Expected lifespan
     * @param protocol Communication protocol
     * @return SmartLight the created smart light instance
     * @throws InfrastructureException if light creation fails
     */
    public SmartLight createSmartLight(String floorId, String id, String name, String manufacturer,
                                      String model, String serialNumber, double energyConsumption,
                                      int brightness, String colorTemperature, boolean isDimmable,
                                      boolean hasMotionSensor, boolean hasAmbientLightSensor,
                                      int lifespan, String protocol) throws InfrastructureException {
        
        Floor floor = getFloorById(floorId);
        
        // Validate that ID doesn't already exist in the system
        if (allComponents.containsKey(id)) {
            throw new InfrastructureException("Component with ID '" + id + "' already exists in the system");
        }
        
        SmartLight light = new SmartLight(id, name, manufacturer, model, serialNumber,
                                         energyConsumption, brightness, colorTemperature, isDimmable,
                                         hasMotionSensor, hasAmbientLightSensor, lifespan, protocol);
        floor.addComponent(light);
        allComponents.put(id, light);
        
        logger.info("Created smart light: " + name + " on floor: " + floor.getName());
        return light;
    }
    
    /**
     * Creates a new air conditioner device in a specified floor
     * @param floorId ID of the parent floor
     * @param id Unique identifier for the AC
     * @param name Name of the AC
     * @param manufacturer Manufacturer
     * @param model Model
     * @param serialNumber Serial number
     * @param energyConsumption Energy consumption
     * @param capacity Cooling capacity
     * @param targetTemperature Target temperature
     * @param mode Operating mode
     * @param fanSpeed Fan speed
     * @param hasWiFi Whether has WiFi
     * @param hasScheduling Whether has scheduling
     * @param refrigerant Refrigerant type
     * @param seer SEER rating
     * @param hasInverterTechnology Whether has inverter technology
     * @return AirConditioner the created air conditioner instance
     * @throws InfrastructureException if AC creation fails
     */
    public AirConditioner createAirConditioner(String floorId, String id, String name,
                                              String manufacturer, String model, String serialNumber,
                                              double energyConsumption, double capacity, int targetTemperature,
                                              String mode, int fanSpeed, boolean hasWiFi,
                                              boolean hasScheduling, String refrigerant, double seer,
                                              boolean hasInverterTechnology) throws InfrastructureException {
        
        Floor floor = getFloorById(floorId);
        
        // Validate that ID doesn't already exist in the system
        if (allComponents.containsKey(id)) {
            throw new InfrastructureException("Component with ID '" + id + "' already exists in the system");
        }
        
        AirConditioner ac = new AirConditioner(id, name, manufacturer, model, serialNumber,
                                              energyConsumption, capacity, targetTemperature, mode,
                                              fanSpeed, hasWiFi, hasScheduling, refrigerant, seer,
                                              hasInverterTechnology);
        floor.addComponent(ac);
        allComponents.put(id, ac);
        
        logger.info("Created air conditioner: " + name + " on floor: " + floor.getName());
        return ac;
    }
    
    /**
     * Creates a new sensor device in a specified floor
     * @param floorId ID of the parent floor
     * @param id Unique identifier for the sensor
     * @param name Name of the sensor
     * @param manufacturer Manufacturer
     * @param model Model
     * @param serialNumber Serial number
     * @param energyConsumption Energy consumption
     * @param sensorType Type of sensor
     * @param measurementUnit Measurement unit
     * @param minRange Minimum range
     * @param maxRange Maximum range
     * @param accuracy Accuracy percentage
     * @param samplingRate Sampling rate
     * @param hasWirelessConnectivity Whether has wireless connectivity
     * @param hasDataLogging Whether has data logging
     * @param alertThreshold Alert threshold
     * @param batteryLife Battery life
     * @return Sensor the created sensor instance
     * @throws InfrastructureException if sensor creation fails
     */
    public Sensor createSensor(String floorId, String id, String name, String manufacturer,
                              String model, String serialNumber, double energyConsumption,
                              String sensorType, String measurementUnit, double minRange,
                              double maxRange, double accuracy, int samplingRate,
                              boolean hasWirelessConnectivity, boolean hasDataLogging,
                              String alertThreshold, int batteryLife) throws InfrastructureException {
        
        Floor floor = getFloorById(floorId);
        
        Sensor sensor = new Sensor(id, name, manufacturer, model, serialNumber, energyConsumption,
                                  sensorType, measurementUnit, minRange, maxRange, accuracy,
                                  samplingRate, hasWirelessConnectivity, hasDataLogging,
                                  alertThreshold, batteryLife);
        floor.addComponent(sensor);
        allComponents.put(id, sensor);
        
        logger.info("Created sensor: " + name + " on floor: " + floor.getName());
        return sensor;
    }
    
    /**
     * Removes a component from the infrastructure
     * @param componentId ID of the component to remove
     * @return boolean true if removed successfully, false if not found
     * @throws InfrastructureException if removal fails
     */
    public boolean removeComponent(String componentId) throws InfrastructureException {
        ValidationUtils.validateNotNullOrEmpty(componentId, "Component ID");
        
        InfrastructureComponent component = allComponents.get(componentId);
        if (component == null) {
            return false;
        }
        
        // Remove from parent if it's not a city
        if (!(component instanceof City)) {
            removeFromParent(componentId);
        } else {
            // If it's a city, remove from cities map
            cities.remove(componentId);
            if (currentCity != null && currentCity.getId().equals(componentId)) {
                currentCity = null;
            }
        }
        
        allComponents.remove(componentId);
        logger.info("Removed component: " + component.getName() + " (ID: " + componentId + ")");
        
        return true;
    }
    
    /**
     * Gets a component by its ID
     * @param componentId ID of the component to retrieve
     * @return InfrastructureComponent if found, null otherwise
     */
    public InfrastructureComponent getComponent(String componentId) {
        return allComponents.get(componentId);
    }
    
    /**
     * Gets all cities
     * @return List of all cities
     */
    public List<City> getAllCities() {
        return new ArrayList<>(cities.values());
    }
    
    /**
     * Displays the complete infrastructure hierarchy
     */
    public void displayInfrastructure() {
        if (cities.isEmpty()) {
            System.out.println("No cities configured in the smart city infrastructure.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SMART CITY INFRASTRUCTURE OVERVIEW");
        System.out.println("=".repeat(80));
        
        for (City city : cities.values()) {
            city.display("");
            System.out.println();
        }
        
        System.out.println("=".repeat(80));
        System.out.println("Total Cities: " + cities.size());
        System.out.println("Total Components: " + allComponents.size());
        System.out.println("=".repeat(80));
    }
    
    /**
     * Displays the current city infrastructure
     */
    public void displayCurrentCityInfrastructure() {
        if (currentCity == null) {
            System.out.println("No current city set. Use 'Set Current City' option first.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("CURRENT CITY INFRASTRUCTURE");
        System.out.println("=".repeat(60));
        currentCity.display("");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Performs maintenance on all infrastructure components
     */
    public void performSystemMaintenance() {
        logger.info("Starting system-wide maintenance");
        
        long startTime = System.currentTimeMillis();
        int componentCount = 0;
        
        for (City city : cities.values()) {
            try {
                city.performMaintenance();
                componentCount++;
            } catch (Exception e) {
                logger.error("Maintenance failed for city " + city.getId(), e);
            }
        }
        
        long duration = System.currentTimeMillis() - startTime;
        logger.logPerformance("System Maintenance", duration);
        logger.info("System-wide maintenance completed. Maintained " + componentCount + " cities.");
        
        System.out.println("System maintenance completed successfully!");
        System.out.println("Maintained " + componentCount + " cities in " + duration + "ms");
    }
    
    /**
     * Validates the entire infrastructure
     * @throws InfrastructureException if validation fails
     */
    public void validateInfrastructure() throws InfrastructureException {
        logger.info("Starting infrastructure validation");
        
        for (City city : cities.values()) {
            city.validateHierarchy();
        }
        
        logger.info("Infrastructure validation completed successfully");
        System.out.println("Infrastructure validation passed!");
    }
    
    /**
     * Gets infrastructure statistics
     * @return String containing infrastructure statistics
     */
    public String getInfrastructureStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("SMART CITY INFRASTRUCTURE STATISTICS\n");
        stats.append("=".repeat(40)).append("\n");
        stats.append("Total Cities: ").append(cities.size()).append("\n");
        stats.append("Total Components: ").append(allComponents.size()).append("\n");
        
        // Calculate total energy consumption
        double totalEnergy = 0;
        for (City city : cities.values()) {
            totalEnergy += city.getEnergyConsumption();
        }
        stats.append("Total Energy Consumption: ").append(String.format("%.2f", totalEnergy)).append(" kWh\n");
        
        // Count component types
        Map<String, Integer> componentCounts = new HashMap<>();
        for (InfrastructureComponent component : allComponents.values()) {
            componentCounts.merge(component.getType(), 1, Integer::sum);
        }
        
        stats.append("\nComponent Breakdown:\n");
        for (Map.Entry<String, Integer> entry : componentCounts.entrySet()) {
            stats.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        return stats.toString();
    }
    
    // Helper methods
    
    private void ensureCurrentCityExists() throws InfrastructureException {
        if (currentCity == null) {
            throw new InfrastructureException("No current city set. Create or select a city first.");
        }
    }
    
    private District getDistrictById(String districtId) throws InfrastructureException {
        InfrastructureComponent component = allComponents.get(districtId);
        if (!(component instanceof District)) {
            throw InfrastructureException.componentNotFound(districtId, "District");
        }
        return (District) component;
    }
    
    private Zone getZoneById(String zoneId) throws InfrastructureException {
        InfrastructureComponent component = allComponents.get(zoneId);
        if (!(component instanceof Zone)) {
            throw InfrastructureException.componentNotFound(zoneId, "Zone");
        }
        return (Zone) component;
    }
    
    private Building getBuildingById(String buildingId) throws InfrastructureException {
        InfrastructureComponent component = allComponents.get(buildingId);
        if (!(component instanceof Building)) {
            throw InfrastructureException.componentNotFound(buildingId, "Building");
        }
        return (Building) component;
    }
    
    private Floor getFloorById(String floorId) throws InfrastructureException {
        InfrastructureComponent component = allComponents.get(floorId);
        if (!(component instanceof Floor)) {
            throw InfrastructureException.componentNotFound(floorId, "Floor");
        }
        return (Floor) component;
    }
    
    private void removeFromParent(String componentId) throws InfrastructureException {
        // Find the parent component and remove the child
        for (InfrastructureComponent component : allComponents.values()) {
            if (component instanceof CompositeComponent) {
                CompositeComponent composite = (CompositeComponent) component;
                if (composite.removeComponent(componentId)) {
                    return;
                }
            }
        }
        throw new InfrastructureException("Could not find parent for component: " + componentId);
    }
}
