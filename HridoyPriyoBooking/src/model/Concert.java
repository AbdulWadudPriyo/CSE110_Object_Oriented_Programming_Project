package model;

public class Concert extends BookingItem {
    private String artist;
    private static final double SERVICE_CHARGE_RATE = 0.10; // 10%

    public Concert(int id, String title, String location, String date,
                   double basePrice, int totalSeats, int availableSeats, String artist) {
        super(id, title, location, date, basePrice, totalSeats, availableSeats);
        this.artist = artist;
    }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    @Override
    public double calculateTotalPrice(int seatCount) {
        double subtotal = getBasePrice() * seatCount;
        return subtotal + (subtotal * SERVICE_CHARGE_RATE);
    }

    @Override
    public String getCategory() { return "Concert"; }

    @Override
    public String getDetails() {
        return "Concert: " + getTitle() + " | Artist: " + artist + " | Venue: " + getLocation()
                + " | Date: " + getDate() + " (incl. 10% service charge)";
    }

    @Override
    public String getSubField() { return artist; }

    @Override
    public String toFileString() {
        return getId() + "|" + getCategory() + "|" + getTitle() + "|" + artist + "|"
                + getLocation() + "|" + getDate() + "|" + getBasePrice() + "|"
                + getTotalSeats() + "|" + getAvailableSeats();
    }
}
