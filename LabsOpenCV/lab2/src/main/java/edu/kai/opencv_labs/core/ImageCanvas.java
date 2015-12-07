package edu.kai.opencv_labs.core;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
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

        sliders.forEach(this::addSliderWithLabel);

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

    public void setIcon(Image image) {
        Icon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
    }

}
