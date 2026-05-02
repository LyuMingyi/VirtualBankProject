package login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The {@code LoginHandler} class implements {@code ActionListener} to handle the login process
 * in a login form. It retrieves user credentials, performs authentication, and handles the form disposal
 * after successful login.
 */
public class LoginHandler implements ActionListener{
    private JTextField emailField;
    private JTextField passwordField;
    private LoginForm loginForm;

    @Override
    public void actionPerformed(ActionEvent e) {
        AccountInfoReader login = new AccountInfoReader();
        login.authenticate(emailField.getText(), passwordField.getText());
        loginForm.dispose();
    }

    /**
     * Constructs a new LoginHandler with the specified email and password fields, and the login form.
     *
     * @param emailField    the text field for the user's email
     * @param passwordField the password field for the user's password
     * @param loginForm     the login form instance
     */
    public LoginHandler(JTextField emailField,JPasswordField passwordField, LoginForm loginForm){
        this.emailField = emailField;
        this.passwordField = passwordField;
        this.loginForm = loginForm;
    }
}
