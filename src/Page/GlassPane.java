package Page;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Represents a transparent panel with a frosted glass effect that overlays an image.
 * This class extends JPanel and overrides the paintComponent method to achieve the glass effect.
 * It is used to enhance UI aesthetics by providing a customizable background with adjustable opacity.
 */
class GlassPane extends JPanel {
    private BufferedImage background;
    private float opacity;

    /**
     * Constructs a new Glass Pane with a specified image path and opacity level.
     * Loads an image from the provided file path and sets the opacity to control the translucency.
     *
     * @param path    the file path of the image to be used as a background
     * @param opacity the opacity level (0.0 - 1.0) where 1 is completely opaque
     */
    public GlassPane(String path, float opacity) {
        this.opacity = opacity;
        try {
            this.background = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOpaque(false);
    }

    /**
     * Custom painting component for the panel.
     * This method is called whenever the panel needs to be rendered. It paints the background image
     * and applies the opacity to create a frosted glass effect over it.
     *
     * @param g the Graphics object used for drawing operations
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        if (background != null) {
            g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
