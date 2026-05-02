package Page;

import AccountType.Account;
import Handler.GetTaskHandler;
import Handler.KidHomeHandler;
import Handler.KidProfileHandler;
import login.AccountInfoReader;
import User.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Goal page.
 */
public class GoalPage extends Page {
    private Page homepage;
    private Page currentpage;
    private JPanel goalPanel;
    private AccountInfoReader ar = new AccountInfoReader();

    /**
     * Constructs a new Goal Page.
     *
     * @param homepage    the main home page used for navigation
     * @param currentpage the current page from where the goal page is accessed
     */
    public GoalPage(Page homepage, Page currentpage) {
        this.homepage = homepage;
        this.currentpage = currentpage;
        this.goalPanel = new JPanel();
        this.goalPanel.setLayout(new BoxLayout(goalPanel, BoxLayout.Y_AXIS));
        this.goalPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        this.goalPanel.setOpaque(false); // Set panel transparent
    }

    /**
     * Opens the goal page with all UI components configured.
     * Initializes the frame, sets up the background panel with a frosted glass effect, configures the navigation bar,
     * and loads existing goals. Also provides a button to create new saving goals.
     */
    public void openPage() {
        Page frame = new Page();
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        frame.setContentPane(backgroundPanel);
        // Set the background panel with image
        GlassPane glassPane = new GlassPane("src/image/notificationBackground.jpg", 0.5f);
        glassPane.setLayout(new BorderLayout());
        backgroundPanel.add(glassPane, BorderLayout.CENTER);

        JPanel topMenuBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topMenuBar.setOpaque(false); // Set top menu bar transparent
        JButton homeButton = new JButton("🏠 Home");
        homeButton.addActionListener(new KidHomeHandler(homepage, frame));
        JButton taskButton = new JButton("💪 Task");
        taskButton.addActionListener(new GetTaskHandler(homepage, frame));
        JButton goalButton = new JButton("🎯 Goal");
        JButton profileButton = new JButton("📭 Profile");
        profileButton.addActionListener(new KidProfileHandler(homepage, frame));

        topMenuBar.add(homeButton);
        topMenuBar.add(taskButton);
        topMenuBar.add(goalButton);
        topMenuBar.add(profileButton);
        backgroundPanel.add(topMenuBar, BorderLayout.NORTH);
        glassPane.add(topMenuBar, BorderLayout.NORTH);
        loadGoals();

        JScrollPane scrollPane = new JScrollPane(goalPanel);
        scrollPane.setOpaque(false); // Make scroll pane transparent
        scrollPane.getViewport().setOpaque(false); // Make viewport transparent
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the list
        glassPane.add(scrollPane, BorderLayout.CENTER);

        JButton createGoalButton = new JButton("Create a Saving Goal");
        createGoalButton.addActionListener(e -> createSavingGoal());
        glassPane.add(createGoalButton, BorderLayout.SOUTH);

        ensureGoalFileExists();


        frame.setVisible(true);
    }

