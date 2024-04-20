package controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class front {
    @FXML
    private ImageView imageView;
    private int currentIndex = 0;
    private List<Image> images = new ArrayList<>();

    @FXML
    public void initialize() {
        // Load images or set them directly
        images.add(new Image("file:@/path/to/image1.jpg"));
        images.add(new Image("file:@/path/to/image2.jpg"));
        // Initially display the first image
        imageView.setImage(images.get(0));
    }

    @FXML
    private void handlePrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            imageView.setImage(images.get(currentIndex));
        }
    }

    @FXML
    private void handleNext() {
        if (currentIndex < images.size() - 1) {
            currentIndex++;
            imageView.setImage(images.get(currentIndex));
        }
    }
}
