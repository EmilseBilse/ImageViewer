package dk.easv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
    private boolean stopShow = false;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;

    @FXML
    private void handleBtnLoadAction()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
            });
            imageView.setImage(images.get(0));
        }
    }

    @FXML
    private void handleBtnPreviousAction()
    {
        /*if (!images.isEmpty())
        {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }

         */
    }

    @FXML
    private void handleBtnNextAction()
    {

    }


    public void Start(ActionEvent actionEvent) throws InterruptedException {
        int waitThis = (int) slider.getValue() * 1000;
        Slideshow thread = new Slideshow(waitThis, images);
        thread.valueProperty().addListener((obs, o, n) -> {
            imageView.setImage(n);
        });
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(thread);

    }

    public void Stop(ActionEvent actionEvent) {
        stopShow = true;
    }
}