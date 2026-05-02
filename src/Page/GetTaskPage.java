package Page;

import Handler.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// Panel class for creating frosted glass effect


/**
 * Represents a page where users can view and manage their tasks.
 * This page displays tasks with detailed information such as task ID, requirement,
 * completion status, and reward. It uses a frosted glass effect for the background,
 * enhancing the visual appeal of the interface.
 */
public class GetTaskPage extends Page {
    private Page homePageFrame;
    private JFrame frame;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;

    /**
     * Constructs a new Get Task Page.
     *
             * @param homePageFrame the home page frame to return to from the task page
    */
    public GetTaskPage(Page homePageFrame) {
        this.homePageFrame = homePageFrame;
    }

    /**
     * Loads tasks from files into the task list.
     * Each task is represented in a detailed format, and tasks are read from user-specific directories.
     * This method reads each task's details from text files and adds them to the list model.
     */
    public void openPage() {
        Page frame = new Page();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        GlassPane glassPane = new GlassPane("src/image/taskBackground.jpg", 0.5f);
        glassPane.setLayout(new BorderLayout());
        backgroundPanel.add(glassPane, BorderLayout.CENTER);

        JPanel topMenuBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topMenuBar.setOpaque(false);
        JButton homeButton = new JButton("🏠 Home");
        homeButton.addActionListener(new KidHomeHandler(this.homePageFrame, frame));
        JButton taskButton = new JButton("💪 Task");
        JButton goalButton = new JButton("🎯 Goal");
        goalButton.addActionListener(new GoalHandler(homePageFrame, frame));
        JButton profileButton = new JButton("📭 Profile");
        profileButton.addActionListener(new KidProfileHandler(homePageFrame, frame));

        topMenuBar.add(homeButton);
        topMenuBar.add(taskButton);
        topMenuBar.add(goalButton);
        topMenuBar.add(profileButton);

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
        // Add listener to handle clicks on the list
        taskList.addMouseListener(new SubmitHandler(listModel, homePageFrame, frame)
        );

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        glassPane.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
        loadTasks();
    }

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
     * The entry point of the application, used to start the GetTaskPage.
     *
     * @param args the input arguments (not used)
     */
    public static void main(String[] args) {
        // The main function can be used to start the application
    }
}

