package edu.kai.opencv_labs.core;

import edu.kai.opencv_labs.ch1.GrayImage;
import edu.kai.opencv_labs.ch2.GrayImageWithContrast;
import edu.kai.opencv_labs.ch1.OriginalImage;
import edu.kai.opencv_labs.ch10.GrayImageRampScalingContrast;
import edu.kai.opencv_labs.ch3.GrayImageWithNegative;
import edu.kai.opencv_labs.ch4.GrayImageLog;
import edu.kai.opencv_labs.ch5.GrayImagePow;
import edu.kai.opencv_labs.ch6.GrayImagePiecewiseLinear;
import edu.kai.opencv_labs.ch7.GrayImageThreshold;
import edu.kai.opencv_labs.ch8.GrayImageCutoffLuminance;
import edu.kai.opencv_labs.ch9.GrayImageContrastZoom;
import org.opencv.core.Core;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }

        OriginalImage originalImage = new OriginalImage();
        GrayImage grayImage = new GrayImage();
        GrayImageWithContrast grayImageWithContrast = new GrayImageWithContrast();
        GrayImageWithNegative grayImageWithNegative = new GrayImageWithNegative();
        GrayImageLog grayImageLog = new GrayImageLog();
        GrayImagePow grayImagePow = new GrayImagePow();
        GrayImagePiecewiseLinear grayImagePiecewiseLinear = new GrayImagePiecewiseLinear();
        GrayImageThreshold grayImageThreshold = new GrayImageThreshold();
        GrayImageCutoffLuminance grayImageCutoffLuminance = new GrayImageCutoffLuminance();
        GrayImageContrastZoom grayImageContrastZoom = new GrayImageContrastZoom();
        GrayImageRampScalingContrast grayImageRampScalingContrast = new GrayImageRampScalingContrast();
    }
}
