package ui;

import math.SplineInterpolator;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Date;

/* Spline Interpolator
 * Christopher Cruzen
 * 05.12.2020
 *
 *   SplinePanel is visual backbone of Spline Interpolator. It handles curve
 * rendering and user interactions.
 */

class SplinePanel extends JPanel {


    /*--- Variable Declarations ---*/

    // State Constants
    private static final boolean SHOW_ANIMATED_POINT = true;
    private static final int LOOP_LENGTH = 10000;
    private static final int CURVE_SAMPLES = 30;
    private static final int CONTROL_POINTS = 5;

    // Interface Constants
    private static final int LINE_WIDTH = 3;
    private static final int POINT_DIAMETER = 11;
    private static final int POINT_DIAMETER_SMALL = 5;
    private static final int POINT_SELECTION_RANGE = 20;
    private static final int POINT_OFFSET = 0;
    private static final int BACKGROUND_LIGHT = 255;
    private static final int BACKGROUND_DARK = 235;
    private static final int BACKGROUND_STRIP_HEIGHT = 20;
    private static final Color LINE_COLOR = new Color(210, 210, 210);
    private static final Color END_POINT_COLOR = new Color(80, 130, 255);
    private static final Color CONTROL_POINT_COLOR = new Color(60, 200, 90);
    private static final Color ANIMATED_POINT_COLOR = new Color(255, 110, 100);
    private static final Color INTERPOLATED_POINT_COLOR = new Color(125, 125, 125);
    private static final Color INTERPOLATED_LINE_COLOR = new Color(125, 125, 125);
    private static final Color TEXT_COLOR = new Color(100, 100, 100);

    // Variables
    private long startTime;
    private SplineInterpolator splineInterpolator;
    private int selectedPointIndex;
    private boolean isPointSelected;
    private Point selectionStartPosition;
    private ArrayList<Point> controlPoints;


    /*--- Constructor ---*/

    SplinePanel() {

        // Initialize Variables
        startTime = (new Date()).getTime();

        // Configure UI
        setBackground(Color.WHITE);
        setupMouseListeners();

        // Initialize Control Points
        controlPoints = new ArrayList<>();
        controlPoints.add(new Point(70, 390));
        controlPoints.add(new Point(1100, 390));
        ArrayList<Point> innerControlPoints = SplineInterpolator.getDivisionPoints(controlPoints, CONTROL_POINTS - 1);
        controlPoints.remove(controlPoints.size() - 1);
        for (int x = 0; x < innerControlPoints.size(); x++) {
            double factor = (x + 1) * (1 / ((double) innerControlPoints.size() + 1));
            controlPoints.add(new Point(
                    innerControlPoints.get(x).x,
                    innerControlPoints.get(x).y + getInitialControlPointOffset(factor)
            ));
        }
        controlPoints.add(new Point(1110, 390));
    }


    /*--- Draw Method ---*/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Setup 2D Graphics
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw Background
        double color = BACKGROUND_DARK;
        double colorDiff = BACKGROUND_LIGHT - BACKGROUND_DARK;
        graphics.setStroke(new BasicStroke(BACKGROUND_STRIP_HEIGHT));
        for (int y = 0; y < getHeight(); y += BACKGROUND_STRIP_HEIGHT - 1) {
            graphics.setColor(new Color((int) color, (int) color, (int) color));
            Line2D line = new Line2D.Float(0, y, getWidth(), y);
            graphics.draw(line);
            color += (colorDiff / (getHeight())) * (BACKGROUND_STRIP_HEIGHT - 1);
        }

        // Draw Control Lines
        graphics.setColor(LINE_COLOR);
        graphics.setStroke(new BasicStroke(LINE_WIDTH));
        for (int x = 0; x < controlPoints.size() - 1; x++) {
            drawLine(graphics, controlPoints.get(x), controlPoints.get(x + 1));
        }

        // Generate Interpolated Points
        ArrayList<Point> curveSamples = getCurveSamplePoints(controlPoints, CURVE_SAMPLES);

        // Generate Animated Point
        Point animatedPoint;
        if (SHOW_ANIMATED_POINT) animatedPoint = getCurveAnimatedPoint();

        // Draw Interpolated Curve
        graphics.setColor(INTERPOLATED_LINE_COLOR);
        drawLine(graphics, controlPoints.get(0), curveSamples.get(0));
        drawLine(graphics, controlPoints.get(controlPoints.size() - 1), curveSamples.get(curveSamples.size() - 1));
        for (int x = 0; x < curveSamples.size() - 1; x++) {
            drawLine(graphics, curveSamples.get(x), curveSamples.get(x + 1));
        }

        // Draw Interpolated Points
        graphics.setColor(INTERPOLATED_POINT_COLOR);
        for (Point point : curveSamples) {
            drawSmallPoint(graphics, point);
        }

        // Draw Animated Point
        graphics.setColor(ANIMATED_POINT_COLOR);
        if (SHOW_ANIMATED_POINT) drawPoint(graphics, animatedPoint);

        // Draw Control Points
        graphics.setColor(CONTROL_POINT_COLOR);
        for (Point point : controlPoints) {
            drawPoint(graphics, point);
        }

        // Draw Boundary Points
        graphics.setColor(END_POINT_COLOR);
        drawPoint(graphics, getFirstPoint());
        drawPoint(graphics, getLastPoint());

        // Draw Text
        graphics.setColor(TEXT_COLOR);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString("Control Points: " + controlPoints.size(), 16, getHeight() - 50);
        graphics.drawString("Curve Samples: " + CURVE_SAMPLES, 16, getHeight() - 20);

        if (SHOW_ANIMATED_POINT) repaint();
    }


    /*--- Private Data Methods ---*/

    private ArrayList<Point> getCurveSamplePoints(ArrayList<Point> points, int divisions) {

        // Declare Local Variables
        splineInterpolator = new SplineInterpolator(points);
        ArrayList<Point> newPoints = new ArrayList<>();

        // Retrieve Interpolated Points
        for (int x = 1; x < divisions + 1; x++) {
            double factor = (double) x / (divisions + 1);
            newPoints.add(splineInterpolator.getInterpolatedPoint(factor));
        }

        return newPoints;
    }

    private Point getCurveAnimatedPoint() {

        // Declare Local Variables
        long time = (new Date()).getTime();
        double factor = ((time - startTime) % LOOP_LENGTH) / (double) LOOP_LENGTH;

        // Return Animated Point
        return splineInterpolator.getInterpolatedPoint(factor);
    }

    private Point getFirstPoint() {
        return controlPoints.get(0);
    }

    private Point getLastPoint() {
        return controlPoints.get(controlPoints.size() - 1);
    }

    private int getInitialControlPointOffset(double factor) {
        return (int) (100 * Math.sin(2 * Math.PI * factor));
    }


    /*--- Private UI Methods ---*/

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
                    controlPoints.get(selectedPointIndex).x = newPositionX;
                    controlPoints.get(selectedPointIndex).y = newPositionY;

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
        for (int x = 0; x < controlPoints.size(); x++) {
            double pointDistance = controlPoints.get(x).distance(position);
            if (pointDistance < distance) {
                distance = pointDistance;
                index = x;
            }
        }

        // Return Point If In Range
        if (controlPoints.get(index).distance(position) < POINT_SELECTION_RANGE) {
            return index;
        } else {
            return null;
        }
    }
}
