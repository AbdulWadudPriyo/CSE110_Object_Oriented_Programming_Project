package ui;

import model.*;
import util.FileManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDashboard extends JFrame {

    private final User currentUser;
    private List<BookingItem> allEvents;

    private JTable eventTable;
    private DefaultTableModel eventTableModel;

    private JComboBox<String> categoryBox;
    private JTextField titleField, subField, locationField, dateField, priceField, seatsField;

    private JTextArea reportArea;

    public AdminDashboard(User user) {
        this.currentUser = user;

        setTitle("HridoyPriyoBooking - Admin Dashboard");
        setSize(1020, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildTopBar(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manage Events", buildManagePanel());
        tabs.addTab("Sales Report", buildReportPanel());
        add(tabs, BorderLayout.CENTER);

        refreshEvents();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(139, 0, 0));
        bar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel welcome = new JLabel("Admin Panel - " + currentUser.getUsername());
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 15));

        RoundedButton logoutBtn = new RoundedButton("Logout", new Color(96, 15, 15), Color.WHITE, 10);
        logoutBtn.setPreferredSize(new Dimension(96, 34));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        bar.add(welcome, BorderLayout.WEST);
        bar.add(logoutBtn, BorderLayout.EAST);
        return bar;
    }

    // ---------------- Manage Events ----------------
    private JPanel buildManagePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        eventTableModel = new DefaultTableModel(
                new String[]{"ID", "Category", "Title", "Extra Info", "Location", "Date", "Price", "Total Seats", "Available"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        eventTable = new JTable(eventTableModel);
        eventTable.setRowHeight(24);
        eventTable.getSelectionModel().addListSelectionListener(e -> loadSelectedIntoForm());

        panel.add(new JScrollPane(eventTable), BorderLayout.CENTER);
        panel.add(buildFormPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBorder(BorderFactory.createTitledBorder("Add / Update Event"));

        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));

        categoryBox = new JComboBox<>(new String[]{"Movie", "Concert", "Transport", "Event"});
        titleField = new JTextField();
        subField = new JTextField();
        locationField = new JTextField();
        dateField = new JTextField();
        priceField = new JTextField();
        seatsField = new JTextField();

        form.add(new JLabel("Category:"));
        form.add(categoryBox);
        form.add(new JLabel("Title:"));
        form.add(titleField);

        form.add(new JLabel("Extra Info (Genre/Artist/Vehicle/Organizer):"));
        form.add(subField);
        form.add(new JLabel("Location / Route:"));
        form.add(locationField);

        form.add(new JLabel("Date (dd-MM-yyyy):"));
        form.add(dateField);
        form.add(new JLabel("Price (Tk):"));
        form.add(priceField);

        outer.add(form, BorderLayout.CENTER);

        JPanel seatsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        seatsRow.add(new JLabel("Total Seats:"));
        seatsField.setColumns(8);
        seatsRow.add(seatsField);
        outer.add(seatsRow, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        RoundedButton addBtn = new RoundedButton("Add New Event", new Color(30, 110, 70), Color.WHITE, 10);
        RoundedButton updateBtn = new RoundedButton("Update Selected", new Color(22, 46, 92), Color.WHITE, 10);
        RoundedButton deleteBtn = new RoundedButton("Delete Selected", new Color(140, 24, 24), Color.WHITE, 10);
        RoundedButton clearBtn = new RoundedButton("Clear Form", new Color(90, 90, 96), Color.WHITE, 10);

        for (RoundedButton b : new RoundedButton[]{addBtn, updateBtn, deleteBtn, clearBtn}) {
            b.setPreferredSize(new Dimension(b.getPreferredSize().width + 24, 34));
        }

        addBtn.addActionListener(e -> addEvent());
        updateBtn.addActionListener(e -> updateEvent());
        deleteBtn.addActionListener(e -> deleteEvent());
        clearBtn.addActionListener(e -> clearForm());

        buttons.add(clearBtn);
        buttons.add(deleteBtn);
        buttons.add(updateBtn);
        buttons.add(addBtn);

        outer.add(buttons, BorderLayout.SOUTH);
        return outer;
    }

    private void refreshEvents() {
        allEvents = FileManager.loadEvents();
        eventTableModel.setRowCount(0);
        for (BookingItem item : allEvents) {
            eventTableModel.addRow(new Object[]{
                    item.getId(), item.getCategory(), item.getTitle(), item.getSubField(),
                    item.getLocation(), item.getDate(), item.getBasePrice(),
                    item.getTotalSeats(), item.getAvailableSeats()
            });
        }
    }

    private void loadSelectedIntoForm() {
        int row = eventTable.getSelectedRow();
        if (row == -1) return;
        categoryBox.setSelectedItem(eventTableModel.getValueAt(row, 1));
        titleField.setText(eventTableModel.getValueAt(row, 2).toString());
        subField.setText(eventTableModel.getValueAt(row, 3).toString());
        locationField.setText(eventTableModel.getValueAt(row, 4).toString());
        dateField.setText(eventTableModel.getValueAt(row, 5).toString());
        priceField.setText(eventTableModel.getValueAt(row, 6).toString());
        seatsField.setText(eventTableModel.getValueAt(row, 7).toString());
    }

    private void clearForm() {
        eventTable.clearSelection();
        titleField.setText("");
        subField.setText("");
        locationField.setText("");
        dateField.setText("");
        priceField.setText("");
        seatsField.setText("");
    }

    private BookingItem buildItemFromForm(int id, int available) {
        String category = (String) categoryBox.getSelectedItem();
        String title = titleField.getText().trim().replace("|", "");
        String sub = subField.getText().trim().replace("|", "");
        String location = locationField.getText().trim().replace("|", "");
        String date = dateField.getText().trim().replace("|", "");
        double price = Double.parseDouble(priceField.getText().trim());
        int seats = Integer.parseInt(seatsField.getText().trim());

        switch (category) {
            case "Movie":
                return new Movie(id, title, location, date, price, seats, available, sub);
            case "Concert":
                return new Concert(id, title, location, date, price, seats, available, sub);
            case "Transport":
                return new Transport(id, title, location, date, price, seats, available, sub);
            default:
                return new GeneralEvent(id, title, location, date, price, seats, available, sub);
        }
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty() || locationField.getText().trim().isEmpty()
                || dateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title, Location and Date are required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(priceField.getText().trim());
            Integer.parseInt(seatsField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a number and Seats must be an integer.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void addEvent() {
        if (!validateForm()) return;
        int newId = FileManager.getNextEventId();
        int seats = Integer.parseInt(seatsField.getText().trim());
        BookingItem newItem = buildItemFromForm(newId, seats);

        allEvents.add(newItem);
        if (FileManager.saveAllEvents(allEvents)) {
            JOptionPane.showMessageDialog(this, "Event added successfully.");
            clearForm();
            refreshEvents();
        }
    }

    private void updateEvent() {
        int row = eventTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an event to update.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm()) return;

        int id = (int) eventTableModel.getValueAt(row, 0);
        int oldAvailable = (int) eventTableModel.getValueAt(row, 8);
        int oldTotal = (int) eventTableModel.getValueAt(row, 7);
        int newTotal = Integer.parseInt(seatsField.getText().trim());

        int newAvailable = oldAvailable + (newTotal - oldTotal);
        if (newAvailable < 0) newAvailable = 0;

        BookingItem updated = buildItemFromForm(id, newAvailable);

        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getId() == id) {
                allEvents.set(i, updated);
                break;
            }
        }

        if (FileManager.saveAllEvents(allEvents)) {
            JOptionPane.showMessageDialog(this, "Event updated successfully.");
            clearForm();
            refreshEvents();
        }
    }

    private void deleteEvent() {
        int row = eventTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an event to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this event permanently?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) eventTableModel.getValueAt(row, 0);
        allEvents.removeIf(ev -> ev.getId() == id);

        if (FileManager.saveAllEvents(allEvents)) {
            JOptionPane.showMessageDialog(this, "Event deleted.");
            clearForm();
            refreshEvents();
        }
    }

    // ---------------- Sales Report ----------------
    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        RoundedButton refreshBtn = new RoundedButton("Generate / Refresh Report", new Color(22, 46, 92), Color.WHITE, 10);
        refreshBtn.setPreferredSize(new Dimension(refreshBtn.getPreferredSize().width + 30, 34));
        refreshBtn.addActionListener(e -> generateReport());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(refreshBtn);
        panel.add(south, BorderLayout.SOUTH);

        generateReport();
        return panel;
    }

    private void generateReport() {
        List<Booking> bookings = FileManager.loadBookings();

        int totalTickets = 0;
        double totalRevenue = 0;
        Map<String, Integer> ticketsByCategory = new HashMap<>();
        Map<String, Double> revenueByCategory = new HashMap<>();
        int cancelledCount = 0;

        for (Booking b : bookings) {
            if (b.getStatus().equalsIgnoreCase("CONFIRMED")) {
                totalTickets += b.getSeatCount();
                totalRevenue += b.getTotalPrice();
                ticketsByCategory.merge(b.getCategory(), b.getSeatCount(), Integer::sum);
                revenueByCategory.merge(b.getCategory(), b.getTotalPrice(), Double::sum);
            } else {
                cancelledCount++;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========= HridoyPriyoBooking Sales Summary =========\n\n");
        sb.append(String.format("Total Confirmed Bookings : %d%n", bookings.size() - cancelledCount));
        sb.append(String.format("Total Cancelled Bookings : %d%n", cancelledCount));
        sb.append(String.format("Total Tickets Sold       : %d%n", totalTickets));
        sb.append(String.format("Total Revenue            : Tk %.2f%n%n", totalRevenue));
        sb.append("---- Breakdown by Category ----\n");
        for (String cat : new String[]{"Movie", "Concert", "Transport", "Event"}) {
            int tk = ticketsByCategory.getOrDefault(cat, 0);
            double rev = revenueByCategory.getOrDefault(cat, 0.0);
            sb.append(String.format("%-10s | Tickets: %-5d | Revenue: Tk %.2f%n", cat, tk, rev));
        }
        sb.append("\n======================================================\n");

        reportArea.setText(sb.toString());
    }
}
