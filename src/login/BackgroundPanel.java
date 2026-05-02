package login;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

/**
 * The {@code BackgroundPanel} class extends {@code JPanel} to create a panel that displays a background image.
 * It loads the image from the specified path and renders it as the background of the panel.
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    /**
     * Constructs a new BackgroundPanel instance, which loads and displays an image from the specified path as the background.
     *
     * @param imagePath the path to the image, should be a class path resource
     * @throws IOException if the image cannot be loaded, an IOException is thrown.
     */
// Receive the path to the image in the constructor
    public BackgroundPanel(String imagePath) {
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            } else {
                System.err.println("Resource not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
//        }
    }
}
