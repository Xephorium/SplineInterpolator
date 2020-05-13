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
        return getDivisionPoints(points, 10);
    }


    /*--- Private Methods ---*/

    /* Note: This method imagines a line between the first and last points
     *       it is passed. It then divides that line into thirds and returns
     *       the two "division" points. This is shown in the diagram below,
     *       with p(0)/p(N) representing the first and last input points and
     *       d(0)/d(1) representing the division points.
     *
     *       p(0)------d(0)------d(1)------p(N)
     *
     */
//    public ArrayList<Point> getTertiaryDivisionPoints(ArrayList<Point> points) {
//        Point firstPoint = points.get(0);
//        Point lastPoint = points.get(points.size() - 1);
//    }

    /* Note: This method imagines a line between the first and last points
     *       it is passed. It then divides that line into a variable number
     *       of segments and returns the "division" points. This is
     *       shown in the diagram below, with p(0)/p(N) representing the first
     *       and last input points and d(0)/d(N) representing the division points.
     *
     *       p(0)-------d(0)--...--d(N)-------p(N)
     *
     */
    public ArrayList<Point> getDivisionPoints(ArrayList<Point> points, int divisions) {

        // Declare Local Variables
        Point firstPoint = points.get(0);
        Point lastPoint = points.get(points.size() - 1);
        System.out.println("First: " + firstPoint);
        System.out.println("Last: " + lastPoint);

        // Calculate Division Points
        ArrayList<Point> divisionPoints = new ArrayList<>();
        for (int x = 0; x < divisions - 1; x++) {
            double interpolationFactor = (x + 1.0) * (1.0 / divisions);
            divisionPoints.add(new Point(
                    (int) (lastPoint.x * interpolationFactor + firstPoint.x * (1.0 - interpolationFactor)),
                    (int) (lastPoint.y * interpolationFactor + firstPoint.y * (1.0 - interpolationFactor))
            ));
        }

        System.out.println("Div #1: " + divisionPoints.get(0));

        return divisionPoints;
    }
}
