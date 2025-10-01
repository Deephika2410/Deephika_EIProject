package factory;

import enums.TicketClass;
import models.PassengerInfo;
import models.RouteInfo;
import models.PricingInfo;
import ticket.TrainTicket;
import ticket.Ticket;
import java.util.Map;

public class TrainTicketFactory extends TicketFactory {
    private int ticketCounter = 2000;

    @Override
    public Ticket createTicket(PassengerInfo passenger, RouteInfo route, TicketClass ticketClass, Map<String, Object> details) {
        ticketCounter++;
        String ticketId = generateTicketId("TRN", ticketCounter);
        double discount = details.get("discount") != null ? (double) details.get("discount") : 0.0;
        PricingInfo pricing = calculatePricing(route, ticketClass, 0.5, discount);
        String coach = (String) details.get("coach");
        String seatNumber = (String) details.get("seatNumber");
        String pnr = (String) details.get("pnr");
        return new TrainTicket(ticketId, passenger, route, pricing, ticketClass, coach, seatNumber, pnr);
    }
}
