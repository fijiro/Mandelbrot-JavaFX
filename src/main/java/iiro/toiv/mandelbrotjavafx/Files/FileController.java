package iiro.toiv.mandelbrotjavafx.Files;

import iiro.toiv.mandelbrotjavafx.Positions.Scale;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

public class FileController {
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

    public ArrayList<Color> readPalette(String path) {
        ArrayList<Color> readPalette = new ArrayList<>();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            while (objectInputStream.available() > 0) {
                readPalette.add(new Color(objectInputStream.readDouble(), objectInputStream.readDouble(), objectInputStream.readDouble(), 1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readPalette;
    }

    public void savePosition(String path, Scale scale) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutputStream.writeDouble(scale.getxMin());
            objectOutputStream.writeDouble(scale.getyMin());
            objectOutputStream.writeDouble(scale.getDiameter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readPosition(String path, Scale scale) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
        while (objectInputStream.available() > 0) {
            scale.setxMin(objectInputStream.readDouble());
            scale.setyMin(objectInputStream.readDouble());
            scale.setDiameter(objectInputStream.readDouble());
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
