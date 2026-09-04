package model;

public class Movie extends BookingItem {
    private String genre;

    public Movie(int id, String title, String location, String date,
                 double basePrice, int totalSeats, int availableSeats, String genre) {
        super(id, title, location, date, basePrice, totalSeats, availableSeats);
        this.genre = genre;
    }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    @Override
    public double calculateTotalPrice(int seatCount) {
        return getBasePrice() * seatCount;
    }

    @Override
    public String getCategory() { return "Movie"; }

    @Override
    public String getDetails() {
        return "Movie: " + getTitle() + " | Genre: " + genre + " | Hall: " + getLocation()
                + " | Show Date: " + getDate();
    }

    @Override
    public String getSubField() { return genre; }

    @Override
    public String toFileString() {
        return getId() + "|" + getCategory() + "|" + getTitle() + "|" + genre + "|"
                + getLocation() + "|" + getDate() + "|" + getBasePrice() + "|"
                + getTotalSeats() + "|" + getAvailableSeats();
    }
}
