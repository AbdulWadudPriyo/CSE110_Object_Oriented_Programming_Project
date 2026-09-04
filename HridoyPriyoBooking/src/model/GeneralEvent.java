package model;

public class GeneralEvent extends BookingItem {
    private String organizer;
    private static final double BOOKING_FEE = 20.0;

    public GeneralEvent(int id, String title, String location, String date,
                         double basePrice, int totalSeats, int availableSeats, String organizer) {
        super(id, title, location, date, basePrice, totalSeats, availableSeats);
        this.organizer = organizer;
    }

    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }

    @Override
    public double calculateTotalPrice(int seatCount) {
        return (getBasePrice() * seatCount) + BOOKING_FEE;
    }

    @Override
    public String getCategory() { return "Event"; }

    @Override
    public String getDetails() {
        return "Event: " + getTitle() + " | Organizer: " + organizer + " | Venue: " + getLocation()
                + " | Date: " + getDate() + " (+ Tk " + BOOKING_FEE + " booking fee)";
    }

    @Override
    public String getSubField() { return organizer; }

    @Override
    public String toFileString() {
        return getId() + "|" + getCategory() + "|" + getTitle() + "|" + organizer + "|"
                + getLocation() + "|" + getDate() + "|" + getBasePrice() + "|"
                + getTotalSeats() + "|" + getAvailableSeats();
    }
}
