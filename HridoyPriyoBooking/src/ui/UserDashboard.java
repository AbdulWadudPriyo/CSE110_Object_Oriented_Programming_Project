package ui;

import model.*;
import util.FileManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserDashboard extends JFrame {

    private final User currentUser;
    private List<BookingItem> allEvents;

    private JTable eventTable;
    private DefaultTableModel eventTableModel;
    private JComboBox<String> categoryFilter;
    private JTextField locationFilter;
    private JTextField dateFilter;
    private JSpinner seatSpinner;
    private JLabel totalPriceLabel;

    private JTable bookingTable;
    private DefaultTableModel bookingTableModel;

    public UserDashboard(User user) {
        this.currentUser = user;

        setTitle("HridoyPriyoBooking - User Dashboard (" + user.getUsername() + ")");
        setSize(980, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildTopBar(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Browse & Book Events", buildBrowsePanel());
        tabs.addTab("My Bookings", buildMyBookingsPanel());
        add(tabs, BorderLayout.CENTER);

        refreshEvents();
        refreshBookings();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(30, 60, 114));
        bar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel welcome = new JLabel("Welcome, " + currentUser.getUsername() + "  (USER)");
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 15));

        RoundedButton logoutBtn = new RoundedButton("Logout", new Color(16, 34, 66), Color.WHITE, 10);
        logoutBtn.setPreferredSize(new Dimension(96, 34));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        bar.add(welcome, BorderLayout.WEST);
        bar.add(logoutBtn, BorderLayout.EAST);
        return bar;
    }

    // ---------------- Browse & Book ----------------
    private JPanel buildBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));

        categoryFilter = new JComboBox<>(new String[]{"All", "Movie", "Concert", "Transport", "Event"});
        locationFilter = new JTextField(14);
        dateFilter = new JTextField(10);
        RoundedButton searchBtn = new RoundedButton("Search", new Color(22, 46, 92), Color.WHITE, 10);
        RoundedButton resetBtn = new RoundedButton("Reset", new Color(90, 90, 96), Color.WHITE, 10);
        searchBtn.setPreferredSize(new Dimension(90, 30));
        resetBtn.setPreferredSize(new Dimension(80, 30));

        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilter);
        filterPanel.add(new JLabel("Location/Route/Title:"));
        filterPanel.add(locationFilter);
        filterPanel.add(new JLabel("Date (dd-MM-yyyy):"));
        filterPanel.add(dateFilter);
        filterPanel.add(searchBtn);
        filterPanel.add(resetBtn);

        panel.add(filterPanel, BorderLayout.NORTH);

        eventTableModel = new DefaultTableModel(
                new String[]{"ID", "Category", "Title", "Details", "Location", "Date", "Price (Tk)", "Available Seats"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        eventTable = new JTable(eventTableModel);
        eventTable.setRowHeight(24);
        eventTable.getSelectionModel().addListSelectionListener(e -> updateTotalPrice());

        panel.add(new JScrollPane(eventTable), BorderLayout.CENTER);

        JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        bookingPanel.setBorder(BorderFactory.createTitledBorder("Book Selected Event"));

        bookingPanel.add(new JLabel("Seats:"));
        seatSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        ((JSpinner.DefaultEditor) seatSpinner.getEditor()).getTextField().setColumns(3);
        seatSpinner.addChangeListener(e -> updateTotalPrice());
        bookingPanel.add(seatSpinner);

        totalPriceLabel = new JLabel("Total: Tk 0.00");
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookingPanel.add(totalPriceLabel);

        RoundedButton bookBtn = new RoundedButton("Book Now", new Color(30, 110, 70), Color.WHITE, 10);
        bookBtn.setPreferredSize(new Dimension(120, 34));
        bookBtn.addActionListener(e -> bookSelectedEvent());
        bookingPanel.add(bookBtn);

        panel.add(bookingPanel, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> applyFilters());
        resetBtn.addActionListener(e -> {
            categoryFilter.setSelectedIndex(0);
            locationFilter.setText("");
            dateFilter.setText("");
            refreshEvents();
        });

        return panel;
    }

    private void refreshEvents() {
        allEvents = FileManager.loadEvents();
        populateEventTable(allEvents);
    }

    private void populateEventTable(List<BookingItem> events) {
        eventTableModel.setRowCount(0);
        for (BookingItem item : events) {
            eventTableModel.addRow(new Object[]{
                    item.getId(), item.getCategory(), item.getTitle(), item.getDetails(),
                    item.getLocation(), item.getDate(), item.getBasePrice(), item.getAvailableSeats()
            });
        }
    }

    private void applyFilters() {
        String category = (String) categoryFilter.getSelectedItem();
        String location = locationFilter.getText().trim().toLowerCase();
        String date = dateFilter.getText().trim();

        List<BookingItem> filtered = allEvents.stream()
                .filter(ev -> category.equals("All") || ev.getCategory().equalsIgnoreCase(category))
                .filter(ev -> location.isEmpty()
                        || ev.getLocation().toLowerCase().contains(location)
                        || ev.getTitle().toLowerCase().contains(location))
                .filter(ev -> date.isEmpty() || ev.getDate().equals(date))
                .collect(Collectors.toList());

        populateEventTable(filtered);
    }

    private BookingItem getSelectedEvent() {
        int row = eventTable.getSelectedRow();
        if (row == -1) return null;
        int id = (int) eventTableModel.getValueAt(row, 0);
        return allEvents.stream().filter(ev -> ev.getId() == id).findFirst().orElse(null);
    }

    private void updateTotalPrice() {
        BookingItem item = getSelectedEvent();
        if (item == null) {
            totalPriceLabel.setText("Total: Tk 0.00");
            return;
        }
        int seats = (int) seatSpinner.getValue();
        double total = item.calculateTotalPrice(seats); // polymorphic call
        totalPriceLabel.setText(String.format("Total: Tk %.2f", total));
    }

    private void bookSelectedEvent() {
        BookingItem item = getSelectedEvent();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Please select an event first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int seats = (int) seatSpinner.getValue();

        List<BookingItem> freshEvents = FileManager.loadEvents();
        BookingItem freshItem = freshEvents.stream()
                .filter(ev -> ev.getId() == item.getId()).findFirst().orElse(null);

        if (freshItem == null) {
            JOptionPane.showMessageDialog(this, "This event is no longer available.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            refreshEvents();
            return;
        }

        if (!freshItem.bookSeats(seats)) {
            JOptionPane.showMessageDialog(this, "Not enough seats available. Only "
                    + freshItem.getAvailableSeats() + " left.", "Booking Failed", JOptionPane.ERROR_MESSAGE);
            refreshEvents();
            return;
        }

        double total = freshItem.calculateTotalPrice(seats);

        if (!FileManager.saveAllEvents(freshEvents)) {
            JOptionPane.showMessageDialog(this, "Could not update seat availability. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String bookingDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
        int bookingId = FileManager.getNextBookingId();
        Booking booking = new Booking(bookingId, currentUser.getId(), currentUser.getUsername(),
                freshItem.getId(), freshItem.getTitle(), freshItem.getCategory(), seats, total,
                bookingDate, "CONFIRMED");
        FileManager.addBooking(booking);

        String ticket = "===== HridoyPriyoBooking E-Ticket =====\n"
                + "Booking ID   : " + bookingId + "\n"
                + "Passenger    : " + currentUser.getUsername() + "\n"
                + "Category     : " + freshItem.getCategory() + "\n"
                + freshItem.getDetails() + "\n"
                + "Seats Booked : " + seats + "\n"
                + "Total Price  : Tk " + String.format("%.2f", total) + "\n"
                + "Booking Time : " + bookingDate + "\n"
                + "Status       : CONFIRMED\n"
                + "========================================";

        JOptionPane.showMessageDialog(this, ticket, "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

        refreshEvents();
        refreshBookings();
    }

    // ---------------- My Bookings ----------------
    private JPanel buildMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bookingTableModel = new DefaultTableModel(
                new String[]{"Booking ID", "Event", "Category", "Seats", "Total (Tk)", "Date", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        bookingTable = new JTable(bookingTableModel);
        bookingTable.setRowHeight(24);

        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        RoundedButton refreshBtn = new RoundedButton("Refresh", new Color(90, 90, 96), Color.WHITE, 10);
        refreshBtn.setPreferredSize(new Dimension(90, 32));
        refreshBtn.addActionListener(e -> refreshBookings());

        RoundedButton cancelBtn = new RoundedButton("Cancel Selected Booking", new Color(140, 24, 24), Color.WHITE, 10);
        cancelBtn.setPreferredSize(new Dimension(cancelBtn.getPreferredSize().width + 24, 32));
        cancelBtn.addActionListener(e -> cancelSelectedBooking());

        actionPanel.add(refreshBtn);
        actionPanel.add(cancelBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshBookings() {
        bookingTableModel.setRowCount(0);
        List<Booking> bookings = FileManager.loadBookings();
        for (Booking b : bookings) {
            if (b.getUserId() == currentUser.getId()) {
                bookingTableModel.addRow(new Object[]{
                        b.getBookingId(), b.getEventTitle(), b.getCategory(), b.getSeatCount(),
                        b.getTotalPrice(), b.getBookingDate(), b.getStatus()
                });
            }
        }
    }

    private void cancelSelectedBooking() {
        int row = bookingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int bookingId = (int) bookingTableModel.getValueAt(row, 0);
        String status = (String) bookingTableModel.getValueAt(row, 6);

        if (status.equals("CANCELLED")) {
            JOptionPane.showMessageDialog(this, "This booking is already cancelled.",
                    "Already Cancelled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this booking? Seats will be released.",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        List<Booking> bookings = FileManager.loadBookings();
        Booking target = null;
        for (Booking b : bookings) {
            if (b.getBookingId() == bookingId) {
                target = b;
                b.setStatus("CANCELLED");
                break;
            }
        }
        if (target == null) return;

        FileManager.saveAllBookings(bookings);

        List<BookingItem> events = FileManager.loadEvents();
        for (BookingItem ev : events) {
            if (ev.getId() == target.getEventId()) {
                ev.releaseSeats(target.getSeatCount());
                break;
            }
        }
        FileManager.saveAllEvents(events);

        JOptionPane.showMessageDialog(this, "Booking cancelled and seats released.",
                "Cancelled", JOptionPane.INFORMATION_MESSAGE);

        refreshEvents();
        refreshBookings();
    }
}
