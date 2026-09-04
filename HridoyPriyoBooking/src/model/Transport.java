package model;

public class Transport extends BookingItem {
    private String vehicleType; // Bus, Train, Flight, Launch...

    public Transport(int id, String title, String location, String date,
                      double basePrice, int totalSeats, int availableSeats, String vehicleType) {
        super(id, title, location, date, basePrice, totalSeats, availableSeats);
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    @Override
    public double calculateTotalPrice(int seatCount) {
        return getBasePrice() * seatCount;
    }

    @Override
    public String getCategory() { return "Transport"; }

    @Override
    public String getDetails() {
        return "Transport: " + getTitle() + " | Type: " + vehicleType + " | Route: " + getLocation()
                + " | Departure: " + getDate();
    }

    @Override
    public String getSubField() { return vehicleType; }

    @Override
    public String toFileString() {
        return getId() + "|" + getCategory() + "|" + getTitle() + "|" + vehicleType + "|"
                + getLocation() + "|" + getDate() + "|" + getBasePrice() + "|"
                + getTotalSeats() + "|" + getAvailableSeats();
    }
}
