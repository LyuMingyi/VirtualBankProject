package Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

import AccountType.*;
import Handler.NotificationHandler;
import Handler.ParentProfileHandler;
import Handler.SetTaskHandler;
import User.*;
import login.*;

/**
 * Represents the home page GUI for parent users, facilitating interactions with account information
 * and their children's accounts. This class extends the abstract Page class and includes functionalities
 * to navigate through account details, set tasks for children, view notifications, and manage parent profiles.
 */
public class homePageForParent extends Page {

    private Page homePage; // The primary JFrame for the GUI
    private JLabel accountInfo; // Label for displaying account details
    private ArrayList<File> accountFiles; // List of account files for the user
    private int currentFileIndex = 0; // Index for navigation through account files
    private String email; // Email of the user
    private User user; // User object for accessing user data

    /**
     * Constructs a new home page GUI specifically designed for parents.
     *
     * @param user the parent user object containing user details
     */
    public homePageForParent(User user){
        this.user = user;
    }

    /**
     * Opens the home page interface, initializes GUI components including navigation buttons,
     * and sets up event listeners for user interaction. It also manages the setup of directories
     * for storing user images and account information.
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

        String imageDirectoryPath = "./registerTable/" + email + "/userImage";
        File imageDirectory = new File(imageDirectoryPath);
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs(); // If the directory does not exist, create the directory
        }


        String accountFilePath = "./registerTable/" + user.getEmail() + "/Accountfile/";
        File accountFileDir = new File(accountFilePath);
        if (!accountFileDir.exists() || accountFileDir.listFiles().length == 0) {
            CurrentAccount ca = new CurrentAccount();
            String path = ca.createAccount(user.getEmail());
            String accountNumber = ca.getAccountNumber();
            CurrentAccount ca1 = new CurrentAccount(0.0 , "ParentAccount", "000000", accountNumber);
            ca1.setEmail(user.getEmail());
            Boolean result = ca1.deposit(9999999);
        }

        String accountInfoPath = "./registerTable/" + user.getEmail() + "/AccountName&Password/";
        File accountInfoDir = new File(accountInfoPath);
        if (!accountInfoDir.exists() || accountInfoDir.listFiles().length == 0) {
            File[] accountFiles = accountFileDir.listFiles();
            if (accountFiles != null && accountFiles.length > 0) {
                String accountFileName = accountFiles[0].getName(); // Get the first file name in the Accountfile directory
                File accountInfoFile = new File(accountInfoDir, accountFileName);
                try {
                    if (accountInfoFile.createNewFile()) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountInfoFile))) {
                            writer.write("ParentAccount\n000000");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        // Create top sidebar panels and buttons
        JPanel topMenuBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topMenuBar.setOpaque(false);
        JButton homeButton = new JButton("🏠Home");
        JButton taskButton = new JButton("💪Task");
        JButton notificationButton = new JButton("📢Notification");
        JButton profileButton = new JButton("📭Profile");

        // Add button to top sidebar panel
        topMenuBar.add(homeButton);
        topMenuBar.add(taskButton);
            taskButton.addActionListener(new SetTaskHandler(homePage, homePage));
        topMenuBar.add(notificationButton);
            notificationButton.addActionListener(new NotificationHandler(homePage, homePage));
        topMenuBar.add(profileButton);
            profileButton.addActionListener(new ParentProfileHandler(homePage,homePage));

        glassPane.add(topMenuBar, BorderLayout.NORTH);

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
                    String accountDetails = getAccountDetails(currentFile);
                    DetailPage detailPage = new DetailPage(user, currentFileIndex);
                    detailPage.openPage();
                }
            }
        });

        JButton checkButton = new JButton("Check my kid's account");
        checkButton.setPreferredSize(new Dimension(200, 50));

        /*
       Parents can directly view all accounts of all children, a very important function! pretending to record
         */
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clickTest!");
                // Create window
                JFrame frame = new JFrame("Kid Accounts");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                AccountInfoReader ar = new AccountInfoReader();
                Parent parent = ar.parentLoader(homePage.getEmail());
                ArrayList<String> kidEmails = parent.getKidsList();

                // Create the main panel using BoxLayout
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

                // Loop through kidEmails to get kid information
                for (String kidEmail : kidEmails) {
                    Kid kid = ar.kidLoader(kidEmail);
                    ArrayList<Account> accountsList = ar.accountListLoader(kid.getEmail());

                    // Create a profile panel and add tags
                    JPanel kidPanel = new JPanel();
                    kidPanel.setLayout(new BoxLayout(kidPanel, BoxLayout.Y_AXIS));
                    kidPanel.setBorder(BorderFactory.createTitledBorder(kid.getName()));  // Add borders and titles to each child's panel

                    // Create a list model
                    DefaultListModel<String> listModel = new DefaultListModel<>();
                    for (Account account : accountsList) {
                        listModel.addElement("<html><b>" + kid.getName() + "</b><br/>" + account.getAccountNumber() + "</html>");
                    }

                    // Create a list and add click events
                    JList<String> list = new JList<>(listModel);
                    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    list.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent evt) {
                            if (evt.getClickCount() == 2) {
                                DetailPage detailPage = new DetailPage(kid, list.locationToIndex(evt.getPoint()));
                                detailPage.openPage();
                            }
                        }
                    });

                    // Add scroll panel
                    JScrollPane scrollPane = new JScrollPane(list);
                    kidPanel.add(scrollPane);

                    // Add child panel to main panel
                    mainPanel.add(kidPanel);
                }

                // Add main panel to window
                frame.add(mainPanel, BorderLayout.CENTER);

                // Set window size and center it
                frame.setSize(500, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });




        // Create a panel to place the create account button and details button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(detailButton); // Add details button
        buttonPanel.add(checkButton);
        glassPane.add(buttonPanel, BorderLayout.SOUTH);


        // Add components to the window
       /* homePage.setLayout(new BorderLayout());
        homePage.add(topMenuBar, BorderLayout.NORTH); // Add the top sidebar to the north
        homePage.add(buttonPanel, BorderLayout.SOUTH);
        homePage.add(leftButton, BorderLayout.WEST);
        homePage.add(rightButton, BorderLayout.EAST);
        homePage.add(accountInfo, BorderLayout.CENTER);*/

        homePage.setVisible(true);
    }

    /**
     * Updates the account information displayed on the GUI based on the current file index.
     * Reads account details from files and updates the label accordingly.
     */
    private void updateAccountInfo() {
        if (!accountFiles.isEmpty()) {
            File currentFile = accountFiles.get(currentFileIndex);
            //String accountType = currentFile.getName().startsWith("FIX") ? "Saving Account" : "Current Account";
            String accountType = "ParentAccount";
            String accountName = "ParentAccount";
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
     * Sets the email of the parent user associated with this GUI.
     *
     * @param user the user object whose email is to be set
     */
    public void setEmail(User user){
        this.email = user.getEmail();
    }

    /**
     * Gets the email of the parent user associated with this GUI.
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
        String accountType = "ParentAccount";
        String accountName = "ParentAccount";
        String accountID = accountFile.getName();

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
        Parent parent = ar.parentLoader("TestParent@gmail.com");
        homePageForParent hp = new homePageForParent(parent);
        hp.openPage();
        // myWindow.openPage();
    }
}