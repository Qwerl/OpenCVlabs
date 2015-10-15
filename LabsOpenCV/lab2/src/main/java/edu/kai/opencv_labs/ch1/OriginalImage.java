package edu.kai.opencv_labs.ch1;

import edu.kai.opencv_labs.core.ImageCanvas;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class OriginalImage extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";
    private ImageCanvas canvas;

    public OriginalImage() {
        run();
    }

    public OriginalImage(String path) {
        IMAGE_PATH = path;
        run();
    }

    @Override
    public void run() {
        try {
            File input = new File(IMAGE_PATH);
            BufferedImage image = ImageIO.read(input);
            canvas = new ImageCanvas("Original", image, new HashMap<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}