package iiro.toiv.mandelbrotjavafx.Positions;

import iiro.toiv.mandelbrotjavafx.ControllerClass;

/**
 * Holds data for a pixel: current iterations, normalized escape time and start and end position.
 */
public class Point extends ControllerClass {
    /**
     * doubles that are saved in each pixel
     */
    public double x = 0, y = 0, x0 = 0, y0 = 0, mu = 0;
    /**
     * Total number of iterations
     */
    public int n = 0;

    @Override
    public String toString() {
        return "x: " + x + " y: " + y + " x0: " + x0 + " y0 " + " n: " + n;
    }
}