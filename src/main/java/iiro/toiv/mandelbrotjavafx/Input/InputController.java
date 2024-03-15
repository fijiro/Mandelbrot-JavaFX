package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.Graphics.GraphicsController;
import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class InputController {
    private double finalX = 0;
    public static boolean alreadyCalulating = false;
    private double finalY = 0;
    private final Mandelbrot mandelbrot = new Mandelbrot();
    private final ExecutorService executor = new ForkJoinPool(1);
    public GraphicsController graphics;
    public Timeline timeline = new Timeline(new KeyFrame(new Duration(100), event -> {
        System.out.println("Recalculating!");
        Main.matrix.resize((int) Main.mCanvas.getWidth(), (int) Main.mCanvas.getHeight());
        executor.submit(mandelbrot);
        executor.submit(graphics);
    }));

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
            if(!alreadyCalulating) {
            finalX = event.getX();
            finalY = event.getY();
            //from 0 - canvas.getWidth().
            System.out.println(finalX + " " + finalY);
            mandelbrot.scaleController.centerTo(finalX, finalY);
            //start timeline to calculate pixels
            timeline.play();
            }

        });
        canvas.setOnScroll(event -> {
            // factor = -1.5 - 1.5
            double factor = event.getDeltaY() / 80 * 3;
            mandelbrot.scaleController.zoomLevel(factor);
            timeline.play();
        });
        Main.iterationField.setOnAction(event -> {
            Mandelbrot.Z = Integer.parseInt(Main.iterationField.getText());
            timeline.play();
        });
        Main.speedField.setOnAction(event -> {
            graphics.palette.adjustSpeed(Double.parseDouble(Main.speedField.getText()));
            graphics.drawPixels(Main.matrix);
        });
    }
    public void addColorAction(double r, double g, double b) {
        graphics.palette.addColor(r,g,b);
    }
}