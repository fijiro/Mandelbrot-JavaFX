package iiro.toiv.mandelbrotjavafx.Files;

import iiro.toiv.mandelbrotjavafx.ControllerClass;
import iiro.toiv.mandelbrotjavafx.Graphics.Palette;
import iiro.toiv.mandelbrotjavafx.Positions.ScaleData;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

public class Files extends ControllerClass {

    public void savePalette(String path, String name, Palette palette) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path, false))) {
            objectOutputStream.writeObject(name);
            for (Color color : palette.getPalette()) {
                objectOutputStream.writeDouble(color.getRed());
                objectOutputStream.writeDouble(color.getGreen());
                objectOutputStream.writeDouble(color.getBlue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        readPalette(path, palette);
    }

    public void readPalette(String path, Palette palette) {
        ArrayList<Color> readPalette = new ArrayList<>();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            String readKey = (String) objectInputStream.readObject();
            while (objectInputStream.available() > 0) {
                readPalette.add(new Color(objectInputStream.readDouble(), objectInputStream.readDouble(), objectInputStream.readDouble(), 1));
            }
            palette.addPalette(readKey, readPalette);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePosition(String path, ScaleData scaleData) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutputStream.writeDouble(scaleData.getxMin());
            objectOutputStream.writeDouble(scaleData.getyMin());
            objectOutputStream.writeDouble(scaleData.getDiameter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
