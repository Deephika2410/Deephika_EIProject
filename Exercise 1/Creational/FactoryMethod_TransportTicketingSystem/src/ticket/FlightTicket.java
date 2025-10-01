package ticket;

import enums.TicketClass;
import models.PassengerInfo;
import models.RouteInfo;
import models.PricingInfo;
import java.util.ArrayList;
import java.util.List;

public class FlightTicket extends Ticket {
    private String flightNumber;
    private String seatNumber;
    private String gate;
    private String airline;

    public FlightTicket(String ticketId, PassengerInfo passenger, RouteInfo route, PricingInfo pricing, TicketClass ticketClass, String flightNumber, String seatNumber, String gate, String airline) {
        super(ticketId, passenger, route, pricing, ticketClass);
        this.flightNumber = flightNumber;
        this.seatNumber = seatNumber;
        this.gate = gate;
        this.airline = airline;
    }

    @Override
    public String getTransportType() {
        return airline + " FLIGHT";
    }

    @Override
    public List<String> getAmenities() {
        List<String> amenities = new ArrayList<>();
        if (ticketClass == TicketClass.ECONOMY) {
            amenities.add("In-flight Entertainment");
            amenities.add("Snack");
            amenities.add("Beverage");
        } else if (ticketClass == TicketClass.BUSINESS) {
            amenities.add("Priority Boarding");
            amenities.add("Lounge Access");
            amenities.add("Premium Meal");
            amenities.add("Extra Legroom");
            amenities.add("Entertainment System");
            amenities.add("Amenity Kit");
        } else {
            amenities.add("Priority Check-in");
            amenities.add("Luxury Lounge");
            amenities.add("Private Suite");
            amenities.add("Gourmet Dining");
            amenities.add("Premium Entertainment");
            amenities.add("Concierge Service");
            amenities.add("Luxury Amenity Kit");
            amenities.add("Chauffeur Service");
        }
        return amenities;
    }

    @Override
    public String getCancellationPolicy() {
        if (ticketClass == TicketClass.ECONOMY) {
            return "Non-refundable. Date change fee: Rs.3000";
        } else if (ticketClass == TicketClass.BUSINESS) {
            return "50% refund up to 48h before departure. Full flexibility for date changes.";
        } else {
            return "Full refund up to 24h before departure. Unlimited date changes.";
        }
    }

    @Override
    public String getBaggageAllowance() {
        String baggage;
        if (ticketClass == TicketClass.ECONOMY) {
            baggage = "Cabin: 7kg | Check-in: 15kg";
        } else if (ticketClass == TicketClass.BUSINESS) {
            baggage = "Cabin: 10kg | Check-in: 30kg";
        } else {
            baggage = "Cabin: 15kg | Check-in: 40kg";
        }
        return "Flight: " + flightNumber + " | Gate: " + gate + " | Seat: " + seatNumber + " | " + baggage;
    }
}
