package edu.kai.opencv_labs.ch2;


import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class GrayImageWithContrast extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private final double MAX_CONTRAST_RATIO = 5.0;
    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 200.0;
    private ImageCanvas canvas;
    private JSlider slider;

    public GrayImageWithContrast() {
        run();
    }

    public GrayImageWithContrast(String path) {
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
            slider = new JSlider(0, (int) (MAX_CONTRAST_RATIO * slidersRate), (int) (1 * slidersRate));
            slider.addChangeListener(e -> update());
            sliders.put(slider, "CONTRAST RATIO");

            canvas = new ImageCanvas("GrayImageWithContrast", image, sliders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mat doContrast(Mat grayImage, double contrastRatio) {
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                pixels[0] = pixels[0] * contrastRatio;
                if (pixels[0] > MAX_BRIGHTNESS) { //защита от переполнения
                    pixels[0] = MAX_BRIGHTNESS;
                }
                result.put(y, x, pixels);
            }
        }
        return result;
    }

    public void update() {
        double currentContrastRatio = slider.getValue()/slidersRate;
        Imgcodecs.imencode(".bmp", doContrast(originalGrayImage, currentContrastRatio), matOfByte);
        try {
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
        } catch (Exception ignore) {/* nop */}
    }
}
