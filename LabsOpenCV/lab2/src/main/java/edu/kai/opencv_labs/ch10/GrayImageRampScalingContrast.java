package edu.kai.opencv_labs.ch10;

import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.Core;
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

import static org.opencv.core.Core.minMaxLoc;

public class GrayImageRampScalingContrast extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 100.0;
    private ImageCanvas canvas;

    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;
    private JSlider slider4;

    public GrayImageRampScalingContrast() {
        run();
    }

    public GrayImageRampScalingContrast(String path) {
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
            slider1 = new JSlider(MIN_BRIGHTNESS, (int) (MAX_BRIGHTNESS * slidersRate), (int) (1 * slidersRate));
            slider2 = new JSlider(MIN_BRIGHTNESS, (int) (MAX_BRIGHTNESS * slidersRate), (int) (1 * slidersRate));
            slider3 = new JSlider(MIN_BRIGHTNESS, (int) (MAX_BRIGHTNESS * slidersRate), (int) (1 * slidersRate));
            slider4 = new JSlider(MIN_BRIGHTNESS, (int) (MAX_BRIGHTNESS * slidersRate), (int) (1 * slidersRate));
            slider1.addChangeListener(e -> update());
            slider2.addChangeListener(e -> update());
            slider3.addChangeListener(e -> update());
            slider4.addChangeListener(e -> update());
            sliders.put(slider1, "1");
            sliders.put(slider2, "2");
            sliders.put(slider3, "3");
            sliders.put(slider4, "4");

            canvas = new ImageCanvas("GrayImageRampScalingContrast", image, sliders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mat doRampScalingContrast(Mat grayImage, double ratio1, double ratio2, double ratio3, double ratio4) {
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        Core.MinMaxLocResult minMaxLocResult = minMaxLoc(grayImage);
        double min = minMaxLocResult.minVal;
        double max = minMaxLocResult.maxVal;

        double a, b;
        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                if (pixels[0] > ratio2 && pixels[0] < ratio1) {
                    a = (ratio1 - ratio2) / (max - min);
                    b = ((ratio2 * max) - (ratio1 * min)) / (max - min);
                    pixels[0] = (a * pixels[0] + b);
                }
                if (pixels[0] > ratio4 && pixels[0] < ratio3) {
                    a = (ratio3 - ratio4) / (max - min);
                    b = ((ratio4 * max) - (ratio3 * min)) / (max - min);
                    pixels[0] = (a * pixels[0] + b);
                }
                if ((pixels[0] < ratio2 || pixels[0] > ratio1) && (pixels[0] < ratio4 || pixels[0] > ratio3)) {
                    pixels[0] = 0;
                }
                result.put(y, x, pixels);
            }
        }
        return result;
    }

    public void update() {
        double ratio1 = slider1.getValue() / slidersRate;
        double ratio2 = slider2.getValue() / slidersRate;
        double ratio3 = slider3.getValue() / slidersRate;
        double ratio4 = slider4.getValue() / slidersRate;
        Imgcodecs.imencode(".jpg", doRampScalingContrast(originalGrayImage, ratio1, ratio2, ratio3, ratio4), matOfByte);
        try {
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
        } catch (Exception ignore) {/* nop */}
    }
}