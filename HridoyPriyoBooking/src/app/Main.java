package app;

import ui.LoginFrame;
import util.FileManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Make sure data/users.txt, data/events.txt, data/bookings.txt exist
        FileManager.ensureDataFiles();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException
                     | IllegalAccessException | UnsupportedLookAndFeelException e) {
                System.err.println("Could not set system look and feel: " + e.getMessage());
            }

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}