package edu.kai.opencv_labs.ch8;

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

public class GrayImageCutoffLuminance extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    double[] pixelsBuffer = new double[1];

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 100.0;

    private ImageCanvas canvas;
    private JSlider sliderMin;
    private JSlider sliderMax;

    public GrayImageCutoffLuminance() {
        run();
    }

    public GrayImageCutoffLuminance(String path) {
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
            sliderMin = new JSlider(MIN_BRIGHTNESS, (int) (MAX_BRIGHTNESS * slidersRate), (1 * MIN_BRIGHTNESS));
            sliderMax = new JSlider(MIN_BRIGHTNESS, (int) (MAX_BRIGHTNESS * slidersRate), (1 * MAX_BRIGHTNESS));
            sliderMin.addChangeListener(e -> update());
            sliderMax.addChangeListener(e -> update());
            sliders.put(sliderMin, "Min");
            sliders.put(sliderMax, "Max");

            canvas = new ImageCanvas("GrayImageCutoffLuminance", image, sliders);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mat doCutoffLuminance(Mat grayImage, double ratioMin, double ratioMax) {
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                if (pixels[0] > ratioMin && pixels[0] < ratioMax) {
                    if (ratioMax > ratioMin) {
                        pixelsBuffer[0] = pixels[0];
                    }
                }
                result.put(y, x, pixelsBuffer);
            }
        }
        return result;
    }

    public void update() {
        double min = sliderMin.getValue() / slidersRate;
        double max = sliderMax.getValue() / slidersRate;
        Imgcodecs.imencode(".bmp", doCutoffLuminance(originalGrayImage, min, max), matOfByte);
        try {
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
        } catch (Exception ignore) {/* nop */}
    }
}