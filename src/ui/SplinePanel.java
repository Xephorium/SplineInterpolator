package ui;

import math.SplineInterpolator;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/* Spline Interpolator
 * Christopher Cruzen
 * 05.12.2020
 *
 *   SplinePanel is visual backbone of Spline Interpolator. It handles curve
 * rendering and user interactions.
 */

class SplinePanel extends JPanel {


    /*--- Variable Declarations ---*/

    private static final int CURVE_SAMPLES = 30;

    private static final int LINE_WIDTH = 3;
    private static final int POINT_DIAMETER = 9;
    private static final int POINT_DIAMETER_SMALL = 7;
    private static final int POINT_SELECTION_RANGE = 20;
    private static final int POINT_OFFSET = 0;
    private static final Color LINE_COLOR = new Color(240, 240, 240);
    private static final Color END_POINT_COLOR = new Color(70, 100, 255);
    private static final Color SOURCE_POINT_COLOR = new Color(60, 200, 90);
    private static final Color NEW_POINT_COLOR = new Color(135, 135, 135);
    private static final Color TEXT_COLOR = new Color(100, 100, 100);

    private SplineInterpolator splineInterpolator;
    private int selectedPointIndex;
    private boolean isPointSelected;
    private Point selectionStartPosition;
    private ArrayList<Point> points;


    /*--- Constructor ---*/

    SplinePanel() {

        // Configure UI
        setBackground(Color.WHITE);
        setupMouseListeners();

        // Initialize Fields
        splineInterpolator = new SplineInterpolator();

        // Initialize Control Points
        points = new ArrayList<>();
        points.add(new Point(200, 400));
        points.add(new Point(450, 200));
        points.add(new Point(750, 600));
        points.add(new Point(1000, 400));
    }


    /*--- Draw Method ---*/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Setup 2D Graphics
        Graphics2D graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke(LINE_WIDTH));
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

        // Draw Boundary Points
        graphics.setColor(END_POINT_COLOR);
        drawPoint(graphics, getFirstPoint());
        drawPoint(graphics, getLastPoint());

        // Draw Text
        graphics.setColor(TEXT_COLOR);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString("Control Points: " + points.size(), 16, getHeight() - 50);
        graphics.drawString("Curve Samples: " + CURVE_SAMPLES, 16, getHeight() - 20);

    }


    /*--- Private Data Methods ---*/

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


    /*--- Private UI Methods ---*/

    private void setupMouseListeners() {
        MouseInputAdapter mouseClickAdapter = new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                // Get Selected Point
                Integer index = selectPointIndex(e.getPoint());

                // If Point Selected, Update State
                if (!isPointSelected && index != null) {
                    isPointSelected = true;
                    selectedPointIndex = index;
                    selectionStartPosition = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                isPointSelected = false;
            }
        };
        MouseInputAdapter mouseDragAdapter = new MouseInputAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (isPointSelected) {

                    // Calculate New Position
                    int differenceX = e.getX() - selectionStartPosition.x;
                    int differenceY = e.getY() - selectionStartPosition.y;
                    int newPositionX = selectionStartPosition.x + differenceX;
                    int newPositionY = selectionStartPosition.y + differenceY;

                    // Perform Bounds Check
                    if (newPositionX > getWidth()) newPositionX = getWidth();
                    if (newPositionX < 0) newPositionX = 0;
                    if (newPositionY > getHeight()) newPositionY = getHeight();
                    if (newPositionY < 0) newPositionY = 0;

                    // Update Position
                    points.get(selectedPointIndex).x = newPositionX;
                    points.get(selectedPointIndex).y = newPositionY;

                    repaint();
                }
            }
        };
        this.addMouseListener(mouseClickAdapter);
        this.addMouseMotionListener(mouseDragAdapter);
    }

    private Integer selectPointIndex(Point position) {

        // Declare Local Variables
        double distance = 1000000; // Arbitrarily High Number
        int index = 0;

        // Find Nearest Point
        for (int x = 0; x < points.size(); x++) {
            double pointDistance = points.get(x).distance(position);
            if (pointDistance < distance) {
                distance = pointDistance;
                index = x;
            }
        }

        // Return Point If In Range
        if (points.get(index).distance(position) < POINT_SELECTION_RANGE) {
            return index;
        } else {
            return null;
        }
    }
}
