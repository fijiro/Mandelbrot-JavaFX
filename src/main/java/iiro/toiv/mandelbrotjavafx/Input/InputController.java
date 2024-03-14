package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.Graphics.GraphicsController;
import iiro.toiv.mandelbrotjavafx.Graphics.PaletteController;
import iiro.toiv.mandelbrotjavafx.Main;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.PositionController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class InputController {
    private double initialX = 0;
    private double offsetX = 0;
    private double finalX = 0;
    private double initialY = 0;
    private double finalY = 0;
    private double offsetY = 0;
    Timeline timeline = new Timeline(new KeyFrame(new Duration(100), event -> {
        System.out.println("Recalculating!");
        Main.matrix.resize((int) Main.mCanvas.getWidth(), (int) Main.mCanvas.getHeight());
        Mandelbrot.calculatePixels(Main.matrix);
        GraphicsController.drawPixels(Main.matrix);
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
            PositionController.Scale.centerTo(finalX, finalY);
            //start timeline to calculate pixels
            timeline.play();
        });
        canvas.setOnScroll(event -> {
            // factor = -1.5 - 1.5
            double factor = event.getDeltaY() / 80 * 3;
            //zoomOperator.zoom(canvas, factor, event.getSceneX(), event.getSceneY());
            //GraphicsController.drawPixels(Main.matrix);
            // determine scale
            //double oldScale = canvas.getScaleX();
            //double scale = oldScale * factor;
            //double f = (scale / oldScale) - 1;

            // determine offset that we will have to move the canvas
            // Bounds bounds = canvas.localToScene(canvas.getBoundsInLocal());
            //double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
            //double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));
            //canvas.setTranslateX(canvas.getTranslateX() - f * dx);
            //canvas.setTranslateY(canvas.getTranslateY() - f * dy);
            //canvas.setScaleX(scale);
            //canvas.setScaleY(scale);

            //GraphicsController.drawPixels(Main.matrix);
            //applyTransforms(canvas);
            //recassign pixels
            //canvas.setScaleX(1);
            // canvas.setScaleY(1);

            //canvas.setTranslateX(canvas.getTranslateX() - (canvas.getWidth() * (scale - 1))/2);
            //canvas.setLayoutX(event.getSceneX());
            //canvas.setTranslateY(canvas.getTranslateY() - (canvas.getHeight() * (scale- 1))/2);
            //canvas.setWidth(canvas.getWidth() * scale);
            //canvas.setHeight(canvas.getHeight() * scale);
            //Mandelbrot.calculatePixels(Main.matrix);
            PositionController.Scale.zoomLevel(factor);
            timeline.play();
        });

        Main.iterationField.setOnAction(event -> {
            Mandelbrot.Z = Integer.parseInt(Main.iterationField.getText());
            timeline.play();
        });
        Main.speedField.setOnAction(event -> {
            GraphicsController.palettes.adjustSpeed(Double.parseDouble(Main.speedField.getText()));
            GraphicsController.drawPixels(Main.matrix);
        });
    }
}