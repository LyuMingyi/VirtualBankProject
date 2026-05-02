package login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/**
 * The {@code BackgroundPanel} class extends {@code JPanel} to create a panel that displays a background image.
 * It loads the image from the specified path and renders it as the background of the panel.
 */
public class CreateAccountHandler implements ActionListener {
    private ButtonGroup accountTypeGroup;
    private JTextField emailField;
    private JTextField idField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signupButton;

    /**
     * Constructs a new CreateAccountHandler with the necessary fields and button.
     *
     * @param accountTypeGroup     the button group for selecting the account type
     * @param emailField           the text field for the email
     * @param idField              the text field for the ID
     * @param passwordField        the password field
     * @param confirmPasswordField the confirm password field
     * @param signupButton         the sign up button
     */
    public CreateAccountHandler(ButtonGroup accountTypeGroup, JTextField nameField, JTextField idField, JPasswordField passwordField, JPasswordField confirmPasswordField,JButton signupButton) {
        this.accountTypeGroup = accountTypeGroup;
        this.emailField = nameField;
        this.idField = idField;
        this.passwordField = passwordField;
        this.confirmPasswordField = confirmPasswordField;
        this.signupButton = signupButton;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String email = emailField.getText();
        String id = idField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String group  = getSelectedButtonText(accountTypeGroup);
        if (password.equals(confirmPassword)) {
            // Registration Logic
            AccountInfoWriter accountInfoWriter = new AccountInfoWriter();
            if(!accountInfoWriter.examDuplicate(email)){
                if(accountInfoWriter.writeAccountInfo(email, id, password,group)){
                    signupButton.setText("signup successfully");
                    // Get the button that triggered the event
                    JButton button = (JButton) e.getSource();
                    // Get the top-level window where the button is located
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(button);
                    // Close window
                    topFrame.dispose();
                }else{
                    signupButton.setText("invalid input");
                }
            }else {
                signupButton.setText("you already have an account");
            }


        } else {
            // Password mismatch
        }

    }

    /**
     * Retrieves the text of the selected button within a ButtonGroup.
     *
     * @param buttonGroup the group of buttons
     * @return the text of the selected button, or null if no button is selected
     */
// This method iterates over all the buttons in the ButtonGroup and returns the text of the selected button.
    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        // If there is no selected button, you can return null or some other appropriate value.
        return null;
    }

}
