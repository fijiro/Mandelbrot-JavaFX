package iiro.toiv.mandelbrotjavafx.Positions;
import javafx.scene.image.Image;

public class Mandelbrot implements Runnable {
    //TODO: add support for multithreading
    public static int Z = 1000;
    private final Matrix matrix;
    public ScaleData scaleData;

    public Mandelbrot(Image image, Matrix matrix) {
        this.matrix = matrix;
        scaleData = new ScaleData(image);
    }

    public void calculatePixels(Matrix matrix) {
        double m;
        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                scaleData.assignCoordinates(matrix.get(i, j), i, j);
                matrix.get(i, j).n = 0;
                while (matrix.get(i, j).n < Z && Math.pow(matrix.get(i, j).x, 2) + Math.pow(matrix.get(i, j).y, 2) <= 4) {
                    if (Thread.currentThread().isInterrupted()) return;
                    repeatMandelbrot(matrix.get(i, j));
                }

                // Twice more for smooth coloring
                repeatMandelbrot(matrix.get(i, j));
                repeatMandelbrot(matrix.get(i, j));

                // Assign normalized n from 0 to 1 for smooth coloring:
                m = Math.sqrt(matrix.get(i, j).x * matrix.get(i, j).x + matrix.get(i, j).y * matrix.get(i, j).y);
                double val = Math.log(Math.log(m)) / Math.log(2.0f);
                if (Double.isNaN(val)) matrix.get(i, j).mu = matrix.get(i, j).n;
                else matrix.get(i, j).mu = Math.clamp(matrix.get(i, j).n - val, 0.0f, matrix.get(i, j).n);
            }
        }
    }

    private void repeatMandelbrot(Point p) {
        double temp;
        temp = p.x * p.x - p.y * p.y + p.x0;
        p.y = 2 * p.x * p.y + p.y0;
        p.x = temp;
        p.n++;
    }

    @Override
    public void run() {
        calculatePixels(matrix);
    }
}
