package iiro.toiv.mandelbrotjavafx.Graphics;


import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GraphicsController {
    private Color color;
    private final GraphicsContext gc;
    //static PixelWriter pixelWriter;
    public PaletteController palettes = new PaletteController();

    public GraphicsController(Canvas canvas) {
        gc = canvas.getGraphicsContext2D();
    }

    public void drawPixels(Matrix matrix) {
        gc.clearRect(0,0,gc.getCanvas().getWidth(),gc.getCanvas().getHeight());
        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                if (matrix.get(i,j).n >= Mandelbrot.Z) color = new Color(0, 0, 0, 1);
                else color = palettes.getPaletteColor(matrix.get(i, j).mu);
                gc.setStroke(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1));
                gc.strokeRect(i, j, 1, 1);
            }
        }
        //drawGrid();
    }
/*
    private static void drawGrid() {
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(3);
        // Draw horizontal lines
        // Size of each grid cell
        int GRID_SIZE = 20;
        for (int y = 0; y < Main.mCanvas.getWidth(); y += GRID_SIZE) {
            gc.strokeLine(0, y, Main.mCanvas.getWidth(), y);
        }

        // Draw vertical lines
        for (int x = 0; x < Main.mCanvas.getHeight(); x += GRID_SIZE) {
            gc.strokeLine(x, 0, x, Main.mCanvas.getHeight());
        }
        // Draw x and y axes
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        // Draw x-axis
        gc.strokeLine(0, Main.mCanvas.getWidth() / 2, Main.mCanvas.getWidth(), Main.mCanvas.getWidth() / 2);

        // Draw y-axis
        gc.strokeLine(Main.mCanvas.getHeight() / 2, 0, Main.mCanvas.getHeight() / 2, Main.mCanvas.getHeight());
    }*/
}