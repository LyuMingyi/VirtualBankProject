package Page;
import User.Parent;
import login.AccountInfoReader;
import login.AccountInfoWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
public class CreateTaskPage extends Page {
    private Page homepage;
    private Page setTaskPage;

    /**
     * Constructs a new {@code CreateTaskPage} with references to the homepage and the page
     * where tasks are set up. These references are used to manage navigation and interactions
     * between different pages within the application.
     *
     * @param homepage    the main homepage of the application
     * @param setTaskPage the page dedicated to task setting and management
     */
    public CreateTaskPage(Page homepage, Page setTaskPage) {
        this.homepage = homepage;
        this.setTaskPage = setTaskPage;
    }

    /**
     * Opens and initializes the create task page interface. This method sets up
     * the graphical user interface components for task creation, including text fields for
     * task description and award, and a button to submit the new task.
     * It includes validation to ensure that the award input is a numeric value.
     */

    public void openPage() {
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window


        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Task description input
        JLabel taskDescriptionLabel = new JLabel("Task description:");
        JTextField taskDescriptionField = new JTextField();
        taskDescriptionField.setPreferredSize(new Dimension(300, 35));
        panel.add(taskDescriptionLabel, gbc);
        panel.add(taskDescriptionField, gbc);

        // Award input
        JLabel awardLabel = new JLabel("Award:");
        JTextField awardField = new JTextField();
        awardField.setPreferredSize(new Dimension(300, 35));
        panel.add(awardLabel, gbc);
        panel.add(awardField, gbc);

        // Award input
        JLabel space = new JLabel("");
        space.setPreferredSize(new Dimension(300, 15));
        panel.add(space, gbc);

        // Create button
        JButton createButton = new JButton("Create");
        createButton.setPreferredSize(new Dimension(200, 40));
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double awardValue = Double.parseDouble(awardField.getText());
                    // Handle the valid input (e.g., store the value or perform other actions)
                    System.out.println("Award value: " + awardValue);
                    AccountInfoWriter ar = new AccountInfoWriter();
                    ar.writeTask(taskDescriptionField.getText(), homepage.getEmail(), awardValue);
                    frame.dispose();
                    SetTaskPage stp = new SetTaskPage(homepage);
                    setTaskPage.dispose();
                    stp.openPage();
                } catch (NumberFormatException ex) {
                    // Invalid input (not a valid double)
                    JOptionPane.showMessageDialog(frame, "Please enter a valid numeric value for the award.");
                    awardField.setText(""); // Clear the input field
                }
            }

        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
