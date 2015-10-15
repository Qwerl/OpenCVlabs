package java.core;

import org.opencv.core.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageCanvas extends JFrame {

    private JPanel panel;
    private JLabel label;
    private Image image;


    public ImageCanvas(String title, BufferedImage image, List<JSlider> sliders) {

        this.image = image;

        setTitle(title);

        Dimension size = new Dimension(image.getWidth(), image.getHeight());
        setSize(image.getWidth() + 40, image.getHeight() + 100);

        panel = new JPanel();
        setContentPane(panel);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));


        panel.setBackground(Color.blue);

        label = new JLabel();
        label.setIcon(new ImageIcon(image));
        label.setSize(size);

        sliders.forEach((JSlider slider) -> panel.add(slider));

        panel.add(label);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
                    ArrayList<JSlider> sliders = new ArrayList<>();
                    sliders.add(new JSlider(0, 100, 20));
                    sliders.add(new JSlider(0, 100, 70));
                    ImageCanvas canvas = new ImageCanvas("hello", image, sliders);
                    canvas.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setIcon(Image image) {
        Icon icon = new ImageIcon(image);
        label.setIcon(icon);
    }

}
