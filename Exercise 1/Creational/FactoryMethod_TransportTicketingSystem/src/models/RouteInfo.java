package models;

import java.time.Duration;
import java.time.LocalDateTime;

public class RouteInfo {
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double distanceKm;

    public RouteInfo(String origin, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, double distanceKm) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.distanceKm = distanceKm;
    }

    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public double getDistanceKm() { return distanceKm; }

    public Duration getDuration() {
        return Duration.between(departureTime, arrivalTime);
    }

    @Override
    public String toString() {
        return origin + " -> " + destination;
    }
}
