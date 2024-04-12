package iiro.toiv.mandelbrotjavafx.Files;

import iiro.toiv.mandelbrotjavafx.ControllerClass;
import iiro.toiv.mandelbrotjavafx.Graphics.Palette;
import iiro.toiv.mandelbrotjavafx.Positions.ScaleData;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

/**
 * Files -Class handles all interactions when saving to and reading from files. Only the current position and one
 * palette can be saved.
 */
public class Files extends ControllerClass {

    /**
     * @param path Path to the palette save file. Cannot be the same path as position save data.
     * @param palette The Palette class.
     */
    public void savePalette(String path, Palette palette, String paletteName) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path, false))) {
            objectOutputStream.writeObject(paletteName);
            for (Color color : palette.getPalette()) {
                objectOutputStream.writeDouble(color.getRed());
                objectOutputStream.writeDouble(color.getGreen());
                objectOutputStream.writeDouble(color.getBlue());
            }
        } catch (IOException ignored) {
        }
        readPalette(path, palette);
    }
    /**
     * @param path Path to the palette save file. Cannot be the same path as position save data.
     * @param palette The Palette class.
     */
    public void readPalette(String path, Palette palette) {
        ArrayList<Color> readPalette = new ArrayList<>();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            String readKey = (String) objectInputStream.readObject();
            while (objectInputStream.available() > 0) {
                readPalette.add(new Color(objectInputStream.readDouble(), objectInputStream.readDouble(), objectInputStream.readDouble(), 1));
            }
            palette.addPalette(readKey, readPalette);
        } catch (IOException | ClassNotFoundException ignored) {
        }
    }
    /**
     * @param path Path to the position save file. Cannot be the same path as palette save data.
     * @param scaleData The ScaleData class that holds position information.
     */
    public void savePosition(String path, ScaleData scaleData) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutputStream.writeDouble(scaleData.getxMin());
            objectOutputStream.writeDouble(scaleData.getyMin());
            objectOutputStream.writeDouble(scaleData.getDiameter());
        } catch (IOException ignored) {
        }
    }
    /**
     * @param path Path to the position save file. Cannot be the same path as palette save data.
     * @param scaleData The ScaleData class that holds position information.
     */
    public void readPosition(String path, ScaleData scaleData) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            if (objectInputStream.available() > 0) {

                scaleData.setxMin(objectInputStream.readDouble());
                scaleData.setyMin(objectInputStream.readDouble());
                scaleData.setDiameter(objectInputStream.readDouble());
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public String toString() {
        return "Files toString method";
    }
}
