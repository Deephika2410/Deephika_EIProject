package ui;

import management.SmartCityManager;
import components.*;
import devices.*;
import exceptions.InfrastructureException;
import utils.Logger;
import utils.ValidationUtils;
import java.util.Scanner;
import java.util.List;

/**
 * ConsoleInterface class providing a user-friendly console interface
 * for interacting with the Smart City Infrastructure Management System.
 * 
 * This class implements a menu-driven interface that allows users to
 * dynamically build and manage city infrastructure at runtime.
 * 
 * Follows the Single Responsibility Principle by focusing solely
 * on user interface concerns.
 * 
 * @author Smart City Infrastructure Team
 * @version 1.0
 */
public class ConsoleInterface {
    
    private final SmartCityManager cityManager;
    private final Scanner scanner;
    private final Logger logger;
    private boolean running;
    
    /**
     * Constructor for ConsoleInterface
     */
    public ConsoleInterface() {
        this.cityManager = new SmartCityManager();
        this.scanner = new Scanner(System.in);
        this.logger = Logger.getInstance();
        this.running = true;
        
        // Configure logger for console output
        logger.setConsoleOutput(true);
        logger.setFileOutput(true);
        logger.setLogLevel(Logger.LogLevel.INFO);
    }
    
    /**
     * Starts the console interface
     */
    public void start() {
        logger.logSystemStartup();
        
        displayWelcomeMessage();
        
        while (running) {
            try {
                displayMainMenu();
                int choice = getIntInput("Enter your choice");
                handleMainMenuChoice(choice);
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                logger.error("Console interface error", e);
                
                System.out.print("Would you like to continue? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (!response.startsWith("y")) {
                    running = false;
                }
            }
        }
        
        displayGoodbyeMessage();
        logger.logSystemShutdown();
    }
    
