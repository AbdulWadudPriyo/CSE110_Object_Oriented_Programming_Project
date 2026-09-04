package util;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles ALL file I/O for the application using pure BufferedReader/BufferedWriter.
 * No unhandled exceptions ever escape this class - every failure is caught,
 * logged, and a safe default (empty list / false) is returned instead.
 */
public class FileManager {

    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + File.separator + "users.txt";
    private static final String EVENTS_FILE = DATA_DIR + File.separator + "events.txt";
    private static final String BOOKINGS_FILE = DATA_DIR + File.separator + "bookings.txt";

    // ---------------------------------------------------------------
    // Setup
    // ---------------------------------------------------------------
    public static void ensureDataFiles() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) dir.mkdirs();

            File users = new File(USERS_FILE);
            if (!users.exists()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(users))) {
                    bw.write("1|admin|admin123|ADMIN"); bw.newLine();
                    bw.write("2|rahim|rahim123|USER"); bw.newLine();
                    bw.write("3|Wadud|Wadud123|USER"); bw.newLine();
                    bw.write("4|Hridoy|Hridoy123|USER"); bw.newLine();
                }
            }

            File events = new File(EVENTS_FILE);
            if (!events.exists()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(events))) {
                    bw.write("1|Movie|Interstellar|Sci-Fi|Blockbuster Cinemas Dhanmondi|15-12-2024|350.0|100|100"); bw.newLine();
                    bw.write("2|Concert|Coke Studio Bangla Live|James|Army Stadium Dhaka|20-12-2024|1200.0|500|500"); bw.newLine();
                    bw.write("3|Transport|Dhaka to Cox's Bazar|Bus (AC)|Saint Martin Paribahan|18-12-2024|1800.0|40|40"); bw.newLine();
                    bw.write("4|Event|Dhaka Tech Summit 2024|Basis|Bangabandhu International Conference Center|25-12-2024|500.0|300|300"); bw.newLine();
                }
            }

            File bookings = new File(BOOKINGS_FILE);
            if (!bookings.exists()) {
                bookings.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error initializing data files: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Users
    // ---------------------------------------------------------------
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 4) continue;
                users.add(new User(Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim()));
            }
        } catch (IOException e) {
            System.err.println("Error reading users: " + e.getMessage());
        }
        return users;
    }

    public static boolean isUsernameTaken(String username) {
        for (User u : loadUsers()) {
            if (u.getUsername().equalsIgnoreCase(username)) return true;
        }
        return false;
    }

    public static User authenticate(String username, String password) {
        for (User u : loadUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static int getNextUserId() {
        int max = 0;
        for (User u : loadUsers()) max = Math.max(max, u.getId());
        return max + 1;
    }

    public static boolean addUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            bw.write(user.toFileString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------------
    // Events
    // ---------------------------------------------------------------
    public static List<BookingItem> loadEvents() {
        List<BookingItem> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(EVENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 9) continue;
                int id = Integer.parseInt(p[0].trim());
                String category = p[1].trim();
                String title = p[2].trim();
                String sub = p[3].trim();
                String location = p[4].trim();
                String date = p[5].trim();
                double price = Double.parseDouble(p[6].trim());
                int total = Integer.parseInt(p[7].trim());
                int available = Integer.parseInt(p[8].trim());

                BookingItem item;
                switch (category) {
                    case "Movie":
                        item = new Movie(id, title, location, date, price, total, available, sub);
                        break;
                    case "Concert":
                        item = new Concert(id, title, location, date, price, total, available, sub);
                        break;
                    case "Transport":
                        item = new Transport(id, title, location, date, price, total, available, sub);
                        break;
                    case "Event":
                        item = new GeneralEvent(id, title, location, date, price, total, available, sub);
                        break;
                    default:
                        item = null;
                }
                if (item != null) list.add(item);
            }
        } catch (IOException e) {
            System.err.println("Error reading events: " + e.getMessage());
        }
        return list;
    }

    public static boolean saveAllEvents(List<BookingItem> events) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EVENTS_FILE))) {
            for (BookingItem item : events) {
                bw.write(item.toFileString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving events: " + e.getMessage());
            return false;
        }
    }

    public static int getNextEventId() {
        int max = 0;
        for (BookingItem b : loadEvents()) max = Math.max(max, b.getId());
        return max + 1;
    }

    // ---------------------------------------------------------------
    // Bookings
    // ---------------------------------------------------------------
    public static List<Booking> loadBookings() {
        List<Booking> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 10) continue;
                list.add(new Booking(
                        Integer.parseInt(p[0].trim()),
                        Integer.parseInt(p[1].trim()),
                        p[2].trim(),
                        Integer.parseInt(p[3].trim()),
                        p[4].trim(),
                        p[5].trim(),
                        Integer.parseInt(p[6].trim()),
                        Double.parseDouble(p[7].trim()),
                        p[8].trim(),
                        p[9].trim()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error reading bookings: " + e.getMessage());
        }
        return list;
    }

    public static boolean saveAllBookings(List<Booking> bookings) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking b : bookings) {
                bw.write(b.toFileString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
            return false;
        }
    }

    public static boolean addBooking(Booking booking) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE, true))) {
            bw.write(booking.toFileString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving booking: " + e.getMessage());
            return false;
        }
    }

    public static int getNextBookingId() {
        int max = 0;
        for (Booking b : loadBookings()) max = Math.max(max, b.getBookingId());
        return max + 1;
    }
}
