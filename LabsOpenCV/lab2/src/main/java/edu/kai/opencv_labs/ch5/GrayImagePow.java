package edu.kai.opencv_labs.ch5;

import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GrayImagePow extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    private final double POW_MAX_RATIO = 2.0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 200.0;
    private ImageCanvas canvas;
    private JSlider slider;

    public GrayImagePow() {
        run();
    }

    public GrayImagePow(String path) {
        IMAGE_PATH = path;
        run();
    }

    @Override
    public void run() {
        try {
            originalGrayImage = Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Imgcodecs.imencode(".jpg", originalGrayImage, matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));

            Map<JSlider, String> sliders = new HashMap<>();
            slider = new JSlider(0, (int) (POW_MAX_RATIO * slidersRate), (int) (1 * slidersRate));
            slider.addChangeListener(e -> update());
            sliders.put(slider, "POW RATIO");

            canvas = new ImageCanvas("GrayImagePow", image, sliders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mat doPow(Mat grayImage, double powRatio) {
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                pixels[0] = Math.pow(pixels[0], powRatio);
                result.put(y, x, pixels);
            }
        }
        return result;
    }

    public void update() {
        double currentContrastRatio = slider.getValue() / slidersRate;
        Imgcodecs.imencode(".bmp", doPow(originalGrayImage, currentContrastRatio), matOfByte);
        try {
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
        } catch (Exception ignore) {/* nop */}
    }
}
