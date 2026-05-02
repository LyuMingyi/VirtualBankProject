package Page;
import User.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import login.*;
import AccountType.*;


/**
 * Represents a detailed view page for a specific user's account.
 * This page allows the user to view account details, and perform actions like deposit,
 * withdraw, transfer, and view transaction history. It supports dynamic interaction based
 * on account type, particularly for savings accounts where interest settings can be adjusted.
 */
public class DetailPage extends Page {
    private User user;
    private JFrame detailFrame;
    private JLabel nameLabel;
    private JLabel balanceLabel;
    private JLabel numberLabel;
    private JPanel buttonPanel;

    private int accountIndex;

    /**
     * Constructs a new detail page for a specified user and account.
     *
     * @param user         the user who owns the account
     * @param accountIndex the index of the account in the user's account list
     */
    public DetailPage(User user, int accountIndex) {
        this.user = user;
        this.accountIndex = accountIndex;
    }

    /**
     * Opens the detail page containing the account information and interaction buttons.
     * It dynamically loads account information such as name, balance, and number from the user's account list.
     * Buttons for deposit, withdraw, transfer, and viewing transaction history are provided.
     * If the account is a savings account, an additional button for setting interest is displayed.
     */
    public void openPage() {
        // Create new window
        detailFrame = new JFrame("Account Details");
        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add account name tag
        AccountInfoReader ar = new AccountInfoReader();
        ArrayList<Account> accountsList = ar.accountListLoader(user.getEmail());
        nameLabel = new JLabel("Account Name: " + accountsList.get(accountIndex).getAccountName());
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        // Add account ID tag
        numberLabel = new JLabel("Account ID: " + accountsList.get(accountIndex).getAccountNumber());
        numberLabel.setHorizontalAlignment(JLabel.CENTER);

        // Add account balance label
        balanceLabel = new JLabel("Account Balance: " + accountsList.get(accountIndex).getBalance());
        balanceLabel.setHorizontalAlignment(JLabel.CENTER);
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD, 20)); // Set font size and bold

        // Add button panel
        buttonPanel = new JPanel(new GridLayout(1, 5));
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton historyButton = new JButton("Transaction history");
        JButton interestButton = new JButton("Set interest");

        // Set button listener
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new window asking the user the amount to deposit
                JFrame depositFrame = new JFrame("Deposit Amount");
                depositFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                depositFrame.setSize(300, 150);
                depositFrame.setLocationRelativeTo(detailFrame); // associated with main window

                // Create labels and text boxes
                JLabel amountLabel = new JLabel("Deposit Amount:");
                JTextField amountField = new JTextField();
                JButton depositButton = new JButton("Deposit");
                JButton exitButton = new JButton("Exit");


                // Set up layout manager
                depositFrame.setLayout(new GridLayout(3, 2));

                // Add components to the window
                depositFrame.add(amountLabel);
                depositFrame.add(amountField);
                depositFrame.add(depositButton);
                depositFrame.add(exitButton);

                // Set button listener
                depositButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the amount entered by the user
                        double amount = Double.parseDouble(amountField.getText());
                        // deposits
                        accountsList.get(accountIndex).deposit(amount);
                        //AccountLog.setDepositLog(user.getEmail(), accountsList.get(accountIndex).getAccountNumber(), amount);
                        // Update account balance label
                        balanceLabel.setText("Account Balance: " + accountsList.get(accountIndex).getBalance());
                        // Close deposit window
                        depositFrame.dispose();
                    }
                });

                exitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Close deposit window
                        depositFrame.dispose();
                    }
                });

                // Show deposit window
                depositFrame.setVisible(true);
            }
        });


        // Set button listener
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new window asking the user for the amount and password they want to withdraw.
                JFrame withdrawFrame = new JFrame("Withdraw Amount");
                withdrawFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                withdrawFrame.setSize(300, 200);
                withdrawFrame.setLocationRelativeTo(detailFrame); // associated with main window

                // Create labels and text boxes
                JLabel amountLabel = new JLabel("Withdraw Amount:");
                JTextField amountField = new JTextField();
                JLabel passwordLabel = new JLabel("Password:");
                JPasswordField passwordField = new JPasswordField();
                JButton withdrawButton = new JButton("Withdraw");
                JButton exitButton = new JButton("Exit");

                // Set up layout manager
                withdrawFrame.setLayout(new GridLayout(4, 2));

                // Add components to the window
                withdrawFrame.add(amountLabel);
                withdrawFrame.add(amountField);
                withdrawFrame.add(passwordLabel);
                withdrawFrame.add(passwordField);
                withdrawFrame.add(withdrawButton);
                withdrawFrame.add(exitButton);

                // Set button listener
                withdrawButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the withdrawal amount entered by the user
                        double amount = Double.parseDouble(amountField.getText());
                        // Get the password entered by the user
                        String inputPassword = new String(passwordField.getPassword());
                        // Get account password
                        String accountPassword = accountsList.get(accountIndex).getAccountPassword();
                        // Check if the password is correct
                        if (inputPassword.equals(accountPassword)) {
                            // Withdrawal operation
                            accountsList.get(accountIndex).withdraw(amount);
                            // Update account balance label
                            balanceLabel.setText("Account Balance: " + accountsList.get(accountIndex).getBalance());
                            // Close withdrawal window
                            withdrawFrame.dispose();
                        } else {
                            // Incorrect password prompt
                            JOptionPane.showMessageDialog(withdrawFrame, "密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                exitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Close withdrawal window
                        withdrawFrame.dispose();
                    }
                });

                // Show withdrawal window
                withdrawFrame.setVisible(true);
            }
        });


        // Set button listener
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new window asking the user for the amount to be transferred, email address, account number and password
                JFrame transferFrame = new JFrame("Transfer");
                transferFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                transferFrame.setSize(400, 300);
                transferFrame.setLocationRelativeTo(detailFrame); // associated with main window

                // Create labels and text boxes
                JLabel amountLabel = new JLabel("Transfer Amount:");
                JTextField amountField = new JTextField();
                JLabel emailLabel = new JLabel("Recipient's Email:");
                JTextField emailField = new JTextField();
                JLabel accountNumberLabel = new JLabel("Recipient's Account Number:");
                JTextField accountNumberField = new JTextField();
                JLabel passwordLabel = new JLabel("Password:");
                JPasswordField passwordField = new JPasswordField();
                JButton transferButton = new JButton("Transfer");
                JButton exitButton = new JButton("Exit");

                // Set up layout manager
                JPanel panel = new JPanel(new GridLayout(7, 2));
                transferFrame.add(panel);

                // Add components to the window
                panel.add(amountLabel);
                panel.add(amountField);
                panel.add(emailLabel);
                panel.add(emailField);
                panel.add(accountNumberLabel);
                panel.add(accountNumberField);
                panel.add(passwordLabel);
                panel.add(passwordField);
                panel.add(transferButton);
                panel.add(exitButton);

                // Set button listener
                transferButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            // Obtain the transfer amount, email, account number and password entered by the user
                            double amount = Double.parseDouble(amountField.getText());
                            String email = emailField.getText();
                            String accountNumber = accountNumberField.getText();
                            String inputPassword = new String(passwordField.getPassword());
                            // Get account password
                            String accountPassword = accountsList.get(accountIndex).getAccountPassword();
                            // Check if the password is correct
                            if (inputPassword.equals(accountPassword)) {
                                // Transfer operation
                                accountsList.get(accountIndex).transfer(email, accountNumber, amount);
                                // Update account balance label
                                balanceLabel.setText("Account Balance: " + accountsList.get(accountIndex).getBalance());
                                // Close transfer window
                                transferFrame.dispose();
                            } else {
                                // Incorrect password prompt
                                JOptionPane.showMessageDialog(transferFrame, "Password incorrect", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                    }
                });

                exitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Close transfer window
                        transferFrame.dispose();
                    }
                });

                // Show transfer window
                transferFrame.setVisible(true);
            }
        });

        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    new AccountVisualizationFrame(user.getEmail(), accountsList.get(accountIndex).getAccountNumber()).setVisible(true);
                });
            }
        });



        // Add button to panel
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(transferButton);
        buttonPanel.add(historyButton);

        // Create the main panel and set the grid layout

        JPanel mainPanel = new JPanel(new GridLayout(4, 1));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // 添加边距

        // Add components to the main panel
        mainPanel.add(nameLabel);
        mainPanel.add(numberLabel);
        mainPanel.add(balanceLabel);
        mainPanel.add(buttonPanel);
        if(accountsList.get(accountIndex).getAccountNumber().startsWith("FIX")){
            mainPanel.setLayout(new GridLayout(5, 1));
            mainPanel.add(interestButton);
            interestButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create a new window
                    JFrame depositTermFrame = new JFrame("Deposit Term Selection");
                    depositTermFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    depositTermFrame.setSize(300, 150);
                    depositTermFrame.setLocationRelativeTo(null); // Center display

                    // Create panel
                    JPanel panel = new JPanel(new GridLayout(3, 1));

                    // Add dropdown menu
                    JComboBox<String> termComboBox = new JComboBox<>();
                    termComboBox.addItem("one_week");
                    termComboBox.addItem("two_weeks");
                    termComboBox.addItem("one_month");
                    termComboBox.addItem("three_months");
                    termComboBox.addItem("six_months");
                    termComboBox.addItem("one_year");
                    termComboBox.addItem("two_years");
                    termComboBox.addItem("three_years");
                    panel.add(new JLabel("Select deposit term:"));
                    panel.add(termComboBox);

                    // Add select button
                    JButton selectButton = new JButton("Select");
                    selectButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String selectedTerm = (String) termComboBox.getSelectedItem();
                            SavingAccount sa = (SavingAccount) accountsList.get(accountIndex);
                            sa.setDepositTerm(selectedTerm);
                            //sa.depositTermFinished();
                            JOptionPane.showMessageDialog(depositTermFrame, "Deposit term selected successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            // Update account balance label
                            balanceLabel.setText("Account Balance: " + accountsList.get(accountIndex).getBalance());
                            depositTermFrame.dispose(); // Close selection window
                        }
                    });
                    panel.add(selectButton);

                    // Add exit button
                    JButton exitButton = new JButton("Exit");
                    exitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            depositTermFrame.dispose(); // Exit selection window
                        }
                    });
                    panel.add(exitButton);

                    // Add a panel to a window
                    depositTermFrame.add(panel);

                    // display window
                    depositTermFrame.setVisible(true);
                }

            });
        }

        // Add main panel to window
        detailFrame.add(mainPanel);

        // Resize window and display
        detailFrame.setSize(960, 480);
        detailFrame.setLocationRelativeTo(null); // Center display
        detailFrame.setVisible(true);
    }
}