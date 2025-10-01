package ticket;

import enums.TicketClass;
import models.PassengerInfo;
import models.RouteInfo;
import models.PricingInfo;
import java.util.ArrayList;
import java.util.List;

public class TrainTicket extends Ticket {
    private String coach;
    private String seatNumber;
    private String pnr;

    public TrainTicket(String ticketId, PassengerInfo passenger, RouteInfo route, PricingInfo pricing, TicketClass ticketClass, String coach, String seatNumber, String pnr) {
        super(ticketId, passenger, route, pricing, ticketClass);
        this.coach = coach;
        this.seatNumber = seatNumber;
        this.pnr = pnr;
    }

    @Override
    public String getTransportType() {
        return "TRAIN";
    }

    @Override
    public List<String> getAmenities() {
        List<String> amenities = new ArrayList<>();
        if (ticketClass == TicketClass.ECONOMY) {
            amenities.add("Sleeper Berth");
            amenities.add("Charging Point");
            amenities.add("Reading Light");
        } else if (ticketClass == TicketClass.BUSINESS) {
            amenities.add("AC Berth");
            amenities.add("Bedding");
            amenities.add("Charging Point");
            amenities.add("Meal");
            amenities.add("Magazine");
        } else {
            amenities.add("AC Cabin");
            amenities.add("Premium Bedding");
            amenities.add("Personal Attendant");
            amenities.add("Multi-course Meal");
            amenities.add("Entertainment");
            amenities.add("Lounge Access");
        }
        return amenities;
    }

    @Override
    public String getCancellationPolicy() {
        return "Cancellation charges apply as per railway rules. Refund based on time of cancellation.";
    }

    @Override
    public String getBaggageAllowance() {
        return "PNR: " + pnr + " | Coach: " + coach + " | Seat: " + seatNumber + " | No limit on personal baggage";
    }
}
