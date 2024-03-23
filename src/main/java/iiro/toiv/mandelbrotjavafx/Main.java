package iiro.toiv.mandelbrotjavafx;

import iiro.toiv.mandelbrotjavafx.Files.FileController;
import iiro.toiv.mandelbrotjavafx.Graphics.Palette;
import iiro.toiv.mandelbrotjavafx.Input.Input;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.Key;


public class Main extends Application {
    //mCanvas holds the mandelbrot
    //mCanvas is from -2 to 2
    //public static final Canvas mCanvas = new Canvas(600, 600);
    public static final WritableImage mImage = new WritableImage(600, 600);
    public static final ImageView mImageView = new ImageView(mImage);
    public Timeline drawLoop = new Timeline(new KeyFrame(Duration.millis(200), event -> {
        Input.graphics.drawPixels(matrix);
    }));
    StackPane centerPane = new StackPane(mImageView);
    //Create matrix that holds values for each pixel
    public static final Matrix matrix = new Matrix((int) mImage.getWidth(), (int) mImage.getHeight());
    public static TextField iterationField = new TextField("100");
    public static TextField speedField = new TextField("1");

    @Override
    public void start(Stage primaryStage) {
        Input input = new Input(mImageView);
        input.graphics.palette.generatePalette(Palette.PaletteType.BlueToWhiteToYellow);
        input.recalculate();
        drawLoop.setCycleCount(Timeline.INDEFINITE);
        drawLoop.play();
        //Calculate mandelbrot per pixel escape time
        //input.timeline.play();
        //input.graphics.timeline.play();
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
        //addColorButton.setMinSize(50, 20);
        Button readPaletteButton = new Button("READ");
        Button savePaletteButton = new Button("SAVE");

        ListView<Color> paletteColors = new ListView<>();
        paletteColors.setItems(FXCollections.observableList(input.graphics.palette.getPalette()));
        paletteColors.setCellFactory(param -> new paletteCellFactory());

        GridPane colorPicker = new GridPane();
        colorPicker.setPadding(new Insets(10));
        colorPicker.add(new Text("Use values 0 - 1."), 0, 0, 3, 1);
        colorPicker.addRow(1, new HBox(5, new Text("R:"), redField, new Text("G:"), greenField, new Text("B:"), blueField));
        colorPicker.addRow(2, new HBox(5, addColorButton, readPaletteButton, savePaletteButton));
        colorPicker.setGridLinesVisible(true);

        addColorButton.setOnAction(event -> {
            input.graphics.palette.addColor(Double.parseDouble(redField.getText()), Double.parseDouble(greenField.getText()), Double.parseDouble(blueField.getText()));
            paletteColors.setItems(FXCollections.observableList(input.graphics.palette.getPalette()));
            input.graphics.drawPixels(Main.matrix);
        });
        readPaletteButton.setOnAction(event -> {
            input.graphics.palette.setPalette(FileController.readPalette("palettes.dat"));
            paletteColors.setItems(FXCollections.observableList(input.graphics.palette.getPalette()));
            input.graphics.drawPixels(matrix);
        });
        savePaletteButton.setOnAction(event -> {
            FileController.savePalette("palettes.dat", input.graphics.palette.getPalette());
            paletteColors.setItems(FXCollections.observableList(input.graphics.palette.getPalette()));
        });


        rightPane.setCenter(colorPicker);
        rightPane.setBottom(new BorderPane(paletteColors, null, null, new HBox(), null));

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

    private static class paletteCellFactory extends ListCell<Color> {
        @Override
        protected void updateItem(Color item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                Rectangle rect = new Rectangle(100, 10);
                rect.setFill(item);
                setGraphic(rect);
            }
            else {
                setGraphic(null);
            }
        }
    }
}
