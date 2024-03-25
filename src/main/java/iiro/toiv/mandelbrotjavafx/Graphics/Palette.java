package iiro.toiv.mandelbrotjavafx.Graphics;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Palette {
    public enum PaletteType {BlueToWhite, BlueToYellow, BlueToWhiteToYellow, Rainbow}

    private transient ArrayList<Color> palette = new ArrayList<>();
    private double colorSpeed = 32;

    public Color getPaletteColor(double mu) {
        // Calculate how far the color is in the loop, from 0 to 1
        double colorDistance = mu / colorSpeed - Math.floor(mu / colorSpeed);
        //default to black
        if (palette.isEmpty() || colorDistance == 1) return new Color(0, 0, 0, 1);

        // Assign the point of how far in palette the color is, from 0 - palette.size()
        double distanceInPalette = (palette.size()) * colorDistance;
        Color pColor2 = null;
        Color pColor1 = null;
        try {
            if (distanceInPalette > palette.size() - 1) {
                pColor1 = palette.getLast();
                pColor2 = palette.getFirst();
            }
            else {
                pColor1 = palette.get((int) Math.floor(distanceInPalette % (palette.size())));
                pColor2 = palette.get((int) Math.ceil(distanceInPalette % (palette.size())));
            }
        } catch (Exception e) {
            System.out.println("dist: " + distanceInPalette + " " + e);
        }
        colorDistance = distanceInPalette - Math.floor(distanceInPalette);
        if (pColor2 == null || pColor1 == null) {
            return new Color(1, 0.1, 1, 1);
        }
        return new Color((pColor2.getRed() - pColor1.getRed()) * colorDistance + pColor1.getRed(), (pColor2.getGreen() - pColor1.getGreen()) * colorDistance + pColor1.getGreen(), (pColor2.getBlue() - pColor1.getBlue()) * colorDistance + pColor1.getBlue(), 1);
    }

    // Clears palette and generates new one
    public void generatePalette(PaletteType pType) {
        palette.clear();
        switch (pType) {
            case BlueToWhite:
                palette.add(new Color(0, 0, 1, 1));
                palette.add(new Color(1, 1, 1, 1));
                break;

            case BlueToYellow:
                palette.add(new Color(0, 0, 1, 1));
                palette.add(new Color(1, 1, 0, 1));
                break;

            case BlueToWhiteToYellow:
                palette.add(new Color(0.08, 0.08, 0.08, 1));
                palette.add(new Color(0, 0, 1, 1));
                palette.add(new Color(1, 1, 1, 1));
                palette.add(new Color(1, 1, 0, 1));
                palette.add(new Color(1, 0, 0, 1));
                palette.add(new Color(0, 1, 0, 1));
                break;
            case Rainbow:
                break;
            default:
                break;
        }
    }

    public void setPalette(ArrayList<Color> palette) {
        this.palette = palette;
    }

    public ArrayList<Color> getPalette() {
        return palette;
    }

    public void addColor(Double r, Double g, Double b) {

        palette.add(new Color(Math.clamp(r,0,1), Math.clamp(g,0,1), Math.clamp(b,0,1), 1));
    }

    public void removeColor(int index) {
        palette.remove(index);
    }

    public void adjustSpeed(double adjustment) {
        colorSpeed += adjustment;
        colorSpeed = Math.max(colorSpeed, 0);
    }
}
