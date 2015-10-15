package edu.kai.opencv_labs.ch9;

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

public class GrayImageContrastZoom extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 200.0;
    private ImageCanvas canvas;
    private JSlider sliderMin;
    private JSlider sliderMax;

    public GrayImageContrastZoom() {
        run();
    }

    public GrayImageContrastZoom(String path) {
        IMAGE_PATH = path;
        run();
    }

    @Override
    public void run() {
        try {
            originalGrayImage = Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Imgcodecs.imencode(".bmp", originalGrayImage, matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));

            Map<JSlider, String> sliders = new HashMap<>();
            sliderMin = new JSlider(MIN_BRIGHTNESS, (int) (MAX_BRIGHTNESS * slidersRate), (int) (MIN_BRIGHTNESS * slidersRate));
            sliderMax = new JSlider(MIN_BRIGHTNESS, (int) (MAX_BRIGHTNESS * slidersRate), (int) (MAX_BRIGHTNESS * slidersRate));
            sliderMin.addChangeListener(e -> update());
            sliderMax.addChangeListener(e -> update());
            sliders.put(sliderMin, "Min");
            sliders.put(sliderMax, "Max");

            canvas = new ImageCanvas("GrayImageContrastZoom", image, sliders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mat doContrastZoom(Mat grayImage, double ratioMin, double ratioMax) {
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        double max = MIN_BRIGHTNESS;
        double min = MAX_BRIGHTNESS;
        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                if (pixels[0] > max) {
                    max = pixels[0];
                }
                if (pixels[0] < min) {
                    min = pixels[0];
                }
            }
        }

        double a, b;
        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                if (pixels[0] > ratioMin && pixels[0] < ratioMax) {
                    a = (ratioMax - ratioMin) / (max - min);
                    b = ((ratioMin * max) - (ratioMax * min)) / (max - min);
                    pixels[0] = (a * pixels[0] + b);
                }
                if (pixels[0] < ratioMin || pixels[0] > ratioMax) {
                    pixels[0] = MIN_BRIGHTNESS;
                }
                result.put(y, x, pixels);
            }
        }
        return result;
    }

    public void update() {
        double min = sliderMin.getValue() / slidersRate;
        double max = sliderMax.getValue() / slidersRate;
        Imgcodecs.imencode(".bmp", doContrastZoom(originalGrayImage, min, max), matOfByte);
        try {
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
        } catch (Exception ignore) {/* nop */}
    }
}