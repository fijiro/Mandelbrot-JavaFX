package iiro.toiv.mandelbrotjavafx.Graphics;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Mandatory interface.
 */
public interface PaletteInterface {
    void generatePaletteMap();

    void addPalette(String paletteName, ArrayList<Color> palette);

    void setPalette(String name);

    String getPaletteName();

    List<String> getKeys();

    ArrayList<Color> getPalette();

    Color getPaletteColor(double mu);

    void addColor(double r, double g, double b);

    void removeColor(int index);

    void setSpeed(double speed);

    void swapColors(int index, int i);
}
