package iiro.toiv.mandelbrotjavafx.Graphics;


import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import iiro.toiv.mandelbrotjavafx.Positions.Point;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Graphics implements Runnable {
    //private final PixelWriter pixelWriter;
    private final PixelWriter imageWriter;
    //    public static Timeline drawTimeline = new Timeline(new KeyFrame(new Duration(), event -> {
//        System.out.println("");
//        },
//            Timeline.INDEFINITE));
    public Palette palette = new Palette();

    public Graphics(WritableImage canvas) {
        imageWriter = canvas.getPixelWriter();
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
                //pixelWriter.setColor(i, j, color);
                imageWriter.setColor(i, j, color);
            }
        }
    }

    public void drawPixel(Point point, int x, int y) {
        Color color;
        if (point.n >= Mandelbrot.Z) color = new Color(0, 0, 0, 1);
        else color = palette.getPaletteColor(point.mu);
        imageWriter.setColor(x, y, color);
    }

    @Override
    public void run() {
        //Platform.runLater(() -> drawPixels(Main.matrix));
        drawPixels(Main.matrix);
    }
}