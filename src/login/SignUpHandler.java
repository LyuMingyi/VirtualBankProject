package login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 // This is where the logic to switch back to the login screen is handled.
 // For example, you can create and display a login form instance
 */
public class SignUpHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        SignUpForm signupForm = new SignUpForm(); // Example of creating a registration screen
        signupForm.setVisible(true); // Display the registration screen
        System.out.println("SignUpHandler");
    }

}
