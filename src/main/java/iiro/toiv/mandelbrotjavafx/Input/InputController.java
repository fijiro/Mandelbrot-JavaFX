package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;

public class InputController {
    private double finalX = 0;
    private double finalY = 0;
    Timeline timeline = new Timeline(new KeyFrame(new Duration(100), event -> {
        System.out.println("Recalculating!");
        Main.matrix.resize((int) Main.mCanvas.getWidth(), (int) Main.mCanvas.getHeight());
        Mandelbrot.calculatePixels(Main.matrix);
        Main.graphicsController.drawPixels(Main.matrix);
    }));
    public InputController(Canvas canvas) {
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
            finalX = event.getX();
            finalY = event.getY();
            //from 0 - canvas.getWidth().
            System.out.println(finalX + " " + finalY);
            Mandelbrot.scaleController.centerTo(finalX,finalY);
            //start timeline to calculate pixels
            timeline.play();
        });
        canvas.setOnScroll(event -> {
            // factor = -1.5 - 1.5
            double factor = event.getDeltaY() / 80 * 3;
            Mandelbrot.scaleController.zoomLevel(factor);
            timeline.play();
        });
        Main.iterationField.setOnAction(event -> {
            Mandelbrot.Z = Integer.parseInt(Main.iterationField.getText());
            timeline.play();
        });

        Main.speedField.setOnAction(event -> {
            Main.graphicsController.palettes.adjustSpeed(Double.parseDouble(Main.speedField.getText()));
            Main.graphicsController.drawPixels(Main.matrix);
        });
    }
}