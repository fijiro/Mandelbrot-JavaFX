package iiro.toiv.mandelbrotjavafx.Positions;

import java.util.ArrayList;

/**
 * Holds all pixels in canvas in an ArrayList. These pixels are accessible through Matrix.get(x, y).
 */
public class Matrix {

    private final ArrayList<Point> matrixData = new ArrayList<>();
    private int matrixRows, matrixColumns;

    public Matrix(int w, int h) {
        resize(w, h);
    }

    public Point get(int x, int y) {
        if (x > matrixColumns || y > matrixRows) {
            System.out.println("ERROR! TRYING TO GET POINT (" + x + "," + y + ")!");
            return matrixData.getFirst();
        }
        return matrixData.get(matrixColumns * y + x);
    }

    public int getWidth() {
        return matrixColumns;
    }

    public int getHeight() {
        return matrixRows;
    }

    public void resize(int w, int h) {
        matrixData.clear();
        matrixColumns = w;
        matrixRows = h;

        //Makes creating pixels faster because arraylist's size doesn't need to be gradually increased.
        matrixData.ensureCapacity(w * h);

        //Fill with empty Points
        for (int i = 0; i < w * h; i++) {
            matrixData.add(new Point());
        }
    }
}
