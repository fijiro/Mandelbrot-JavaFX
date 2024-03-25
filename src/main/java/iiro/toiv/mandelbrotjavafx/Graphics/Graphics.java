package iiro.toiv.mandelbrotjavafx.Graphics;


import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Graphics {
    private final PixelWriter imageWriter;
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
                imageWriter.setColor(i, j, color);
            }
        }
    }
}