    /**
     * Loads the goals from a file and displays them in the goal panel.
     * Each goal is presented with current progress and a delete option.
     */
    private void loadGoals() {
        goalPanel.removeAll();
        ArrayList<Account> accountList = ar.accountListLoader(homepage.getEmail());
        String filePath = "./registerTable/" + homepage.getEmail() + "/goal.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JPanel goalItemPanel = new JPanel();
                    goalItemPanel.setLayout(new BoxLayout(goalItemPanel, BoxLayout.Y_AXIS));
                    goalItemPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

                    String[] parts = line.split(",");
                    String accountNumber = parts[0].split(":")[1].trim();
                    double goalAmount = Double.parseDouble(parts[1].split(":")[1].trim());
                    Account account = findAccountByNumber(accountList, accountNumber);
                    if (account != null) {
                        double currentBalance = account.getBalance();
                        goalItemPanel.add(new JLabel("AccountNumber: " + accountNumber));
                        JLabel goalLabel = new JLabel("Saving Goal: " + currentBalance + " / " + goalAmount);
                        goalLabel.setFont(goalLabel.getFont().deriveFont(Font.BOLD));
                        goalItemPanel.add(goalLabel);
                        JProgressBar progressBar = new JProgressBar(0, (int) goalAmount);
                        progressBar.setValue((int) currentBalance);
                        goalItemPanel.add(progressBar);
                        if (currentBalance >= goalAmount) {
                            goalItemPanel.add(new JLabel("Congratulations! You have reached your goal!"));
                        } else {
                            goalItemPanel.add(new JLabel("You need " + (goalAmount - currentBalance) + " more to reach your goal."));
                        }

                        JButton deleteButton = new JButton("Delete");
                        String finalLine = line;
                        deleteButton.addActionListener(e -> {
                            deleteGoal(filePath, finalLine);
                            goalPanel.remove(goalItemPanel);
                            goalPanel.revalidate();
                            goalPanel.repaint();
                        });
                        goalItemPanel.add(deleteButton);

                        goalPanel.add(goalItemPanel);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        goalPanel.revalidate();
        goalPanel.repaint();
    }

    /**
     * Deletes a specific goal from the file and updates the UI.
     *
     * @param filePath     the path to the file containing the goals
     * @param lineToRemove the specific line (goal) to be removed from the file
     */
    private void deleteGoal(String filePath, String lineToRemove) {
        File file = new File(filePath);
        List<String> out;
        try {
            out = Files.lines(file.toPath())
                    .filter(line -> !line.trim().equals(lineToRemove.trim()))
                    .collect(Collectors.toList());
            Files.write(file.toPath(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds an account by its number from a list of accounts.
     *
     * @param accountList   the list of accounts to search through
     * @param accountNumber the account number to find
     * @return the Account if found, otherwise null
     */
    private Account findAccountByNumber(ArrayList<Account> accountList, String accountNumber) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Opens a dialog to create a new saving goal associated with a selected account.
     */
    private void createSavingGoal() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create Saving Goal");
        dialog.setSize(300, 300);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.setLocationRelativeTo(null);

        JLabel accountLabel = new JLabel("Which account would you like to choose?");
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dialog.add(accountLabel);

        JComboBox<String> accountDropdown = new JComboBox<>();
        accountDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        ArrayList<Account> accountList = ar.accountListLoader(homepage.getEmail());
        if (accountList != null) {
            for (Account account : accountList) {
                accountDropdown.addItem(account.getAccountName());
            }
        }
        dialog.add(accountDropdown);

        JLabel goalLabel = new JLabel("Set your goal:");
        goalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dialog.add(goalLabel);

        JTextField goalField = new JTextField(10);
        goalField.setAlignmentX(Component.CENTER_ALIGNMENT);
        dialog.add(goalField);

        JButton createButton = new JButton("Create");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double goal = Double.parseDouble(goalField.getText());
                    int selectedAccountIndex = accountDropdown.getSelectedIndex();
                    String accountNumber = accountList.get(selectedAccountIndex).getAccountNumber();

                    String directoryPath = "./registerTable/" + homepage.getEmail() + "/";
                    File goalFile = new File(directoryPath + "goal.txt");
                    try (FileWriter writer = new FileWriter(goalFile, true)) {
                        writer.append("accountNumber: " + accountNumber + ", goal: " + goal + "\n");
                        writer.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    dialog.dispose();
                    loadGoals(); // Reload the goals to update the UI
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid goal!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitButton.addActionListener(e -> dialog.dispose());

        dialog.add(createButton);
        dialog.add(exitButton);
        dialog.setVisible(true);
    }

    /**
     * Ensures that the necessary directory and file for storing goals exist.
     */
    private void ensureGoalFileExists() {
        String directoryPath = "./registerTable/" + homepage.getEmail() + "/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File goalFile = new File(directoryPath + "goal.txt");
        try {
            goalFile.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
