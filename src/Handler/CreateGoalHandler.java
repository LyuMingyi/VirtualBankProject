package Handler;

import AccountType.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import Page.*;

/**
 * Handles the creation of financial goals for a user's account.
 * This class responds to GUI action events and facilitates the saving of a user-defined financial goal
 * associated with a specific account into a file system. It supports handling user input from the dialog,
 * updating the goal information, and navigating between different UI pages.
 */
public class CreateGoalHandler implements ActionListener {
    private double goal;
    private int selectedAccountIndex;
    private ArrayList<Account> accountList;

    private Page homepage;

    private Page currentPage;
    private  JDialog dialog;

    /**
     * Constructs a new CreateGoalHandler with the specified parameters.
     *
     * @param goal the financial goal to be set
     * @param selectedAccountIndex the index of the selected account
     * @param accountList the list of user's accounts
     * @param homepage the homepage object of the user interface
     * @param currentPage the current page displayed to the user
     * @param dialog the dialog window for goal creation
     */
    public CreateGoalHandler(double goal, int selectedAccountIndex, ArrayList<Account> accountList, Page homepage, Page currentPage,  JDialog dialog) {
        this.goal = goal;
        this.selectedAccountIndex = selectedAccountIndex;
        this.accountList = accountList;
        this.homepage = homepage;
        this.currentPage = currentPage;
        this.dialog = dialog;
    }

    /**
     * Handles the action performed event when the user attempts to create a new goal.
     * It writes the goal information to a file and transitions the user interface to display the goal page.
     * If an error occurs, such as invalid input, it displays an error message to the user.
     *
     * @param e the event that triggers the action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //double goal = Double.parseDouble(goalField.getText());
            //int selectedAccountIndex = accountDropdown.getSelectedIndex();
            String accountNumber = accountList.get(selectedAccountIndex).getAccountNumber();

            String directoryPath = "./registerTable/" + homepage.getEmail() + "/";
            File goalFile = new File(directoryPath + "goal.txt");
            try (FileWriter writer = new FileWriter(goalFile, true)) {
                writer.append("accountNumber: " + accountNumber + ", goal: " + goal + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            dialog.dispose();
            currentPage.dispose();
            GoalPage gp = new GoalPage(homepage, currentPage);
            gp.openPage();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Invalid goal!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
