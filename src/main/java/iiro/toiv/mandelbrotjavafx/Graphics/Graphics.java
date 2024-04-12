package iiro.toiv.mandelbrotjavafx.Graphics;


import iiro.toiv.mandelbrotjavafx.ControllerClass;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Graphics class contains all thing related to drawing the mandelbrot set, such as Palette.
 */
public class Graphics extends ControllerClass {
    /**
     * imageWriter colors each pixel on screen.
     */
    private final PixelWriter imageWriter;
    public Palette palette = new Palette();

    /**
     * @param image Image that the matrix is drawn on.
     */
    public Graphics(WritableImage image) {
        imageWriter = image.getPixelWriter();
    }

    /**
     * Draws and colors each pixel based on their mu (normalized escape time) from palette.
     * @param matrix matrix containing pixels.
     */
    public void drawPixels(Matrix matrix) {
        Color color;
        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                if (matrix.get(i, j).n >= Mandelbrot.Z) color = new Color(0, 0, 0, 1);
                else color = palette.getPaletteColor(matrix.get(i, j).mu);
                imageWriter.setColor(i, j, color);
            }
        }
    }

    @Override
    public String toString() {
        return "Graphics toString method";
    }
}