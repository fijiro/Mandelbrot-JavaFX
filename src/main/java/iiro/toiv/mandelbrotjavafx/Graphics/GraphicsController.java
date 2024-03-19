package iiro.toiv.mandelbrotjavafx.Graphics;


import iiro.toiv.mandelbrotjavafx.Input.InputController;
import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GraphicsController implements Runnable {
    private final PixelWriter pixelWriter;
    public PaletteController palette = new PaletteController();
    public Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
        ;
    }));

    public GraphicsController(Canvas canvas) {
        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
    }

    /**
     * Draws and colors each pixel based on their mu (escape time) from palette.
     *
     * @param matrix matrix containing pixels.
     */
    public void drawPixels(Matrix matrix) {
        Color color;
        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                if (matrix.get(i, j).n >= Mandelbrot.Z) color = new Color(0, 0, 0, 1);
                else color = palette.getPaletteColor(matrix.get(i, j).mu);
                pixelWriter.setColor(i, j, color);
            }
        }
        InputController.alreadyCalulating = false;
    }

    @Override
    public void run() {
        //Platform.runLater(() -> drawPixels(Main.matrix));
        drawPixels(Main.matrix);
    }
}