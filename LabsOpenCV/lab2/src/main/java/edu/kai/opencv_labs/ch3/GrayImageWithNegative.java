package edu.kai.opencv_labs.ch3;

import edu.kai.opencv_labs.core.ImageCanvas;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

public class GrayImageWithNegative extends Thread {

    public static String IMAGE_PATH = "img/1.bmp";

    public GrayImageWithNegative() {
        run();
    }

    public GrayImageWithNegative(String path) {
        IMAGE_PATH = path;
        run();
    }

    @Override
    public void run() {
        try {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".bmp", doNegative(Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE)), matOfByte);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
            ImageCanvas canvas = new ImageCanvas("GrayImageWithNegative", image, new HashMap<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mat doNegative(Mat grayImage) {
        Mat invertColorMatrix = new Mat(grayImage.rows(), grayImage.cols(), grayImage.type(), new Scalar(255, 255, 255));
        Mat result = grayImage.clone();
        Core.subtract(invertColorMatrix, grayImage, result);
        return result;
    }
}
