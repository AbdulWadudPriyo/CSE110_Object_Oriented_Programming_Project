package ui;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel painted as a rounded rectangle with a soft drop shadow,
 * used for "card" style containers (e.g. the login card).
 */
public class RoundedPanel extends JPanel {
    private final int radius;
    private final Color bgColor;
    private final boolean shadow;

    public RoundedPanel(int radius, Color bgColor) {
        this(radius, bgColor, true);
    }

    public RoundedPanel(int radius, Color bgColor, boolean shadow) {
        this.radius = radius;
        this.bgColor = bgColor;
        this.shadow = shadow;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int shadowGap = shadow ? 6 : 0;
        if (shadow) {
            g2.setColor(new Color(20, 30, 60, 35));
            g2.fillRoundRect(3, shadowGap, getWidth() - 6, getHeight() - shadowGap, radius, radius);
        }

        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth() - shadowGap, getHeight() - shadowGap, radius, radius);
        g2.dispose();

        super.paintComponent(g);
    }
}
