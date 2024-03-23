package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.Graphics.Graphics;
import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class Input {
    private double finalX = 0, finalY = 0;
    private final Mandelbrot mandelbrot;
    private final WritableImage image;
    private final ExecutorService executor = new ForkJoinPool(1);

    //TODO: Miksi <?>
    private Future<?> future;
    public Graphics graphics;

    public void recalculate() {
        System.out.println("Recalculating!");
        Main.matrix.resize((int) image.getWidth(), (int) image.getHeight());
        //if (!executor.isShutdown()) {
        //future.cancel(true);
        //}
        executor.submit(mandelbrot);
        //mandelbrot.calculatePixels(Main.matrix);
        //graphics.drawPixels(Main.matrix);
        /*mandelbrot.run();
        graphics.run();*/
        //executor.submit(mandelbrot);
        //executor.submit(graphics);
    }

    public Input(ImageView imageView) {
        image = (WritableImage) imageView.getImage();
        mandelbrot = new Mandelbrot(image);
        graphics = new Graphics(image);

        imageView.setOnMouseReleased(event -> {
            finalX = event.getX();
            finalY = event.getY();
            System.out.println(finalX + " " + finalY);
            //from 0 - imageView.getWidth().
            mandelbrot.scaleController.centerTo(finalX, finalY);
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
            graphics.drawPixels(Main.matrix);
        });
    }
/*
    public void addColorAction(double r, double g, double b) {

    }*/
}