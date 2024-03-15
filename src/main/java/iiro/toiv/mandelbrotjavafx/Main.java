package iiro.toiv.mandelbrotjavafx;

import iiro.toiv.mandelbrotjavafx.Files.FileController;
import iiro.toiv.mandelbrotjavafx.Graphics.PaletteController;
import iiro.toiv.mandelbrotjavafx.Input.InputController;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {
    //mCanvas holds the mandelbrot
    //mCanvas is from -2 to 2
    public static final Canvas mCanvas = new Canvas(600, 600);
    StackPane centerPane = new StackPane(mCanvas);
    //Create matrix that holds values for each pixel
    public static final Matrix matrix = new Matrix((int) mCanvas.getWidth(), (int) mCanvas.getHeight());
    public static TextField iterationField = new TextField("100");
    public static TextField speedField = new TextField("1");

    @Override
    public void start(Stage primaryStage) {
        InputController input = new InputController(mCanvas);
        input.graphics.palette.generatePalette(PaletteController.PaletteType.BlueToWhiteToYellow);
        //TODO: input.graphics.palette.setPalette(FileController.readPalette("palettes.dat"));
        //Calculate mandelbrot per pixel escape time
        input.timeline.play();
        input.graphics.timeline.play();
        //iterationField.fireEvent();
        //create coordinate panel with axises
        centerPane.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        centerPane.setLayoutX(0);

        //right pane
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
        addColorButton.setOnAction(event -> input.addColorAction(Double.parseDouble(redField.getText()), Double.parseDouble(greenField.getText()), Double.parseDouble(blueField.getText())));
        rightPane.setCenter(colorPicker);
        ListView<Color> paletteColors = new ListView<>();
        paletteColors.setItems(input.graphics.palette.getPalette());
        rightPane.setBottom(paletteColors);

        //left pane
        BorderPane leftPane = new BorderPane();
        leftPane.setMinWidth(200);
        leftPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        GridPane infoPane = new GridPane();
        infoPane.addRow(0, new Text("Iterations: "), iterationField);
        infoPane.addRow(1, new Text("Increase speed: "), speedField);
        leftPane.setCenter(infoPane);

        BorderPane uiPane = new BorderPane(centerPane, null, rightPane, null, leftPane);
        StackPane root = new StackPane(uiPane);
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mandelbrot");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
