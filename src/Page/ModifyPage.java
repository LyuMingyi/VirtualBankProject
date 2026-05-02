package Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import login.*;

/**
 * The ModifyPage class provides a user interface for modifying or deleting tasks. It includes fields
 * for updating task descriptions and awards, and it integrates with other pages such as the homepage
 * and setTaskPage for updates and navigation.
 */
public class ModifyPage extends Page {
    private String taskDetails; // Detailed string containing task information
    private Page homepage; // Reference to the homepage for navigation and task updates
    private Page setTaskPage; // Reference to the set task page for additional task manipulation

    /**
     * Constructs a new ModifyPage with the specified task details, homepage, and set task page references.
     * This page allows for modification or deletion of specific tasks.
     *
     * @param taskDetails the task details in string format
     * @param homepage    the homepage reference for navigation
     * @param setTaskPage the set task page for task updating
     */
    public ModifyPage(String taskDetails, Page homepage, Page setTaskPage) {
        this.taskDetails = taskDetails;
        this.homepage = homepage;
        this.setTaskPage = setTaskPage;
    }

    /**
     * Opens the Modify Task page, sets up the GUI components including text fields and buttons for task modification and deletion,
     * and handles the actions performed on these components.
     */
    public void openPage() {
        JFrame frame = new JFrame("Modify Task");
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close current window only

        // Create a text field and set initial value
        //JTextField taskDescriptionField = new JTextField(getTaskRequirementFromDetails(taskDetails));
        //JTextField awardField = new JTextField(getAwardFromDetails(taskDetails));


        // Main panel, using BoxLayout layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Task description label and text field
        JLabel taskDescriptionLabel = new JLabel("Task Description:");
        taskDescriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField taskDescriptionField = new JTextField(getTaskRequirementFromDetails(taskDetails));
        taskDescriptionField.setMaximumSize(new Dimension(400, 50)); // Set the maximum size of the input box
        taskDescriptionField.setBorder(BorderFactory.createCompoundBorder(
                taskDescriptionField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Increase border thickness

        // Reward labels and text fields
        JLabel awardLabel = new JLabel("Award:");
        awardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField awardField = new JTextField(getAwardFromDetails(taskDetails));
        awardField.setMaximumSize(new Dimension(400, 50)); // Set the maximum size of the input box
        awardField.setBorder(BorderFactory.createCompoundBorder(
                awardField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Increase border thickness

        // button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the user's email (assuming homepage is an existing object)
                String email = homepage.getEmail();

                // Get task ID
                String taskID = getTaskIDFromDetails(taskDetails);

                AccountInfoWriter aw = new AccountInfoWriter();
                // Call taskDeleter function to delete
                aw.taskDeleter(email, taskID);

                // Close the ModifyPage window
                frame.dispose();
                setTaskPage.dispose();
                SetTaskPage stp = new SetTaskPage(homepage);
                stp.openPage();
            }
        });
        JButton modifyButton = new JButton("Modify");
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the modified task name and reward
                String modifiedTaskName = taskDescriptionField.getText();
                Double modifiedAward = Double.parseDouble(awardField.getText());

                // Get the user's email (assuming homepage is an existing object)
                String email = homepage.getEmail();

                // Get task ID
                String taskID = getTaskIDFromDetails(taskDetails);

                AccountInfoWriter ar = new AccountInfoWriter();
                // Call the taskModifier function to modify
                ar.taskModifier(email, taskID, modifiedTaskName, modifiedAward);

                // Close the ModifyPage window
                frame.dispose();
                setTaskPage.dispose();
                SetTaskPage stp = new SetTaskPage(homepage);
                stp.openPage();
            }

        });

        // Add components to the main panel
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(taskDescriptionLabel);
        mainPanel.add(taskDescriptionField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 添加间隔
        mainPanel.add(awardLabel);
        mainPanel.add(awardField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 添加间隔

        // Add button to button panel
        buttonPanel.add(deleteButton);
        buttonPanel.add(modifyButton);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue());

        // Add main panel to window
        frame.add(mainPanel);

        frame.setVisible(true);
    }

    /**
     * Extracts the task ID from the detailed task information. This is used to identify which task is being modified or deleted.
     *
     * @param taskDetails the detailed task information
     * @return the task ID extracted from the details
     */
    private String getTaskIDFromDetails(String taskDetails) {
        // Parse task details and extract task ID
        // Assume that the task ID is located after "Task ID:" and before "Task Requirement:"
        int startIndex = taskDetails.indexOf("<font color=\"#00BFFF\" size=\"5\">Task ID:</font></b> ") + "<font color=\"#00BFFF\" size=\"5\">Task ID:</font></b> ".length();
        int endIndex = taskDetails.indexOf("<br>", startIndex);
        return taskDetails.substring(startIndex, endIndex);
    }

    /**
     * Extracts the task requirement description from the detailed task information.
     *
     * @param taskDetails the detailed task information
     * @return the task requirement extracted from the details
     */
    // Extract task requirements from task details
    private String getTaskRequirementFromDetails(String taskDetails) {
        // Analyze task details and extract task requirements
        // Assume that the task details are in the format "<html><b><font color=\"#00BFFF\" size=\"5\">Task ID:</font></b> ... Task Requirement: .. .</html>"
        // This needs to be parsed according to the actual task details format.
        // Example: Suppose the task requirement is located after "Task Requirement: "
        int startIndex = taskDetails.indexOf("Task Requirement: ") + "Task Requirement: ".length();
        int endIndex = taskDetails.indexOf("<br>", startIndex);
        return taskDetails.substring(startIndex, endIndex);
    }

    /**
     * Extracts the award associated with the task from the detailed task information.
     *
     * @param taskDetails the detailed task information
     * @return the award extracted from the details
     */
    // Extract rewards from task details
    private String getAwardFromDetails(String taskDetails) {
        // Analyze task details and extract rewards
        // Assume the award is located after "Award: "
        int startIndex = taskDetails.indexOf("Award: ") + "Award: ".length();
        int endIndex = taskDetails.indexOf("$", startIndex);
        return taskDetails.substring(startIndex, endIndex);
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
    }
}
