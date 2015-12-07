package edu.kai.opencv_labs.ch4;

import edu.kai.opencv_labs.core.HistogramFrame;
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

public class GrayImageOtsu extends Thread {

    public static String IMAGE_PATH = "img/111111.jpg";

    private final int MAX_BRIGHTNESS = 255;
    private final int MIN_BRIGHTNESS = 0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private double slidersRate = 100.0;
    private ImageCanvas canvas;

    public GrayImageOtsu() {
        this.start();
    }

    public GrayImageOtsu(String path) {
        IMAGE_PATH = path;
        this.start();
    }

    @Override
    public void run() {
        try {
            originalGrayImage = Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Imgcodecs.imencode(".bmp", originalGrayImage, matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
            Imgcodecs.imencode(".jpg", doOtsuBinarization(originalGrayImage), matOfByte);
            HashMap sliders = new HashMap();
            canvas = new ImageCanvas("Оцу", image, sliders);
            canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mat doOtsuBinarization(Mat grayImage) {
        Mat result = grayImage.clone();

        int[] histogram = getHistogram(grayImage);
        Size size = grayImage.size();
        int area = (int) size.area();
        new HistogramFrame("OTSU Histogram", histogram, area);
        int threshold = getThreshold(histogram, area);

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

    private int getThreshold(int[] histogram, int size) {
        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }
        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) {
                continue;
            }
            wF = size - wB;

            if (wF == 0) {
                break;
            }

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;
    }

}