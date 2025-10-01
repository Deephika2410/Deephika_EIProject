package system;

import enums.TicketClass;
import factory.TicketFactory;
import management.SeatManager;
import models.PassengerInfo;
import models.RouteInfo;
import ticket.Ticket;
import java.util.HashMap;
import java.util.Map;

public class TravelBookingSystem {
    private Map<String, TicketFactory> factories = new HashMap<>();
    private SeatManager seatManager = new SeatManager();

    public TravelBookingSystem() {
        registerDefaultFactories();
    }

    private void registerDefaultFactories() {
        factories.put("bus", new factory.BusTicketFactory());
        factories.put("train", new factory.TrainTicketFactory());
        factories.put("flight", new factory.FlightTicketFactory());
    }

    public void registerFactory(String transportType, TicketFactory factory) {
        factories.put(transportType.toLowerCase(), factory);
    }

    public Ticket bookTicket(String transportType, PassengerInfo passenger, RouteInfo route, TicketClass ticketClass, Map<String, Object> details) {
        TicketFactory factory = factories.get(transportType.toLowerCase());
        if (factory == null) {
            System.out.println("Error: No factory registered for '" + transportType + "'");
            return null;
        }
        
        String routeKey = route.getOrigin() + "-" + route.getDestination();
        String seatNumber = (String) details.get("seatNumber");
        
        // Check if seat is available and book it
        if (seatNumber != null && !seatManager.bookSeat(transportType, routeKey, seatNumber)) {
            System.out.println("Error: Seat " + seatNumber + " is already booked!");
            return null;
        }
        
        Ticket ticket = factory.createTicket(passenger, route, ticketClass, details);
        ticket.confirmPayment();
        return ticket;
    }
    
    public SeatManager getSeatManager() {
        return seatManager;
    }
}
