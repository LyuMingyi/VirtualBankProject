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

import AccountType.*;
import Handler.GetTaskHandler;
import Handler.GoalHandler;
import Handler.KidProfileHandler;
import User.*;
import login.*;
import login.BackgroundPanel;

/**
 * Represents the home page GUI for kid users, facilitating interactions with account information.
 * This class extends the abstract Page class and includes functionalities to navigate through account details,
 * create new accounts, and access different functionalities such as tasks, profiles, and goals.
 */
public class homePageForKid extends Page {

    private Page homePage; // The primary JFrame for the GUI
    private JLabel accountInfo; // Label for displaying account details
    private ArrayList<File> accountFiles; // List of account files for the user
    private int currentFileIndex = 0; // Index for navigation through account files
    private String email; // Email of the user
    private User user; // User object for accessing user data

    /**
     * Constructs a new home page GUI specifically designed for kids.
     *
     * @param user the kid user object containing user details
     */
    public homePageForKid(User user){
        this.user = user;
    }

    /**
     * Opens the home page interface, initializes GUI components including navigation buttons,
     * and sets up event listeners for user interaction. It also sets up a background panel with a translucent glass pane.
     */
    public void openPage() {

        homePage = new Page();
        homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homePage.setSize(1280, 720);
        homePage.setEmail(user);
        homePage.setLocationRelativeTo(null); // Center the window
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        homePage.setContentPane(backgroundPanel);
        GlassPane glassPane = new GlassPane("src/image/accountBackground.jpg", 0.5f);
        glassPane.setLayout(new BorderLayout());
        backgroundPanel.add(glassPane, BorderLayout.CENTER);


        // Create top sidebar panels and buttons
        JPanel topMenuBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topMenuBar.setOpaque(false);
        JButton homeButton = new JButton("🏠 Home");
        JButton taskButton = new JButton("💪 Task");
        taskButton.addActionListener(new GetTaskHandler(homePage, homePage));
        JButton profileButton = new JButton("📭 Profile");
        profileButton.addActionListener(new KidProfileHandler(homePage, homePage));
        JButton goalButton = new JButton("🎯 Goal");
        goalButton.addActionListener(new GoalHandler(homePage, homePage));

        // Add button to top sidebar panel
        topMenuBar.add(homeButton);
        topMenuBar.add(taskButton);
        topMenuBar.add(goalButton);
        topMenuBar.add(profileButton);
        glassPane.add(topMenuBar, BorderLayout.NORTH);
        // Create account button
        JButton createAccountButton = new JButton("+Create Account");
        createAccountButton.setActionCommand("OPEN_CREATE_ACCOUNT");
        createAccountButton.setPreferredSize(new Dimension(200, 50)); // Set the recommended size for buttons
        glassPane.add(createAccountButton, BorderLayout.SOUTH);
        // Create event listener
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("OPEN_CREATE_ACCOUNT".equals(e.getActionCommand())) {
                    CreateAccountPage cap = new CreateAccountPage(homePage);
                    cap.openPage();
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
        glassPane.add(accountInfo, BorderLayout.CENTER);
        JButton detailButton = new JButton("Detail");
        detailButton.setPreferredSize(new Dimension(200, 50)); // Set the recommended size for buttons
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!accountFiles.isEmpty()) {
                    File currentFile = accountFiles.get(currentFileIndex);
                    //
                    AccountInfoReader ar = new AccountInfoReader();
                    ArrayList<Account> accountsList = ar.accountListLoader(homePage.getEmail());
                    System.out.println(accountsList.get(currentFileIndex).getBalance());
                    //
                    String accountDetails = getAccountDetails(currentFile);
                    DetailPage detailPage = new DetailPage(user, currentFileIndex);
                    detailPage.openPage();
                }
            }
        });

        // Create left and right buttons
        JButton leftButton = new JButton(new ImageIcon("src/left.png"));
        JButton rightButton = new JButton(new ImageIcon("src/right.png"));
        leftButton.setOpaque(false);
        leftButton.setContentAreaFilled(false);
        leftButton.setBorderPainted(false);

        rightButton.setOpaque(false);
        rightButton.setContentAreaFilled(false);
        rightButton.setBorderPainted(false);
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

        // Create a panel to place the create account button and details button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(createAccountButton);
        buttonPanel.add(detailButton); // Add details button
        glassPane.add(buttonPanel, BorderLayout.SOUTH);
        // Add components to the window
        //backgroundPanel.add(topMenuBar, BorderLayout.NORTH); // Add the top sidebar to the north
        //backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        glassPane.add(leftButton, BorderLayout.WEST);
        glassPane.add(rightButton, BorderLayout.EAST);
        //backgroundPanel.add(accountInfo, BorderLayout.CENTER);

        //homePage.setContentPane(backgroundPanel);
        homePage.setVisible(true);
    }

    /**
     * Updates the account information displayed on the GUI based on the current file index.
     * Reads account details from files and updates the label accordingly.
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
     * such as after creating a new account or updating an existing one. It updates the display of account information.
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
     * Sets the email of the kid user associated with this GUI.
     *
     * @param user the user object whose email is to be set
     */
    public void setEmail(User user){
        this.email = user.getEmail();
    }

    /**
     * Gets the email of the kid user associated with this GUI.
     *
     * @return the email of the user
     */
    public String getEmail(){
        return this.user.getEmail();
    }

    /**
     * Retrieves the detailed account information for a given account file.
     * The details include account type, account name, and account ID.
     *
     * @param accountFile the file containing the account details
     * @return a formatted HTML string containing detailed account information
     */
    private String getAccountDetails(File accountFile) {
        String accountType = accountFile.getName().startsWith("FIX") ? "Saving Account" : "Current Account";
        String accountName = "";
        String accountID = accountFile.getName();
        /*AccountInfoReader ar = new AccountInfoReader();
        ArrayList<Account> accountsList = ar.accountListLoader(homePage.getEmail());
        int i = 0;
        while(true){
            if((accountsList.get(i).getAccountName() + ".txt").equals(accountID))){
                System.out.println(accountsList.get(i).getBalance());
                break;
            }
            i++;
        }
*/
        try (BufferedReader reader = new BufferedReader(new FileReader(accountFile))) {
            accountName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "<html><div style='text-align: center;'>" +
                "<h1>Account Type: " + accountType + "</h1>" +
                "<h2>Account Name: " + accountName + "</h2>" +
                "<h3>Account ID: " + accountID + "</h3>" +
                "</div></html>";
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        AccountInfoReader ar = new AccountInfoReader();
        Kid kid = ar.kidLoader("testKid@163.com");
        homePageForKid hp = new homePageForKid(kid);
        hp.openPage();
        // myWindow.openPage();
    }
}
