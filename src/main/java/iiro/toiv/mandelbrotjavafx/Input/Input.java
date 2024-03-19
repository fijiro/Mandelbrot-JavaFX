package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.Graphics.Graphics;
import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import javafx.scene.canvas.Canvas;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Input {
    private double finalX = 0, finalY = 0;
    public static volatile boolean alreadyCalulating = false;
    private final Mandelbrot mandelbrot = new Mandelbrot();
    private final ExecutorService executor = new ForkJoinPool(1);
    public Graphics graphics;

    public void recalculate() {
        System.out.println("Recalculating!");
        Main.matrix.resize((int) Main.mCanvas.getWidth(), (int) Main.mCanvas.getHeight());
        mandelbrot.calculatePixels(Main.matrix);
        graphics.drawPixels(Main.matrix);
        /*mandelbrot.run();
        while (alreadyCalulating) {
            Thread.onSpinWait();
        }
        graphics.run();*/
        //executor.submit(mandelbrot);
        //executor.submit(graphics);
    }

    public Input(Canvas canvas) {
        graphics = new Graphics(canvas);
        canvas.setOnMouseReleased(event -> {
            if (!alreadyCalulating) {
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
            if (!alreadyCalulating) {
                // factor = -1.5 - 1.5
                double factor = event.getDeltaY() / 80 * 3;
                mandelbrot.scaleController.zoomLevel(factor);
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