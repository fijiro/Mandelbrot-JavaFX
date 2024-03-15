package iiro.toiv.mandelbrotjavafx.Graphics;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PaletteController {
    public enum PaletteType {BlueToWhite, BlueToYellow, BlueToWhiteToYellow}

    private final ArrayList<Color> palette = new ArrayList<>();
    private double colorSpeed = 32;

    public Color getPaletteColor(double mu) {
        // Calculate how far the color is in the loop, from 0 to 1
        double colorDistance = mu / colorSpeed - Math.floor(mu / colorSpeed);
        // Draw black when no escape
        //if (palette.isEmpty() || colorDistance == 1) return new Color(0, 0, 0, 0);

        double distanceInPalette = (palette.size() - 1) * colorDistance;
        Color paletteColor1 = palette.get((int) Math.floor(distanceInPalette));
        Color paletteColor2 = palette.get((int) Math.ceil(distanceInPalette));
        distanceInPalette -= Math.floor(distanceInPalette);

        Color color = new Color((paletteColor2.getRed() - paletteColor1.getRed()) * distanceInPalette + paletteColor1.getRed(),
                (paletteColor2.getGreen() - paletteColor1.getGreen()) * distanceInPalette + paletteColor1.getGreen(),
                (paletteColor2.getBlue() - paletteColor1.getBlue()) * distanceInPalette + paletteColor1.getBlue(), 1);
        return color;
    }

    // Clears palette and generates new one
    public void generatePalette(int z, PaletteType pType) {
        palette.clear();
        switch (pType) {
            case BlueToWhite:
                palette.add(new Color(0, 0, 1, 1));
                palette.add(new Color(1, 1, 1, 1));
                palette.add(new Color(0, 0, 1, 1));
                break;

            case BlueToYellow:
                palette.add(new Color(0, 0, 1, 1));
                palette.add(new Color(1, 1, 0, 1));
                palette.add(new Color(0, 0, 1, 1));
                break;

            case BlueToWhiteToYellow:
                palette.add(new Color(0.08, 0.08, 0.08, 1));
                palette.add(new Color(0, 0, 1, 1));
                palette.add(new Color(1, 1, 1, 1));
                palette.add(new Color(1, 1, 0, 1));
                palette.add(new Color(1, 0, 0, 1));
                palette.add(new Color(0.08, 0.08, 0.08, 1));
                break;

            default:
                break;
        }
    }

    public void adjustSpeed(double adjustment) {
        colorSpeed = Math.clamp(colorSpeed + adjustment, 1, colorSpeed + adjustment);
    }
}
