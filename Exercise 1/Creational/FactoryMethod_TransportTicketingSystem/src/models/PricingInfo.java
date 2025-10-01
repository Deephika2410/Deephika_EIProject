package models;

public class PricingInfo {
    private double baseFare;
    private double taxes;
    private double serviceCharge;
    private double discount;

    public PricingInfo(double baseFare, double taxes, double serviceCharge, double discount) {
        this.baseFare = baseFare;
        this.taxes = taxes;
        this.serviceCharge = serviceCharge;
        this.discount = discount;
    }

    public double getBaseFare() { return baseFare; }
    public double getTaxes() { return taxes; }
    public double getServiceCharge() { return serviceCharge; }
    public double getDiscount() { return discount; }
    public double getTotal() { return baseFare + taxes + serviceCharge - discount; }

    @Override
    public String toString() {
        return "Rs." + String.format("%.2f", getTotal());
    }
}
