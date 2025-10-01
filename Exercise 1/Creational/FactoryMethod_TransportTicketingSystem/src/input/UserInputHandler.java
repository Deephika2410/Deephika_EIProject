package input;

import enums.TicketClass;
import management.SeatManager;
import models.PassengerInfo;
import models.RouteInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserInputHandler {
    private Scanner scanner = new Scanner(System.in);

    public PassengerInfo getPassengerInfo(String transportType) {
        System.out.println("\n============================================================");
        System.out.println("PASSENGER INFORMATION");
        System.out.println("============================================================");
        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter passenger age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());
        
        if (transportType.equals("flight")) {
            System.out.print("Enter Passport number: ");
        } else {
            System.out.print("Enter Aadhar number: ");
        }
        String idNumber = scanner.nextLine().trim();
        System.out.print("Enter contact number: ");
        String contact = scanner.nextLine().trim();
        return new PassengerInfo(name, age, idNumber, contact);
    }

    public RouteInfo getRouteInfo() {
        System.out.println("\n============================================================");
        System.out.println("ROUTE INFORMATION");
        System.out.println("============================================================");
        System.out.print("Enter origin city: ");
        String origin = scanner.nextLine().trim();
        System.out.print("Enter destination city: ");
        String destination = scanner.nextLine().trim();
        
        // Use default distance
        double distanceKm = 100.0;
        
        System.out.println("\nDeparture Date & Time:");
        System.out.print("  Enter date (YYYY-MM-DD): ");
        String depDate = scanner.nextLine().trim();
        System.out.print("  Enter time (HH:MM or HH.MM): ");
        String depTime = scanner.nextLine().trim().replace(".", ":");
        LocalDateTime departureTime = LocalDateTime.parse(depDate + "T" + depTime);
        System.out.println("\nArrival Date & Time:");
        System.out.print("  Enter date (YYYY-MM-DD): ");
        String arrDate = scanner.nextLine().trim();
        System.out.print("  Enter time (HH:MM or HH.MM): ");
        String arrTime = scanner.nextLine().trim().replace(".", ":");
        LocalDateTime arrivalTime = LocalDateTime.parse(arrDate + "T" + arrTime);
        return new RouteInfo(origin, destination, departureTime, arrivalTime, distanceKm);
    }

    public TicketClass getTicketClass() {
        System.out.println("\n============================================================");
        System.out.println("SELECT TICKET CLASS");
        System.out.println("============================================================");
        System.out.println("1. Economy");
        System.out.println("2. Business");
        System.out.println("3. First Class");
        System.out.print("\nEnter choice (1-3): ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "2": return TicketClass.BUSINESS;
            case "3": return TicketClass.FIRST_CLASS;
            default: return TicketClass.ECONOMY;
        }
    }

    public Map<String, Object> getBusDetails(SeatManager seatManager, RouteInfo route) {
        Map<String, Object> details = new HashMap<>();
        System.out.println("\n============================================================");
        System.out.println("BUS BOOKING DETAILS");
        System.out.println("============================================================");
        
        // Show available bus types
        System.out.println("Available Bus Types:");
        System.out.println("1. AC Sleeper - Premium comfort with reclining berths");
        System.out.println("2. Non-AC Sleeper - Economy sleeper berths");
        System.out.println("3. AC Seater - Air conditioned push back seats");
        System.out.println("4. Non-AC Seater - Standard seating");
        System.out.print("Select bus type (1-4): ");
        String busChoice = scanner.nextLine().trim();
        
        String[] busTypes = {"AC Sleeper", "Non-AC Sleeper", "AC Seater", "Non-AC Seater"};
        int choice = 0;
        try {
            choice = Integer.parseInt(busChoice) - 1;
            if (choice < 0 || choice >= busTypes.length) choice = 0;
        } catch (NumberFormatException e) {
            choice = 0;
        }
        details.put("busType", busTypes[choice]);
        
        String routeKey = route.getOrigin() + "-" + route.getDestination();
        String seatNumber = seatManager.selectSeatInteractively("bus", routeKey, scanner);
        if (seatNumber == null) {
            return null; // User chose to go back
        }
        details.put("seatNumber", seatNumber);
        return details;
    }

    public Map<String, Object> getTrainDetails(SeatManager seatManager, RouteInfo route) {
        Map<String, Object> details = new HashMap<>();
        System.out.println("\n============================================================");
        System.out.println("TRAIN BOOKING DETAILS");
        System.out.println("============================================================");
        
        // Show available train classes
        System.out.println("Available Train Classes:");
        System.out.println("1. Sleeper Class (SL) - Basic sleeper berths");
        System.out.println("2. AC 3 Tier (3AC) - Air conditioned 3-tier berths");
        System.out.println("3. AC 2 Tier (2AC) - Air conditioned 2-tier berths");
        System.out.println("4. AC 1 Tier (1AC) - Air conditioned private cabins");
        System.out.print("Select train class (1-4): ");
        String trainChoice = scanner.nextLine().trim();
        
        String[] coachPrefixes = {"S", "B", "A", "H"};
        int choice = 0;
        try {
            choice = Integer.parseInt(trainChoice) - 1;
            if (choice < 0 || choice >= coachPrefixes.length) choice = 0;
        } catch (NumberFormatException e) {
            choice = 0;
        }
        details.put("coach", coachPrefixes[choice] + (1 + (int)(Math.random() * 5)));
        
        String routeKey = route.getOrigin() + "-" + route.getDestination();
        String seatNumber = seatManager.selectSeatInteractively("train", routeKey, scanner);
        if (seatNumber == null) {
            return null; // User chose to go back
        }
        details.put("seatNumber", seatNumber);
        
        System.out.print("Enter PNR number (10 digits) or press Enter to generate: ");
        String pnrInput = scanner.nextLine().trim();
        String pnr = pnrInput.length() == 10 ? pnrInput : String.valueOf(1000000000L + (long)(Math.random() * 9000000000L));
        details.put("pnr", pnr);
        return details;
    }

    public Map<String, Object> getFlightDetails(SeatManager seatManager, RouteInfo route) {
        Map<String, Object> details = new HashMap<>();
        System.out.println("\n============================================================");
        System.out.println("FLIGHT BOOKING DETAILS");
        System.out.println("============================================================");
        
        // Show available airlines
        System.out.println("Available Airlines:");
        System.out.println("1. Air India - National carrier");
        System.out.println("2. IndiGo - Low cost carrier");
        System.out.println("3. SpiceJet - Budget airline");
        System.out.println("4. Vistara - Premium service");
        System.out.println("5. GoAir - Value airline");
        System.out.print("Select airline (1-5): ");
        String airlineChoice = scanner.nextLine().trim();
        
        String[] airlines = {"Air India", "IndiGo", "SpiceJet", "Vistara", "GoAir"};
        int choice = 0;
        try {
            choice = Integer.parseInt(airlineChoice) - 1;
            if (choice < 0 || choice >= airlines.length) choice = 1; // Default to IndiGo
        } catch (NumberFormatException e) {
            choice = 1;
        }
        details.put("airline", airlines[choice]);
        
        // Generate flight number based on airline
        String[] flightPrefixes = {"AI", "6E", "SG", "UK", "G8"};
        String flightNumber = flightPrefixes[choice] + (100 + (int)(Math.random() * 900));
        details.put("flightNumber", flightNumber);
        System.out.println("Flight Number: " + flightNumber);
        
        String routeKey = route.getOrigin() + "-" + route.getDestination();
        String seatNumber = seatManager.selectSeatInteractively("flight", routeKey, scanner);
        if (seatNumber == null) {
            return null; // User chose to go back
        }
        details.put("seatNumber", seatNumber);
        
        // Generate gate number
        String gate = "G" + (1 + (int)(Math.random() * 20));
        details.put("gate", gate);
        System.out.println("Gate Number: " + gate);
        
        return details;
    }
}