    /**
     * Displays the welcome message
     */
    private void displayWelcomeMessage() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("               SMART CITY INFRASTRUCTURE MANAGEMENT SYSTEM");
        System.out.println("                         Composite Design Pattern Demo");
        System.out.println("=".repeat(80));
        System.out.println("Welcome to the Smart City Infrastructure Management System!");
        System.out.println("This system demonstrates the Composite Design Pattern by allowing you to");
        System.out.println("build a hierarchical city infrastructure dynamically at runtime.");
        System.out.println();
        System.out.println("Hierarchy: City -> Districts -> Zones -> Buildings -> Floors -> Devices");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Displays the main menu
     */
    private void displayMainMenu() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("                    MAIN MENU");
        System.out.println("-".repeat(60));
        System.out.println("1.  Create New City");
        System.out.println("2.  Set Current City");
        System.out.println("3.  Create District");
        System.out.println("4.  Create Zone");
        System.out.println("5.  Create Building");
        System.out.println("6.  Create Floor");
        System.out.println("7.  Create Smart Light");
        System.out.println("8.  Create Air Conditioner");
        System.out.println("9.  Create Sensor");
        System.out.println("10. Remove Component");
        System.out.println("11. Display Current City Infrastructure");
        System.out.println("12. Display All Infrastructure");
        System.out.println("13. View Infrastructure Statistics");
        System.out.println("14. Perform System Maintenance");
        System.out.println("15. Validate Infrastructure");
        System.out.println("16. View Component Details");
        System.out.println("17. List All Cities");
        System.out.println("18. System Settings");
        System.out.println("0.  Exit");
        System.out.println("-".repeat(60));
    }
    
    /**
     * Handles main menu choice selection
     * @param choice The menu choice selected by user
     */
    private void handleMainMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    createCity();
                    break;
                case 2:
                    setCurrentCity();
                    break;
                case 3:
                    createDistrict();
                    break;
                case 4:
                    createZone();
                    break;
                case 5:
                    createBuilding();
                    break;
                case 6:
                    createFloor();
                    break;
                case 7:
                    createSmartLight();
                    break;
                case 8:
                    createAirConditioner();
                    break;
                case 9:
                    createSensor();
                    break;
                case 10:
                    removeComponent();
                    break;
                case 11:
                    cityManager.displayCurrentCityInfrastructure();
                    break;
                case 12:
                    cityManager.displayInfrastructure();
                    break;
                case 13:
                    displayStatistics();
                    break;
                case 14:
                    performMaintenance();
                    break;
                case 15:
                    validateInfrastructure();
                    break;
                case 16:
                    viewComponentDetails();
                    break;
                case 17:
                    listAllCities();
                    break;
                case 18:
                    systemSettings();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (InfrastructureException e) {
            System.err.println("Infrastructure Error: " + e.getMessage());
            logger.error("Infrastructure operation failed", e);
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            logger.error("Unexpected error in menu handling", e);
        }
    }
    
    /**
     * Creates a new city
     */
    private void createCity() throws InfrastructureException {
        System.out.println("\n--- CREATE NEW CITY ---");
        
        String id = getStringInput("Enter City ID");
        String name = getStringInput("Enter City Name");
        String country = getStringInput("Enter Country");
        String state = getStringInput("Enter State/Province");
        long population = getLongInput("Enter Population");
        double area = getDoubleInput("Enter Area (sq km)");
        
        City city = cityManager.createCity(id, name, country, state, population, area);
        
        System.out.println("SUCCESS: City '" + city.getName() + "' created and set as current city!");
        System.out.println(city.getDetailedInfo());
    }
    
    /**
     * Sets the current city for operations
     */
    private void setCurrentCity() throws InfrastructureException {
        System.out.println("\n--- SET CURRENT CITY ---");
        
        List<City> cities = cityManager.getAllCities();
        if (cities.isEmpty()) {
            System.out.println("No cities available. Please create a city first.");
            return;
        }
        
        System.out.println("Available Cities:");
        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            System.out.println((i + 1) + ". " + city.getName() + " (ID: " + city.getId() + 
                             ") - " + city.getState() + ", " + city.getCountry());
        }
        
        int choice = getIntInput("Select city number") - 1;
        if (choice >= 0 && choice < cities.size()) {
            City selectedCity = cities.get(choice);
            cityManager.setCurrentCity(selectedCity.getId());
            System.out.println("SUCCESS: Current city set to '" + selectedCity.getName() + "'");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Creates a new district
     */
    private void createDistrict() throws InfrastructureException {
        System.out.println("\n--- CREATE DISTRICT ---");
        
        if (cityManager.getCurrentCity() == null) {
            System.out.println("No current city set. Please create or select a city first.");
            return;
        }
        
        String id = getStringInput("Enter District ID");
        String name = getStringInput("Enter District Name");
        
        System.out.println("District Types: Residential, Commercial, Industrial, Mixed, Educational, Healthcare");
        String districtType = getStringInput("Enter District Type");
        
        long population = getLongInput("Enter District Population");
        double area = getDoubleInput("Enter District Area (sq km)");
        String administrator = getStringInput("Enter Administrator Name");
        
        District district = cityManager.createDistrict(id, name, districtType, population, area, administrator);
        
        System.out.println("SUCCESS: District '" + district.getName() + "' created!");
        System.out.println(district.getDetailedInfo());
    }
    
    /**
     * Creates a new zone
     */
    private void createZone() throws InfrastructureException {
        System.out.println("\n--- CREATE ZONE ---");
        
        String districtId = getStringInput("Enter Parent District ID");
        String id = getStringInput("Enter Zone ID");
        String name = getStringInput("Enter Zone Name");
        
        System.out.println("Zone Types: Residential, Office, Retail, Manufacturing, Warehouse, Educational, Healthcare, Entertainment, Transportation, Utilities, Mixed");
        String zoneType = getStringInput("Enter Zone Type");
        
        String function = getStringInput("Enter Primary Function");
        double area = getDoubleInput("Enter Zone Area (sq m)");
        
        System.out.println("Security Levels: Low, Medium, High, Critical");
        String securityLevel = getStringInput("Enter Security Level");
        
        boolean accessControl = getBooleanInput("Require Access Control? (y/n)");
        
        Zone zone = cityManager.createZone(districtId, id, name, zoneType, function, area, securityLevel, accessControl);
        
        System.out.println("SUCCESS: Zone '" + zone.getName() + "' created!");
        System.out.println(zone.getDetailedInfo());
    }
    
    /**
     * Creates a new building
     */
    private void createBuilding() throws InfrastructureException {
        System.out.println("\n--- CREATE BUILDING ---");
        
        String zoneId = getStringInput("Enter Parent Zone ID");
        String id = getStringInput("Enter Building ID");
        String name = getStringInput("Enter Building Name");
        
        System.out.println("Building Types: Office, Residential, Retail, Industrial, Warehouse, Hospital, School, Hotel, Parking, Mixed-Use, Government");
        String buildingType = getStringInput("Enter Building Type");
        
        int numberOfFloors = getIntInput("Enter Number of Floors");
        double totalArea = getDoubleInput("Enter Total Area (sq m)");
        int yearBuilt = getIntInput("Enter Year Built");
        String architect = getStringInput("Enter Architect Name");
        
        System.out.println("Energy Ratings: A+, A, B, C, D, E, F");
        String energyRating = getStringInput("Enter Energy Rating");
        
        boolean hasElevator = getBooleanInput("Has Elevator? (y/n)");
        boolean hasEmergencyExits = getBooleanInput("Has Emergency Exits? (y/n)");
        
        Building building = cityManager.createBuilding(zoneId, id, name, buildingType, numberOfFloors,
                                                      totalArea, yearBuilt, architect, energyRating,
                                                      hasElevator, hasEmergencyExits);
        
        System.out.println("SUCCESS: Building '" + building.getName() + "' created!");
        System.out.println(building.getDetailedInfo());
    }
    
    /**
     * Creates a new floor
     */
    private void createFloor() throws InfrastructureException {
        System.out.println("\n--- CREATE FLOOR ---");
        
        String buildingId = getStringInput("Enter Parent Building ID");
        String id = getStringInput("Enter Floor ID");
        String name = getStringInput("Enter Floor Name");
        int floorNumber = getIntInput("Enter Floor Number (0 for ground, negative for basement)");
        
        System.out.println("Floor Types: Office, Residential, Retail, Industrial, Storage, Parking, Mechanical, Lobby, Restaurant, Conference, Laboratory, Medical");
        String floorType = getStringInput("Enter Floor Type");
        
        double area = getDoubleInput("Enter Floor Area (sq m)");
        int numberOfRooms = getIntInput("Enter Number of Rooms");
        double ceilingHeight = getDoubleInput("Enter Ceiling Height (m)");
        String hvacZone = getStringInput("Enter HVAC Zone");
        boolean hasFireSuppression = getBooleanInput("Has Fire Suppression System? (y/n)");
        boolean hasEmergencyLighting = getBooleanInput("Has Emergency Lighting? (y/n)");
        int occupancyLimit = getIntInput("Enter Occupancy Limit");
        
        Floor floor = cityManager.createFloor(buildingId, id, name, floorNumber, floorType, area,
                                             numberOfRooms, ceilingHeight, hvacZone, hasFireSuppression,
                                             hasEmergencyLighting, occupancyLimit);
        
        System.out.println("SUCCESS: Floor '" + floor.getName() + "' created!");
        System.out.println(floor.getDetailedInfo());
    }
    
    /**
     * Creates a new smart light
     */
    private void createSmartLight() throws InfrastructureException {
        System.out.println("\n--- CREATE SMART LIGHT ---");
        
        String floorId = getStringInput("Enter Parent Floor ID");
        String id = getStringInput("Enter Light ID");
        String name = getStringInput("Enter Light Name");
        String manufacturer = getStringInput("Enter Manufacturer");
        String model = getStringInput("Enter Model");
        String serialNumber = getStringInput("Enter Serial Number");
        double energyConsumption = getDoubleInput("Enter Energy Consumption (kWh)");
        int brightness = getIntInput("Enter Brightness Level (0-100)");
        
        System.out.println("Color Temperatures: Warm, Cool, Daylight, RGB, Tunable");
        String colorTemperature = getStringInput("Enter Color Temperature");
        
        boolean isDimmable = getBooleanInput("Is Dimmable? (y/n)");
        boolean hasMotionSensor = getBooleanInput("Has Motion Sensor? (y/n)");
        boolean hasAmbientLightSensor = getBooleanInput("Has Ambient Light Sensor? (y/n)");
        int lifespan = getIntInput("Enter Expected Lifespan (hours)");
        
        System.out.println("Protocols: WiFi, Zigbee, Bluetooth, Z-Wave, Thread, Matter");
        String protocol = getStringInput("Enter Communication Protocol");
        
        SmartLight light = cityManager.createSmartLight(floorId, id, name, manufacturer, model,
                                                       serialNumber, energyConsumption, brightness,
                                                       colorTemperature, isDimmable, hasMotionSensor,
                                                       hasAmbientLightSensor, lifespan, protocol);
        
        System.out.println("SUCCESS: Smart Light '" + light.getName() + "' created!");
        System.out.println(light.getDetailedInfo());
    }
    
    /**
     * Creates a new air conditioner
     */
    private void createAirConditioner() throws InfrastructureException {
        System.out.println("\n--- CREATE AIR CONDITIONER ---");
        
        String floorId = getStringInput("Enter Parent Floor ID");
        String id = getStringInput("Enter AC ID");
        String name = getStringInput("Enter AC Name");
        String manufacturer = getStringInput("Enter Manufacturer");
        String model = getStringInput("Enter Model");
        String serialNumber = getStringInput("Enter Serial Number");
        double energyConsumption = getDoubleInput("Enter Energy Consumption (kWh)");
        double capacity = getDoubleInput("Enter Capacity (BTU/hr)");
        int targetTemperature = getIntInput("Enter Target Temperature (°C)");
        
        System.out.println("Modes: Cool, Heat, Auto, Fan, Dry, Off");
        String mode = getStringInput("Enter Operating Mode");
        
        int fanSpeed = getIntInput("Enter Fan Speed (1-5)");
        boolean hasWiFi = getBooleanInput("Has WiFi? (y/n)");
        boolean hasScheduling = getBooleanInput("Has Scheduling? (y/n)");
        
        System.out.println("Refrigerants: R-32, R-410A, R-22, R-134a, R-407C, R-290");
        String refrigerant = getStringInput("Enter Refrigerant Type");
        
        double seer = getDoubleInput("Enter SEER Rating");
        boolean hasInverterTechnology = getBooleanInput("Has Inverter Technology? (y/n)");
        
        AirConditioner ac = cityManager.createAirConditioner(floorId, id, name, manufacturer, model,
                                                            serialNumber, energyConsumption, capacity,
                                                            targetTemperature, mode, fanSpeed, hasWiFi,
                                                            hasScheduling, refrigerant, seer, hasInverterTechnology);
        
        System.out.println("SUCCESS: Air Conditioner '" + ac.getName() + "' created!");
        System.out.println(ac.getDetailedInfo());
    }
    
    /**
     * Creates a new sensor
     */
    private void createSensor() throws InfrastructureException {
        System.out.println("\n--- CREATE SENSOR ---");
        
        String floorId = getStringInput("Enter Parent Floor ID");
        String id = getStringInput("Enter Sensor ID");
        String name = getStringInput("Enter Sensor Name");
        String manufacturer = getStringInput("Enter Manufacturer");
        String model = getStringInput("Enter Model");
        String serialNumber = getStringInput("Enter Serial Number");
        double energyConsumption = getDoubleInput("Enter Energy Consumption (kWh)");
        
        System.out.println("Sensor Types: Temperature, Humidity, Motion, CO2, CO, Air Quality, Noise, Light, Pressure, Smoke, Gas, Water Level, Vibration, Occupancy, Door/Window, Proximity");
        String sensorType = getStringInput("Enter Sensor Type");
        
        String measurementUnit = getStringInput("Enter Measurement Unit (e.g., °C, %, ppm)");
        double minRange = getDoubleInput("Enter Minimum Range");
        double maxRange = getDoubleInput("Enter Maximum Range");
        double accuracy = getDoubleInput("Enter Accuracy (%)");
        int samplingRate = getIntInput("Enter Sampling Rate (samples/min)");
        boolean hasWirelessConnectivity = getBooleanInput("Has Wireless Connectivity? (y/n)");
        boolean hasDataLogging = getBooleanInput("Has Data Logging? (y/n)");
        String alertThreshold = getStringInput("Enter Alert Threshold");
        int batteryLife = getIntInput("Enter Battery Life (months, 0 for hardwired)");
        
        Sensor sensor = cityManager.createSensor(floorId, id, name, manufacturer, model, serialNumber,
                                                energyConsumption, sensorType, measurementUnit, minRange,
                                                maxRange, accuracy, samplingRate, hasWirelessConnectivity,
                                                hasDataLogging, alertThreshold, batteryLife);
        
        System.out.println("SUCCESS: Sensor '" + sensor.getName() + "' created!");
        System.out.println(sensor.getDetailedInfo());
    }
    
    /**
     * Removes a component from the infrastructure
     */
    private void removeComponent() throws InfrastructureException {
        System.out.println("\n--- REMOVE COMPONENT ---");
        
        String componentId = getStringInput("Enter Component ID to remove");
        
        boolean removed = cityManager.removeComponent(componentId);
        if (removed) {
            System.out.println("SUCCESS: Component removed successfully!");
        } else {
            System.out.println("Component not found with ID: " + componentId);
        }
    }
    
    /**
     * Displays infrastructure statistics
     */
    private void displayStatistics() {
        System.out.println("\n--- INFRASTRUCTURE STATISTICS ---");
        System.out.println(cityManager.getInfrastructureStatistics());
    }
    
    /**
     * Performs system maintenance
     */
    private void performMaintenance() {
        System.out.println("\n--- PERFORMING SYSTEM MAINTENANCE ---");
        cityManager.performSystemMaintenance();
    }
    
    /**
     * Validates the infrastructure
     */
    private void validateInfrastructure() {
        System.out.println("\n--- VALIDATING INFRASTRUCTURE ---");
        try {
            cityManager.validateInfrastructure();
        } catch (InfrastructureException e) {
            System.err.println("Validation Error: " + e.getMessage());
        }
    }
    
    /**
     * Views details of a specific component
     */
    private void viewComponentDetails() {
        System.out.println("\n--- VIEW COMPONENT DETAILS ---");
        
        String componentId = getStringInput("Enter Component ID");
        var component = cityManager.getComponent(componentId);
        
        if (component != null) {
            System.out.println(component.getDetailedInfo());
        } else {
            System.out.println("Component not found with ID: " + componentId);
        }
    }
    
    /**
     * Lists all cities
     */
    private void listAllCities() {
        System.out.println("\n--- ALL CITIES ---");
        
        List<City> cities = cityManager.getAllCities();
        if (cities.isEmpty()) {
            System.out.println("No cities available.");
            return;
        }
        
        for (City city : cities) {
            System.out.println(city.getCityStatistics());
            System.out.println("-".repeat(40));
        }
    }
    
    /**
     * System settings menu
     */
    private void systemSettings() {
        System.out.println("\n--- SYSTEM SETTINGS ---");
        System.out.println("1. Change Log Level");
        System.out.println("2. Toggle File Logging");
        System.out.println("3. View Current Settings");
        System.out.println("0. Back to Main Menu");
        
        int choice = getIntInput("Enter choice");
        
        switch (choice) {
            case 1:
                changeLogLevel();
                break;
            case 2:
                toggleFileLogging();
                break;
            case 3:
                displayCurrentSettings();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void changeLogLevel() {
        System.out.println("Log Levels:");
        System.out.println("1. DEBUG");
        System.out.println("2. INFO");
        System.out.println("3. WARNING");
        System.out.println("4. ERROR");
        
        int choice = getIntInput("Select log level");
        
        switch (choice) {
            case 1:
                logger.setLogLevel(Logger.LogLevel.DEBUG);
                break;
            case 2:
                logger.setLogLevel(Logger.LogLevel.INFO);
                break;
            case 3:
                logger.setLogLevel(Logger.LogLevel.WARNING);
                break;
            case 4:
                logger.setLogLevel(Logger.LogLevel.ERROR);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        System.out.println("Log level changed successfully!");
    }
    
    private void toggleFileLogging() {
        // This would toggle file logging - implementation depends on logger
        System.out.println("File logging toggle feature - implementation specific");
    }
    
    private void displayCurrentSettings() {
        System.out.println("Current Settings:");
        System.out.println("Log Level: " + logger.getLogLevel().getLabel());
        System.out.println("Console Output: Enabled");
        System.out.println("File Output: Enabled");
    }
    
    /**
     * Displays goodbye message
     */
    private void displayGoodbyeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Thank you for using Smart City Infrastructure Management System!");
        System.out.println("Demonstrating Composite Design Pattern with SOLID principles.");
        System.out.println("=".repeat(60));
    }
    
    // Helper methods for input handling
    
    private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt + ": ");
            input = scanner.nextLine().trim();
        }
        return input;
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
    
    private long getLongInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal number.");
            }
        }
    }
    
    private boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' for yes or 'n' for no.");
            }
        }
    }
}
