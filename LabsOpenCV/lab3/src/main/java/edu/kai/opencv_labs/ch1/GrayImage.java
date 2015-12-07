package edu.kai.opencv_labs.ch1;

import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GrayImage extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private MatOfByte matOfByte = new MatOfByte();

    private Mat originalGrayImage = new MatOfByte();

    private double slidersRate = 100.0;
    private ImageCanvas canvas;

    public GrayImage() {
        this.start();
    }

    public GrayImage(String path) {
        IMAGE_PATH = path;
        this.start();
    }

    @Override
    public void run() {
        try {
            Map<JSlider, String> sliders = new HashMap<>();

            originalGrayImage = Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Imgcodecs.imencode(".bmp", originalGrayImage, matOfByte);
            BufferedImage grayImage = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
            new ImageCanvas("lab3 gray", grayImage, sliders);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}