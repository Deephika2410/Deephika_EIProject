package factory;

import enums.TicketClass;
import models.PassengerInfo;
import models.RouteInfo;
import models.PricingInfo;
import ticket.Ticket;

public abstract class TicketFactory {
    public abstract Ticket createTicket(PassengerInfo passenger, RouteInfo route, TicketClass ticketClass, java.util.Map<String, Object> details);

    public PricingInfo calculatePricing(RouteInfo route, TicketClass ticketClass, double baseRatePerKm, double discount) {
        double baseFare = route.getDistanceKm() * baseRatePerKm;
        double multiplier = 1.0;
        switch (ticketClass) {
            case ECONOMY: multiplier = 1.0; break;
            case BUSINESS: multiplier = 1.8; break;
            case FIRST_CLASS: multiplier = 3.0; break;
        }
        baseFare *= multiplier;
        double taxes = baseFare * 0.12;
        double serviceCharge = baseFare * 0.05;
        return new PricingInfo(baseFare, taxes, serviceCharge, discount);
    }

    public String generateTicketId(String prefix, int sequence) {
        return prefix + String.format("%06d", sequence);
    }
}
