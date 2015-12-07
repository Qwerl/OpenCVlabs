package edu.kai.opencv_labs.ch2;

import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;

import static edu.kai.opencv_labs.core.HistogramFrame.getHistogram;

public class GrayImageProbabilityDensityBrightness extends Thread {

    public static String IMAGE_PATH = "img/1111111.bmp";

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 100.0;
    private ImageCanvas canvas;

    public GrayImageProbabilityDensityBrightness() {
        this.start();
    }

    public GrayImageProbabilityDensityBrightness(String path) {
        IMAGE_PATH = path;
        this.start();
    }

    @Override
    public void run() {
        try {
            originalGrayImage = Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Imgcodecs.imencode(".bmp", originalGrayImage, matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
            Imgcodecs.imencode(".jpg", doBinarization(originalGrayImage), matOfByte);
            HashMap sliders = new HashMap();
            canvas = new ImageCanvas("Анализ плотности распределения вероятности яркости", image, sliders);
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Считаем гистограмму,
     * находим самую частовстречающуюся яркость,
     * по ней разделяем на MAX_BRIGHTNESS/MIN_BRIGHTNESS
     */
    public Mat doBinarization(Mat grayImage) {
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        int[] histograms = getHistogram(grayImage);

        int max = 0;
        int min = 255;
        int maxIndex = 0;
        int minIndex = 0;
        //ищем максимально встречающуюся яркость
        for (int i = 0; i < histograms.length; i++) {
            int histogram = histograms[i];
            if (max < histogram) {
                max = histogram;
                maxIndex = i;
            }
            if (min > histogram) {
                min = histogram;
                minIndex = i;
            }
        }

        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = grayImage.get(y, x);
                if (pixels[0] > maxIndex) {
                    pixels[0] = MAX_BRIGHTNESS;
                } else {
                    pixels[0] = MIN_BRIGHTNESS;
                }
                result.put(y, x, pixels);
            }
        }

        return result;
    }

    public void update() {
        try {
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
        } catch (Exception ignore) {/* nop */}
    }
}