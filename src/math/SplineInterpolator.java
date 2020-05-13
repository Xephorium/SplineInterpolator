package math;

import java.awt.*;
import java.util.ArrayList;

/* Spline Interpolator
 * Christopher Cruzen
 * 05.12.2020
 *
 *
 */


public class SplineInterpolator {


    /*--- Constructor ---*/

    public SplineInterpolator() {

    }


    /*--- Public Methods ---*/

    public ArrayList<Point> getTestPoints(ArrayList<Point> points) {

        // Declare Local Variables
        QuadraticInterpolator quadInterpolator = new QuadraticInterpolator(points);
        ArrayList<Point> newPoints = new ArrayList<>();

        // Draw Interpolated Points
        int divisions = 30;
        for (int x = 1; x < divisions + 1; x++) {
            double factor = (double) x / (divisions + 1);
            newPoints.add(quadInterpolator.getPoint(factor));
        }

        return newPoints;
    }


    /*--- Private Methods ---*/

    /* Note: This method imagines a line between the first and last points
     *       it is passed. It then divides that line into a variable number
     *       of segments and returns the "division" points. This is
     *       shown in the diagram below, with p(0)/p(N) representing the first
     *       and last input points and d(0)/d(N) representing the division points.
     *
     *       p(0)-------d(0)--...--d(N)-------p(N)
     *
     */
    private ArrayList<Point> getDivisionPoints(ArrayList<Point> points, int divisions) {

        // Declare Local Variables
        Point firstPoint = points.get(0);
        Point lastPoint = points.get(points.size() - 1);

        // Calculate Division Points
        ArrayList<Point> divisionPoints = new ArrayList<>();
        for (int x = 0; x < divisions - 1; x++) {
            double interpolationFactor = (x + 1.0) * (1.0 / divisions);
            divisionPoints.add(linearlyInterpolatePoints(firstPoint, lastPoint, interpolationFactor));
        }

        return divisionPoints;
    }

    /* Note: This method performs linear interpolation between the two passed points,
     *       returning the location factor/1.0 of the way between them.
     */
    private Point linearlyInterpolatePoints(Point pointOne, Point pointTwo, double factor) {
        return new Point(
                (int) (pointTwo.x * factor + pointOne.x * (1.0 - factor)),
                (int) (pointTwo.y * factor + pointOne.y * (1.0 - factor))
        );
    }
}
