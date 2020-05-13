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

    private static final int CURVE_SAMPLES = 20;

    private static final int LINE_WIDTH = 2;
    private static final int POINT_DIAMETER = 7;
    private static final int POINT_DIAMETER_SMALL = 5;
    private static final int POINT_OFFSET = 0;
    private static final Color LINE_COLOR = new Color(240, 240, 240);
    private static final Color END_POINT_COLOR = new Color(70, 100, 255);
    private static final Color SOURCE_POINT_COLOR = new Color(60, 200, 90);
    private static final Color NEW_POINT_COLOR = new Color(135, 135, 135);

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
        graphics.setStroke(new BasicStroke(LINE_WIDTH));
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate Draw Dimensions
        int width = this.getSize().width;
        int height = this.getSize().height;

        // Define Source Points
        points = new ArrayList<>();
        points.add(new Point(100, height / 2));
        points.add(new Point(width / 3, height / 4));
        points.add(new Point(width - (width / 2), height - (height / 3)));
        points.add(new Point(width - 100, height / 2));

        // Draw Source Lines
        graphics.setColor(LINE_COLOR);
        for (int x = 0; x < points.size() - 1; x++) {
            drawLine(graphics, points.get(x), points.get(x + 1));
        }

        // Draw Source Points
        graphics.setColor(SOURCE_POINT_COLOR);
        for (Point point : points) {
            drawPoint(graphics, point);
        }

        // Draw Boundary Points
        graphics.setColor(END_POINT_COLOR);
        drawPoint(graphics, getFirstPoint());
        drawPoint(graphics, getLastPoint());
        
        // Draw Interpolated Curve
        graphics.setColor(NEW_POINT_COLOR);
        ArrayList<Point> curveSamples = splineInterpolator.getTestPoints(points, CURVE_SAMPLES);
        drawLine(graphics, points.get(0), curveSamples.get(0));
        drawLine(graphics, points.get(points.size() - 1), curveSamples.get(curveSamples.size() - 1));
        for (int x = 0; x < curveSamples.size() - 1; x++) {
            drawLine(graphics, curveSamples.get(x), curveSamples.get(x + 1));
        }
        // Draw Interpolated Points
        graphics.setColor(NEW_POINT_COLOR);
        for (Point point : curveSamples) {
            drawSmallPoint(graphics, point);
        }

    }


    /*--- Private Methods ---*/

    private void drawLine(Graphics2D graphics, Point start, Point end) {
        Line2D line = new Line2D.Float(start.x, start.y, end.x, end.y);
        graphics.draw(line);
    }

    private void drawPoint(Graphics2D graphics, Point point) {
        Ellipse2D.Double circle = new Ellipse2D.Double(
                point.x - (POINT_DIAMETER / 2) + POINT_OFFSET,
                point.y - (POINT_DIAMETER / 2) + POINT_OFFSET,
                POINT_DIAMETER,
                POINT_DIAMETER
        );
        graphics.fill(circle);
    }

    private void drawSmallPoint(Graphics2D graphics, Point point) {
        Ellipse2D.Double circle = new Ellipse2D.Double(
                point.x - (POINT_DIAMETER_SMALL / 2) + POINT_OFFSET,
                point.y - (POINT_DIAMETER_SMALL / 2) + POINT_OFFSET,
                POINT_DIAMETER_SMALL,
                POINT_DIAMETER_SMALL
        );
        graphics.fill(circle);
    }

    private Point getFirstPoint() {
        return points.get(0);
    }

    private Point getLastPoint() {
        return points.get(points.size() - 1);
    }
}
