package edu.kai.opencv_labs.ch1;

import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GrayImageAdaptiveThreshold extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private MatOfByte matOfByte = new MatOfByte();

    private Mat originalGrayImage = new MatOfByte();
    private Mat adaptive;


    private double slidersRate = 100.0;
    private ImageCanvas canvas;

    public GrayImageAdaptiveThreshold() {
        this.start();
    }

    public GrayImageAdaptiveThreshold(String path) {
        IMAGE_PATH = path;
        this.start();
    }

    public void run() {
        try {
            Map<JSlider, String> sliders = new HashMap<>();

            originalGrayImage = Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            adaptive = originalGrayImage.clone();

            Imgproc.adaptiveThreshold(
                    originalGrayImage,                  //source
                    adaptive,                           //destination
                    255,                                //maxValue
                    Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, //ADAPTIVE_THRESH_MEAN_C
                    Imgproc.THRESH_BINARY,              //THRESH_BINARY, THRESH_BINARY_INV, THRESH_MASK, THRESH_OTSU, THRESH_TOZERO, THRESH_TOZERO_INV, THRESH_TRIANGLE, THRESH_TRUNC
                    3,
                    5
            );
            Imgcodecs.imencode(".bmp", adaptive, matOfByte);
            BufferedImage adaptiveImage = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
            new ImageCanvas("lab3 adaptive", adaptiveImage, sliders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}