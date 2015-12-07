package edu.kai.opencv_labs.ch5;

import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GrayImageAdaptiveThresholdOtsu extends Thread {

    public static String IMAGE_PATH = "img/1111111.bmp";

    private static final int MAX_BRIGHTNESS = 255;
    private static final int MIN_BRIGHTNESS = 0;

    private Mat originalGrayImage = new MatOfByte();
    private MatOfByte matOfByte = new MatOfByte();

    private ImageCanvas canvas;
    private int bufferSize = 10;

    private boolean isWork = false;
    private JSlider sliderBufferSize;

    public GrayImageAdaptiveThresholdOtsu() {
        this.start();
    }

    public GrayImageAdaptiveThresholdOtsu(String path) {
        IMAGE_PATH = path;
        this.start();
    }

    @Override
    public void run() {
        try {
            originalGrayImage = Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Imgcodecs.imencode(".bmp", originalGrayImage, matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
            Imgcodecs.imencode(".jpg", doAdaptiveThreshold(originalGrayImage), matOfByte);
            Map<JSlider, String> sliders = new HashMap<>();
            sliderBufferSize = new JSlider(0, 10000, 2);
            sliderBufferSize.addChangeListener(e -> update());
            sliders.put(sliderBufferSize, "BufferSize");
            canvas = new ImageCanvas("AdaptiveThresholdOtsu", image, sliders);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mat doAdaptiveThreshold(Mat grayImage) {
        isWork = true;
        Mat result = grayImage.clone();
        Size size = grayImage.size();

        ArrayList<Number> pixelList = new ArrayList<>((int) (size.height * size.width));
        ArrayList<Number> resultList = new ArrayList<>((int) (size.height * size.width));

        boolean tickType = true;

        for (int y = 0; y < size.height; y++) {
            if (tickType) {
                //прямой ход
                for (int x = 0; x < size.width; x++) {
                    double[] pixels = grayImage.get(y, x);
                    pixelList.add((int) pixels[0]);
                }
            } else {
                //обратный ход
                for (int x = (int) size.width - 1; x >= 0; x--) {
                    double[] pixels = grayImage.get(y, x);
                    pixelList.add((int) pixels[0]);
                }
            }
            tickType = !tickType;
        }

        //пробегаемся по прямой, усредняем
        for (int i = 0; i < pixelList.size(); i++) {
            int[] buffer = new int[bufferSize];

            //забиваем буффер
            for (int n = i; n > i - bufferSize; n--) {
                if (n <= 0) {
                    buffer[i - n] = (int) pixelList.get(i);
                } else {
                    buffer[i - n] = (int) pixelList.get(i - n);
                }
            }

            //буффер забит, обрабатывает содержимое
            double average;
            double sum = 0;
            for (int aBuffer : buffer) {
                sum += aBuffer;
            }
            average = sum / bufferSize;

            if ((int) pixelList.get(i) > average) {
                resultList.add(MAX_BRIGHTNESS);
            } else {
                resultList.add(MIN_BRIGHTNESS);
            }
        }

        //раскладываем массив обратно в матрицу
        int i = 0;
        tickType = true;
        for (int y = 0; y < size.height; y++) {
            if (tickType) {
                //прямой ход
                for (int x = 0; x < size.width; x++) {
                    double[] pixels = new double[1];
                    pixels[0] = (resultList.get(i)).doubleValue();
                    result.put(y, x, pixels);
                    i++;
                }
            } else {
                //обратный ход
                for (int x = (int) size.width; x > 0; x--) {
                    double[] pixels = new double[1];
                    pixels[0] = (resultList.get(i)).doubleValue();
                    result.put(y, x, pixels);
                    i++;
                }
            }
            tickType = !tickType;
        }
        isWork = false;
        return result;
    }

    public void update() {
        if (!isWork) {
            bufferSize = sliderBufferSize.getValue();
            System.out.println(bufferSize);
            Imgcodecs.imencode(".bmp", doAdaptiveThreshold(originalGrayImage), matOfByte);
            try {
                canvas.setIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
            } catch (Exception ignore) {/* nop */}
            System.out.println(bufferSize + " complete");
        }
    }

}