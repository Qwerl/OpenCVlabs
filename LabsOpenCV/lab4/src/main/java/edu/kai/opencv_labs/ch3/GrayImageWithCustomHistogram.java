package edu.kai.opencv_labs.ch3;

import edu.kai.opencv_labs.core.HistogramFrame;
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
import java.util.Random;

import static edu.kai.opencv_labs.core.HistogramFrame.getHistogram;

public class GrayImageWithCustomHistogram extends Thread {
    public static String IMAGE_PATH = "img/11111111.jpg";

    private ImageCanvas canvas;
    private Mat originalGrayImage = new MatOfByte();
    //private Mat
    private MatOfByte matOfByte = new MatOfByte();

    private static final int MAX_BRIGHTNESS = 255;

    public GrayImageWithCustomHistogram() {
        run();
    }

    public GrayImageWithCustomHistogram(String path) {
        IMAGE_PATH = path;
        run();
    }

    @Override
    public void run() {
        try {
            originalGrayImage = Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Imgcodecs.imencode(".bmp", originalGrayImage, matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
            Imgcodecs.imencode(".jpg", doHistogramEqualization(originalGrayImage), matOfByte);
            Map<JSlider, String> sliders = new HashMap<>();
            canvas = new ImageCanvas("GrayImageWithCustomHistogram", image, sliders);
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Mat doHistogramEqualization(Mat mat) {
        // Get the Lookup table for histogram equalization
        new HistogramFrame("Original image histogram", mat);
        int[] histLUT = getCustomHistogram(mat);

        Mat result = mat.clone();
        Size size = mat.size();

        for (int x = 0; x < size.width; x++) {
            for (int y = 0; y < size.height; y++) {

                // Get pixels by R, G, B
                double[] pixels = mat.get(y, x);
                int pixel = (int) pixels[0];

                // Set new pixel values using the histogram lookup table
                pixel = histLUT[pixel];

                // Write pixels into image
                result.put(y, x, pixel);
            }
        }
        new HistogramFrame("Histogram after equalization", result);
        return result;
    }

    private int[] getCustomHistogram(Mat mat) {
        return var5();
    }

    private int[] var1() {
        int[] imageLUT = new int[256];

        int sum = 0;
        for (int i = 0; i < imageLUT.length / 2; i++) {
            imageLUT[i] = imageLUT.length - i * 2;
            sum += imageLUT.length - i;
        }
        for (int i = imageLUT.length / 2; i < imageLUT.length; i++) {
            imageLUT[i] = i * 2 - imageLUT.length;
            sum += imageLUT.length - i;
        }

        new HistogramFrame("Задавакмая гистограмма", imageLUT, sum);
        return imageLUT;
    }

    private int[] var2() {
        int[] imageLUT = new int[256];

        int sum = 0;
        for (int i = 0; i < imageLUT.length / 2; i++) {
            imageLUT[i] = i * 2 - imageLUT.length + MAX_BRIGHTNESS;
            sum += imageLUT.length - i;
        }
        for (int i = imageLUT.length / 2; i < imageLUT.length; i++) {
            imageLUT[i] = imageLUT.length - i * 2 + MAX_BRIGHTNESS;
            sum += imageLUT.length - i;
        }


        new HistogramFrame("Задавакмая гистограмма", imageLUT, sum);
        return imageLUT;
    }

    private int[] var3() {
        int[] imageLUT = new int[256];

        for (int i = 0; i < imageLUT.length; i++) {
            imageLUT[i] = new Random().nextInt(MAX_BRIGHTNESS);
        }

        for (int i = 128 - 63; i < 256 - 128; i++) {
            imageLUT[i] = 0;
        }

        int sum = 0;
        for (int anImageLUT : imageLUT) {
            sum += anImageLUT;
        }
        System.out.println(sum);
        new HistogramFrame("Задавакмая гистограмма", imageLUT, sum);
        return imageLUT;
    }

    private int[] var4(Mat mat) {
        Size size = mat.size();

        // Get an image histogram
        int[] histogram = getHistogram(mat);

        // Create the lookup table
        int[] imageLUT = new int[256];

        long sum = 0;

        // Calculate the scale factor
        float scale_factor = (float) (MAX_BRIGHTNESS / (size.width * size.height));

        for (int i = 0; i < imageLUT.length; i++) {
            sum += histogram[i];
            int val = (int) (sum * scale_factor);
            if (val > MAX_BRIGHTNESS) {
                imageLUT[i] = MAX_BRIGHTNESS;
            } else {
                imageLUT[i] = val;
            }
        }
        for (int i = 128 - 63; i < 256 - 63; i++) {
            imageLUT[i] = 0;
        }
        sum = 0;
        for (int anImageLUT : imageLUT) {
            sum += anImageLUT;
        }

        new HistogramFrame("Задавакмая гистограмма", imageLUT, (int) sum);
        return imageLUT;
    }

    private int[] var5() {
        int[] imageLUT = new int[256];

        for (int i = 0; i < imageLUT.length; i++) {
            imageLUT[i] = 0;
        }

        for (int i = 64+31, j = 0; i < imageLUT.length; i++, j++) {
            int histSeg = j * 3;
            if (histSeg > MAX_BRIGHTNESS) {
                imageLUT[i] = MAX_BRIGHTNESS;
            } else {
                imageLUT[i] = histSeg;
            }
        }

        int sum = 0;
        for (int anImageLUT : imageLUT) {
            sum += anImageLUT;
        }
        System.out.println(sum);
        new HistogramFrame("Задавакмая гистограмма", imageLUT, sum);
        return imageLUT;
    }

}
