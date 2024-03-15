package iiro.toiv.mandelbrotjavafx.Graphics;


import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GraphicsController implements Runnable{
    private final GraphicsContext gc;
    //static PixelWriter pixelWriter;
    public PaletteController palette = new PaletteController();
    public Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
        ;
    }));

    public GraphicsController(Canvas canvas) {
        gc = canvas.getGraphicsContext2D();
    }

    /**
     * Draws and colors each pixel based on their mu (escape time) from palette.
     * @param matrix matrix containing pixels.
     */
    public void drawPixels(Matrix matrix) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        Color color;
        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                if (matrix.get(i, j).n >= Mandelbrot.Z) color = new Color(0, 0, 0, 1);
                else color = palette.getPaletteColor(matrix.get(i, j).mu);
                gc.setStroke(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1));
                gc.strokeRect(i, j, 1, 1);
            }
        }
    }

    @Override
    public void run() {
        drawPixels(Main.matrix);
    }
}