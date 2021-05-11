package dk.easv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController
{
    private final List<Image> images = new ArrayList<>();
    public Button btnLoad;
    public Button btnPrevious;
    public Button btnNext;
    public Button btnStart;
    public Button btnStop;
    public Slider slider;
    public Label lblFileName;
    public Label lblColors;
    private Slideshow thread;

    private int currentImageIndex;

    private ArrayList<String> fileNames = new ArrayList<>();

    @FXML
    public void initialize() {
        Slideshow.stopShow = false;
    }

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;

    @FXML
    private void handleBtnLoadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if ( files != null && !files.isEmpty()) {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
                fileNames.add(f.getName());
            });
            imageView.setImage(images.get(0));
        }
    }

    @FXML
    private void handleBtnPreviousAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1 + images.size()) % images.size();
            displayImage();
        }
    }

    public void Start(ActionEvent actionEvent) {
        Slideshow.stopShow = false;
        int waitThis = (int) ((slider.getMax()-slider.getValue()) * 1000);
        thread = new Slideshow(waitThis,images,fileNames);
        thread.setFileName(lblFileName);
        thread.setColors(lblColors);
        thread.valueProperty().addListener((obs, o, n) -> {
            imageView.setImage(n);
        });
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(thread);
        waitSpeedListener();
    }

    public void Stop(ActionEvent actionEvent) {
        Slideshow.stopShow = true;
    }

    private void displayImage(){
        lblFileName.setText("Picture: " + fileNames.get(currentImageIndex));
        imageView.setImage(images.get(currentImageIndex));
    }

    private void waitSpeedListener(){
        this.slider.valueProperty().addListener((observable,oldValue,newValue) -> {
            thread.setWaitTime((int) ((slider.getMax()-slider.getValue()) * 1000));
        });
    }
}