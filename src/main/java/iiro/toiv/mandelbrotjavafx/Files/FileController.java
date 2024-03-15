package iiro.toiv.mandelbrotjavafx.Files;

import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

public class FileController {
    public static void savePalette(String path, ArrayList<Color> palette) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutputStream.writeObject(palette);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Color> readPalette(String path) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            return (ArrayList<Color>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void savePosition(String path, double xMin, double yMin, double dist) {

    }

    public void readPosition(String path) {

    }
}
