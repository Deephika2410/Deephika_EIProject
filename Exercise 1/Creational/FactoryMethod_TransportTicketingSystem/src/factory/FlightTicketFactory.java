package factory;

import enums.TicketClass;
import models.PassengerInfo;
import models.RouteInfo;
import models.PricingInfo;
import ticket.FlightTicket;
import ticket.Ticket;
import java.util.Map;

public class FlightTicketFactory extends TicketFactory {
    private int ticketCounter = 3000;

    @Override
    public Ticket createTicket(PassengerInfo passenger, RouteInfo route, TicketClass ticketClass, Map<String, Object> details) {
        ticketCounter++;
        String ticketId = generateTicketId("FLT", ticketCounter);
        double discount = details.get("discount") != null ? (double) details.get("discount") : 0.0;
        PricingInfo pricing = calculatePricing(route, ticketClass, 2.5, discount);
        String airline = (String) details.get("airline");
        String flightNumber = (String) details.get("flightNumber");
        String seatNumber = (String) details.get("seatNumber");
        String gate = (String) details.get("gate");
        return new FlightTicket(ticketId, passenger, route, pricing, ticketClass, flightNumber, seatNumber, gate, airline);
    }
}
