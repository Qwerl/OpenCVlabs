package edu.kai.opencv_labs.core;

import org.opencv.core.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ImageCanvas {

    private JFrame frame;
    private JPanel panel;
    private JLabel imageLabel;
    private Image image;

    public ImageCanvas(String title, BufferedImage image, Map<JSlider, String> sliders) {
        this.image = image;

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        sliders.forEach((slider, label) -> addSliderWithLabel(slider, label));

        imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

        frame.add(panel, BorderLayout.NORTH);
        frame.add(imageLabel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void addSliderWithLabel(JSlider slider, String labelText) {
        JPanel sliderWithLabel = new JPanel(new GridLayout(0, 1));

        JLabel label = new JLabel(labelText);
        label.setSize(new Dimension(50, 20));
        sliderWithLabel.add(label);

        slider.setMinorTickSpacing(slider.getMaximum() / 50);
        slider.setMajorTickSpacing(slider.getMaximum() / 5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        Hashtable dictionary = new Hashtable<String, String>();
        for (int i = 0; i <= slider.getMaximum(); i = i + (slider.getMaximum()) / 5) {
            dictionary.put(i, new JLabel((i / 100.0) + ""));
        }
        slider.setLabelTable(dictionary);

        sliderWithLabel.add(slider);

        panel.add(sliderWithLabel);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Metal".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    File input = new File("img/1.bmp");
                    BufferedImage image = ImageIO.read(input);
                    HashMap<JSlider, String> sliders = new HashMap<>();
                    sliders.put(new JSlider(0, 100, 20), "hello1");
                    sliders.put(new JSlider(0, 100, 70), "hello2");

                    new ImageCanvas("hello", image, sliders);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setIcon(Image image) {
        Icon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
    }

}
