package Page;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

/**
 * The {@code BackgroundPanel} class extends {@code JPanel} to support backgrounds with images.
 * It allows for the creation of a panel that paints an image as its background.
 *
 * Usage example:
 * <pre>
 * BackgroundPanel panel = new BackgroundPanel("/path/to/image.png");
 * frame.add(panel);
 * </pre>
 */
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    /**
     * Constructs a new {@code BackgroundPanel} with the specified image path.
     * This constructor loads the image from the provided path and prepares it to be used as a background.
     * If the image cannot be loaded, an error message is printed.
     *
     * @param imagePath The relative path to the image file.
     */
// Receive the image path in the constructor
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

    /**
     * Paints the background image onto the panel.
     * This method is called automatically by the Swing framework when the panel needs to be rendered.
     * It scales the image to fit the current size of the panel.
     *
     * @param g The {@code Graphics} object used for drawing the image.
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}