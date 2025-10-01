package management;

import java.util.*;

public class SeatManager {
    private Map<String, Map<String, Set<String>>> bookedSeats = new HashMap<>();
    
    // Initialize seats for different transport types
    public SeatManager() {
        initializeSeats();
    }
    
    private void initializeSeats() {
        // Initialize bus seats
        bookedSeats.put("bus", new HashMap<>());
        
        // Initialize train seats
        bookedSeats.put("train", new HashMap<>());
        
        // Initialize flight seats
        bookedSeats.put("flight", new HashMap<>());
    }
    
    public List<String> getAvailableSeats(String transportType, String route) {
        List<String> availableSeats = new ArrayList<>();
        Set<String> booked = getBookedSeatsForRoute(transportType, route);
        
        switch (transportType.toLowerCase()) {
            case "bus":
                for (int i = 1; i <= 40; i++) {
                    String seat = i + "A";
                    if (!booked.contains(seat)) availableSeats.add(seat);
                    seat = i + "B";
                    if (!booked.contains(seat)) availableSeats.add(seat);
                }
                break;
            case "train":
                String[] coaches = {"S1", "S2", "A1", "A2", "B1", "B2"};
                for (String coach : coaches) {
                    for (int i = 1; i <= 20; i++) {
                        String seat = coach + "-" + i;
                        if (!booked.contains(seat)) availableSeats.add(seat);
                    }
                }
                break;
            case "flight":
                String[] rows = {"A", "B", "C", "D", "E", "F"};
                for (int i = 1; i <= 30; i++) {
                    for (String row : rows) {
                        String seat = i + row;
                        if (!booked.contains(seat)) availableSeats.add(seat);
                    }
                }
                break;
        }
        return availableSeats;
    }
    
    public boolean isSeatAvailable(String transportType, String route, String seat) {
        Set<String> booked = getBookedSeatsForRoute(transportType, route);
        return !booked.contains(seat);
    }
    
    public boolean bookSeat(String transportType, String route, String seat) {
        if (isSeatAvailable(transportType, route, seat)) {
            getBookedSeatsForRoute(transportType, route).add(seat);
            return true;
        }
        return false;
    }
    
    public void displayAvailableSeats(String transportType, String route) {
        List<String> available = getAvailableSeats(transportType, route);
        Set<String> booked = getBookedSeatsForRoute(transportType, route);
        
        System.out.println("\n============================================================");
        System.out.println("SEAT AVAILABILITY - " + transportType.toUpperCase());
        System.out.println("Route: " + route);
        System.out.println("============================================================");
        
        switch (transportType.toLowerCase()) {
            case "bus":
                displayBusSeats(available, booked);
                break;
            case "train":
                displayTrainSeats(available, booked);
                break;
            case "flight":
                displayFlightSeats(available, booked);
                break;
        }
        
        System.out.println("\nLegend: [Available] [BOOKED]");
        System.out.println("Total Available: " + available.size());
        System.out.println("============================================================");
    }
    
    private void displayBusSeats(List<String> available, Set<String> booked) {
        System.out.println("     FRONT");
        System.out.println("   A    B");
        for (int i = 1; i <= 40; i++) {
            String seatA = i + "A";
            String seatB = i + "B";
            String displayA = available.contains(seatA) ? String.format("%3s", seatA) : "[X]";
            String displayB = available.contains(seatB) ? String.format("%3s", seatB) : "[X]";
            System.out.printf("%2d %s  %s%n", i, displayA, displayB);
            if (i % 10 == 0) System.out.println("   ----");
        }
    }
    
    private void displayTrainSeats(List<String> available, Set<String> booked) {
        String[] coaches = {"S1", "S2", "A1", "A2", "B1", "B2"};
        for (String coach : coaches) {
            System.out.println("\nCoach: " + coach);
            System.out.print("Seats: ");
            for (int i = 1; i <= 20; i++) {
                String seat = coach + "-" + i;
                if (available.contains(seat)) {
                    System.out.printf("%6s", seat);
                } else {
                    System.out.printf("%6s", "[X]");
                }
                if (i % 5 == 0) System.out.println();
            }
        }
    }
    
    private void displayFlightSeats(List<String> available, Set<String> booked) {
        System.out.println("    A  B  C     D  E  F");
        for (int i = 1; i <= 30; i++) {
            System.out.printf("%2d ", i);
            String[] positions = {"A", "B", "C", "D", "E", "F"};
            for (int j = 0; j < positions.length; j++) {
                String seat = i + positions[j];
                if (available.contains(seat)) {
                    System.out.printf("%2s ", seat);
                } else {
                    System.out.print("[X] ");
                }
                if (j == 2) System.out.print("   "); // Aisle space
            }
            System.out.println();
            if (i % 10 == 0) System.out.println("   ----------------------");
        }
    }
    
    private Set<String> getBookedSeatsForRoute(String transportType, String route) {
        return bookedSeats.get(transportType.toLowerCase())
                .computeIfAbsent(route, k -> new HashSet<>());
    }
    
    public String selectSeatInteractively(String transportType, String route, Scanner scanner) {
        while (true) {
            displayAvailableSeats(transportType, route);
            System.out.print("\nEnter seat number (or 'back' to go back): ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("back")) {
                return null;
            }
            
            if (isSeatAvailable(transportType, route, input)) {
                return input;
            } else {
                System.out.println("Seat " + input + " is not available or invalid. Please choose another seat.");
            }
        }
    }
}
