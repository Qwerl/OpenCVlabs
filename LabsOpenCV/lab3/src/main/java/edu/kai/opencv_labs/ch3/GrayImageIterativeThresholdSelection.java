package edu.kai.opencv_labs.ch3;

import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;

public class GrayImageIterativeThresholdSelection extends Thread {

    public static String IMAGE_PATH = "img/1111111.bmp";

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 100.0;
    private ImageCanvas canvas;

    public GrayImageIterativeThresholdSelection() {
        this.start();
    }

    public GrayImageIterativeThresholdSelection(String path) {
        IMAGE_PATH = path;
        this.start();
    }

    @Override
    public void run() {
        try {
            originalGrayImage = Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Imgcodecs.imencode(".bmp", originalGrayImage, matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
            Imgcodecs.imencode(".jpg", doFrequencyBinarization(originalGrayImage), matOfByte);
            HashMap sliders = new HashMap();
            canvas = new ImageCanvas("Алгоритм последовательного уточнения", image, sliders);
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mat doFrequencyBinarization(Mat grayImage) {
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        int threshold = getThreshold(grayImage, size);

        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                if (pixels[0] > threshold) {
                    pixels[0] = MAX_BRIGHTNESS;
                } else {
                    pixels[0] = MIN_BRIGHTNESS;
                }
                result.put(y, x, pixels);
            }
        }

        return result;
    }

    private int getThreshold(Mat grayImage, Size size) {
        double epsilon = 0.1;

        int averageOfZero = 0;
        int averageOfOne = 0;
        int oldThreshold = 0;
        int threshold = 0;
        int countZero, countOne;
        int sumBrightnessOfZero, sumBrightnessOfOne;

        int iterationsCount = 0;

        while ((averageOfZero + averageOfOne == 0) || (Math.abs(oldThreshold - threshold) > epsilon)) {
            iterationsCount++;
            countZero = 0; countOne = 0;
            sumBrightnessOfZero = 0; sumBrightnessOfOne = 0;
            for (int y = 0; y < size.height; y++) {
                for (int x = 0; x < size.width; x++) {
                    oldThreshold = threshold;
                    double[] pixels = grayImage.get(y, x);

                    int pixel = (int) pixels[0];

                    if (pixel > threshold) {
                        sumBrightnessOfOne = (sumBrightnessOfOne + pixel);
                        countOne++;
                    } else {
                        sumBrightnessOfZero = (sumBrightnessOfZero + pixel);
                        countZero++;
                    }
                }
            }
            averageOfZero = sumBrightnessOfZero / countZero;
            averageOfOne = sumBrightnessOfOne / countOne;
            threshold = (averageOfZero + averageOfOne) / 2;
            System.out.println("Значение порога на " + iterationsCount + " итерации: " + threshold);
        }

        System.out.println("количество итераций: " + iterationsCount);
        return threshold;
    }

}