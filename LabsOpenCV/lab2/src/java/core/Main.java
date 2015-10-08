package core;

import ch1.GrayImage;
import ch2.GrayImageWithContrast;
import ch1.OriginalImage;
import ch10.GrayImageRampScalingContrast;
import ch3.GrayImageWithNegative;
import ch4.GrayImageLog;
import ch5.GrayImagePow;
import ch6.GrayImagePiecewiseLinear;
import ch7.GrayImageThreshold;
import ch8.GrayImageCutoffLuminance;
import ch9.GrayImageContrastZoom;
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
