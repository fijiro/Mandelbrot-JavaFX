package iiro.toiv.mandelbrotjavafx.Input;

import iiro.toiv.mandelbrotjavafx.Files.Files;
import iiro.toiv.mandelbrotjavafx.Graphics.Graphics;
import iiro.toiv.mandelbrotjavafx.Graphics.Palette;
import iiro.toiv.mandelbrotjavafx.Positions.Mandelbrot;
import iiro.toiv.mandelbrotjavafx.Positions.Matrix;
import iiro.toiv.mandelbrotjavafx.Positions.ScaleData;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Input {
    public final Mandelbrot mandelbrot;
    public final Graphics graphics;
    private final Matrix matrix;
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

        imageView.setOnMouseReleased(event -> {
            //from 0 - imageView.getWidth().
            mandelbrot.scaleData.centerTo(event.getX(), event.getY());
            //start timeline to calculate pixels
            recalculate();

        });
        imageView.setOnScroll(event -> {
            //zooms in if scrolled up, out if scrolled down
            mandelbrot.scaleData.zoomLevel(event.getDeltaY() > 0);
            recalculate();

        });
    }

    public void addColorAction(TextField redField, TextField greenField, TextField blueField, ListView<Color> paletteColors) {
        try {
            graphics.palette.addColor(Double.parseDouble(redField.getText()), Double.parseDouble(greenField.getText()),
                    Double.parseDouble(blueField.getText()));
        } catch (NumberFormatException | NullPointerException e) {
            //System.out.println("Given values are not between 0-1");
            return;
        }
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }

    public void removeColorAction(ListView<Color> paletteColors) {
        if (!graphics.palette.getPalette().isEmpty()) {
            graphics.palette.removeColor(paletteColors.getSelectionModel().getSelectedIndex() < 0 ?
                    graphics.palette.getPalette().size() - 1 : paletteColors.getSelectionModel().getSelectedIndex());
            paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
        }
    }

    public void speedFieldAction(TextField field) {
        double speed;
        try {
            speed = Double.parseDouble(field.getText());
        } catch (NullPointerException | NumberFormatException e) {
            return;
        }
        graphics.palette.adjustSpeed(speed);
        //For smooth coloring
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
    public void readPaletteAction(ListView<Color> paletteColors) {
        files.readPalette("palettes.dat", graphics.palette);
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }
    public void savePaletteAction(ListView<Color> paletteColors) {
        files.savePalette("palettes.dat", graphics.palette.getPalette());
        paletteColors.setItems(FXCollections.observableList(graphics.palette.getPalette()));
    }
    public void savePositionAction() {
        files.savePosition("position.dat", mandelbrot.scaleData);
    }

    public void readPositionAction() {
        files.readPosition("position.dat", mandelbrot.scaleData);
        recalculate();
    }
}