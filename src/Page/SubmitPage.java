package Page;

import login.AccountInfoReader;
import User.*;
import login.AccountInfoWriter;

import javax.swing.*;

/**
 * The type Submit page.
 */
public class SubmitPage extends Page {

    private String taskDetail;
    private Page homepage;
    private Page getTaskPage;

    /**
     * Instantiates a new Submit page.
     *
     * @param taskDetail  the task detail
     * @param homepage    the homepage
     * @param getTaskPage the get task page
     */
    public SubmitPage(String taskDetail, Page homepage, Page getTaskPage) {
        this.taskDetail = taskDetail;
        this.homepage = homepage;
        this.getTaskPage = getTaskPage;
    }
    /**
     * Opens the set task page.
     */
    public void openPage() {
        System.out.println(taskDetail);
        AccountInfoReader ar = new AccountInfoReader();
        Kid kid = ar.kidLoader(homepage.getEmail());

        String kidName = kid.getName();
        System.out.println(kid.getParentEmail());

        // Display a confirmation dialog with English button names
        int result = JOptionPane.showConfirmDialog(null, "Would you like to submit the task completion notification to your parents?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            AccountInfoWriter aw = new AccountInfoWriter();
            aw.taskSubmit(homepage.getEmail(), kid.getParentEmail(), taskDetail);
            // User clicked "Yes," implement the notification submission logic here
            System.out.println("The task has been submitted to the parents.");
        } else {
            // User clicked "No," handle accordingly
            System.out.println("The task has not been submitted.");
        }
    }
}

