package iiro.toiv.mandelbrotjavafx.Positions;

import javafx.scene.image.Image;

/**
 * ScaleData is used to convert pixel coordinates to mandelbrot set coordinates. It also handles zoom and centering.
 */
public class ScaleData {

    private double xMin = -2, yMin = -1.5, diameter = 3;
    private final double imageWidth, imageHeight;

    /**
     * @param image Image of the mandelbrot set
     */
    public ScaleData(Image image) {
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
    }

    /**
     * Increases the overall scale of the set
     *
     * @param zoomIn true when zooming in, false when zooming out.
     */
    public void zoomLevel(boolean zoomIn) {
        double newDiameter;
        if (zoomIn) newDiameter = diameter / 1.5;
        else newDiameter = diameter * 1.5;
        // Save zoom level
        xMin += (diameter - newDiameter) / 2;
        yMin += (diameter - newDiameter) / 2;
        diameter = newDiameter;
    }

    /**
     * Most important method in ScaleData. Assigns mandelbrot set coordinates to each pixel.
     *
     * @param point     Point for the coordinate
     * @param xPosition x position in image
     * @param yPosition y position in image
     */
    public void assignCoordinates(Point point, int xPosition, int yPosition) {
        point.x0 = getxMin() + (getDiameter() / imageWidth * xPosition);
        point.y0 = getyMin() + (getDiameter() / imageHeight * yPosition);
        point.x = point.x0;
        point.y = point.y0;
    }

    /**
     * @param x horizontal distance from canvas center
     * @param y vertical distance from canvas center
     */
    public void centerTo(double x, double y) {
        xMin += x / imageWidth * diameter - diameter / 2;
        yMin += y / imageHeight * diameter - diameter / 2;
    }

    /**
     * @return xMin
     */
    public double getxMin() {
        return xMin;
    }

    /**
     * @return yMin
     */
    public double getyMin() {
        return yMin;
    }

    /**
     * @return diameter
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * Used when reading positions from a file.
     *
     * @param xMin minimum x coordinate
     */
    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    /**
     * Used when reading positions from a file.
     *
     * @param yMin minimum y coordinate
     */
    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    /**
     * Used when reading positions from files.
     *
     * @param diameter saved diameter
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    /**
     * @return center on x-axis
     */
    public double getxCenter() {
        return xMin + diameter / 2;
    }

    /**
     * @return center on y-axis
     */
    public double getyCenter() {
        return yMin + diameter / 2;
    }
}
