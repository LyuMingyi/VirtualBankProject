package Page;

import Handler.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * The type Set task page.
 */
public class SetTaskPage extends Page {
    private Page homePageFrame;
    private JFrame frame;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;

    /**
     * Instantiates a new Set task page.
     *
     * @param homePageFrame the home page frame
     */
    public SetTaskPage(Page homePageFrame) {
        this.homePageFrame = homePageFrame;
    }

    public void openPage() {
        Page frame = new Page();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null); // Center the window

        JPanel backgroundPanel = new JPanel(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        GlassPane glassPane = new GlassPane("src/image/taskBackground.jpg", 0.5f);
        glassPane.setLayout(new BorderLayout());
        backgroundPanel.add(glassPane, BorderLayout.CENTER);
        // Create top menu bar panel and buttons
        JPanel topMenuBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topMenuBar.setOpaque(false);
        JButton homeButton = new JButton("🏠Home");
        JButton taskButton = new JButton("💪Task");
        JButton notificationButton = new JButton("📢Notification");
        JButton profileButton = new JButton("📭Profile");

        // Add buttons to the top menu bar panel
        topMenuBar.add(homeButton);
            homeButton.addActionListener(new ParentHomeHandler(this.homePageFrame, frame));
        topMenuBar.add(taskButton);
        topMenuBar.add(notificationButton);
            notificationButton.addActionListener(new NotificationHandler(homePageFrame, frame));
        topMenuBar.add(profileButton);
            profileButton.addActionListener(new ParentProfileHandler(homePageFrame, frame));

        // Add the top menu bar panel to the window's north (top) side
        glassPane.add(topMenuBar, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setOpaque(false);
        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel) {
                    ((JLabel) c).setOpaque(true);
                   ((JLabel) c).setBackground(new Color(255, 255, 255, 120)); // Semi-transparent
                }
                return c;
            }
        });
        taskList.addMouseListener(new ModifyHandler(listModel, homePageFrame, frame));

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the list

        glassPane.add(scrollPane, BorderLayout.CENTER);

        JButton createTaskButton = new JButton("Create a Task");
        createTaskButton.setFont(new Font("Arial", Font.BOLD, 14));
        createTaskButton.setBackground(new Color(30, 144, 255)); // Set button color
        createTaskButton.setForeground(Color.BLACK); // Set text color
        createTaskButton.setFocusPainted(false); // Remove border around text when clicked
        createTaskButton.addActionListener(new createTaskHandler(homePageFrame, frame));

        JButton deliverTaskButton = new JButton("Deliver a Task"); // New button
        deliverTaskButton.setFont(new Font("Arial", Font.BOLD, 14));
        deliverTaskButton.setBackground(new Color(60, 179, 113)); // Set button color
        deliverTaskButton.setForeground(Color.BLACK); // Set text color
        deliverTaskButton.setFocusPainted(false); // Remove border around text when clicked
       deliverTaskButton.addActionListener(new deliverTaskHandler(homePageFrame, frame)); // Add action listener

        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.setBackground(new Color(245, 245, 245)); // Set panel background color
        southPanel.add(createTaskButton);
        southPanel.add(deliverTaskButton); // Add the new button to the panel
        glassPane.add(southPanel, BorderLayout.SOUTH);

        loadTasks(); // Load tasks from files
        frame.setVisible(true);
    }

    // Private helper methods for GUI components

    /**
     * Loads tasks from files and displays them in the task list.
     */
    private void loadTasks() {
        File taskDir = new File("./registerTable/" + homePageFrame.getEmail() + "/task/");
        File[] taskFiles = taskDir.listFiles((dir, name) -> name.endsWith(".txt"));

        if (taskFiles != null) {
            for (File taskFile : taskFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(taskFile))) {
                    String taskId = reader.readLine();
                    String taskRequirement = reader.readLine();
                    String completion = reader.readLine();
                    String status = reader.readLine();
                    String award = reader.readLine();

                    String taskDetails = "<html><b><font color=\"#00BFFF\" size=\"5\">Task ID:</font></b> " + taskId + "<br>" +
                            "Task Requirement: " + taskRequirement + "<br>" +
                            "Completion: " + completion + "<br>" +
                            "Status: " + status + "<br>" +
                            "Award: " + award + "$" + "</html>";

                    listModel.addElement(taskDetails);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Entry point for testing
    }
}

