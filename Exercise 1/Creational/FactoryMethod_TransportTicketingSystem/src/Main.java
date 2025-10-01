import enums.TicketClass;
import input.UserInputHandler;
import models.PassengerInfo;
import models.RouteInfo;
import system.TravelBookingSystem;
import ticket.Ticket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  TRANSPORT TICKET GENERATOR - Factory Method Pattern");
        System.out.println("============================================================");
        TravelBookingSystem bookingSystem = new TravelBookingSystem();
        UserInputHandler inputHandler = new UserInputHandler();
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while (true) {
            System.out.println("\n============================================================");
            System.out.println("SELECT TRANSPORT TYPE");
            System.out.println("============================================================");
            System.out.println("1. Bus");
            System.out.println("2. Train");
            System.out.println("3. Flight");
            System.out.println("4. Exit");
            System.out.print("\nEnter your choice (1-4): ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("4")) {
                System.out.println("\nThank you for using our booking system!");
                break;
            }
            String transportType = null;
            if (choice.equals("1")) transportType = "bus";
            else if (choice.equals("2")) transportType = "train";
            else if (choice.equals("3")) transportType = "flight";
            else {
                System.out.println("Invalid choice! Please try again.");
                continue;
            }
            try {
                PassengerInfo passenger = inputHandler.getPassengerInfo(transportType);
                RouteInfo route = inputHandler.getRouteInfo();
                TicketClass ticketClass = inputHandler.getTicketClass();
                Map<String, Object> details = new HashMap<>();
                details.put("discount", 0.0);
                
                if (transportType.equals("bus")) {
                    Map<String, Object> busDetails = inputHandler.getBusDetails(bookingSystem.getSeatManager(), route);
                    if (busDetails == null) continue; // User chose to go back
                    details.putAll(busDetails);
                } else if (transportType.equals("train")) {
                    Map<String, Object> trainDetails = inputHandler.getTrainDetails(bookingSystem.getSeatManager(), route);
                    if (trainDetails == null) continue; // User chose to go back
                    details.putAll(trainDetails);
                } else if (transportType.equals("flight")) {
                    Map<String, Object> flightDetails = inputHandler.getFlightDetails(bookingSystem.getSeatManager(), route);
                    if (flightDetails == null) continue; // User chose to go back
                    details.putAll(flightDetails);
                }
                Ticket ticket = bookingSystem.bookTicket(transportType, passenger, route, ticketClass, details);
                if (ticket != null) {
                    System.out.println("\n============================================================");
                    System.out.println("BOOKING SUCCESSFUL!");
                    System.out.println("============================================================");
                    System.out.println(ticket.displayTicket());
                    System.out.print("\nWould you like to book another ticket? (y/n): ");
                    String another = scanner.nextLine().trim().toLowerCase();
                    if (!another.equals("y")) {
                        System.out.println("\nThank you for using our booking system!");
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("\nError: " + e.getMessage());
                System.out.println("Please try again with valid inputs.");
            }
        }
    }
}
