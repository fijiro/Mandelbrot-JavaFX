package iiro.toiv.mandelbrotjavafx.Positions;

import javafx.scene.image.Image;

/**
 * Mandelbrot -class contains methods to calculate pixel escape time and ScaleData.
 */
public class Mandelbrot  {
    public static int Z = 1000;
    public ScaleData scaleData;

    /**
     * Constructor that assigns matrix and scaledata for image.
     *
     * @param image  Image that matrix is drawn on
     */
    public Mandelbrot(Image image) {
        scaleData = new ScaleData(image);
    }

    /**
     * Each pixel gets calculated and the mandelbrot formula is repeated here.
     *
     * @param matrix matrix of Points
     */
    public void calculatePixels(Matrix matrix, int startRow, int endRow) {
        double mu, normalizedMu;
        for (int i = 0; i < matrix.getWidth(); i++) {
            if (Thread.currentThread().isInterrupted()) return;
            for (int j = startRow; j < endRow; j++) {
                Point p = matrix.get(i, j);
                scaleData.assignCoordinates(p, i, j);
                p.n = 0;
                while (p.n < Z && Math.pow(p.x, 2) + Math.pow(p.y, 2) <= 4) {
                    repeatMandelbrot(p);
                }

                // Twice more for smooth coloring.
                repeatMandelbrot(p);
                repeatMandelbrot(p);

                // Assign normalizedMu n from 0 to 1 for smooth coloring:
                mu = Math.sqrt(p.x * p.x + p.y * p.y);
                normalizedMu = Math.log(Math.log(mu)) / Math.log(2.0f);
                // Null coalescing
                /*if (Double.isNaN(normalizedMu)) p.mu = p.n;
                else*/ p.mu = Math.clamp(p.n - normalizedMu, 0.0f, p.n);
            }
        }
    }

    /**
     * Formula for Mandelbrot set.
     *
     * @param p Point that gets calculated
     */
    private void repeatMandelbrot(Point p) {
        double temp = p.x * p.x - p.y * p.y + p.x0;
        p.y = 2 * p.x * p.y + p.y0;
        p.x = temp;
        p.n++;
    }
}
