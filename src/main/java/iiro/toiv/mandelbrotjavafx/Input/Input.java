package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.ControllerClass;
import iiro.toiv.mandelbrotjavafx.Files.Files;
import iiro.toiv.mandelbrotjavafx.Graphics.Graphics;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Input extends ControllerClass {
    public final Graphics graphics;
    private final Matrix matrix;
    private final Mandelbrot mandelbrot;
    private final Files files = new Files();
    private final WritableImage image;
    private final ExecutorService executor = new ForkJoinPool(1);

    public void recalculate() {
        matrix.resize((int) image.getWidth(), (int) image.getHeight());
        executor.submit(mandelbrot);
    }

    public Input(ImageView imageView, Matrix matrix) {
        this.matrix = matrix;
        image = (WritableImage) imageView.getImage();
        mandelbrot = new Mandelbrot(image, matrix);
        graphics = new Graphics(image);
    }

    public void imageMouseAction(MouseEvent event) {
        //from 0 - imageView.getWidth().
        mandelbrot.scaleData.centerTo(event.getX(), event.getY());
        //start timeline to calculate pixels
        recalculate();
    }

    public void ImageScrollAction(ScrollEvent event) {
        //zooms in if scrolled up, out if scrolled down
        mandelbrot.scaleData.zoomLevel(event.getDeltaY() > 0);
        recalculate();
    }


    public void addColorAction(TextField redField, TextField greenField, TextField blueField, ListView<Color> paletteColors) {
        try {
            graphics.palette.addColor(Double.parseDouble(redField.getText()), Double.parseDouble(greenField.getText()), Double.parseDouble(blueField.getText()));
        } catch (NumberFormatException | NullPointerException e) {
            throw new RuntimeException(e);
        }
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }

    public void removeColorAction(ListView<Color> paletteColors) {
        if (!graphics.palette.getPalette().isEmpty()) {
            graphics.palette.removeColor(paletteColors.getSelectionModel().getSelectedIndex() < 0 ? graphics.palette.getPalette().size() - 1 : paletteColors.getSelectionModel().getSelectedIndex());
            paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
        }
    }

    public void speedSliderAction(Slider slider) {
        graphics.palette.setSpeed(slider.getValue());
        //For smooth coloring
        graphics.drawPixels(matrix);
    }

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

    public void paletteChoiceAction(ChoiceBox<String> paletteChoice, ListView<Color> paletteColors) {
        //Change default from null to saved palette
        if (paletteChoice.getValue() == null) paletteChoice.setValue(graphics.palette.getPaletteName());
        graphics.palette.setPalette(paletteChoice.getValue());
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }

    public void readPaletteAction(ListView<Color> colorListView, ChoiceBox<String> paletteChoice) {
        files.readPalette("palettes.dat", graphics.palette);
        colorListView.setItems(FXCollections.observableList(graphics.palette.getPalette()));
        paletteChoice.setItems(FXCollections.observableList(graphics.palette.getKeys()));
        paletteChoice.setValue(graphics.palette.getPaletteName());

    }

    public void savePaletteAction(ListView<Color> colorListView, TextField nameTextField, ChoiceBox<String> paletteChoice) {
        files.savePalette("palettes.dat", nameTextField.getText(), graphics.palette);
        colorListView.setItems(FXCollections.observableList(graphics.palette.getPalette()));
        paletteChoice.setItems(FXCollections.observableList(graphics.palette.getKeys()));
        paletteChoice.setValue(graphics.palette.getPaletteName());
    }

    public void savePositionAction() {
        files.savePosition("position.dat", mandelbrot.scaleData);
    }

    public void readPositionAction() {
        files.readPosition("position.dat", mandelbrot.scaleData);
        recalculate();
    }

    public void moveUpAction(ListView<Color> paletteColors) {
        int index = paletteColors.getSelectionModel().getSelectedIndex() < 0 ? graphics.palette.getPalette().size() - 1 : paletteColors.getSelectionModel().getSelectedIndex();
        if (index - 1 < 0) return;
        graphics.palette.swapColors(index, index - 1);
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }

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