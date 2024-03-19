package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.Graphics.GraphicsController;
import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class InputController {
    private double finalX = 0, finalY = 0;
    public static volatile boolean alreadyCalulating = false;
    private final Mandelbrot mandelbrot = new Mandelbrot();
    private final ExecutorService executor = new ForkJoinPool(1);
    public GraphicsController graphics;

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

    public InputController(Canvas canvas) {
        graphics = new GraphicsController(canvas);
        /*
        canvas.setOnMousePressed(event -> {
            initialX = event.getSceneX();
            offsetX = event.getSceneX() - canvas.getLayoutX();
            initialY = event.getSceneY();
            offsetY = event.getSceneY() - canvas.getLayoutY();
        });
        canvas.setOnMouseDragged(event -> {
            canvas.setLayoutX(event.getSceneX() - offsetX);
            canvas.setLayoutY(event.getSceneY() - offsetY);
        });*/
        canvas.setOnMouseReleased(event -> {
            if (!alreadyCalulating) {
                finalX = event.getX();
                finalY = event.getY();
                //from 0 - canvas.getWidth().
                System.out.println(finalX + " " + finalY);
                mandelbrot.scaleController.centerTo(finalX, finalY);
                //start timeline to calculate pixels
            }
            recalculate();
        });
        canvas.setOnScroll(event -> {
            // factor = -1.5 - 1.5
            double factor = event.getDeltaY() / 80 * 3;
            mandelbrot.scaleController.zoomLevel(factor);
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