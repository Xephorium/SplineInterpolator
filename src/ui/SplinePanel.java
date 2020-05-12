package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

/* Spline Interpolator
 * Christopher Cruzen
 * 05.12.2020
 *
 *   SplinePanel is visual backbone of Spline Interpolator. It manages curve rendering,
 * requesting a variable number of point samples from the SplineInterpolator class to
 * display.
 */

class SplinePanel extends JPanel {


    /*--- Variable Declarations ---*/

    private static final int LINE_WIDTH = 2;
    private static final Color LINE_COLOR = Color.GRAY;


    /*--- Constructor ---*/

    SplinePanel() {
        this.setBackground(Color.WHITE);
    }


    /*--- Draw Method ---*/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Setup 2D Graphics
        Graphics2D graphics = (Graphics2D) g;
        graphics.setColor(LINE_COLOR);
        graphics.setStroke(new BasicStroke(LINE_WIDTH));

        // Calculate Draw Variables
        int width = this.getSize().width;
        int height = this.getSize().height;
        Point lineStart = new Point(100, height / 2);
        Point lineEnd = new Point(width - 100, height / 2);

        // Draw Line
        Line2D line = new Line2D.Float(lineStart.x, lineStart.y, lineEnd.x, lineEnd.y);
        graphics.draw(line);
    }
}
