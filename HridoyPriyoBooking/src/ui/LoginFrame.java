package ui;

import model.User;
import util.FileManager;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private static final Color PRIMARY = new Color(30, 60, 114);
    private static final Color PRIMARY_LIGHT = new Color(64, 100, 172);
    private static final Color BG_TOP = new Color(41, 74, 133);
    private static final Color BG_BOTTOM = new Color(12, 26, 58);
    private static final Color FIELD_BORDER = new Color(210, 214, 224);
    private static final Color TEXT_MUTED = new Color(120, 128, 145);
    private static final Color TEXT_DARK = new Color(28, 32, 40);

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("HridoyPriyoBooking - Login");
        setSize(560, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GradientPanel background = new GradientPanel(BG_TOP, BG_BOTTOM);
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        RoundedPanel card = new RoundedPanel(26, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(38, 40, 34, 40));
        card.setPreferredSize(new Dimension(460, 600));

        // ---- Logo row (left aligned, like a brand header) ----
        JPanel logoRow = new JPanel();
        logoRow.setLayout(new BoxLayout(logoRow, BoxLayout.X_AXIS));
        logoRow.setOpaque(false);
        logoRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoRow.setMaximumSize(new Dimension(1000, 46));

        RoundedPanel logoBadge = new RoundedPanel(12, new Color(232, 238, 250), false);
        logoBadge.setLayout(new GridBagLayout());
        logoBadge.setPreferredSize(new Dimension(40, 40));
        logoBadge.setMaximumSize(new Dimension(40, 40));
        JLabel logoText = new JLabel("\uD83C\uDFAB");
        logoText.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        logoBadge.add(logoText);

        JLabel brandTitle = new JLabel("HridoyPriyoBooking");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandTitle.setForeground(PRIMARY);
        brandTitle.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        logoRow.add(logoBadge);
        logoRow.add(brandTitle);
        logoRow.add(Box.createHorizontalGlue());

        card.add(logoRow);
        card.add(Box.createRigidArea(new Dimension(0, 36)));

        // ---- Heading ----
        JLabel eyebrow = new JLabel("Please enter your details");
        eyebrow.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        eyebrow.setForeground(TEXT_MUTED);
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heading = new JLabel("Welcome back");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(TEXT_DARK);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(eyebrow);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(heading);
        card.add(Box.createRigidArea(new Dimension(0, 28)));

        // ---- Username field ----
        usernameField = new PlaceholderTextField("Username");
        styleField(usernameField);
        card.add(usernameField);
        card.add(Box.createRigidArea(new Dimension(0, 16)));

        // ---- Password field ----
        passwordField = new PlaceholderPasswordField("Password");
        styleField(passwordField);
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        // ---- Remember me / Forgot password row ----
        JPanel optionsRow = new JPanel(new BorderLayout());
        optionsRow.setOpaque(false);
        optionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionsRow.setMaximumSize(new Dimension(1000, 28));

        JCheckBox rememberBox = new JCheckBox("Remember me");
        rememberBox.setOpaque(false);
        rememberBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rememberBox.setForeground(TEXT_MUTED);
        rememberBox.setFocusPainted(false);
        rememberBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton forgotBtn = new JButton("Forgot password?");
        forgotBtn.setFocusPainted(false);
        forgotBtn.setBorderPainted(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setForeground(PRIMARY_LIGHT);
        forgotBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        forgotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Please contact the admin to reset your password.",
                "Forgot Password", JOptionPane.INFORMATION_MESSAGE));

        optionsRow.add(rememberBox, BorderLayout.WEST);
        optionsRow.add(forgotBtn, BorderLayout.EAST);

        card.add(optionsRow);
        card.add(Box.createRigidArea(new Dimension(0, 24)));

        // ---- Login button ----
        RoundedButton loginBtn = new RoundedButton("Login", PRIMARY, Color.WHITE, 12);
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(1000, 50));
        loginBtn.setPreferredSize(new Dimension(1000, 50));

        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 232, 238));
        sep.setMaximumSize(new Dimension(1000, 1));
        card.add(sep);
        card.add(Box.createRigidArea(new Dimension(0, 18)));

        // ---- Bottom "create account" row ----
        JPanel bottomRow = new JPanel();
        bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.X_AXIS));
        bottomRow.setOpaque(false);
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomRow.setMaximumSize(new Dimension(1000, 26));

        JLabel noAccount = new JLabel("Don't have an account?");
        noAccount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noAccount.setForeground(TEXT_MUTED);

        JButton registerBtn = new JButton("Create New Account");
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setForeground(PRIMARY_LIGHT);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setMargin(new Insets(0, 6, 0, 0));

        bottomRow.add(noAccount);
        bottomRow.add(registerBtn);
        bottomRow.add(Box.createHorizontalGlue());

        card.add(bottomRow);

        background.add(card, new GridBagConstraints());

        loginBtn.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin());
        registerBtn.addActionListener(e -> new RegisterDialog(this).setVisible(true));
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(new RoundedBorder(10, FIELD_BORDER, 1));
        field.setMaximumSize(new Dimension(1000, 56));
        field.setPreferredSize(new Dimension(1000, 56));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_DARK);
        field.setMargin(new Insets(4, 16, 4, 16));
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = FileManager.authenticate(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose();
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            new AdminDashboard(user).setVisible(true);
        } else {
            new UserDashboard(user).setVisible(true);
        }
    }

    /**
     * A JTextField that shows light gray placeholder text when empty,
     * since Swing has no built-in placeholder support.
     */
    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;

        PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(160, 165, 178));
                g2.setFont(getFont());
                Insets ins = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int x = ins.left;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }

    /**
     * A JPasswordField that shows light gray placeholder text when empty.
     */
    private static class PlaceholderPasswordField extends JPasswordField {
        private final String placeholder;

        PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(160, 165, 178));
                g2.setFont(getFont());
                Insets ins = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int x = ins.left;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }
}
