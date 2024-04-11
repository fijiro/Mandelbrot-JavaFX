package iiro.toiv.mandelbrotjavafx.Graphics;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Mandatory interface.
 */
public interface PaletteInterface {
    void generatePalette(String pType);

    void setPalette(ArrayList<Color> palette);

    ArrayList<Color> getPalette();

    Color getPaletteColor(double mu);

    void addColor(double r, double g, double b);

    void removeColor(int index);

    void setSpeed(double speed);

    void swapColors(int index, int i);
}
