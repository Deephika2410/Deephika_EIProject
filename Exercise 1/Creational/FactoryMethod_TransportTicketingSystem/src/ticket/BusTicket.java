package ticket;

import enums.TicketClass;
import models.PassengerInfo;
import models.RouteInfo;
import models.PricingInfo;
import java.util.ArrayList;
import java.util.List;

public class BusTicket extends Ticket {
    private String seatNumber;
    private String busType;

    public BusTicket(String ticketId, PassengerInfo passenger, RouteInfo route, PricingInfo pricing, TicketClass ticketClass, String seatNumber, String busType) {
        super(ticketId, passenger, route, pricing, ticketClass);
        this.seatNumber = seatNumber;
        this.busType = busType;
    }

    @Override
    public String getTransportType() {
        return busType + " BUS";
    }

    @Override
    public List<String> getAmenities() {
        List<String> amenities = new ArrayList<>();
        amenities.add("Water Bottle");
        amenities.add("First Aid Kit");
        if (ticketClass == TicketClass.ECONOMY) {
            amenities.add("Reading Light");
        } else if (ticketClass == TicketClass.BUSINESS) {
            amenities.add("AC");
            amenities.add("Reclining Seat");
            amenities.add("Charging Port");
            amenities.add("WiFi");
        } else {
            amenities.add("AC");
            amenities.add("Luxury Recliner");
            amenities.add("Entertainment System");
            amenities.add("WiFi");
            amenities.add("Meal");
            amenities.add("Blanket & Pillow");
        }
        return amenities;
    }

    @Override
    public String getCancellationPolicy() {
        return "Free cancellation up to 24h before departure. 50% refund within 24h.";
    }

    @Override
    public String getBaggageAllowance() {
        return "Seat: " + seatNumber + " | 2 bags (max 15kg each)";
    }
}
