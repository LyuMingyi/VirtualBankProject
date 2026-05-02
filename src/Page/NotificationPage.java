/**
 * This class represents the notification page, where parents can view task notifications from their kids.
 * Parents can accept or reject task submissions from their kids.
 */
package Page;

import AccountType.Account;
import Handler.NotificationHandler;
import Handler.ParentHomeHandler;
import Handler.ParentProfileHandler;
import Handler.SetTaskHandler;
import login.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Notification page.
 */
public class NotificationPage extends Page {
    private Page homepage;

    /**
     * Constructs a new NotificationPage with the specified homepage.
     *
     * @param homepage the homepage associated with this page
     */
    public NotificationPage(Page homepage) {
        this.homepage = homepage;
    }

    /**
     * Opens the notification page.
     */
    public void openPage() {
        // create a new window
        Page frame = new Page();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null); //center display
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        GlassPane glassPane = new GlassPane("src/image/notificationBackground.jpg", 0.5f);
        glassPane.setLayout(new BorderLayout());
        backgroundPanel.add(glassPane, BorderLayout.CENTER);
        // create a top button field
        JPanel topMenuBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topMenuBar.setOpaque(false);
        JButton homeButton = new JButton("🏠 Home");
        JButton taskButton = new JButton("💪 Task");
        JButton notificationButton = new JButton("📢 Notification");
        JButton profileButton = new JButton("📭 Profile");

        topMenuBar.add(homeButton);
        homeButton.addActionListener(new ParentHomeHandler(this.homepage, frame));
        topMenuBar.add(taskButton);
        taskButton.addActionListener(new SetTaskHandler(homepage, frame));
        topMenuBar.add(notificationButton);
        //notificationButton.addActionListener(new NotificationHandler(homepage, frame));
        topMenuBar.add(profileButton);
        profileButton.addActionListener(new ParentProfileHandler(homepage, frame));

        glassPane.add(topMenuBar, BorderLayout.NORTH);
       // frame.getContentPane().add(topMenuBar, BorderLayout.NORTH);

