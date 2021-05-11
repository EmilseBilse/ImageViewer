package dk.easv;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Slideshow extends Task<Image>{
    private int currentImageIndex = 0;
    private final List<Image> images;
    private int waitTime;
    private ArrayList<String> fileNames;
    private int red;
    private int green;
    private int blue;
    private Label fileName;

    public Label getColors() {
        return colors;
    }

    public void setColors(Label colors) {
        this.colors = colors;
    }

    private Label colors;
    public static boolean stopShow;

    public Label getFileName() {
        return fileName;
    }

    public void setFileName(Label fileName) {
        this.fileName = fileName;
    }

    public Slideshow(int waitTime, List<Image> images, ArrayList<String> fileNames) {
        this.waitTime = waitTime;
        this.images = images;
        this.fileNames = fileNames;
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
            getPixelInfo();
            updateValue(images.get(currentImageIndex));
            Thread.sleep(waitTime);
        }
    }

    private void getPixelInfo(){
        Image img = images.get(currentImageIndex);
        PixelReader pReader = img.getPixelReader();
        red = 0;
        green = 0;
        blue = 0;

        for (int readY = 0; readY < img.getHeight(); readY++){
            for (int readX = 0; readX < img.getWidth(); readX++){
                Color color = pReader.getColor(readX,readY);
                if(color.getBlue() > color.getGreen() && color.getBlue() > color.getRed()){
                    blue++;
                }else if(color.getGreen() > color.getBlue() && color.getGreen() > color.getRed()){
                    green++;
                }else if(color.getRed() > color.getBlue() && color.getRed() > color.getGreen()){
                    red++;
                }
            }
        }
        Platform.runLater(() ->{
            colors.setText("Red: " + red + " Green: " + green + " Blue: " + blue);
        });
    }

}
