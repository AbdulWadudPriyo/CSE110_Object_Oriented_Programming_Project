package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A flat, rounded-rectangle JButton with a subtle hover/press color shift.
 */
public class RoundedButton extends JButton {
    private final Color baseColor;
    private final Color hoverColor;
    private final Color pressColor;
    private final int radius;
    private boolean hovering = false;
    private boolean pressing = false;

    public RoundedButton(String text, Color baseColor, Color textColor, int radius) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = shift(baseColor, 22);
        this.pressColor = shift(baseColor, -22);
        this.radius = radius;

        setForeground(textColor);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { hovering = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { hovering = false; repaint(); }
            @Override
            public void mousePressed(MouseEvent e) { pressing = true; repaint(); }
            @Override
            public void mouseReleased(MouseEvent e) { pressing = false; repaint(); }
        });
    }

    private static Color shift(Color c, int amount) {
        int r = clamp(c.getRed() + amount);
        int g = clamp(c.getGreen() + amount);
        int b = clamp(c.getBlue() + amount);
        return new Color(r, g, b);
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fill = pressing ? pressColor : (hovering ? hoverColor : baseColor);
        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();

        super.paintComponent(g);
    }
}
