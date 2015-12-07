package edu.kai.opencv_labs.core;

import edu.kai.opencv_labs.ch2.GrayImageProbabilityDensityBrightness;
import edu.kai.opencv_labs.ch3.GrayImageIterativeThresholdSelection;
import edu.kai.opencv_labs.ch4.GrayImageOtsu;
import edu.kai.opencv_labs.ch5.GrayImageAdaptiveThresholdOtsu;
import org.opencv.core.Core;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }

        GrayImageProbabilityDensityBrightness probabilityDensityBrightness = new GrayImageProbabilityDensityBrightness();
        GrayImageIterativeThresholdSelection thresholdSelection = new GrayImageIterativeThresholdSelection();
        GrayImageOtsu otsu = new GrayImageOtsu();
        GrayImageAdaptiveThresholdOtsu adaptiveThresholdOtsu = new GrayImageAdaptiveThresholdOtsu();
    }
}
