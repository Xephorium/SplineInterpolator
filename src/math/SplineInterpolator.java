package math;

import java.awt.*;
import java.util.ArrayList;

/* Spline Interpolator
 * Christopher Cruzen
 * 05.12.2020
 *
 *   The Spline Interpolator class performs quadratic interpolation between an arbitrary number
 * of points, returning the location (factor/1.0) of the way along a smoothed curve through them.
 * The class uses De Casteljau's Algorithm for smoothing, as adapted from the Stack Overflow post
 * below.
 *
 * Post: https://math.stackexchange.com/questions/43947/casteljaus-algorithm-practical-example
 * De Casteljau's Algorithm: https://en.wikipedia.org/wiki/De_Casteljau%27s_algorithm
 *
 *   In the process of reading up on the math theory behind spline calculation, this UCLA paper
 * was also extremely helpful: https://www.math.ucla.edu/~baker/149.1.02w/handouts/dd_splines.pdf
 */

public class SplineInterpolator {


    /*--- Variable Declarations ---*/

    private double[] xValues;
    private double[] yValues;
    private int pointCount;
    private double[][] matrix;


    /*--- Constructor ---*/

    public SplineInterpolator(ArrayList<Point> points) {
        pointCount = points.size();
        xValues = new double[pointCount];
        yValues = new double[pointCount];
        this.matrix = new double[pointCount][pointCount];

        // Parse Input Points
        for (int index = 0; index < points.size(); index++) {
            xValues[index] = ((double) points.get(index).x);
            yValues[index] = ((double) points.get(index).y);
        }
    }


    /*--- Public Instance Methods ---*/

    public Point getInterpolatedPoint(double factor) {
        return new Point(
                (int) calculateInterpolatedValue(xValues, factor),
                (int) calculateInterpolatedValue(yValues, factor)
        );
    }


    /*--- Public Static Methods ---*/

    /* Note: This method imagines a line between the first and last points
     *       it is passed. It then divides that line into a variable number
     *       of segments and returns the "division" points. This is
     *       shown in the diagram below, with p(0)/p(N) representing the first
     *       and last input points and d(0)/d(N) representing the division points.
     *
     *       p(0)-------d(0)--...--d(N)-------p(N)
     *
     */
    public static ArrayList<Point> getDivisionPoints(ArrayList<Point> points, int divisions) {

        // Declare Local Variables
        Point firstPoint = points.get(0);
        Point lastPoint = points.get(points.size() - 1);

        // Calculate Division Points
        ArrayList<Point> divisionPoints = new ArrayList<>();
        for (int x = 0; x < divisions - 1; x++) {
            double interpolationFactor = (x + 1.0) * (1.0 / divisions);
            divisionPoints.add(new Point(
                    (int) (linearlyInterpolateValues(lastPoint.x, firstPoint.x, interpolationFactor)),
                    (int) (linearlyInterpolateValues(lastPoint.y, firstPoint.y, interpolationFactor))
            ));
        }

        return divisionPoints;
    }

    /* Note: This method performs linear interpolation between the two passed values,
     *       returning the location (factor/1.0) of the way between them.
     */
    public static double linearlyInterpolateValues(double pointOne, double pointTwo, double factor) {
        return pointOne * factor + pointTwo * (1.0 - factor);
    }


    /*--- Private Interpolation Methods ---*/

    private void populateFirstMatrixRow(double[] dimensionValues) {
        for (int index = 0; index < pointCount; index++) {
            matrix[0][index] = dimensionValues[index];
        }
    }

    private double calculateInterpolatedValue(double[] dimensionValues, double factor) {
        populateFirstMatrixRow(dimensionValues);

        // Perform Recursive Linear Interpolation of Coordinate Values
        for (int j = 1; j < pointCount; j++) {
            for (int i = 0; i < pointCount - j; i++) {
                matrix[j][i] = linearlyInterpolateValues(matrix[j - 1][i + 1], matrix[j - 1][i], factor);
            }
        }
        return(matrix[pointCount - 1][0]);
    }
}