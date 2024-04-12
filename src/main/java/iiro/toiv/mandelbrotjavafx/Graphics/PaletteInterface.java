package iiro.toiv.mandelbrotjavafx.Graphics;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Mandatory interface.
 */
public interface PaletteInterface {
    /**
     * Clears and generates four default palettes.
     */
    void generateDefaultMap();

    /**
     * @param paletteName User given string for the palette name.
     * @param palette list of colors
     */
    void addPalette(String paletteName, ArrayList<Color> palette);

    /**
     * @param name Change current palette to existing palette in the paletteMap.
     */
    void setPalette(String name);

    /**
     * @return Returns the paletteMap key (palette name) for the current palette.
     */
    String getPaletteName();

    /**
     * @return Returns all keys (palette names) from paletteMap.
     */
    List<String> getKeys();

    /**
     * @return Returns selected palette.
     */
    ArrayList<Color> getPalette();

    /**
     * @param mu Normalized color distance. This enables smooth coloring between different palette colors.
     * @return Returns a blend of colors between two palette colors based on the escape time.
     */
    Color getPaletteColor(double mu);

    /**
     * Add a color to selected palette
     * @param r Red, 0 - 1
     * @param g Green, 0 - 1
     * @param b Blue, 0 - 1
     */
    void addColor(double r, double g, double b);

    /**
     * Remove color from selected palette.
     * @param index Index of the removed color in palette.
     */
    void removeColor(int index);

    /**
     * Assign a new color speed.
     * @param speed Color speed
     */
    void setSpeed(double speed);

    /**
     * Swap two Colors inside palette at indexes 1 and 2.
     * @param index1 First index
     * @param index2 Second index
     */
    void swapColors(int index1, int index2);
}