        // create a list
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // get the email address
        String email = homepage.getEmail();
        Path dirPath = Paths.get("./registerTable/" + email);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.txt")) {
            for (Path entry : stream) {
                if (!entry.getFileName().toString().equals("info.txt")) {
                    // Read file content and add to list model
                    String content = new String(Files.readAllBytes(entry));
                    listModel.addElement(content);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a list to display file contents
        JList<String> list = new JList<>(listModel);
        list.setOpaque(false);
        list.setCellRenderer(new NotificationCellRenderer()); // Set cell renderer
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();

                if (evt.getClickCount() == 2) { //double click
                    int index = list.locationToIndex(evt.getPoint());
                    String selectedValue = (String) list.getModel().getElementAt(index);
                    String kidName = extractKidName(selectedValue); // The method of extracting kidName needs to be implemented by yourself

                    int response = JOptionPane.showConfirmDialog(frame, "Do you accept the submission from " + kidName + "？", "请求确认", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        // The user clicked "Accept", write the code to accept the request here
                        String email = extractEmail(selectedValue);
                        AccountInfoReader ar = new AccountInfoReader();
                        List<String> accountList = ar.loadAccountList(email);

                        JComboBox<String> accountComboBox = new JComboBox<>(accountList.toArray(new String[0]));
                        JTextField awardField = new JTextField(10);
                        JPanel panel = new JPanel();
                        panel.add(new JLabel("Choose Account:"));
                        panel.add(accountComboBox);
                        /*panel.add(new JLabel("Award:"));
                        panel.add(awardField);*/

                        int result = JOptionPane.showConfirmDialog(null, panel, "Award Task",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            String selectedAccount = (String) accountComboBox.getSelectedItem();
                            //double award = Double.parseDouble(awardField.getText());
                            double award = extractAward(selectedValue);
                            //System.out.println(extractAward(selectedValue));
                            ArrayList<Account> parentAccounts = ar.accountListLoader(homepage.getEmail());
                            Account parentAccount = parentAccounts.get(0);
                            parentAccount.transfer(email, selectedAccount, award);
                            JOptionPane.showMessageDialog(frame, "Transfer successful!");
                            AccountInfoWriter aw = new AccountInfoWriter();
                            //aw.taskDeleter(homepage.getEmail(), extractTaskID(selectedValue));
                            aw.taskDeleter(email, extractTaskID(selectedValue));//The email here is the child’s email
                            frame.dispose();
                            deleteNotification(homepage.getEmail(), extractTaskID(selectedValue));
                            NotificationPage np = new NotificationPage(homepage);
                            np.openPage();
                        }
                    } else {
                        // user click the exit button
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        glassPane.add(scrollPane, BorderLayout.CENTER);

        // add some distance to
        list.setBorder(BorderFactory.createEmptyBorder(10, 200, 10, 200)); // Increase margins
        list.setFont(list.getFont().deriveFont(Font.PLAIN, 20)); // Increase text size
        //frame.add(new JScrollPane(list));

        // display window
        frame.setVisible(true);
    }

    // Custom cell renderer, supports text wrapping
    private static class NotificationCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String text = (String) value;
            list.setBackground(new Color(255, 255, 255, 120));
            // Insert line break tag at fixed position of each notification text using regular expression
            text = text.replaceAll("(\r\n|\r|\n)", "<br/>");
            text = "<html>" + text.replaceAll("(Task ID:+?<br/>)", "$1<br/>") + "</html>";
            return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        }
    }

    // Method to extract kidName (you need to implement it according to the actual situation)
    private String extractKidName(String notificationText) {
        // Use regular expression to match first word of first line
        Pattern pattern = Pattern.compile("^(\\w+)");
        Matcher matcher = pattern.matcher(notificationText);
        if (matcher.find()) {
            // If a matching word is found, return it
            return matcher.group(1);
        }
        return "Unknown"; // If no matching word is found, return "Unknown"
    }

    // How to extract email
    private String extractEmail(String notificationText) {
        // Use regular expressions to match email addresses
        Pattern pattern = Pattern.compile("^.*\\nFrom: (\\S+@\\S+\\.\\S+)");
        Matcher matcher = pattern.matcher(notificationText);
        if (matcher.find()) {
            // If a matching email address is found, return it
            return matcher.group(1);
        }
        return "Unknown"; // If no matching email address is found, returns "Unknown"
    }

    // How to extract award
    private double extractAward(String notificationText) {
        // Use regular expressions to match Task Award
        Pattern pattern = Pattern.compile("Task Award:\\s*(\\d+(\\.\\d+)?)");
        Matcher matcher = pattern.matcher(notificationText);
        if (matcher.find()) {
            // If a matching Task Award is found, return the award
            return Double.parseDouble(matcher.group(1));
        }
        return 0.0; // If no matching Task Award is found, 0.0 is returned.
    }

    // How to extract task ID
    private String extractTaskID(String notificationText) {
        // Use regular expressions to match Task IDs
        Pattern pattern = Pattern.compile("Task ID: (\\d+)");
        Matcher matcher = pattern.matcher(notificationText);
        if (matcher.find()) {
            // If a matching Task ID is found, return the ID
            return matcher.group(1);
        }
        return "-1"; // If no matching Task ID is found, return "-1"
    }

    private void deleteNotification(String email, String taskID) {
        // Build notification file path
        String filePath = "./registerTable/" + email + "/notification_" + taskID + ".txt";

        // Create File object
        File file = new File(filePath);

        // Check if the file exists and is the file type
        if (file.exists() && file.isFile()) {
            // Delete Files
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Notification file deleted successfully.");
            } else {
                System.out.println("Failed to delete notification file.");
            }
        } else {
            System.out.println("Notification file does not exist or is not a file.");
        }
    }

}

