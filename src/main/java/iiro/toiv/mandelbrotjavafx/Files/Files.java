package iiro.toiv.mandelbrotjavafx.Files;

import iiro.toiv.mandelbrotjavafx.Graphics.Palette;
import iiro.toiv.mandelbrotjavafx.Positions.ScaleData;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

public class Files {

    public void savePalette(String path, ArrayList<Color> palette) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            for (Color color : palette) {
                objectOutputStream.writeDouble(color.getRed());
                objectOutputStream.writeDouble(color.getGreen());
                objectOutputStream.writeDouble(color.getBlue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readPalette(String path, Palette palette) {
        ArrayList<Color> readPalette = new ArrayList<>();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            while (objectInputStream.available() > 0) {
                readPalette.add(new Color(objectInputStream.readDouble(), objectInputStream.readDouble(), objectInputStream.readDouble(), 1));
            }
            palette.setPalette(readPalette);
        } catch (IOException ignored) {
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
}
