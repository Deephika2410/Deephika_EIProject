package factory;

import enums.TicketClass;
import models.PassengerInfo;
import models.RouteInfo;
import models.PricingInfo;
import ticket.BusTicket;
import ticket.Ticket;
import java.util.Map;

public class BusTicketFactory extends TicketFactory {
    private int ticketCounter = 1000;

    @Override
    public Ticket createTicket(PassengerInfo passenger, RouteInfo route, TicketClass ticketClass, Map<String, Object> details) {
        ticketCounter++;
        String ticketId = generateTicketId("BUS", ticketCounter);
        double discount = details.get("discount") != null ? (double) details.get("discount") : 0.0;
        PricingInfo pricing = calculatePricing(route, ticketClass, 0.8, discount);
        String seatNumber = (String) details.get("seatNumber");
        String busType = (String) details.get("busType");
        return new BusTicket(ticketId, passenger, route, pricing, ticketClass, seatNumber, busType);
    }
}
