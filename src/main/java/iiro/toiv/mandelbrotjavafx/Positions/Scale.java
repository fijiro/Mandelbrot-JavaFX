package iiro.toiv.mandelbrotjavafx.Positions;

import iiro.toiv.mandelbrotjavafx.Main;

public class Scale {

    private double xMin = -2;
    private double yMin = -1.5;
    private double distance = 3;

    /**
     * Increases the overall scale of the set
     *
     * @param zoomIn positive when zooming in, negative when zooming out.
     */
    public void zoomLevel(boolean zoomIn) {
        // Zoom in
        double dist2;
        if (zoomIn) {dist2 = distance / 1.5;}
        // Zoom out
        else {dist2 = distance * 1.5;}
        // Save zoom
        xMin += (distance - dist2) / 2;
        yMin += (distance - dist2) / 2;
        distance = dist2;
    }

    /**
     * @param x horizontal distance from canvas center
     * @param y vertical distance from canvas center
     */
    public void centerTo(double x, double y) {
        xMin += x / Main.mImage.getWidth() * distance - distance / 2;
        yMin += y / Main.mImage.getHeight() * distance - distance / 2;
    }

    public double getxMin() {
        return xMin;
    }

    public double getyMin() {
        return yMin;
    }

    public double getDistance() {
        return distance;
    }
}
