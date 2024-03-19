package iiro.toiv.mandelbrotjavafx.Positions;

import iiro.toiv.mandelbrotjavafx.Main;

public class Scale {

    private double xMin = -2;
    private double yMin = -1.5;
    private double dist = 3;

    /**
     * Increases the overall scale of the set
     * @param zoomAmount either -1.5 or 1.5
     */
    public void zoomLevel(double zoomAmount) {
        // Zoom in
        double dist2;
        if (zoomAmount >= 0) {dist2 = dist / zoomAmount;}
        // Zoom out
        else {dist2 = dist * -zoomAmount;}
        // Save zoom
        xMin += (dist - dist2) / 2;
        yMin += (dist - dist2) / 2;
        dist = dist2;
    }
    /**
     * @param x horizontal distance from canvas center
     * @param y vertical distance from canvas center
     */
    public void centerTo(double x, double y) {
        xMin += x / Main.mCanvas.getWidth() * dist - dist / 2;
        yMin += y / Main.mCanvas.getHeight() * dist - dist / 2;
    }

    public double getxMin() {
        return xMin;
    }

    public double getyMin() {
        return yMin;
    }

    public double getDist() {
        return dist;
    }
}
