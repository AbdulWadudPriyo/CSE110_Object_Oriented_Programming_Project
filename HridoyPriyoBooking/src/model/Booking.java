package model;

public class Booking {
    private int bookingId;
    private int userId;
    private String username;
    private int eventId;
    private String eventTitle;
    private String category;
    private int seatCount;
    private double totalPrice;
    private String bookingDate;
    private String status; // CONFIRMED / CANCELLED

    public Booking(int bookingId, int userId, String username, int eventId, String eventTitle,
                   String category, int seatCount, double totalPrice, String bookingDate, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.username = username;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.category = category;
        this.seatCount = seatCount;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public int getBookingId() { return bookingId; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getEventId() { return eventId; }
    public String getEventTitle() { return eventTitle; }
    public String getCategory() { return category; }
    public int getSeatCount() { return seatCount; }
    public double getTotalPrice() { return totalPrice; }
    public String getBookingDate() { return bookingDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return bookingId + "|" + userId + "|" + username + "|" + eventId + "|" + eventTitle + "|"
                + category + "|" + seatCount + "|" + totalPrice + "|" + bookingDate + "|" + status;
    }
}
