package ui;

import model.User;
import util.FileManager;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {

    private static final Color PRIMARY = new Color(30, 60, 114);
    private static final Color FIELD_BORDER = new Color(210, 216, 230);
    private static final Color TEXT_MUTED = new Color(120, 128, 145);

    public RegisterDialog(JFrame parent) {
        super(parent, "Create New Account", true);
        setSize(400, 470);
        setLocationRelativeTo(parent);
        setResizable(false);
        setUndecorated(false);

        GradientPanel background = new GradientPanel(new Color(41, 74, 133), new Color(12, 26, 58));
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        RoundedPanel card = new RoundedPanel(24, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 26, 30));
        card.setPreferredSize(new Dimension(330, 400));

        JLabel header = new JLabel("Create Account");
        header.setFont(new Font("Segoe UI", Font.BOLD, 19));
        header.setForeground(PRIMARY);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Join HridoyPriyoBooking");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(sub);
        card.add(Box.createRigidArea(new Dimension(0, 24)));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();

        card.add(fieldLabel("USERNAME"));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        styleField(usernameField);
        card.add(usernameField);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        card.add(fieldLabel("PASSWORD"));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        styleField(passwordField);
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        card.add(fieldLabel("CONFIRM PASSWORD"));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        styleField(confirmField);
        card.add(confirmField);
        card.add(Box.createRigidArea(new Dimension(0, 22)));

        RoundedButton registerBtn = new RoundedButton("Register", PRIMARY, Color.WHITE, 12);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(1000, 42));
        registerBtn.setPreferredSize(new Dimension(1000, 42));
        card.add(registerBtn);

        background.add(card, new GridBagConstraints());

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim().replace("|", "");
            String password = new String(passwordField.getPassword()).replace("|", "");
            String confirm = new String(confirmField.getPassword()).replace("|", "");

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (FileManager.isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int newId = FileManager.getNextUserId();
            User newUser = new User(newId, username, password, "USER");
            if (FileManager.addUser(newUser)) {
                JOptionPane.showMessageDialog(this, "Account created successfully! You can now login.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not save account. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, FIELD_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        Dimension size = new Dimension(1000, 44);
        field.setMinimumSize(size);
        field.setMaximumSize(size);
        field.setPreferredSize(size);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBackground(new Color(250, 251, 253));
    }
}