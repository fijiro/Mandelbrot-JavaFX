package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.ControllerClass;
import iiro.toiv.mandelbrotjavafx.Files.Files;
import iiro.toiv.mandelbrotjavafx.Graphics.Graphics;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * Input contains all methods for handling input actions. Basically superclass
 */
public class Input extends ControllerClass {
    /**
     * graphics object, used to access palette and mandelbrot drawing.
     */
    private final Graphics graphics;
    private final Matrix matrix;
    private final Mandelbrot mandelbrot;
    private final Files files = new Files();
    private final WritableImage image;
    private final ExecutorService calculationExecutor = new ForkJoinPool();

    /**
     * Wipes old matrix data and starts threads for calculating the pixel escape times.
     */
    public void recalculate() {
        matrix.resize((int) image.getWidth(), (int) image.getHeight());
        int numberOfThreads = Runtime.getRuntime().availableProcessors();  // Use all available CPU cores
        int rowsPerThread = matrix.getHeight() / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? matrix.getHeight() : (i + 1) * rowsPerThread;
            calculationExecutor.submit(() -> mandelbrot.calculatePixels(matrix, startRow, endRow));
        }
    }


    /**
     * Constructor for Input
     *
     * @param imageView ImageView that the mandelbrot set is drawn on
     * @param matrix    Matrix that holds data for each pixel
     */
    public Input(ImageView imageView, Matrix matrix) {
        this.matrix = matrix;
        image = (WritableImage) imageView.getImage();
        mandelbrot = new Mandelbrot(image);
        graphics = new Graphics(image);
        recalculate();
        //Color all pixels 5 times each second
        Timeline drawLoop = new Timeline(new KeyFrame(Duration.millis(200), event -> graphics.drawPixels(matrix)));
        drawLoop.setCycleCount(Timeline.INDEFINITE);
        try (ExecutorService coloringExecutor = new ForkJoinPool(1)) {
            coloringExecutor.execute(drawLoop::play);
        }
    }

    /**
     * Action that handles mouse clicks on the mandelbrot set. Centers to clicked location
     *
     * @param event Mouse button pressed event
     * @param x     Text for x position
     * @param y     Text for y position
     */
    public void imageMouseAction(MouseEvent event, Text x, Text y) {
        mandelbrot.scaleData.centerTo(event.getX(), event.getY());
        x.setText(new DecimalFormat("#.##########").format(mandelbrot.scaleData.getxCenter()));
        y.setText(new DecimalFormat("#.##########").format(mandelbrot.scaleData.getyCenter()));
        recalculate();
    }

    /**
     * Handles mouse wheel scrolling when mouse pointer is on the mandelbrot set. Zooms to the center of the screen
     * by scroll amount.
     *
     * @param event Event for scrolling action.
     */
    public void ImageScrollAction(ScrollEvent event) {
        //zooms in if scrolled up, out if scrolled down
        mandelbrot.scaleData.zoomLevel(event.getDeltaY() > 0);
        recalculate();
    }


    /**
     * Adds a new color at the end of the palette.
     *
     * @param redField      TextField for red
     * @param greenField    TextField for green
     * @param blueField     TextField for blue
     * @param paletteColors ListView for palette colors
     */
    public void addColorAction(TextField redField, TextField greenField, TextField blueField, ListView<Color> paletteColors) {
        try {
            graphics.palette.addColor(Double.parseDouble(redField.getText()), Double.parseDouble(greenField.getText()), Double.parseDouble(blueField.getText()));
        } catch (NumberFormatException | NullPointerException e) {
            throw new RuntimeException(e);
        }
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }

    /**
     * Removes the selected color from palette.
     *
     * @param paletteColors ListView of palette colors.
     */
    public void removeColorAction(ListView<Color> paletteColors) {
        if (!graphics.palette.getPalette().isEmpty()) {
            graphics.palette.removeColor(paletteColors.getSelectionModel().getSelectedIndex() < 0 ? graphics.palette.getPalette().size() - 1 : paletteColors.getSelectionModel().getSelectedIndex());
            paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
        }
    }

    /**
     * Set new color speed when slider is dragged.
     *
     * @param slider Slider for color speed
     */
    public void speedSliderAction(Slider slider) {
        graphics.palette.setSpeed(slider.getValue());
    }

    /**
     * Adjust maximum or minimum speed value.
     *
     * @param field  TextField that contains the speed value
     * @param slider Slider for speed
     * @param isMax  true if the field is ma
     */
    public void speedFieldAction(TextField field, Slider slider, boolean isMax) {
        double value;
        try {
            value = Double.parseDouble(field.getText());
        } catch (NullPointerException | NumberFormatException e) {
            return;
        }
        if (isMax) {
            slider.setMax(Math.max(slider.getMin() + 1, value));
            field.setText(String.valueOf(slider.getMax()));
        }
        else {
            slider.setMin(Math.min(slider.getMax() - 1, value));
            field.setText(String.valueOf(slider.getMin()));
        }
        slider.setValue(Math.clamp(slider.getValue(), slider.getMin(), slider.getMax()));
        graphics.drawPixels(matrix);
    }

    /**
     * Update maximum iterations through the mandelbrot formula
     *
     * @param field TextField that contains the maximum iterations
     */
    public void iterationFieldAction(TextField field) {
        int z;
        try {
            z = Integer.parseInt(field.getText());
        } catch (NumberFormatException e) {
            return;
        }
        Mandelbrot.Z = z;
        recalculate();
    }

    /**
     * Action for selecting a new palette from the ChoiceBox.
     *
     * @param paletteChoice ChoiceBox containing different palettes saved in paletteMap
     * @param paletteColors list of colors for current palette.
     */
    public void paletteChoiceAction(ChoiceBox<String> paletteChoice, ListView<Color> paletteColors) {
        //Change default from null to saved palette
        if (paletteChoice.getValue() == null) paletteChoice.setValue(graphics.palette.getPaletteName());
        graphics.palette.setPalette(paletteChoice.getValue());
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }

    /**
     * Read and use palette colors and name from palettes.dat.
     *
     * @param paletteChoice ChoiceBox containing different palettes saved in paletteMap
     * @param paletteColors list of colors for current palette
     */
    public void readPaletteAction(ChoiceBox<String> paletteChoice, ListView<Color> paletteColors) {
        files.readPalette("palettes.dat", graphics.palette);
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
        paletteChoice.setItems(FXCollections.observableList(graphics.palette.getKeys()));
        paletteChoice.setValue(graphics.palette.getPaletteName());

    }

    /**
     * Save current palette colors and name from palettes.dat
     *
     * @param paletteChoice ChoiceBox containing different palettes saved in paletteMap
     * @param paletteColors list of colors for current palette
     * @param paletteName   TextField containing the user-given name for palette.
     */
    public void savePaletteAction(ChoiceBox<String> paletteChoice, ListView<Color> paletteColors, TextField paletteName) {
        files.savePalette("palettes.dat", graphics.palette, paletteName.getText());
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
        paletteChoice.setItems(FXCollections.observableList(graphics.palette.getKeys()));
        paletteChoice.setValue(graphics.palette.getPaletteName());
    }

    /**
     * Save current position to position.dat.
     */
    public void savePositionAction() {
        files.savePosition("position.dat", mandelbrot.scaleData);
    }

    /**
     * Read saved position from position.dat and relocate to it.
     *
     * @param x Text for x position
     * @param y Text for y position
     */
    public void readPositionAction(Text x, Text y) {
        files.readPosition("position.dat", mandelbrot.scaleData);
        x.setText(new DecimalFormat("#.##########").format(mandelbrot.scaleData.getxCenter()));
        y.setText(new DecimalFormat("#.##########").format(mandelbrot.scaleData.getyCenter()));
        recalculate();
    }

    /**
     * Move color up in palette.
     *
     * @param paletteColors List of palette colors
     */
    public void moveUpAction(ListView<Color> paletteColors) {
        int index = paletteColors.getSelectionModel().getSelectedIndex() < 0 ? graphics.palette.getPalette().size() - 1 : paletteColors.getSelectionModel().getSelectedIndex();
        if (index - 1 < 0) return;
        graphics.palette.swapColors(index, index - 1);
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }

    /**
     * Move color down in palette.
     *
     * @param paletteColors List of palette colors
     */
    public void moveDownAction(ListView<Color> paletteColors) {
        int index = paletteColors.getSelectionModel().getSelectedIndex() < 0 ? graphics.palette.getPalette().size() - 1 : paletteColors.getSelectionModel().getSelectedIndex();
        if (index + 2 > paletteColors.getItems().size()) return;
        graphics.palette.swapColors(index, index + 1);
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }

    @Override
    public String toString() {
        return "Input toString text";
    }
}