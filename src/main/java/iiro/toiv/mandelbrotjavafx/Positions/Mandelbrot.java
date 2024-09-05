package iiro.toiv.mandelbrotjavafx.Positions;

import javafx.scene.image.Image;

/**
 * Mandelbrot -class contains methods to calculate pixel escape time and ScaleData.
 */
public class Mandelbrot implements Runnable {
    public static int Z = 1000;
    private final Matrix matrix;
    public ScaleData scaleData;

    /**
     * Constructor that assigns matrix and scaledata for image.
     *
     * @param image  Image that matrix is drawn on
     * @param matrix matrix of Points
     */
    public Mandelbrot(Image image, Matrix matrix) {
        this.matrix = matrix;
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
            for (int j = startRow; j < endRow; j++) {
                scaleData.assignCoordinates(matrix.get(i, j), i, j);
                matrix.get(i, j).n = 0;
                while (matrix.get(i, j).n < Z && Math.pow(matrix.get(i, j).x, 2) + Math.pow(matrix.get(i, j).y, 2) <= 4) {
                    if (Thread.currentThread().isInterrupted()) return;
                    repeatMandelbrot(matrix.get(i, j));
                }

                // Twice more for smooth coloring.
                repeatMandelbrot(matrix.get(i, j));
                repeatMandelbrot(matrix.get(i, j));

                // Assign normalizedMu n from 0 to 1 for smooth coloring:
                mu = Math.sqrt(matrix.get(i, j).x * matrix.get(i, j).x + matrix.get(i, j).y * matrix.get(i, j).y);
                normalizedMu = Math.log(Math.log(mu)) / Math.log(2.0f);
                // Null coalescing
                if (Double.isNaN(normalizedMu)) matrix.get(i, j).mu = matrix.get(i, j).n;
                else matrix.get(i, j).mu = Math.clamp(matrix.get(i, j).n - normalizedMu, 0.0f, matrix.get(i, j).n);
            }
        }
    }

    /**
     * Formula for Mandelbrot set.
     *
     * @param p Point that gets calculated
     */
    private void repeatMandelbrot(Point p) {
        double temp;
        temp = p.x * p.x - p.y * p.y + p.x0;
        p.y = 2 * p.x * p.y + p.y0;
        p.x = temp;
        p.n++;
    }

    @Override
    public void run() {
        calculatePixels(matrix, 0, 100);
    }
}
