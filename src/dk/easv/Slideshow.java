package dk.easv;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import java.util.List;

public class Slideshow extends Task<Image>{
    private int currentImageIndex = 0;
    private final List<Image> images;
    private int waitTime;
    public static boolean stopShow;

    public Slideshow(int waitTime, List<Image> images) {
        this.waitTime = waitTime;
        this.images = images;
    }

    @Override
    protected Image call() throws Exception {
        while(!stopShow){
            moveNext();
            updateValue(images.get(currentImageIndex));
            Thread.sleep(waitTime);
        }
        return images.get(currentImageIndex);
    }

    public void moveNext(){
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();
        }
    }
}
