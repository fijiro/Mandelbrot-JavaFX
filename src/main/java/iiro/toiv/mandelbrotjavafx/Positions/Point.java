package iiro.toiv.mandelbrotjavafx.Positions;

/**
 * Holds data for a pixel: current iterations, iteration and start position.
 */
public class Point {
    public double x = 0, y = 0, x0 = 0, y0 = 0, mu = 0;
    public int n = 0;

    //For debugging
    @Override
    public String toString() {
        return "x: " + x + " y: " + y + " x0: " + x0 + " y0 " + " n: " + n;
    }
}