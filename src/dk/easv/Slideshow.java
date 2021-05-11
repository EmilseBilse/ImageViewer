package dk.easv;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Slideshow extends Task<Image>{
    private int currentImageIndex = 0;
    private final List<Image> images;
    private int waitTime;
    private ArrayList<String> fileNames;

    public Label getFileName() {
        return fileName;
    }

    public void setFileName(Label fileName) {
        this.fileName = fileName;
    }

    private Label fileName;
    public static boolean stopShow;

    public Slideshow(int waitTime, List<Image> images, ArrayList<String> fileNames) {
        this.waitTime = waitTime;
        this.images = images;
        this.fileNames = fileNames;
    }

    public Slideshow(List<Image> images){
        this.images = images;
    }

    @Override
    protected Image call() throws Exception {

        startShow();

        return images.get(currentImageIndex);
    }

    public void moveNext(){
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();

        }
    }

    private void startShow() throws InterruptedException {
        while(!stopShow){
            moveNext();
            Platform.runLater(() -> {
                fileName.setText("Picture: " + fileNames.get(currentImageIndex));
            });
            updateValue(images.get(currentImageIndex));
            Thread.sleep(waitTime);
        }
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
