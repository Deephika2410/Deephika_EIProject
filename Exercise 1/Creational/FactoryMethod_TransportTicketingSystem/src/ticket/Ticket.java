package ticket;

import enums.TicketClass;
import enums.PaymentStatus;
import models.PassengerInfo;
import models.RouteInfo;
import models.PricingInfo;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Ticket {
    protected String ticketId;
    protected PassengerInfo passenger;
    protected RouteInfo route;
    protected PricingInfo pricing;
    protected TicketClass ticketClass;
    protected LocalDateTime bookingTime;
    protected PaymentStatus paymentStatus;
    protected String qrCode;

    public Ticket(String ticketId, PassengerInfo passenger, RouteInfo route, PricingInfo pricing, TicketClass ticketClass) {
        this.ticketId = ticketId;
        this.passenger = passenger;
        this.route = route;
        this.pricing = pricing;
        this.ticketClass = ticketClass;
        this.bookingTime = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
        this.qrCode = generateQrCode();
    }

    private String generateQrCode() {
        return "QR-" + ticketId;
    }

    public void confirmPayment() {
        this.paymentStatus = PaymentStatus.CONFIRMED;
    }

    public void cancelTicket() {
        this.paymentStatus = PaymentStatus.CANCELLED;
    }

    public abstract String getTransportType();
    public abstract List<String> getAmenities();
    public abstract String getCancellationPolicy();
    public abstract String getBaggageAllowance();

    public String displayTicket() {
        long hours = route.getDuration().toHours();
        long minutes = route.getDuration().toMinutes() % 60;
        StringBuilder sb = new StringBuilder();
        sb.append("============================================================\n");
        sb.append("                " + getTransportType() + " TICKET\n");
        sb.append("============================================================\n");
        sb.append("Ticket ID: " + ticketId + "                    QR Code: " + qrCode + "\n");
        sb.append("Booking Time: " + bookingTime.toString().replace('T', ' ') + "\n");
        sb.append("Payment Status: " + paymentStatus + "\n\n");
        sb.append("PASSENGER INFORMATION:\n  " + passenger + "\n  Contact: " + passenger.getContact() + "\n\n");
        sb.append("JOURNEY DETAILS:\n  Route: " + route + "\n  Departure: " + route.getDepartureTime().toString().replace('T', ' ') + "\n  Arrival: " + route.getArrivalTime().toString().replace('T', ' ') + "\n  Duration: " + hours + "h " + minutes + "m\n  Distance: " + route.getDistanceKm() + " km\n  Class: " + ticketClass + "\n\n");
        sb.append("PRICING BREAKDOWN:\n  Base Fare:        Rs." + String.format("%.2f", pricing.getBaseFare()) + "\n");
        sb.append("  Taxes & Fees:     Rs." + String.format("%.2f", pricing.getTaxes()) + "\n");
        sb.append("  Service Charge:   Rs." + String.format("%.2f", pricing.getServiceCharge()) + "\n");
        sb.append("  ----------------------------------------\n");
        sb.append("  TOTAL:            Rs." + String.format("%.2f", pricing.getTotal()) + "\n\n");
        sb.append("AMENITIES:\n  " + String.join(", ", getAmenities()) + "\n\n");
        sb.append("BAGGAGE ALLOWANCE:\n  " + getBaggageAllowance() + "\n\n");
        sb.append("CANCELLATION POLICY:\n  " + getCancellationPolicy() + "\n");
        sb.append("============================================================\n");
        return sb.toString();
    }
}
