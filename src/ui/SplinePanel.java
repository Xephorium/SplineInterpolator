package ui;

import math.SplineInterpolator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

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
    private static final Color LINE_COLOR = new Color(170, 170, 170);
    private static final Color END_BACKGROUND_COLOR = new Color(70, 100, 255);
    private static final Color END_FOREGROUND_COLOR = END_BACKGROUND_COLOR;

    private SplineInterpolator splineInterpolator;
    private ArrayList<Point> points;


    /*--- Constructor ---*/

    SplinePanel() {
        setBackground(Color.WHITE);
        splineInterpolator = new SplineInterpolator();
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

        // Calculate Draw Dimensions
        int width = this.getSize().width;
        int height = this.getSize().height;

        // Define Points
        points = new ArrayList<>();
        points.add(new Point(100, height / 2));
        points.add(new Point(width - 100, height / 2));

        // Draw Line
        Line2D line = new Line2D.Float(getFirstPoint().x, getFirstPoint().y, getLastPoint().x, getLastPoint().y);
        graphics.draw(line);

        // Draw Boundary Point Backgrounds
        graphics.setColor(END_BACKGROUND_COLOR);
        Ellipse2D.Double circle = new Ellipse2D.Double(
                getFirstPoint().x - (POINT_DIAMETER / 2) + POINT_OFFSET,
                getFirstPoint().y - (POINT_DIAMETER / 2) + POINT_OFFSET,
                POINT_DIAMETER,
                POINT_DIAMETER
        );
        graphics.fill(circle);
        circle = new Ellipse2D.Double(
                getLastPoint().x - (POINT_DIAMETER / 2) + POINT_OFFSET,
                getLastPoint().y - (POINT_DIAMETER / 2) + POINT_OFFSET,
                POINT_DIAMETER,
                POINT_DIAMETER
        );
        graphics.fill(circle);

        // Draw Boundary Point Foregrounds
        graphics.setColor(END_FOREGROUND_COLOR);
        graphics.drawOval(
                getFirstPoint().x - (POINT_DIAMETER / 2) + POINT_OFFSET,
                getFirstPoint().y - (POINT_DIAMETER / 2) + POINT_OFFSET,
                POINT_DIAMETER,
                POINT_DIAMETER
        );
        graphics.drawOval(
                getLastPoint().x - (POINT_DIAMETER / 2) + POINT_OFFSET,
                getLastPoint().y - (POINT_DIAMETER / 2) + POINT_OFFSET,
                POINT_DIAMETER,
                POINT_DIAMETER
        );
        
        // Draw Returned Points
        for (Point point : splineInterpolator.getTestPoints(points)) {
            graphics.setColor(END_FOREGROUND_COLOR);
            circle = new Ellipse2D.Double(
                    point.x + POINT_OFFSET,
                    point.y + POINT_OFFSET,
                    POINT_DIAMETER,
                    POINT_DIAMETER
            );
            graphics.fill(circle);
        }
    }


    /*--- Private Methods ---*/

    private Point getFirstPoint() {
        return points.get(0);
    }

    private Point getLastPoint() {
        return points.get(points.size() - 1);
    }
}
