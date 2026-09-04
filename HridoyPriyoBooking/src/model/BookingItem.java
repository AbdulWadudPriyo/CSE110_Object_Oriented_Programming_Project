package model;

/**
 * Abstract base class for all bookable items.
 * Demonstrates Encapsulation (private fields + getters/setters)
 * and defines the contract for Polymorphism via abstract methods.
 */
public abstract class BookingItem {
    private int id;
    private String title;
    private String location;
    private String date;          // format: dd-MM-yyyy
    private double basePrice;
    private int totalSeats;
    private int availableSeats;

    public BookingItem(int id, String title, String location, String date,
                        double basePrice, int totalSeats, int availableSeats) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.date = date;
        this.basePrice = basePrice;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    // ---- Encapsulation: getters & setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    // ---- Operational methods ----
    public boolean bookSeats(int count) {
        if (count > 0 && count <= availableSeats) {
            availableSeats -= count;
            return true;
        }
        return false;
    }

    public void releaseSeats(int count) {
        availableSeats = Math.min(totalSeats, availableSeats + count);
    }

    // ---- Abstract methods: Polymorphism / Overriding ----
    public abstract double calculateTotalPrice(int seatCount);
    public abstract String getCategory();
    public abstract String getDetails();
    public abstract String getSubField();
    public abstract String toFileString();
}
