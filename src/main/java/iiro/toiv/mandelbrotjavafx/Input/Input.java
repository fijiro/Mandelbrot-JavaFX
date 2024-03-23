package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.Graphics.Graphics;
import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Input {
    private final Mandelbrot mandelbrot;
    public Graphics graphics;
    private final Matrix matrix;
    private final WritableImage image;
    private final ExecutorService executor = new ForkJoinPool(1);

    //TODO: Miksi <?>
    //private Future<?> future;

    public void recalculate() {
        System.out.println("Recalculating!");
        matrix.resize((int) image.getWidth(), (int) image.getHeight());
        //if (!executor.isShutdown()) {
        //future.cancel(true); tarpeeton
        //}
        executor.submit(mandelbrot);
        /*mandelbrot.calculatePixels(Main.matrix);
        graphics.drawPixels(Main.matrix);
        mandelbrot.run();
        graphics.run();
        executor.submit(mandelbrot);
        executor.submit(graphics);*/
    }

    public Input(ImageView imageView, Matrix matrix) {
        this.matrix = matrix;
        image = (WritableImage) imageView.getImage();
        mandelbrot = new Mandelbrot(image, matrix);
        graphics = new Graphics(image);

        imageView.setOnMouseReleased(event -> {
            System.out.println(event.getX() + " " + event.getY());
            //from 0 - imageView.getWidth().
            mandelbrot.scaleController.centerTo(event.getX(), event.getY());
            //start timeline to calculate pixels
            recalculate();

        });
        imageView.setOnScroll(event -> {
            System.out.println(event.getDeltaY());
            //zooms in if scrolled up, out if scrolled down
            mandelbrot.scaleController.zoomLevel(event.getDeltaY() > 0);
            recalculate();

        });
        Main.iterationField.setOnAction(event -> {
            Mandelbrot.Z = Integer.parseInt(Main.iterationField.getText());
            recalculate();
        });
        Main.speedField.setOnAction(event -> {
            double speed = Double.parseDouble(Main.speedField.getText());
            graphics.palette.adjustSpeed(speed);
            graphics.drawPixels(matrix);
        });
    }
/*
    public void addColorAction(double r, double g, double b) {

    }*/
}