package login;

import javax.swing.*;
import java.awt.*;

/**
 If there is no selected button, you can return null or some other appropriate value.
 */
public class LoginForm extends JFrame {

    /**
     * Constructs a new LoginForm. Sets the title, default close operation, size, and layout.
     * Initializes the user interface components and adds them to the frame.
     */
    public LoginForm() {
        setTitle("Login Form"); // Setting the window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720); // Setting the form size
        setLayout(new BorderLayout(10, 10)); // Setting up the Layout Manager
        setLocationRelativeTo(null);

        //  Create and add a background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("/image/loginBackground.jpg");
        setContentPane(backgroundPanel); // Setting the background panel as a content panel
        backgroundPanel.setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        //  Login tab for the North
        JLabel loginLabel = new JLabel("Login", SwingConstants.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(loginLabel, BorderLayout.NORTH);

        // Forms panel in the centre
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 400, 50, 400));
        formPanel.setOpaque(false); // Set to transparent to show background image

        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(600, 60));
        emailField.setBorder(BorderFactory.createTitledBorder("Enter your email"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(600, 60));
        passwordField.setBorder(BorderFactory.createTitledBorder("Enter your password"));

        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        // South button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);

        JButton loginButton = new JButton("Log in");
        loginButton.setBackground(new Color(255, 140, 0)); // orange button
        loginButton.setForeground(Color.GRAY);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(loginButton);//Login button
        loginButton.addActionListener(new LoginHandler(emailField, passwordField, this));

        JButton signUpButton = new JButton("Sign up");
        signUpButton.setForeground(Color.GRAY);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(signUpButton);
        signUpButton.addActionListener(new SignUpHandler());

        add(buttonPanel, BorderLayout.SOUTH);
    }

}
