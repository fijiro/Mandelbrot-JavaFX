package iiro.toiv.mandelbrotjavafx;

import iiro.toiv.mandelbrotjavafx.Input.Input;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main.java is resposible for drawing all JavaFX elements and starting the Application thread.
 */
public class Main extends Application {
    public static void main(String[] args) {launch(args);}

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

        //right pane, holds fields related to palettes and coloring.
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
        Button moveUpButton = new Button("UP");
        Button moveDownButton = new Button("DOWN");
        TextField nameField = new TextField("PaletteName");
        ListView<Color> paletteColors = new ListView<>();
        paletteColors.setCellFactory(param -> new PaletteCellFactory());
        paletteColors.setMaxWidth(160);
        paletteColors.setMaxHeight(300);

        TextField minSpeedField = new TextField("0.1");
        TextField maxSpeedField = new TextField("100.0");
        minSpeedField.setMaxWidth(50);
        maxSpeedField.setMaxWidth(50);

        Slider speedSlider = new Slider();
        speedSlider.setValue(32);
        speedSlider.setMin(0.1);
        speedSlider.setMax(100);

        ChoiceBox<String> paletteChoice = new ChoiceBox<>();
        paletteChoice.setItems(FXCollections.observableList(input.graphics.palette.getKeys()));

        GridPane colorPicker = new GridPane();
        colorPicker.setAlignment(Pos.BOTTOM_CENTER);
        colorPicker.setPadding(new Insets(10));
        colorPicker.add(new Text("Use values 0 - 1."), 0, 0, 3, 1);
        colorPicker.addRow(1, new HBox(5, minSpeedField, speedSlider, maxSpeedField));
        colorPicker.addRow(2, paletteChoice);
        colorPicker.addRow(3, new HBox(5, new Text("R:"), redField, new Text("G:"), greenField, new Text("B:"), blueField));
        colorPicker.addRow(4, new HBox(5, addColorButton, removeColorButton, moveUpButton, moveDownButton));
        colorPicker.addRow(5, new HBox(5, readPaletteButton, savePaletteButton, nameField));

        //TODO: remove gridlines from final version
        colorPicker.setGridLinesVisible(true);

        rightPane.setCenter(colorPicker);
        rightPane.setBottom(new StackPane(paletteColors));

        //left pane, holds fields related to the position and scaleData.
        BorderPane leftPane = new BorderPane();
        leftPane.setMinWidth(200);
        leftPane.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, null, null)));
        //TODO: add uneditable fields that show scale and position data.
        Text xPositionField = new Text("0.00");
        Text yPositionField = new Text("0.00");
        GridPane infoGrid = new GridPane();
        Button savePositionButton = new Button("SAVE");
        Button readPositionButton = new Button("READ");
        TextField iterationField = new TextField("1000");
        iterationField.setMaxWidth(50);
        infoGrid.addRow(0, new Text("Iterations: "), iterationField);
        infoGrid.addRow(1, new Text("X, Y: "), xPositionField, yPositionField);
        infoGrid.addRow(2, savePositionButton, readPositionButton);
        leftPane.setCenter(infoGrid);


        mImageView.setOnMouseReleased(event -> input.imageMouseAction(event, xPositionField, yPositionField));
        mImageView.setOnScroll(input::ImageScrollAction);
        addColorButton.setOnAction(event -> input.addColorAction(redField, greenField, blueField, paletteColors));
        removeColorButton.setOnAction(event -> input.removeColorAction(paletteColors));
        moveUpButton.setOnAction(event -> input.moveUpAction(paletteColors));
        moveDownButton.setOnAction(event -> input.moveDownAction(paletteColors));
        readPaletteButton.setOnAction(event -> input.readPaletteAction(paletteColors, paletteChoice));
        savePaletteButton.setOnAction(event -> input.savePaletteAction(paletteColors, nameField, paletteChoice));
        savePositionButton.setOnAction(event -> input.savePositionAction());
        readPositionButton.setOnAction(event -> input.readPositionAction());
        iterationField.setOnAction(event -> input.iterationFieldAction(iterationField));
        speedSlider.setOnMouseDragged(event -> input.speedSliderAction(speedSlider));
        minSpeedField.setOnAction(event -> input.speedFieldAction(minSpeedField, speedSlider, false));
        maxSpeedField.setOnAction(event -> input.speedFieldAction(maxSpeedField, speedSlider, true));
        paletteChoice.setOnAction(event -> input.paletteChoiceAction(paletteChoice, paletteColors));

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
     * PaletteCellFactory is required to customize the paletteColors ListView. Shows colors as rectangles with black
     * outlines.
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
