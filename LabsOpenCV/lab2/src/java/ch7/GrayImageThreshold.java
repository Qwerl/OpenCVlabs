package ch7;

import core.ImageCanvas;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GrayImageThreshold extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 200.0;
    private ImageCanvas canvas;
    private JSlider slider;

    public GrayImageThreshold() {
        run();
    }

    public GrayImageThreshold(String path) {
        IMAGE_PATH = path;
        run();
    }

    @Override
    public void run() {
        try {
            originalGrayImage = Highgui.imread(IMAGE_PATH, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            Highgui.imencode(".bmp", originalGrayImage, matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));

            ArrayList<JSlider> sliders = new ArrayList<>();
            slider = new JSlider(MIN_BRIGHTNESS, (int) (MAX_BRIGHTNESS * slidersRate), (int) (1 * slidersRate));
            slider.addChangeListener(e -> update());
            sliders.add(slider);

            canvas = new ImageCanvas("GrayImageThreshold", image, sliders);
            canvas.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mat doThreshold(Mat grayImage, double brightnessThreshold) {
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                if (pixels[0] >= brightnessThreshold) {
                    pixels[0] = MAX_BRIGHTNESS;
                }
                if (pixels[0] < brightnessThreshold) {
                    pixels[0] = MIN_BRIGHTNESS;
                }
                result.put(y, x, pixels);
            }
        }
        return result;
    }

    public void update() {
        double currentBrightnessThreshold = slider.getValue() / slidersRate;
        Highgui.imencode(".bmp", doThreshold(originalGrayImage, currentBrightnessThreshold), matOfByte);
        try {
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
        } catch (Exception ignore) {/* nop */}
    }
}
