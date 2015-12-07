package edu.kai.opencv_labs.core;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HistogramFrame {

    private static final int LINE_WIDTH = 1;
    private static final int LINE_WIDTH_BORDER = 2;
    private static final double LINE_HEIGHT_MULTIPLIER = 0.02;
    private static final double LINE_HEIGHT_MULTIPLIER_FOR_PERCENT = LINE_HEIGHT_MULTIPLIER * 8000;

    private String name;
    private int[] histogram;
    private double[] percentHistogram;

    public static int[] getHistogram(Mat mat) {
        int[] histogram = new int[256];
        Size size = mat.size();
        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                double[] pixels = mat.get(y, x);
                int pixel = (int) pixels[0];
                histogram[pixel]++;
            }
        }
        return histogram;
    }

    public static double[] getPercentHistogram(int[] histogram, int size) {
        double[] percentHistogram = new double[256];
        for (int i = 0; i < 256; i++) {
            percentHistogram[i] = ((double) histogram[i] / size) * 100;
        }

        return percentHistogram;
    }

    public HistogramFrame(String name, int[] histogram, int size) {
        this.name = name;
        this.histogram = histogram;
        this.percentHistogram = getPercentHistogram(histogram, size);
        init();
    }

    public HistogramFrame(String name, Mat mat) {
        this(name, getHistogram(mat), (int) mat.size().area());
    }

    private void init() {
        JFrame frame = new JFrame(name);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Squares panel = new Squares();

//        paintHistogram(panel);
        paintPercentHistogram(panel);

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private void paintHistogram(Squares panel) {
        for (int i = 0; i < histogram.length; i++) {
            double histogramSegment = histogram[i];
            panel.addSquare(
                    (LINE_WIDTH + LINE_WIDTH_BORDER) * i + 1,
                    1,
                    LINE_WIDTH,
                    (int) (histogramSegment * LINE_HEIGHT_MULTIPLIER)
            );
        }
    }

    private void paintPercentHistogram(Squares panel) {
        for (int i = 0; i < percentHistogram.length; i++) {
            double histogramSegment = percentHistogram[i];
            panel.addSquare(
                    (LINE_WIDTH + LINE_WIDTH_BORDER) * i + 1,
                    1,
                    LINE_WIDTH,
                    (int) (histogramSegment * LINE_HEIGHT_MULTIPLIER_FOR_PERCENT)
            );
        }
    }


    class Squares extends JPanel {
        private static final int W = 256 * (LINE_WIDTH + LINE_WIDTH_BORDER);
        private int dynamicH;
        public static final int BORDER = 10;

        private List<Rectangle> squares = new ArrayList<>();

        public void addSquare(int x, int y, int width, int height) {
            Rectangle rect = new Rectangle(x, y, width, height);
            if (dynamicH - BORDER < height) {
                dynamicH = height + BORDER;
            }
            squares.add(rect);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(W + BORDER + BORDER, dynamicH + BORDER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            for (int i = 0; i < squares.size(); i++) {
                Rectangle square = squares.get(i);
                g2.setColor(new Color(i,i,i));
                g2.drawRect(
                        square.x + BORDER,
                        dynamicH - square.y - square.height,
                        square.width,
                        square.height
                );
            }
        }

    }

}
