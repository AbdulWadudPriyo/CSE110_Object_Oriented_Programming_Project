package ui;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * A simple rounded-rectangle border used for text fields and badges.
 */
public class RoundedBorder extends AbstractBorder {
    private final int radius;
    private final Color color;
    private final int thickness;

    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (thickness <= 0) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRoundRect(x + thickness / 2, y + thickness / 2,
                width - thickness - 1, height - thickness - 1, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(10, 14, 10, 14);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(10, 14, 10, 14);
        return insets;
    }
}
