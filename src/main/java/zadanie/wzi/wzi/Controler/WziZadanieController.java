package zadanie.wzi.wzi.Controler;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import zadanie.wzi.wzi.Model.DICOMData;
import zadanie.wzi.wzi.Drawings.Drawings;
import zadanie.wzi.wzi.Parser.Parser;

import java.io.*;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class WziZadanieController {

    @FXML
    Label valueSlider1;
    @FXML
    Label valueSlider2;
    @FXML
    Label valueSlider3;
    @FXML
    Label plik;
    @FXML
    Label areaPixel;
    @FXML
    Label areaVoxel;
    @FXML
    Label perimeterPixel;
    @FXML
    Label perimeterVoxel;
    @FXML
    Label labelAreaPixel;
    @FXML
    Label labelAreaVoxel;
    @FXML
    Label labelPerimeterPixel;
    @FXML
    Label labelPerimeterVoxel;
    @FXML
    Label brightness;
    @FXML
    Label contrast;
    @FXML
    Label windowValueLabel;
    @FXML
    Label levelValueLabel;
    @FXML
    Label machine_endian;
    @FXML
    Label files_endian;


    @FXML
    Slider slider1;
    @FXML
    Slider slider2;
    @FXML
    Slider slider3;
    @FXML
    Slider brigthnessSlider;
    @FXML
    Slider contrastSlider;
    @FXML
    Slider windowValueSlider;
    @FXML
    Slider levelValueSlider;

    @FXML
    ComboBox<String> colors;

    @FXML
    ImageView image1;
    @FXML
    ImageView image2;
    @FXML
    ImageView image3;

    @FXML
    StackPane drawing1;
    @FXML
    StackPane drawing2;
    @FXML
    StackPane drawing3;

    @FXML
    CheckBox polygon;
    @FXML
    CheckBox synchronize;
    @FXML
    CheckBox voxel;
    @FXML
    CheckBox rotate;

    @FXML
    Canvas draw1;
    @FXML
    Canvas draw2;
    @FXML
    Canvas draw3;

    Drawings drawings = new Drawings();

    List<DICOMData> dicomDataList = new ArrayList<>();

    private ChangeListener<Number> slider1ChangeListener1;
    private ChangeListener<Number> slider1ChangeListener2;
    private ChangeListener<Number> slider1ChangeListener3;

    private ChangeListener<Number> slider1ChangeListenerVoxel1;
    private ChangeListener<Number> slider1ChangeListenerVoxel2;
    private ChangeListener<Number> slider1ChangeListenerVoxel3;

    private Image originalImage1;
    private Image originalImage2;
    private Image originalImage3;

    @FXML
    private void initialize() {
        colors.getItems().addAll("gray",
                "twilight",
                "viridis",
                "inferno",
                "ocean",
                "gist heist",
                "copper");

        colors.getSelectionModel().selectFirst();
    }

    @FXML
    protected void changeValueOfSlider1() {
        int slider = (int) slider1.getValue();
        valueSlider1.setText(String.valueOf(slider));
    }

    @FXML
    protected void changeValueOfSlider2() {
        int slider = (int) slider2.getValue();
        valueSlider2.setText(String.valueOf(slider));
    }

    @FXML
    protected void changeValueOfSlider3() {
        int slider = (int) slider3.getValue();
        valueSlider3.setText(String.valueOf(slider));
    }

    @FXML
    public void changeValueOfContrastSlider() {
        changeBCValues(contrastSlider, contrast);
    }

    @FXML
    public void changeValueOfBrightnessSlider() {
        changeBCValues(brigthnessSlider, brightness);
    }

    @FXML
    public void changeValueOfWindowSlider() {
        int sliderValue = (int) windowValueSlider.getValue();
        windowValueLabel.setText(String.valueOf(sliderValue));

        int level = (int) levelValueSlider.getValue();
        setWindowAndLevelValues(sliderValue, level);
    }

    @FXML
    public void changeValueOfLevelSlider() {
        int sliderValue = (int) levelValueSlider.getValue();
        levelValueLabel.setText(String.valueOf(sliderValue));

        int window = (int) windowValueSlider.getValue();
        setWindowAndLevelValues(window, sliderValue);
    }

    @FXML
    protected void loadAction() {
        initializeSlidersAndLabels(slider1, valueSlider1, 1);
        initializeSlidersAndLabels(slider2, valueSlider2, 2);
        initializeSlidersAndLabels(slider3, valueSlider3, 3);

        int window = (int) windowValueSlider.getValue();
        int level = (int) levelValueSlider.getValue();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Wybierz folder");
        File directory = directoryChooser.showDialog(new Stage());
        if (directory != null && directory.isDirectory()) {
            plik.setText("Załadowano:" + directory.getName());
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                Arrays.sort(files, Comparator.comparing(File::getName, new NumericStringComparator()));
                for (File file : files) {
                    Parser parser = new Parser();
                    parser.parseDICOM(file);

                    DICOMData dicomData = parser.dicomData;
                    dicomDataList.add(dicomData);
                }

                showImageInFirstView(0, window, level);

                showImageInSecondView(0, window, level);
                slider2.setValue(0);
                valueSlider2.setText("0");

                showImageInThirdView(0, window, level);
                slider3.setValue(0);
                valueSlider3.setText("0");

                machine_endian.setText("Endian maszyny: " + ByteOrder.nativeOrder());
                files_endian.setText("Endian plików: " + dicomDataList.get(0).getEndian());
            }
        }

        initializeOriginalImages();
    }

    @FXML
    protected void voxelAction() {
        int windowValue = (int) windowValueSlider.getValue();
        int levelValue = (int) levelValueSlider.getValue();
        if (voxel.isSelected()) {
            addListenersForVoxels();
            drawings.setupMouseEventForVoxelDrawing(draw1, draw2, draw3, 0, 1);
            slider2.setValue(0);
            slider3.setValue(0);
            showImageInSecondView(0, windowValue, levelValue);
            showImageInThirdView(0, windowValue, levelValue);
            drawings.setupMouseEventForVoxelDrawing(draw2, draw1, draw3, 0, 2);
            drawings.setupMouseEventForVoxelDrawing(draw3, draw1, draw2, 0, 3);

            drawings.setVoxelSelected(true);
        } else {
            drawings.setVoxelSelected(false);
        }
    }

    @FXML
    protected void polygonAction() {
        if (polygon.isSelected() &&
                (image1.getImage() != null || image2.getImage() != null || image3.getImage() != null)) {
            drawings.setupMouseEventsForDrawings(draw1, draw2, draw3);
            drawings.setPolygonSelected(true);
        } else {
            drawings.setPolygonSelected(false);
        }
    }

    @FXML
    protected void areaAction() {
        if (!dicomDataList.isEmpty()) {
            if (drawings.isPolygonClosed()) {
                labelAreaPixel.setVisible(true);
                labelAreaVoxel.setVisible(true);
                labelPerimeterPixel.setVisible(true);
                labelPerimeterVoxel.setVisible(true);

                double areaInPixels = drawings.calculatePolygonAreaInPixels();
                double areaInVoxels = drawings.calculatePolygonAreaInVoxels();

                double perimeterInPixels = drawings.calculatePerimeterInPixels();
                double perimeterInVoxels = drawings.calculatePerimeterInVoxels();

                areaPixel.setText(areaInPixels + " pixeli");
                areaVoxel.setText(areaInVoxels + " voxeli");

                perimeterPixel.setText(perimeterInPixels + " pixeli");
                perimeterVoxel.setText(perimeterInVoxels + " voxeli");

                var pixels = drawings.getPolygonPointsList();
                for (double[] pixel : pixels) {
                    System.out.println(pixel[0] + " " + pixel[1]);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd!");
                alert.setHeaderText("Niezamknięty wielokąt!");
                alert.setContentText("Proszę zamknąć wielokąt! (PPM - prawy przycisk myszy)");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd!");
            alert.setHeaderText("Nie załadowano plików DICOM!");
            alert.setContentText("Proszę załądować pliki DICOM!");
            alert.showAndWait();
        }
    }

    @FXML
    protected void clearAction() {
        clearDrawingsLabelsAndImages();
    }

    @FXML
    protected void resetAction() {
        plik.setText("");
        slider1.setValue(0);
        valueSlider1.setText(String.valueOf(0));
        slider2.setValue(0);
        valueSlider2.setText(String.valueOf(0));
        slider3.setValue(0);
        valueSlider3.setText(String.valueOf(0));
        colors.getSelectionModel().selectFirst();
        windowValueLabel.setText(String.valueOf(0));
        windowValueSlider.setValue(0);
        levelValueLabel.setText(String.valueOf(0));
        levelValueSlider.setValue(0);

        slider1.valueProperty().removeListener(slider1ChangeListener1);
        slider2.valueProperty().removeListener(slider1ChangeListener2);
        slider3.valueProperty().removeListener(slider1ChangeListener3);

        slider1.valueProperty().removeListener(slider1ChangeListenerVoxel1);
        slider2.valueProperty().removeListener(slider1ChangeListenerVoxel2);
        slider3.valueProperty().removeListener(slider1ChangeListenerVoxel3);



        dicomDataList.clear();

        image1.setImage(null);
        image2.setImage(null);
        image3.setImage(null);


        clearDrawingsLabelsAndImages();
    }

    @FXML
    protected void colorsAction() {
        String selectedColormap = colors.getValue();

        if (selectedColormap != null) {
            double brightnessValue = brigthnessSlider.getValue() / 100.0;
            double contrastValue = contrastSlider.getValue() / 100.0;

            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(brightnessValue);
            colorAdjust.setContrast(contrastValue);

            switch (selectedColormap) {
                case "gray" -> setGrayLUT();
                case "twilight " -> setTwilightLUT();
                case "viridis" -> setViridisLUT();
                case "inferno" -> setInfernoLUT();
                case "ocean" -> setOceanLUT();
                case "gist heist" -> setGistHeistLUT();
                case "copper" -> setCopperLUT();
            }
        }
    }

    private void initializeOriginalImages() {
        originalImage1 = image1.getImage();
        originalImage2 = image2.getImage();
        originalImage3 = image3.getImage();
    }

    private void resetToOriginalImages() {
        image1.setImage(originalImage1);
        image2.setImage(originalImage2);
        image3.setImage(originalImage3);
    }

    private void setGistHeistLUT() {
        resetToOriginalImages();

        ImageView[] imageViews = {image1, image2, image3};
        for (ImageView imageView : imageViews) {
            Image image = imageView.getImage();
            if (image != null) {
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();

                WritableImage newImage = new WritableImage(width, height);
                PixelReader pixelReader = image.getPixelReader();
                PixelWriter pixelWriter = newImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = pixelReader.getColor(x, y);
                        double grayValue = color.getRed();

                        Color gHeatColor;
                        if (grayValue < 0.5) {
                            gHeatColor = new Color(grayValue * 2, 0, 0, 1.0);
                        } else {
                            gHeatColor = new Color(1.0, (grayValue - 0.5) * 2, (grayValue - 0.5) * 2, 1.0);
                        }

                        pixelWriter.setColor(x, y, gHeatColor);
                    }
                }

                imageView.setImage(newImage);
            }
        }
    }

    private void setCopperLUT() {
        resetToOriginalImages();

        ImageView[] imageViews = {image1, image2, image3};
        for (ImageView imageView : imageViews) {
            Image image = imageView.getImage();
            if (image != null) {
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();

                WritableImage newImage = new WritableImage(width, height);
                PixelReader pixelReader = image.getPixelReader();
                PixelWriter pixelWriter = newImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = pixelReader.getColor(x, y);
                        double grayValue = color.getRed();

                        double red = grayValue * 0.7;
                        double green = grayValue * 0.35;
                        double blue = grayValue * 0.1;

                        Color copperColor = Color.color(red, green, blue);
                        pixelWriter.setColor(x, y, copperColor);
                    }
                }

                imageView.setImage(newImage);
            }
        }
    }

    private void setOceanLUT() {
        resetToOriginalImages();

        ImageView[] imageViews = {image1, image2, image3};
        for (ImageView imageView : imageViews) {
            Image image = imageView.getImage();
            if (image != null) {
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();

                WritableImage newImage = new WritableImage(width, height);
                PixelReader pixelReader = image.getPixelReader();
                PixelWriter pixelWriter = newImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = pixelReader.getColor(x, y);
                        double grayValue = color.getRed();

                        double blueIntensity = grayValue < 0.5 ? grayValue * 2 : 1;
                        double greenIntensity = grayValue < 0.5 ? grayValue * 2 : 1 - ((grayValue - 0.5) * 2);

                        Color oceanColor = new Color(0, greenIntensity, blueIntensity, 1.0);

                        pixelWriter.setColor(x, y, oceanColor);
                    }
                }

                imageView.setImage(newImage);
            }
        }
    }

    private void setInfernoLUT() {
        resetToOriginalImages();

        ImageView[] imageViews = {image1, image2, image3};
        for (ImageView imageView : imageViews) {
            Image image = imageView.getImage();
            if (image != null) {
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();

                WritableImage newImage = new WritableImage(width, height);
                PixelReader pixelReader = image.getPixelReader();
                PixelWriter pixelWriter = newImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = pixelReader.getColor(x, y);
                        double grayValue = color.getRed();

                        double hue = 0.75 * grayValue;
                        double saturation = 0.8 + 0.2 * grayValue;
                        double brightness = 0.7 + 0.3 * grayValue;

                        Color infernoColor = Color.hsb(hue * 360.0, saturation, brightness);
                        pixelWriter.setColor(x, y, infernoColor);
                    }
                }

                imageView.setImage(newImage);
            }
        }
    }

    private void setViridisLUT() {
        resetToOriginalImages();

        ImageView[] imageViews = {image1, image2, image3};
        for (ImageView imageView : imageViews) {
            Image image = imageView.getImage();
            if (image != null) {
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();

                WritableImage newImage = new WritableImage(width, height);
                PixelReader pixelReader = image.getPixelReader();
                PixelWriter pixelWriter = newImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = pixelReader.getColor(x, y);
                        double grayValue = color.grayscale().getRed();

                        double hue = 0.5 - grayValue * 0.75;
                        double saturation = 0.8;

                        Color viridisColor = Color.hsb(hue * 360, saturation, grayValue);

                        pixelWriter.setColor(x, y, viridisColor);
                    }
                }

                imageView.setImage(newImage);
            }
        }
    }

    private void setTwilightLUT() {
        resetToOriginalImages();

        ImageView[] imageViews = {image1, image2, image3};
        for (ImageView imageView : imageViews) {
            Image image = imageView.getImage();
            if (image != null) {
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();

                WritableImage newImage = new WritableImage(width, height);
                PixelReader pixelReader = image.getPixelReader();
                PixelWriter pixelWriter = newImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = pixelReader.getColor(x, y);
                        double grayValue = color.grayscale().getRed(); // Get grayscale value
                        double value = grayValue;

                        // Twilight colormap transformation
                        double hue = 0.65 - value * 0.35; // Adjusted hue shift for Twilight-like colormap
                        double saturation = 0.8 + 0.2 * value;
                        double brightness = 0.6 + 0.4 * value;

                        Color twilightColor = Color.hsb(hue * 360, saturation, brightness);

                        pixelWriter.setColor(x, y, twilightColor);
                    }
                }

                imageView.setImage(newImage);
            }
        }
    }

    private void setGrayLUT() {
        resetToOriginalImages();

        ImageView[] imageViews = {image1, image2, image3};
        for (ImageView imageView : imageViews) {
            Image image = imageView.getImage();
            if (image != null) {
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();

                WritableImage newImage = new WritableImage(width, height);
                PixelReader pixelReader = image.getPixelReader();
                PixelWriter pixelWriter = newImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = pixelReader.getColor(x, y);
                        double grayValue = color.grayscale().getRed();

                        Color grayColor = new Color(grayValue, grayValue, grayValue, 1.0);

                        pixelWriter.setColor(x, y, grayColor);
                    }
                }

                imageView.setImage(newImage);
            }
        }
    }

    @FXML
    protected void synchronizeAction() {

    }

    @FXML
    protected void rotateAction() {
        Image[] images = {image1.getImage(), image2.getImage(), image3.getImage()};
        for (Image image : images) {
            if (image != null) {
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();

                WritableImage rotatedImage = new WritableImage(width, height);

                PixelReader pixelReader = image.getPixelReader();
                PixelWriter pixelWriter = rotatedImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = pixelReader.getColor(x, y);

                        pixelWriter.setColor(width - x - 1, height - y - 1, color);
                    }
                }

                if (image == image1.getImage()) {
                    image1.setImage(rotatedImage);
                } else if (image == image2.getImage()) {
                    image2.setImage(rotatedImage);
                } else if (image == image3.getImage()) {
                    image3.setImage(rotatedImage);
                }
            }
        }
    }

    // Metoda do obracania obrazu o 90 stopni w prawo (jeśli to konieczne)
    private WritableImage rotateImage(WritableImage image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage rotatedImage = new WritableImage(height, width);
        PixelWriter rotatedPixelWriter = rotatedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                javafx.scene.paint.Color color = image.getPixelReader().getColor(x, y);
                rotatedPixelWriter.setColor(height - y - 1, x, color);
            }
        }

        return rotatedImage;
    }

    public static class NumericStringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            String[] parts1 = s1.split("(?<=\\D)(?=\\d)");
            String[] parts2 = s2.split("(?<=\\D)(?=\\d)");

            // Porównanie prefiksów (części nie-liczbowych) nazw plików
            int prefixComparison = parts1[0].compareTo(parts2[0]);
            if (prefixComparison != 0) {
                return prefixComparison;
            }

            // Porównanie części liczbowych nazw plików jako liczby całkowite
            int num1 = Integer.parseInt(parts1[1]);
            int num2 = Integer.parseInt(parts2[1]);
            return Integer.compare(num1, num2);
        }
    }

    private void clearDrawingsLabelsAndImages() {
        drawings.clearCanvasDrawing(draw1);
        drawings.clearCanvasDrawing(draw2);
        drawings.clearCanvasDrawing(draw3);
        drawings.setupMouseEventsForDrawings(draw1, draw2, draw3);
        drawings.setupMouseEventForVoxelDrawing(draw1, draw2, draw3, 0, 1);
        drawings.setupMouseEventForVoxelDrawing(draw2, draw1, draw3, 0, 2);
        drawings.setupMouseEventForVoxelDrawing(draw3, draw1, draw2, 0, 3);

        labelPerimeterPixel.setVisible(false);
        labelPerimeterVoxel.setVisible(false);
        labelAreaVoxel.setVisible(false);
        labelAreaPixel.setVisible(false);

        areaVoxel.setText("");
        areaPixel.setText("");
        perimeterVoxel.setText("");
        perimeterPixel.setText("");

        ColorAdjust noEffect = new ColorAdjust();
        image1.setEffect(noEffect);
        image2.setEffect(noEffect);
        image3.setEffect(noEffect);

        brigthnessSlider.setValue(0);
        brightness.setText(String.valueOf(0));
        contrastSlider.setValue(0);
        contrast.setText(String.valueOf(0));
        windowValueSlider.setValue(0);
        windowValueLabel.setText(String.valueOf(0));
        levelValueSlider.setValue(0);
        levelValueLabel.setText(String.valueOf(0));
    }

    private void showImageInFirstView(int indexOfFile, int windowValue, int levelValue) {
        DICOMData dicomData = dicomDataList.get(indexOfFile);
        int rows = dicomData.getRows();
        int cols = dicomData.getColumns();

        WritableImage image = new WritableImage(rows, cols);

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                char pixelColor = getPixelColor(x, y, indexOfFile, windowValue, levelValue);
                Color color = Color.rgb(pixelColor, pixelColor, pixelColor);
                image.getPixelWriter().setColor(x, y, color);
            }
        }

        image = rotateImage(image);

        image1.setImage(image);
    }

    private void showImageInSecondView(int slierValue, int windowValue, int levelValue) {
        int rows = dicomDataList.get(0).getRows();
        int cols = dicomDataList.get(0).getColumns();
        int size = dicomDataList.size();

        WritableImage image = new WritableImage(rows, cols);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < cols; y++) {
                char pixelColor = getPixelColor(slierValue, y, x, windowValue, levelValue);
                Color color = Color.rgb(pixelColor, pixelColor, pixelColor);
                image.getPixelWriter().setColor(y, size - 1 - x, color);
            }
        }

        int newHeight = (int) ((float) size * dicomDataList.get(0).getSliceThickness() / dicomDataList.get(0).getPixelSpacing()[0]) + 2;
        int newWidth = 580;
        WritableImage scaledImage = new WritableImage(newWidth, newHeight);

        PixelReader pixelReader = image.getPixelReader();

        for (int newX = 0; newX < newWidth; newX++) {
            for (int newY = 0; newY < newHeight; newY++) {
                int originalX = (int) ((float) newX / newWidth * rows);
                int originalY = (int) ((float) newY / newHeight * size);

                Color color = pixelReader.getColor(originalX, originalY);
                scaledImage.getPixelWriter().setColor(newX, newY, color);
            }
        }

        image2.setImage(scaledImage);
    }

    private void showImageInThirdView(int slierValue, int windowValue, int levelValue) {
        int rows = dicomDataList.get(0).getRows();
        int cols = dicomDataList.get(0).getColumns();
        int size = dicomDataList.size();

        WritableImage image = new WritableImage(rows, cols);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < rows; y++) {
                char pixelColor = getPixelColor(y, slierValue, x, windowValue, levelValue);
                Color color = Color.rgb(pixelColor, pixelColor, pixelColor);
                image.getPixelWriter().setColor(y, size - 1 - x, color);
            }
        }

        int newHeight = (int) ((float) size * dicomDataList.get(0).getSliceThickness() / dicomDataList.get(0).getPixelSpacing()[1]) + 2;
        int newWidth = 580;
        WritableImage scaledImage = new WritableImage(newWidth, newHeight);

        PixelReader pixelReader = image.getPixelReader();

        for (int newX = 0; newX < newWidth; newX++) {
            for (int newY = 0; newY < newHeight; newY++) {
                int originalX = (int) ((float) newX / newWidth * rows);
                int originalY = (int) ((float) newY / newHeight * size);

                Color color = pixelReader.getColor(originalX, originalY);
                scaledImage.getPixelWriter().setColor(newX, newY, color);
            }
        }

        image3.setImage(scaledImage);
    }

    private void addListenerToSliders(Slider slider, Integer indexOfFunction) {
        if (indexOfFunction == 1) {
            slider1ChangeListener1 = (observable, oldValue, newValue) -> {
                int index = newValue.intValue();
                if (index >= 0) {
                    showImageInFirstView(index, (int) windowValueSlider.getValue(), (int) levelValueSlider.getValue());
                }
            };

            slider.valueProperty().addListener(slider1ChangeListener1);
        } else if (indexOfFunction == 2) {
            slider1ChangeListener2 = (observable, oldValue, newValue) -> {
                int index = newValue.intValue();
                if (index >= 0) {
                    showImageInSecondView(index, (int) windowValueSlider.getValue(), (int) levelValueSlider.getValue());
                }
            };

            slider.valueProperty().addListener(slider1ChangeListener2);
        } else if (indexOfFunction == 3) {
            slider1ChangeListener3 = (observable, oldValue, newValue) -> {
                int index = newValue.intValue();
                if (index >= 0) {
                    showImageInThirdView(index, (int) windowValueSlider.getValue(), (int) levelValueSlider.getValue());
                }
            };

            slider.valueProperty().addListener(slider1ChangeListener3);
        }
    }

    private void addListenersForVoxels() {
        slider1ChangeListenerVoxel1 = (observable, oldValue, newValue) -> {
            int index = newValue.intValue();
            if (index >= 0) {
                drawings.setupMouseEventForVoxelDrawing(draw1, draw2, draw3, index, 1);
                Point3D point = drawings.getPoint3D();
//                showImageInSecondView(, (int) windowValueSlider.getValue(), (int) levelValueSlider.getValue());
//                showImageInThirdView(, (int) windowValueSlider.getValue(), (int) levelValueSlider.getValue());
            }
        };
        slider1.valueProperty().addListener(slider1ChangeListenerVoxel1);

        slider1ChangeListenerVoxel2 = (observable, oldValue, newValue) -> {
            int index = newValue.intValue();
            if (index >= 0) {
                drawings.setupMouseEventForVoxelDrawing(draw2, draw1, draw3, index, 2);
                Point3D point = drawings.getPoint3D();
//                showImageInFirstView(, (int) windowValueSlider.getValue(), (int) levelValueSlider.getValue());
//                showImageInThirdView(, (int) windowValueSlider.getValue(), (int) levelValueSlider.getValue());
            }
        };
        slider2.valueProperty().addListener(slider1ChangeListenerVoxel2);

        slider1ChangeListenerVoxel3 = (observable, oldValue, newValue) -> {
            int index = newValue.intValue();
            if (index >= 0) {
                drawings.setupMouseEventForVoxelDrawing(draw3, draw1, draw2, index, 3);
                Point3D point = drawings.getPoint3D();
//                showImageInFirstView(, (int) windowValueSlider.getValue(), (int) levelValueSlider.getValue());
//                showImageInSecondView(, (int) windowValueSlider.getValue(), (int) levelValueSlider.getValue());
            }
        };
        slider3.valueProperty().addListener(slider1ChangeListenerVoxel3);
    }

    private void initializeSlidersAndLabels(Slider slider, Label valueSlider, int indexOfFunction) {
        int valueOfSlider = (int) slider.getValue();
        valueSlider.setText(String.valueOf(valueOfSlider));
        addListenerToSliders(slider, indexOfFunction);
    }

    private void setEffects(ColorAdjust colorAdjust, double v, double v1) {
        colorAdjust.setHue(v);
        colorAdjust.setSaturation(v1);
        image1.setEffect(colorAdjust);
        image2.setEffect(colorAdjust);
        image3.setEffect(colorAdjust);
    }

    private void changeBCValues(Slider slider, Label label) {
        int sliderValue = (int) slider.getValue();
        label.setText(String.valueOf(sliderValue));

        double brightnessValue = brigthnessSlider.getValue() / 100.0;
        double contrastValue = contrastSlider.getValue() / 100.0;

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(brightnessValue);
        colorAdjust.setContrast(contrastValue);

        image1.setEffect(colorAdjust);
        image2.setEffect(colorAdjust);
        image3.setEffect(colorAdjust);
    }

    private void setWindowAndLevelValues(int window, int sliderValue) {
        int firstView = (int) slider1.getValue();
        int secondView = (int) slider2.getValue();
        int thirdView = (int) slider3.getValue();

        showImageInFirstView(firstView, window, sliderValue);
        showImageInSecondView(secondView, window, sliderValue);
        showImageInThirdView(thirdView, window, sliderValue);
    }

    public char getPixelColor(int x, int y, int index, int windowValue, int levelValue) {
        DICOMData dicomData = dicomDataList.get(index);
        var pixelData = dicomData.getPixelData();
        float value = pixelData[x][y] * dicomData.getRescaleSlope() + dicomData.getRescaleIntercept();
        char yMin = 0;
        char yMax = 255;
        float center;
        float width;

        if (windowValue > 0 || levelValue > 0) {
            center = windowValue;
            width = levelValue;
        } else {
            center = dicomData.getWindowCenter() - 0.5f;
            width = dicomData.getWindowWidth() - 1.0f;
        }

        if (value <= (center - width / 2.0f)) {
            return yMin;
        } else if (value > (center + width / 2.0f)) {
            return yMax;
        } else {
            return (char) (((value - center) / width + 0.5f) * (yMax - yMin) + yMin);
        }
    }
}