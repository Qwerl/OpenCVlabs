package edu.kai.opencv_labs.ch1;

import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

public class GrayImage extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    private ImageCanvas canvas;

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
            Imgcodecs.imencode(".bmp", Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE), matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));

            canvas = new ImageCanvas("GrayImage", image, new HashMap<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
