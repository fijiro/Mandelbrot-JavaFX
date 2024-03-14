package iiro.toiv.mandelbrotjavafx.Positions;

import iiro.toiv.mandelbrotjavafx.Main;

import java.util.ArrayList;

public class PositionController {
    public static class Scale {
        static double xMin = -2;
        static double yMin = -1.5;
        static double dist = 3;
        static double dist2 = dist;
        public static boolean changed = true;

        // -1.5 - 1.5
        public static void zoomLevel(double zoomAmount) {
            // Zoom in
            if (zoomAmount >= 0) {dist2 = dist / zoomAmount;}
            // Zoom out
            else {dist2 = dist * -zoomAmount;}
            // Save zoom
            xMin += (dist - dist2) / 2;
            yMin += (dist - dist2) / 2;
            dist = dist2;
            changed = true;

        }

        public static void centerTo(double x, double y) {
            // = difference / -1 amount
            // window size : dist should be constant
            //xMin += dist * x ;
            double cursorToPaneX = x / Main.mCanvas.getWidth();
            double cursorToPaneY = y / Main.mCanvas.getHeight();

            xMin += x / Main.mCanvas.getWidth() * dist - dist / 2;
            yMin += y / Main.mCanvas.getHeight() * dist - dist / 2;
            changed = true;
        }

        static void assignCoords(double x, double y, int xDist, int yDist) {
            x = xMin + (dist / Main.mCanvas.getWidth() * xDist);
            y = yMin + (dist / Main.mCanvas.getHeight() * yDist);
        }
    }

    public static class Matrix {
        private final ArrayList<Point> m_data = new ArrayList<>();
        private int m_rows, m_columns;

        public static class Point {
            public double x = 0, y = 0, x0, y0;
            public int n = 0;
            public double mu = 0;

            private Point(int xInCanvas, int yInCanvas, int canvasWidth, int canvasHeight) {
                //scales 0 = -2, canvas.Width() = 2
                // x/canvas.Width() = x/canvas.Width()*4
                this.x0 = (double) xInCanvas / canvasWidth * (2 - (-2)) - 2;
                this.y0 = (double) yInCanvas / canvasHeight * (2 - (-2)) - 2;
            }

            @Override
            public String toString() {
                return "x: " + x + " y: " + y + " x0: " + x0 + " y0 " + " n: " + n;
            }
        }

        public Matrix(int w, int h) {
            resize(w, h);
        }

        public Point get(int x, int y) {
            if (x > m_columns || y > m_rows) {
                System.out.println("ERROR! TRYING TO GET POINT (" + x + "," + y + ")!");
                return m_data.getFirst();
            }
            return m_data.get(m_columns * y + x);
        }

        public int getWidth() {
            return m_columns;
        }

        public int getHeight() {
            return m_rows;
        }

        public void resize(int w, int h) {
            m_data.clear();
            m_rows = h;
            m_columns = w;
            m_data.ensureCapacity(h * w);
            for (int i = 0; i < w * h; i++) {
                m_data.add(new Point(i % w, i / w, w, h));
            }
        }
    }
}
