package iiro.toiv.mandelbrotjavafx;

import iiro.toiv.mandelbrotjavafx.Graphics.GraphicsController;
import iiro.toiv.mandelbrotjavafx.Graphics.PaletteController;
import iiro.toiv.mandelbrotjavafx.Input.InputController;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.PositionController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //Calculate mandelbrot per pixel: escape time
    //Calculate pixel position
    //Create matrix that holds values for each pixel
    //mandelbrotCanvas holds the mandelbrot
    //mCanvas is from -2 to 2
    public static final Canvas mCanvas = new Canvas(500, 500);
    StackPane centerPane = new StackPane(mCanvas);
    public static final PositionController.Matrix matrix = new PositionController.Matrix((int) mCanvas.getWidth(), (int) mCanvas.getHeight());
    private InputController inputController = new InputController(mCanvas);
    private GraphicsController graphicsController = new GraphicsController(mCanvas);
    public static TextField iterationField = new TextField("100");
    public static TextField speedField = new TextField("1");

    @Override
    public void start(Stage primaryStage) {
        GraphicsController.palettes.generatePalette(20, PaletteController.PaletteType.BlueToWhiteToYellow);
        Mandelbrot.calculatePixels(matrix);
        GraphicsController.drawPixels(matrix);
        //create coordinate panel with axises
        centerPane.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        centerPane.setLayoutX(0);

        BorderPane rightPane = new BorderPane();
        rightPane.setMinWidth(300);
        rightPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        TextField redField = new TextField("1");
        redField.setMaxWidth(50);
        TextField greenField = new TextField("1");
        greenField.setMaxWidth(50);
        TextField blueField = new TextField("1");
        blueField.setMaxWidth(50);
        Button addColorButton = new Button("ADD");
        addColorButton.setMinSize(50, 20);
        GridPane colorPicker = new GridPane();
        colorPicker.setPadding(new Insets(10));
        colorPicker.addRow(0, new Text("R:"), redField, new Text("G:"), greenField, new Text("B:"), blueField);
        colorPicker.addRow(1, addColorButton);
        rightPane.setCenter(colorPicker);

        BorderPane leftPane = new BorderPane();
        leftPane.setMinWidth(200);
        leftPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        GridPane infoPane = new GridPane();
        infoPane.addRow(0, new Text("Iterations: "), iterationField);
        infoPane.addRow(1, new Text("Speed: "), speedField);
        leftPane.setCenter(infoPane);

        BorderPane uiPane = new BorderPane(centerPane, null, rightPane, null, leftPane);
        StackPane root = new StackPane(uiPane);
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Coordinate Grid");
        primaryStage.show();
    }
}
