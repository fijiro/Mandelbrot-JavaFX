package iiro.toiv.mandelbrotjavafx;

import iiro.toiv.mandelbrotjavafx.Input.Input;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.application.Application;
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

/**
 * Main.java is resposible for drawing all JavaFX elements and starting the Application thread.
 *
 * @author Iiro Toivanen @ <a href="https://github.com/fijiro/Mandelbrot-JavaFX">Github</a>
 * @version 1.0
 * @since 2024
 **/
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
        TextField nameField = new TextField("My palette");
        ChoiceBox<String> paletteChoice = new ChoiceBox<>();
        ListView<Color> paletteColors = new ListView<>();
        paletteColors.setCellFactory(param -> new PaletteCellFactory());
        paletteColors.setMaxWidth(160);
        paletteColors.setMaxHeight(300);

        TextField minSpeedField = new TextField("36");
        TextField maxSpeedField = new TextField("1000.0");
        minSpeedField.setMaxWidth(50);
        maxSpeedField.setMaxWidth(50);

        Slider speedSlider = new Slider();
        speedSlider.setValue(36);
        speedSlider.setMin(Double.parseDouble(minSpeedField.getText()));
        speedSlider.setMax(Double.parseDouble(maxSpeedField.getText()));


        GridPane palettePane = new GridPane();
        palettePane.setAlignment(Pos.BOTTOM_CENTER);
        palettePane.setPadding(new Insets(10));
        //palettePane.add(new Text("Use values 0 - 1."), 0, 0, 3, 1);
        palettePane.addRow(1, new HBox(5, minSpeedField, speedSlider, maxSpeedField));
        palettePane.addRow(2, paletteChoice);
        palettePane.addRow(3, new HBox(5, readPaletteButton, savePaletteButton, nameField));
        palettePane.addRow(4, new HBox(5, new Text("R:"), redField, new Text("G:"), greenField, new Text("B:"),
                blueField));
        palettePane.addRow(5, new HBox(5, addColorButton, removeColorButton, moveUpButton, moveDownButton));

        palettePane.setGridLinesVisible(true);

        rightPane.setCenter(palettePane);
        rightPane.setBottom(new StackPane(paletteColors));

        //left pane, holds fields related to the position and scaleData.
        BorderPane leftPane = new BorderPane();
        leftPane.setMinWidth(250);
        leftPane.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, null, null)));
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
        infoGrid.setAlignment(Pos.CENTER);
        infoGrid.setGridLinesVisible(true);
        leftPane.setCenter(infoGrid);


        mImageView.setOnMouseReleased(event -> input.imageMouseAction(event, xPositionField, yPositionField));
        mImageView.setOnScroll(input::ImageScrollAction);
        addColorButton.setOnAction(event -> input.addColorAction(redField, greenField, blueField, paletteColors));
        removeColorButton.setOnAction(event -> input.removeColorAction(paletteColors));
        moveUpButton.setOnAction(event -> input.moveUpAction(paletteColors));
        moveDownButton.setOnAction(event -> input.moveDownAction(paletteColors));
        paletteChoice.setOnAction(event -> input.paletteChoiceAction(paletteChoice, paletteColors));
        readPaletteButton.setOnAction(event -> input.readPaletteAction(paletteChoice, paletteColors));
        savePaletteButton.setOnAction(event -> input.savePaletteAction(paletteChoice, paletteColors, nameField));
        savePositionButton.setOnAction(event -> input.savePositionAction());
        readPositionButton.setOnAction(event -> input.readPositionAction(xPositionField, yPositionField));
        iterationField.setOnAction(event -> input.iterationFieldAction(iterationField));
        speedSlider.setOnMouseDragged(event -> input.speedSliderAction(speedSlider));
        minSpeedField.setOnAction(event -> input.speedFieldAction(minSpeedField, speedSlider, false));
        maxSpeedField.setOnAction(event -> input.speedFieldAction(maxSpeedField, speedSlider, true));

        //If palettes.dat does not exist, default to BW&Y palette
        paletteChoice.setValue("Blue, White & Yellow");
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
