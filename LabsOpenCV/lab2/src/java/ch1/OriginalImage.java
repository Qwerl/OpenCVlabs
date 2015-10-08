package ch1;

import core.ImageCanvas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OriginalImage extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

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
            ImageCanvas canvas = new ImageCanvas("Original", image, new ArrayList<JSlider>());
            canvas.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}