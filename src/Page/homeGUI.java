package Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import User.*;

/**
 * Represents the main home page GUI for users to interact with their account information.
 * This class extends the abstract Page class and includes functionalities to create accounts,
 * navigate through existing accounts, and update account details.
 */
public class homeGUI extends Page {

    private Page homePage; // The primary home page interface
    private JLabel accountInfo; // Label to display account details
    private ArrayList<File> accountFiles; // List of account files for the user
    private int currentFileIndex = 0; // Index for navigation through accounts
    private String email; // Email of the user
    private User user; // User object for accessing user data

    /**
     * Constructs a new home GUI.
     *
     * @param user the user object containing user details
     */
    public homeGUI(User user){
        this.user = user;
    }

    /**
     * Opens the home page interface and initializes all GUI components including buttons,
     * labels, and navigation controls. It sets up event listeners for interaction.
     */
    public void openPage() {
        homePage = new Page();
        homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homePage.setSize(1280, 720);
        homePage.setEmail(user);

        // Create account button
        JButton createAccountButton = new JButton("+Create Account");
        createAccountButton.setActionCommand("OPEN_CREATE_ACCOUNT");
        createAccountButton.setPreferredSize(new Dimension(200, 50)); // Set the recommended size for buttons

        // Create event listener
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("OPEN_CREATE_ACCOUNT".equals(e.getActionCommand())) {
                    CreateAccountPage cap = new CreateAccountPage(homePage);
                    cap.openPage();
                    //homePage.setVisible(false);
                    //homePage.dispose();
                }
            }
        };
        createAccountButton.addActionListener(actionListener);

        // Read account file
        File dir = new File("./registerTable/" + user.getEmail() + "/AccountName&Password");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        accountFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                accountFiles.add(file);
            }
        }

        // Create account information labels
        accountInfo = new JLabel();
        accountInfo.setHorizontalAlignment(JLabel.CENTER);
        updateAccountInfo();

        // Create left and right buttons
        JButton leftButton = new JButton(new ImageIcon("src/left.png"));
        JButton rightButton = new JButton(new ImageIcon("src/right.png"));

        // Add button event listener
        leftButton.addActionListener(e -> {
            if (currentFileIndex == 0) {
                currentFileIndex = accountFiles.size() - 1;
            } else {
                currentFileIndex--;
            }
            updateAccountInfo();
        });

        rightButton.addActionListener(e -> {
            if (currentFileIndex == accountFiles.size() - 1) {
                currentFileIndex = 0;
            } else {
                currentFileIndex++;
            }
            updateAccountInfo();
        });

        // Create a panel to place the create account button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(createAccountButton);

        // Add components to the window
        homePage.setLayout(new BorderLayout());
        homePage.add(buttonPanel, BorderLayout.SOUTH);
        homePage.add(leftButton, BorderLayout.WEST);
        homePage.add(rightButton, BorderLayout.EAST);
        homePage.add(accountInfo, BorderLayout.CENTER);


        homePage.setVisible(true);

    }

    /**
     * Updates the account information displayed on the home page based on the current file index.
     * This method reads account details from files and updates the UI accordingly.
     */
    private void updateAccountInfo() {
        if (!accountFiles.isEmpty()) {
            File currentFile = accountFiles.get(currentFileIndex);
            String accountType = currentFile.getName().startsWith("FIX") ? "Saving Account" : "Current Account";
            String accountName = "";
            String accountID = currentFile.getName();

            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                accountName = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String info = "<html><div style='text-align: center;'>" +
                    "<h1>Account Type: " + accountType + "</h1>" +
                    "<h2>Account Name: " + accountName + "</h2>" +
                    "<h3>Account ID: " + accountID + "</h3>" +
                    "</div></html>";
            accountInfo.setText(info);
        }
    }

    /**
     * Refreshes the list of account files from the storage when account details change,
     * such as after creating a new account or updating an existing one.
     */
    public void refreshAccountFiles() {
        File dir = new File("./registerTable/" + user.getEmail() + "/AccountName&Password");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        accountFiles.clear();
        if (files != null) {
            for (File file : files) {
                accountFiles.add(file);
            }
        }
        updateAccountInfo();  // Update displayed account information
    }

    /**
     * Creates a top menu bar with navigation buttons (Home, Task, Profile) on the specified frame.
     *
     * @param frame the JFrame on which the menu bar will be added
     */
    public void createTopMenuBar(JFrame frame) {
        // Create a panel to place buttons
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create button
        JButton homeButton = new JButton("Home");
        JButton taskButton = new JButton("Task");
        JButton profileButton = new JButton("Profile");

        // Add button to panel
        panel.add(homeButton);
        panel.add(taskButton);
        panel.add(profileButton);

        // Add the panel to the north (top) part of the window
        frame.add(panel, BorderLayout.NORTH);
    }

    /**
     * Sets the email of the user associated with this GUI.
     *
     * @param user the user object whose email is to be set
     */
    public void setEmail(User user){
        this.email = user.getEmail();
    }

    /**
     * Gets the email of the user associated with this GUI.
     *
     * @return the email of the user
     */
    public String getEmail(){
        return this.user.getEmail();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
       // homeGUI myWindow = new homeGUI("lmy@qmul.ac.uk");
       // myWindow.openPage();
    }
}