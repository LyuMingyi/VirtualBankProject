package login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The {@code MainFrame} class extends {@code JFrame} to create a main application window that uses {@code CardLayout} to manage different panels.
 * This implementation specifically adds a {@code LoginForm} as one of the panels that can be displayed.
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    /**
     * Constructs a new MainFrame, initializing the layout, setting the window's title, default close operation, size, and location.
     * It creates and adds a {@code LoginForm} to the card layout panel.
     */
    public MainFrame() {
        setTitle("Bank");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        LoginForm loginForm = new LoginForm();
        cardPanel.add(loginForm, "Login");
        add(cardPanel);
        setVisible(true);
    }
}
