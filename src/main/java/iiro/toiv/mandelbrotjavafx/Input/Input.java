package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.Graphics.Graphics;
import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Input {
    private double finalX = 0, finalY = 0;
    public static boolean quitThreads = false;
    private final Mandelbrot mandelbrot = new Mandelbrot();
    private final ExecutorService executor = new ForkJoinPool(1);
    public static Graphics graphics;

    public void recalculate() {
        quitThreads = true;
        System.out.println("Recalculating!");
        Main.matrix.resize((int) Main.mImage.getWidth(), (int) Main.mImage.getHeight());
        try {
            while(executor.awaitTermination(10, TimeUnit.MILLISECONDS)){
                System.out.println(executor.isTerminated());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.execute(mandelbrot);
        quitThreads = false;
        //mandelbrot.calculatePixels(Main.matrix);
        //graphics.drawPixels(Main.matrix);
        /*mandelbrot.run();
        graphics.run();*/
        //executor.submit(mandelbrot);
        //executor.submit(graphics);
    }

    public Input(ImageView canvas) {
        graphics = new Graphics((WritableImage) canvas.getImage());
        canvas.setOnMouseReleased(event -> {
            if (!quitThreads) {
                finalX = event.getX();
                finalY = event.getY();
                //from 0 - canvas.getWidth().
                System.out.println(finalX + " " + finalY);
                mandelbrot.scaleController.centerTo(finalX, finalY);
                //start timeline to calculate pixels
                recalculate();
            }
        });
        canvas.setOnScroll(event -> {
            if (!quitThreads) {
                //zooms in if scrolled up
                System.out.println(event.getDeltaY());
                mandelbrot.scaleController.zoomLevel(event.getDeltaY() > 0);
                recalculate();
            }
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