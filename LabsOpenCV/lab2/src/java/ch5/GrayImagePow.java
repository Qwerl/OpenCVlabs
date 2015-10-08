package ch5;

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
            originalGrayImage = Highgui.imread(IMAGE_PATH, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            Highgui.imencode(".jpg", originalGrayImage, matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));

            ArrayList<JSlider> sliders = new ArrayList<>();
            slider = new JSlider(0, (int) (POW_MAX_RATIO * slidersRate), (int) (1 * slidersRate));
            slider.addChangeListener(e -> update());
            sliders.add(slider);

            canvas = new ImageCanvas("GrayImagePow", image, sliders);
            canvas.setVisible(true);
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
        Highgui.imencode(".bmp", doPow(originalGrayImage, currentContrastRatio), matOfByte);
        try {
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
        } catch (Exception ignore) {/* nop */}
    }
}
