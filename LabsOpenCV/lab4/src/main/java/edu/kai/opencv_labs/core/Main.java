package edu.kai.opencv_labs.core;

import edu.kai.opencv_labs.ch2.GrayImageHistogramEqualization;
import edu.kai.opencv_labs.ch3.GrayImageWithCustomHistogram;
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

        GrayImageHistogramEqualization equalization = new GrayImageHistogramEqualization();
        GrayImageWithCustomHistogram customHistogram = new GrayImageWithCustomHistogram();
  }
}
