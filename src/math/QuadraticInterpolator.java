package math;

import java.awt.*;
import java.util.ArrayList;

/* Spline Interpolator
 * Christopher Cruzen
 * 05.12.2020
 *
 *   The Quadratic Interpolator class performs quadratic interpolation between the four
 * points passed to it, returning the location factor/1.0 of the way between them on a
 * smoothed curve. The class uses Casteljau's Algorithm for smoothing, as adapted from
 * the Stack Overflow post below.
 *
 * Post: https://math.stackexchange.com/questions/43947/casteljaus-algorithm-practical-example
 */

public class QuadraticInterpolator {


    /*--- Variable Declarations ---*/

    private static final int POINT_COUNT = 4;

    private ArrayList<Double> xValues;
    private ArrayList<Double> yValues;
    private double[][] matrix;


    /*--- Constructor ---*/

    public QuadraticInterpolator(ArrayList<Point> points) {
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        this.matrix = new double[POINT_COUNT][POINT_COUNT];
        parseInputPoints(points);
    }


    /*--- Public Methods ---*/

    public Point getPoint(double factor) {
        return new Point(
                (int) evaluateDimension(factor, xValues),
                (int) evaluateDimension(factor, yValues)
        );
    }


    /*--- Private Methods ---*/

    private void initializeMatrix(ArrayList<Double> dimensionValues) {
        for (int i = 0; i < POINT_COUNT; i++) {
            matrix[0][i] = dimensionValues.get(i);
        }
    }

    private double evaluateDimension(double t, ArrayList<Double> dimensionValues) {
        initializeMatrix(dimensionValues);
        for (int j = 1; j < POINT_COUNT; j++) {
            for (int i = 0; i < POINT_COUNT - j; i++) {
                matrix[j][i] = matrix[j-1][i] * (1-t) + matrix[j-1][i+1] * t;
            }
        }
        return(matrix[POINT_COUNT-1][0]);
    }

    private void parseInputPoints(ArrayList<Point> points) {
        for (Point point : points) {
            xValues.add((double) point.x);
            yValues.add((double) point.y);
        }
    }
}