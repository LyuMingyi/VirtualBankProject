package login;

import javax.swing.*;
import java.awt.*;

/**
 * Constructs a new MainFrame, initializing the layout, setting the window's title, default close operation, size, and location.
 * It creates and adds a {@code LoginForm} to the card layout panel.
 */
public class SignUpForm extends JFrame {

    /**
     * Constructs a new SignUpForm. It sets the window size, layout, and adds various panels
     * for user input and interaction.
     */
    public SignUpForm() {
        setSize(1280, 720); // Set the window size
        setLayout(new BorderLayout()); // Set the layout manager

        // Form panel initialization and addition of components
        JPanel formPanel = new JPanel(new GridLayout(0, 1));
        add(formPanel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);

        // Account type selection panel and components
        JPanel accountTypePanel = new JPanel(new FlowLayout());
        ButtonGroup accountTypeGroup = new ButtonGroup();
        JRadioButton parentButton = new JRadioButton("Parent");
        JRadioButton kidButton = new JRadioButton("Kid");
        accountTypeGroup.add(parentButton);
        accountTypeGroup.add(kidButton);
        accountTypePanel.add(parentButton);
        accountTypePanel.add(kidButton);
        formPanel.add(accountTypePanel);

        //  Various user input fields and the signup button
        JTextField emailField = new JTextField(15);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        // ID input
        JTextField idField = new JTextField(15);
        formPanel.add(new JLabel("Enter your Id:"));
        formPanel.add(idField);

        // Password Input
        JPasswordField passwordField = new JPasswordField(15);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        // Confirm password entry
        JPasswordField confirmPasswordField = new JPasswordField(15);
        formPanel.add(new JLabel("Confirm password:"));
        formPanel.add(confirmPasswordField);

        // Register button
        JButton signupButton = new JButton("Signup");
        formPanel.add(signupButton);

        signupButton.addActionListener(new CreateAccountHandler(accountTypeGroup,emailField,idField,passwordField,confirmPasswordField,signupButton));
        // Register Button Event Handling

        // Bottom panel to include ‘Already have an account?’ Links
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton loginButton = new JButton("Already have an account?");
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.addActionListener(e -> {
            // This is where the logic to switch back to the login screen is handled.
            // For example, you can create and display a login form instance
            LoginForm loginForm = new LoginForm();
        });
        bottomPanel.add(loginButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
