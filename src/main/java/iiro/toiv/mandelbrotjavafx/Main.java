package iiro.toiv.mandelbrotjavafx;

import iiro.toiv.mandelbrotjavafx.Input.Input;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

/**
 * Main.java is resposible for drawing all JavaFX elements and starting the Application.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        //Create matrix that holds values for each pixel
        Matrix matrix = new Matrix(600, 600);
        //mImage is the "Canvas" that the Mandelbrot set is drawn on.
        WritableImage mImage = new WritableImage(600, 600);
        ImageView mImageView = new ImageView(mImage);
        Input input = new Input(mImageView, matrix);
        //The set is drawn on Application thread while calculations are done on another thread.
        Timeline drawLoop = new Timeline(new KeyFrame(Duration.millis(200), event -> input.graphics.drawPixels(matrix)));
        input.recalculate();
        drawLoop.setCycleCount(Timeline.INDEFINITE);
        drawLoop.play();

        //Center pane, holds only the Mandelbrot set.
        StackPane centerPane = new StackPane(mImageView);
        centerPane.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, null, null)));
        centerPane.setLayoutX(0);

        //right pane, holds fields related to palettes.
        BorderPane rightPane = new BorderPane();
        rightPane.setMinWidth(300);
        rightPane.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, null, null)));
        TextField redField = new TextField("1");
        redField.setMaxWidth(50);
        TextField greenField = new TextField("1");
        greenField.setMaxWidth(50);
        TextField blueField = new TextField("1");
        blueField.setMaxWidth(50);
        Button addColorButton = new Button(" + ");
        Button removeColorButton = new Button(" - ");
        Button readPaletteButton = new Button("READ");
        Button savePaletteButton = new Button("SAVE");

        ListView<Color> paletteColors = new ListView<>();
        paletteColors.setCellFactory(param -> new PaletteCellFactory());
        paletteColors.setMaxWidth(160);
        paletteColors.setMaxHeight(300);

        TextField speedField = new TextField("1");
        speedField.setMaxWidth(50);

        GridPane colorPicker = new GridPane();
        colorPicker.setAlignment(Pos.BOTTOM_CENTER);
        colorPicker.setPadding(new Insets(10));
        colorPicker.add(new Text("Use values 0 - 1."), 0, 0, 3, 1);
        colorPicker.addRow(1, new HBox(5, new Text("R:"), redField, new Text("G:"), greenField, new Text("B:"), blueField));
        colorPicker.addRow(2, new HBox(5, new Text("Increase speed: "), speedField));
        colorPicker.addRow(3, new HBox(5, addColorButton, removeColorButton));
        colorPicker.addRow(4, new HBox(5, readPaletteButton, savePaletteButton));
        //TODO: remove gridlines from final version
        colorPicker.setGridLinesVisible(true);

        rightPane.setCenter(colorPicker);
        rightPane.setBottom(new StackPane(paletteColors));

        //left pane, holds fields related to the position and scaleData.
        BorderPane leftPane = new BorderPane();
        leftPane.setMinWidth(200);
        leftPane.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, null, null)));
        Button savePositionButton = new Button("SAVE");
        Button readPositionButton = new Button("READ");
        TextField iterationField = new TextField("1000");
        iterationField.setMaxWidth(100);
        //TODO: add uneditable fields that show scale and position data.
        //TextField xPositionField ...

        GridPane infoGrid = new GridPane();
        infoGrid.addRow(0, new Text("Iterations: "), iterationField);
        infoGrid.addRow(1, savePositionButton, readPositionButton);
        leftPane.setCenter(infoGrid);

        addColorButton.setOnAction(event -> input.addColorAction(redField, greenField, blueField, paletteColors));
        removeColorButton.setOnAction(event -> input.removeColorAction(paletteColors));
        readPaletteButton.setOnAction(event -> input.readPaletteAction(paletteColors));
        savePaletteButton.setOnAction(event -> input.savePaletteAction(paletteColors));
        savePositionButton.setOnAction(event -> input.savePositionAction());
        readPositionButton.setOnAction(event -> input.readPositionAction());
        iterationField.setOnAction(event -> input.iterationFieldAction(iterationField));
        speedField.setOnAction(event -> input.speedFieldAction(speedField));
        //Reads saved palette on startup.
        readPaletteButton.fire();

        BorderPane uiPane = new BorderPane(centerPane, null, rightPane, null, leftPane);
        StackPane root = new StackPane(uiPane);
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mandelbrot");
        primaryStage.show();
    }

    /**
     * PaletteCellFactory is required to customize the paletteColors ListView.
     */
    private static class PaletteCellFactory extends ListCell<Color> {
        @Override
        protected void updateItem(Color item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                Rectangle rect = new Rectangle(100, 10);
                rect.setFill(item);
                rect.setStroke(Color.BLACK);
                setGraphic(rect);
            }
            else {
                setGraphic(null);
            }
        }
    }
}
