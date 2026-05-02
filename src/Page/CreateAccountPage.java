package Page;
import AccountType.CurrentAccount;
import AccountType.SavingAccount;
import User.Kid;
import login.AccountInfoReader;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The {@code CreateAccountPage} class extends {@code Page} to provide functionality
 * for creating new bank accounts. It allows users to select account types, enter account details,
 * and create accounts. It manages the transition between the homepage and the account creation page.
 *
 * Usage example:
 * <pre>
 * CreateAccountPage cap = new CreateAccountPage(homepageFrame);
 * cap.openPage();
 * </pre>
 */
public class CreateAccountPage extends Page {

    private Page homepageFrame;
    private Page createAccountFrame;

    /**
     * Constructs a new {@code CreateAccountPage} with a reference to the homepage frame.
     * This reference is used to manage visibility and transitions between the homepage
     * and the create account page.
     *
     * @param homepageFrame the homepage frame, typically the main application window
     */
    public CreateAccountPage(Page homepageFrame) {
        this.homepageFrame = homepageFrame;
    }

    /**
     * Opens and initializes the create account page interface. This method sets up
     * the GUI components necessary for account creation, including input fields for account
     * name and password, account type selection, and navigation buttons. It handles all the
     * logic for creating an account based on user inputs and selected account type.
     */
    public void openPage() {
        createAccountFrame = new Page();
        createAccountFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        JButton backButton = new JButton("<-Back");
        backButton.addActionListener(e -> {
            createAccountFrame.dispose();
            homepageFrame.dispose();
            AccountInfoReader ar = new AccountInfoReader();
            Kid kid = ar.kidLoader(homepageFrame.getEmail());
            homePageForKid hp = new homePageForKid(kid);
            hp.openPage();
            //homepageFrame.repaint();
            //homepageFrame.setVisible(true);

        });

        JLabel accountTypeLabel = new JLabel("Choose account type:");
        String[] accountTypes = {"Saving Account", "Current Account"};
        JComboBox<String> accountTypeComboBox = new JComboBox<>(accountTypes);
        accountTypeComboBox.insertItemAt("Choose account type", 0);
        accountTypeComboBox.setSelectedIndex(0);


        // Add account name label and input box
        JLabel accountNameLabel = new JLabel("Account Name:");
        JTextField accountNameField = new JTextField(20); // Assuming the account name input box is 20 in length


        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(6);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField(6);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            String accountName = accountNameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Verify whether the password is a 6-digit pure integer
            if (!password.matches("\\d{6}") || !confirmPassword.matches("\\d{6}")) {
                JOptionPane.showMessageDialog(createAccountFrame, "The password must be a 6-digit number.");
                return;
            }

            // Verify whether the password entered twice is the same
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(createAccountFrame, "The passwords entered twice are different.");
                return;
            }

            // Check if Saving Account is selected
            if (accountTypeComboBox.getSelectedItem().equals("Saving Account")) {
                // Create FDaccount object
                SavingAccount fdAccount = new SavingAccount();
                fdAccount.setAccountName(accountName);
                fdAccount.setAccountPassword(password);
                // Create an account using methods of the FDaccount object
                String accountFilepath = fdAccount.creatAccount(homepageFrame.getEmail());
                String accountFilename = fdAccount.getAccountNumber();
                // Save the account name and password in the src/AccountName&Password/ directory, and the file name is consistent with the account ID.
                String accountInfoPath = "./registerTable/" + homepageFrame.getEmail() + "/AccountName&Password/";
                try (FileWriter accountInfoWriter = new FileWriter(accountInfoPath + accountFilename)) {
                    accountInfoWriter.write(accountName + "\n" + password);
                    JOptionPane.showMessageDialog(createAccountFrame, "Saving account created successfully!");
                    createAccountFrame.dispose();
                    homepageFrame.setVisible(true);

                    AccountInfoReader ar = new AccountInfoReader();
                    Kid kid = ar.kidLoader(homepageFrame.getEmail());
                    homePageForKid hp = new homePageForKid(kid);
                    hp.openPage();
                    homepageFrame.dispose();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(createAccountFrame, "Account creation failed.");
                }
            }else if(accountTypeComboBox.getSelectedItem().equals("Current Account")){
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setAccountName(accountName);
                currentAccount.setAccountPassword(password);
                String accountFilepath = currentAccount.createAccount(homepageFrame.getEmail());
                String accountFilename = currentAccount.getAccountNumber();
                String accountInfoPath = "./registerTable/" + homepageFrame.getEmail() + "/AccountName&Password/";
                try (FileWriter accountInfoWriter = new FileWriter(accountInfoPath + accountFilename)) {
                    accountInfoWriter.write(accountName + "\n" + password);
                    JOptionPane.showMessageDialog(createAccountFrame, "Current account created successfully!");
                    createAccountFrame.dispose();
                    homepageFrame.setVisible(true);

                    AccountInfoReader ar = new AccountInfoReader();
                    Kid kid = ar.kidLoader(homepageFrame.getEmail());
                    homePageForKid hp = new homePageForKid(kid);
                    hp.openPage();
                    homepageFrame.dispose();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(createAccountFrame, "Account creation failed.");
                }
            }
        });

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        createAccountFrame.add(backButton, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;
        createAccountFrame.add(accountTypeLabel, gbc);
        gbc.gridx = 1;
        createAccountFrame.add(accountTypeComboBox, gbc);

// Set the position of the account name label and input box
        gbc.gridx = 0;
        gbc.gridy = 2;
        createAccountFrame.add(accountNameLabel, gbc);
        gbc.gridx = 1;
        createAccountFrame.add(accountNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        createAccountFrame.add(passwordLabel, gbc);
        gbc.gridx = 1;
        createAccountFrame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        createAccountFrame.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1;
        createAccountFrame.add(confirmPasswordField, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 5;
        createAccountFrame.add(createButton, gbc);

        createAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createAccountFrame.setSize(1280, 720);
        createAccountFrame.setLocationRelativeTo(null);
        createAccountFrame.setVisible(true);
    }
}
