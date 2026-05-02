package Page;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import login.*;
import User.*;
import Task.*;

/**
 * The {@code CreateTaskPage} class extends {@code Page} and is used to provide the interface
 * for creating new tasks within the application. It allows users to input task descriptions
 * and define awards for task completion. It also handles navigation between this task creation
 * page and other related pages.
 *
 * Usage example:
 * <pre>
 * CreateTaskPage ctp = new CreateTaskPage(homepage, setTaskPage);
 * ctp.openPage();
 * </pre>
 */
public class DeliverTaskPage extends Page {
    private Page homePageFrame;
    private JFrame frame;
    private JComboBox<String> taskComboBox;
    private JComboBox<String> kidComboBox;
    private JButton deliverButton;
    private Map<String, String> kidEmailMap;

    private Page currentPage;

    /**
     * Instantiates a new Deliver task page.
     *
     * @param homePageFrame the home page frame
     * @param currentPage   the current page
     */
    public DeliverTaskPage(Page homePageFrame, Page currentPage) {
        this.homePageFrame = homePageFrame;
        this.currentPage = currentPage;
        kidEmailMap = new HashMap<>();
    }

    /**
     * Opens and initializes the Deliver Task page interface. This method sets up
     * the graphical user interface components for selecting a task and a kid, and a button to
     * assign the selected task. It handles the logic for task delivery including task selection
     * validation and updates upon successful task assignment.
     */
    public void openPage() {
        frame = new JFrame("Deliver Task");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);

        // Set the main panel layout to BoxLayout, vertically arranged
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 500, 50, 500)); // Increase margin

        // Task combo box
        taskComboBox = new JComboBox<>();
        taskComboBox.addItem("Choose a task:");
        // Add tasks
        for (String task : getTasks()) {
            taskComboBox.addItem(task);
        }
        taskComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 30))); // Add spacing
        panel.add(taskComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 40))); // Add spacing

        // Kid combo box
        kidComboBox = new JComboBox<>();
        kidComboBox.addItem("Choose a kid:");
        // Load kid names and emails
        AccountInfoReader ar = new AccountInfoReader();
        Parent p = ar.parentLoader(homePageFrame.getEmail());
        ArrayList<String> kidsList = p.getKidsList();
        ArrayList<String> kidNameList = ar.kidNameLoader(kidsList);
        for (int i = 0; i < kidNameList.size(); i++) {
            String kidName = kidNameList.get(i);
            String kidEmail = kidsList.get(i);
            kidEmailMap.put(kidName, kidEmail);
            kidComboBox.addItem(kidName);
        }
        kidComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(kidComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Deliver button
        deliverButton = new JButton("Deliver");
        deliverButton.addActionListener(e -> {
            AccountInfoWriter aw = new AccountInfoWriter();
            aw.deliverTask(homePageFrame.getEmail(),
                    kidEmailMap.get(kidComboBox.getSelectedItem()),
                    getSelectedTask((String) taskComboBox.getSelectedItem()));
            frame.dispose();
            currentPage.dispose();
            SetTaskPage stp = new SetTaskPage(homePageFrame);
            stp.openPage();
        });
        deliverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(deliverButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    /**
     * Retrieves the list of tasks associated with the user's account. This method reads task
     * details from files and formats them for display in the task selection dropdown.
     *
     * @return a list of formatted strings representing tasks
     */
    private List<String> getTasks() {
        List<String> tasks = new ArrayList<>();
        File taskDir = new File("./registerTable/" + homePageFrame.getEmail() + "/task/");
        File[] taskFiles = taskDir.listFiles((dir, name) -> name.endsWith(".txt"));

        if (taskFiles != null) {
            for (File taskFile : taskFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(taskFile))) {
                    String taskId = reader.readLine();
                    String taskRequirement = reader.readLine();
                    reader.readLine(); // Skip the completion line
                    reader.readLine(); // Skip the status line
                    String award = reader.readLine();

                    // Format the task details as required by the getSelectedTask method
                    String taskDetails = "Task ID: " + taskId + ", Task Name: " + taskRequirement + ", Money: " + award;
                    tasks.add(taskDetails);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tasks;
    }

    /**
     * Parses the selected task string and returns a {@code Task} object.
     * This method extracts task ID, name, and monetary award from the formatted task string.
     *
     * @param taskDetails the formatted task string selected from the task dropdown
     * @return a {@code Task} object constructed from the extracted details
     */
    private Task getSelectedTask(String taskDetails) {
        // Split the task details string
        String[] parts = taskDetails.split(", ");
        int taskID = Integer.parseInt(parts[0].split(": ")[1]);
        String taskName = parts[1].split(": ")[1];
        double money = 0;
        try {
            money = Double.parseDouble(parts[2].split(": ")[1].replace("$", ""));
        } catch (NumberFormatException e) {
            System.err.println("Error parsing money value from task details: " + parts[2]);
        }
        return new Task(taskID, taskName, money);
    }

    /**
     * The entry point of the application for testing.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Entry point for testing
    }
}
