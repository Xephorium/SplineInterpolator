package ui.utility;

import java.awt.*;

/* Spline Interpolator
 * Christopher Cruzen
 * 05.12.2020
 *
 * DisplayUtility contains common screen dimension retrieval methods.
 */

public class DisplayUtility {

    public static int getWindowStartX(int windowWidth) {
        return (getScreenWidth()/2) - (windowWidth /2);
    }

    public static int getWindowStartY(int windowHeight) {
        return (getScreenHeight()/2) - (windowHeight /2);
    }

    public static int getScreenHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }

    public static int getScreenWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }
}
