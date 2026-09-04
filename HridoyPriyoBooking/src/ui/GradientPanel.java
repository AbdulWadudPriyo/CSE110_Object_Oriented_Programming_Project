package ui;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel that paints a smooth top-to-bottom gradient background.
 */
public class GradientPanel extends JPanel {
    private final Color colorTop;
    private final Color colorBottom;

    public GradientPanel(Color colorTop, Color colorBottom) {
        this.colorTop = colorTop;
        this.colorBottom = colorBottom;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        GradientPaint gp = new GradientPaint(0, 0, colorTop, 0, getHeight(), colorBottom);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
