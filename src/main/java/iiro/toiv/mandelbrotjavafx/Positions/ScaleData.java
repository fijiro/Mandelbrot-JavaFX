package iiro.toiv.mandelbrotjavafx.Positions;

import javafx.scene.image.Image;

public class ScaleData {

    private double xMin = -2, yMin = -1.5, diameter = 3;
    private final double imageWidth, imageHeight;

    public ScaleData(Image image) {
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
    }

    /**
     * Increases the overall scale of the set
     *
     * @param zoomIn positive when zooming in, negative when zooming out.
     */
    public void zoomLevel(boolean zoomIn) {
        // Zoom in
        double dist2;
        if (zoomIn) {dist2 = diameter / 1.5;}
        // Zoom out
        else {dist2 = diameter * 1.5;}
        // Save zoom
        xMin += (diameter - dist2) / 2;
        yMin += (diameter - dist2) / 2;
        diameter = dist2;
    }

    public void assignCoordinates(Point point, int xPosition, int yPosition) {
        point.x0 = getxMin() + (getDiameter() / imageWidth * xPosition);
        point.y0 = getyMin() + (getDiameter() / imageHeight * yPosition);
        point.x = point.x0;
        point.y = point.y0;
    }

    /**
     * @param x horizontal diameter from canvas center
     * @param y vertical diameter from canvas center
     */
    public void centerTo(double x, double y) {
        xMin += x / imageWidth * diameter - diameter / 2;
        yMin += y / imageHeight * diameter - diameter / 2;
    }

    public double getxMin() {
        return xMin;
    }

    public double getyMin() {
        return yMin;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public double getxCenter() {
        return xMin + diameter / 2;
    }
    public double getyCenter() {
        return yMin + diameter / 2;
    }
}
