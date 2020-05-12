package ui;

import ui.utility.DisplayUtility;

import javax.swing.*;
import java.awt.*;

/* Spline Interpolator
 * Christopher Cruzen
 * 05.12.2020
 *
 * SplineWindow is the base UI class of Spline Interpolator.
 */

public class SplineWindow {


    /*--- Variables ---*/

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private JFrame frame;
    private JPanel panel;


    /*--- Constructor ---*/

    public SplineWindow() {
        setGlobalLookAndFeel();
        initializeFrameAttributes();

        initializeViewClasses();
        addViewClasses();
    }


    /*--- Public Methods ---*/

    public void show() {
        frame.setVisible(true);
    }


    /*--- Private Methods --*/

    private void setGlobalLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // A Whole Lot of Nothin'
        }
    }

    private void initializeFrameAttributes() {
        frame = new JFrame("Spline Interpolator");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocation(
                DisplayUtility.getWindowStartX(WINDOW_WIDTH),
                DisplayUtility.getWindowStartY(WINDOW_HEIGHT)
        );
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    private void initializeViewClasses() {
        panel = new JPanel();
    }

    private void addViewClasses() {
        frame.add(panel, BorderLayout.CENTER);
    }
}
