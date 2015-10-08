package ch1;

import core.ImageCanvas;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GrayImage extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    public GrayImage() {
        run();
    }

    public GrayImage(String path) {
        IMAGE_PATH = path;
        run();
    }

    @Override
    public void run() {
        try {
            MatOfByte matOfByte = new MatOfByte();
            Highgui.imencode(".bmp", Highgui.imread(IMAGE_PATH, Highgui.CV_LOAD_IMAGE_GRAYSCALE), matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));

            ImageCanvas canvas = new ImageCanvas("GrayImage", image, new ArrayList<JSlider>());
            canvas.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
