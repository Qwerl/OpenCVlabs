package edu.kai.opencv_labs.ch4;

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

public class GrayImageLog extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    private final double LOG_MAX_RATIO = 100.0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 100.0;
    private ImageCanvas canvas;
    private JSlider slider;

    public GrayImageLog() {
        run();
    }

    public GrayImageLog(String path) {
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
            slider = new JSlider(0, (int) (LOG_MAX_RATIO * slidersRate), (int) (1 * slidersRate));
            slider.addChangeListener(e -> update());
            sliders.put(slider, "LOG RATIO");
            canvas = new ImageCanvas("GrayImageLog", image, sliders);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mat doLog(Mat grayImage, double logRatio) {
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                pixels[0] = Math.log(1 + pixels[0]) * logRatio;
                result.put(y, x, pixels);
            }
        }
        return result;
    }

    public void update() {
        double currentContrastRatio = slider.getValue() / slidersRate;
        Imgcodecs.imencode(".bmp", doLog(originalGrayImage, currentContrastRatio), matOfByte);
        try {
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
        } catch (Exception ignore) {/* nop */}
    }
}