package ui;

import math.SplineInterpolator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
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
    private static final int POINT_DIAMETER = 6;
    private static final int POINT_OFFSET = 0;
    private static final Color LINE_COLOR = new Color(160, 160, 160);
    private static final Color END_BACKGROUND_COLOR = new Color(70, 100, 255);
    private static final Color END_FOREGROUND_COLOR = END_BACKGROUND_COLOR;

    private SplineInterpolator splineInterpolator;


    /*--- Constructor ---*/

    SplinePanel() {
        this.setBackground(Color.WHITE);
        this.splineInterpolator = new SplineInterpolator();
    }


    /*--- Draw Method ---*/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Setup 2D Graphics
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

        // Draw End Point Backgrounds
        graphics.setColor(END_BACKGROUND_COLOR);
        Ellipse2D.Double circle = new Ellipse2D.Double(
                lineStart.x - (POINT_DIAMETER / 2) + POINT_OFFSET,
                lineStart.y - (POINT_DIAMETER / 2) + POINT_OFFSET,
                POINT_DIAMETER,
                POINT_DIAMETER
        );
        graphics.fill(circle);
        circle = new Ellipse2D.Double(
                lineEnd.x - (POINT_DIAMETER / 2) + POINT_OFFSET,
                lineEnd.y - (POINT_DIAMETER / 2) + POINT_OFFSET,
                POINT_DIAMETER,
                POINT_DIAMETER
        );
        graphics.fill(circle);

        // Draw End Point Foregrounds
        graphics.setColor(END_FOREGROUND_COLOR);
        graphics.drawOval(
                lineStart.x - (POINT_DIAMETER / 2) + POINT_OFFSET,
                lineStart.y - (POINT_DIAMETER / 2) + POINT_OFFSET,
                POINT_DIAMETER,
                POINT_DIAMETER
        );
        graphics.drawOval(
                lineEnd.x - (POINT_DIAMETER / 2) + POINT_OFFSET,
                lineEnd.y - (POINT_DIAMETER / 2) + POINT_OFFSET,
                POINT_DIAMETER,
                POINT_DIAMETER
        );
    }
}
