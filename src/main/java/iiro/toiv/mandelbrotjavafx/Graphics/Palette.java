package iiro.toiv.mandelbrotjavafx.Graphics;

import iiro.toiv.mandelbrotjavafx.ControllerClass;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Palette is responsible for handling everything about coloring.
 */
public class Palette extends ControllerClass implements PaletteInterface {
    @Override
    public String toString() {
        return "Palette toString Method";
    }
    private final Map<String, ArrayList<Color>> paletteMap = new HashMap<>();
    private String paletteName;

    private ArrayList<Color> palette = new ArrayList<>();
    private double colorSpeed = 32;

    public Palette() {
        generatePaletteMap();
    }

    // Clears palette and generates new one
    public void generatePaletteMap() {
        palette.clear();
        palette.add(new Color(0, 1, 0, 1));
        palette.add(new Color(1, 1, 1, 1));
        paletteMap.put("Green To White", new ArrayList<>(palette));

        palette.clear();
        palette.add(new Color(0, 0, 1, 1));
        palette.add(new Color(1, 1, 0, 1));
        paletteMap.put("Blue To Yellow", new ArrayList<>(palette));

        palette.clear();
        palette.add(new Color(0.08, 0.08, 0.08, 1));
        palette.add(new Color(0, 0, 1, 1));
        palette.add(new Color(1, 1, 1, 1));
        palette.add(new Color(1, 1, 0, 1));
        palette.add(new Color(1, 0, 0, 1));
        paletteMap.put("Blue, White & Yellow", new ArrayList<>(palette));

        palette.clear();
        palette.add(new Color(1, 0, 0, 1));
        palette.add(new Color(1, 1, 0, 1));
        palette.add(new Color(0, 1, 0, 1));
        palette.add(new Color(0, 1, 1, 1));
        palette.add(new Color(0, 0, 1, 1));
        palette.add(new Color(1, 0, 1, 1));
        paletteMap.put("Rainbow", new ArrayList<>(palette));
    }


    public void addPalette(String paletteName, ArrayList<Color> palette) {
        paletteMap.clear();
        generatePaletteMap();

        System.out.println("Adding new palette: " + palette);
        this.palette = palette;
        this.paletteName = paletteName;
        for (Color color : this.palette) {
            System.out.println(color);
        }
        //Add palette to map
        paletteMap.put(paletteName, new ArrayList<>(palette));
    }
    public void setPalette(String name) {
        if (!paletteMap.containsKey(name)) {
            System.out.println("NO PALETTE WITH NAME: " + name);
        }
        System.out.println("SELECT PALETTE: " + name);
        this.paletteName = name;
        this.palette = paletteMap.get(paletteName);
    }

    public String getPaletteName() {
        return paletteName;
    }

    public List<String> getKeys() {
        return new ArrayList<>(paletteMap.keySet());
    }

    public ArrayList<Color> getPalette() {
        return palette;
        //return paletteMap.get("string");
    }

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

    public void addColor(double r, double g, double b) {
        palette.add(new Color(Math.clamp(r, 0, 1), Math.clamp(g, 0, 1), Math.clamp(b, 0, 1), 1));
    }

    public void removeColor(int index) {
        palette.remove(index);
    }

    public void setSpeed(double speed) {
        colorSpeed = speed;
    }

    public void swapColors(int index1, int index2) {
        Collections.swap(palette, index1, index2);
    }
}
