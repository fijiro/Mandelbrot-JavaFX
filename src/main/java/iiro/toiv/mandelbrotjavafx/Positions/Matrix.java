package iiro.toiv.mandelbrotjavafx.Positions;

import java.util.ArrayList;

/**
 * Holds all pixels in a canvas/image in an ArrayList. These pixels are accessible through Matrix.get(x, y).
 */
public class Matrix {

    /**
     * Contains all Points for canvas/image.
     */
    private final ArrayList<Point> matrixData = new ArrayList<>();
    /**
     * matrixRows/Columns are used to get a Point from matrixData.
     */
    private int matrixRows, matrixColumns;

    /**
     * Constructor for Matrix uses the resize() method.
     *
     * @param w Image width
     * @param h Image height
     */
    public Matrix(int w, int h) {
        resize(w, h);
    }

    /**
     * Retrieves a Point from matrixData.
     *
     * @param x x Distance from (0,0)
     * @param y y Distance from (0,0)
     * @return Point at the current image
     */
    public Point get(int x, int y) {
        if (x > matrixColumns || y > matrixRows) {
            System.out.println("ERROR! TRYING TO GET POINT (" + x + "," + y + ")!");
            return matrixData.getFirst();
        }
        return matrixData.get(matrixColumns * y + x);
    }

    /**
     * @return Returns matrix width.
     */
    public int getWidth() {
        return matrixColumns;
    }

    /**
     * @return Returns matrix height.
     */
    public int getHeight() {
        return matrixRows;
    }

    /**
     * @param w Matrix width to be resized to
     * @param h Matrix height to be resized to
     */
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
