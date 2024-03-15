package iiro.toiv.mandelbrotjavafx.Positions;

import iiro.toiv.mandelbrotjavafx.Input.InputController;
import iiro.toiv.mandelbrotjavafx.Main;
import javafx.scene.canvas.Canvas;

public class Mandelbrot implements Runnable {
    //TODO: add support for multithreading
    public static int Z = 100;
    public Scale scaleController = new Scale();

    public synchronized void calculatePixels(Matrix matrix) {
        double m;
        InputController.alreadyCalulating = true;
        for (int i = 0; i < matrix.getWidth(); i++) {
            if (Thread.currentThread().isInterrupted()) return;
            for (int j = 0; j < matrix.getHeight(); j++) {
                //if (!isPixelOnScreen(Main.mCanvas, i, j)) continue;
                //scaleController.assignCoords(matrix.get(i, j).x0, matrix.get(i, j).y0, i, j);
                matrix.get(i, j).x0 = scaleController.getxMin() + (scaleController.getDist() / Main.mCanvas.getWidth() * i);
                matrix.get(i, j).y0 = scaleController.getyMin() + (scaleController.getDist() / Main.mCanvas.getHeight() * j);
                matrix.get(i, j).x = matrix.get(i, j).x0;
                matrix.get(i, j).y = matrix.get(i, j).y0;
                matrix.get(i, j).n = 0;
                while (matrix.get(i, j).n < Z && Math.pow(matrix.get(i, j).x, 2) + Math.pow(matrix.get(i, j).y, 2) <= 4) {
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
        InputController.alreadyCalulating = false;
    }

    private void repeatMandelbrot(Point p) {
        double temp;
        temp = p.x * p.x - p.y * p.y + p.x0;
        p.y = 2 * p.x * p.y + p.y0;
        p.x = temp;
        p.n++;
    }
    /*
    public boolean isPixelOnScreen(Canvas canvas, double x, double y) {
        double canvasX = canvas.getBoundsInLocal().getMinX();
        double canvasY = canvas.getBoundsInLocal().getMinY();
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        return x >= canvasX && x < canvasX + canvasWidth && y >= canvasY && y < canvasY + canvasHeight;
    }
    */
    @Override
    public void run() {
        calculatePixels(Main.matrix);
    }
}
