package Page;

import Handler.NotificationHandler;
import Handler.ParentHomeHandler;
import Handler.SetTaskHandler;
import User.*;
import login.AccountInfoReader;
import login.AccountInfoWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * The type Profile page for parent.
 */
public class ProfilePageForParent extends Page {
    private Page homepage;
    private Parent parent;
    private JLabel profileImageLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel kidsLabel;

    /**
     * Instantiates a new Profile page for parent.
     *
     * @param homepage the homepage
     */
    public ProfilePageForParent(Page homepage) {
        this.homepage = homepage;
        AccountInfoReader ar = new AccountInfoReader();
        this.parent = ar.parentLoader(homepage.getEmail());
    }

    /**
     * Opens the profile page for the parent user.
     */
    public void openPage() {
        // Create a new window
        Page frame = new Page();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null); // Center the window
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        frame.setContentPane(backgroundPanel);
        GlassPane glassPane = new GlassPane("src/image/profileBackground.jpg", 0.5f);
        glassPane.setLayout(new BorderLayout());
        backgroundPanel.add(glassPane, BorderLayout.CENTER);

        // Create a top bar and add buttons
        JPanel topMenuBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topMenuBar.setOpaque(false);
        JButton homeButton = new JButton("🏠 Home");
        JButton taskButton = new JButton("💪 Task");
        JButton notificationButton = new JButton("📢 Notification");
        JButton profileButton = new JButton("📭 Profile");

        topMenuBar.add(homeButton);
            homeButton.addActionListener(new ParentHomeHandler(homepage, frame));
        topMenuBar.add(taskButton);
            taskButton.addActionListener(new SetTaskHandler(homepage, frame));
        topMenuBar.add(notificationButton);
            notificationButton.addActionListener(new NotificationHandler(homepage, frame));
        topMenuBar.add(profileButton);

        // Add the upper border to the north of the window (BorderLayout.NORTH)
        glassPane.add(topMenuBar, BorderLayout.NORTH);

        // Create a center panel and use GridBagLayout to more precisely control component positioning
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0)); // Increase top and bottom margins
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Increase spacing between elements
        glassPane.add(centerPanel, BorderLayout.CENTER);

        // Create avatar folders and handle avatar display
        handleProfileImage(centerPanel, gbc, parent.getEmail(), frame);

        // Add username and email
        addProfileLabels(centerPanel, gbc, parent.getName(), parent.getEmail());

        // Add child information
        handleKidsList(centerPanel, gbc, parent.getKidsList());

        // Add "Add my kid" button
        addButton(centerPanel, gbc, frame);

        themeButton(centerPanel, gbc, frame);



        // display window
        frame.setVisible(true);
    }

    // Private helper methods for GUI components

    /**
     * Handles the display and update of the profile image.
     *
     * @param panel the panel where the profile image will be displayed
     * @param gbc   the GridBagConstraints for layout control
     * @param email the email of the parent user
     * @param frame the parent frame for file chooser dialog
     */
    private void handleProfileImage(JPanel panel, GridBagConstraints gbc, String email, Page frame) {
        String imageDirectoryPath = "./registerTable/" + email + "/userImage";
        File imageDirectory = new File(imageDirectoryPath);
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
            copyDefaultImage(email);
        }
        File[] imageFiles = imageDirectory.listFiles();
        if (imageFiles != null && imageFiles.length > 0) {
            ImageIcon imageIcon = new ImageIcon(imageFiles[0].getPath());
            Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon newImageIcon = new ImageIcon(image);
            profileImageLabel = new JLabel(newImageIcon);
            profileImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                    int result = fileChooser.showOpenDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        updateProfileImage(selectedFile, imageDirectoryPath);
                    }
                }
            });
            panel.add(profileImageLabel, gbc);
        }
    }

    /**
     * Updates the profile image with the selected file.
     *
     * @param file              the selected file for the new profile image
     * @param imageDirectoryPath the path to the directory where profile images are stored
     */
    private void updateProfileImage(File file, String imageDirectoryPath) {
        try {
            Path sourcePath = file.toPath();
            Path targetPath = Paths.get(imageDirectoryPath, file.getName());
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            ImageIcon newIcon = new ImageIcon(targetPath.toString());
            Image newImg = newIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            profileImageLabel.setIcon(new ImageIcon(newImg));
            profileImageLabel.repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to update profile image: " + e.getMessage());
        }
    }

    /**
     * Adds name and email labels to the profile panel.
     *
     * @param panel the panel where the labels will be added
     * @param gbc   the GridBagConstraints for layout control
     * @param name  the name of the parent user
     * @param email the email of the parent user
     */
    private void addProfileLabels(JPanel panel, GridBagConstraints gbc, String name, String email) {
        nameLabel = new JLabel("<html><h1 style='font-size:18px;'><b>Name:</b> " + name + "</h1></html>");
        panel.add(nameLabel, gbc);
        emailLabel = new JLabel("<html><h1 style='font-size:18px;'><b>Email:</b> " + email + "</h1></html>");
        panel.add(emailLabel, gbc);
    }

    /**
     * Handles the display of child information.
     *
     * @param panel     the panel where the child information will be displayed
     * @param gbc       the GridBagConstraints for layout control
     * @param kidsList  the list of child names associated with the parent user
     */
    private void handleKidsList(JPanel panel, GridBagConstraints gbc, ArrayList<String> kidsList) {
        if (!kidsList.isEmpty()) {
            StringBuilder kidsInfo = new StringBuilder("<html><h1 style='font-size:18px;'><b>My Kids:</b></h1><br/>");
            for (String kid : kidsList) {
                kidsInfo.append(kid).append("<br/>");
            }
            kidsInfo.append("</html>");
            kidsLabel = new JLabel(kidsInfo.toString());
            panel.add(kidsLabel, gbc);
        }
    }

    /**
     * Adds a button to the profile panel for adding a new kid.
     *
     * @param panel the panel where the button will be added
     * @param gbc   the GridBagConstraints for layout control
     * @param frame the parent frame for handling button click event
     */
    private void addButton(JPanel panel, GridBagConstraints gbc, Page frame) {
        JButton addKidButton = new JButton("Add my kid");
        addKidButton.setPreferredSize(new Dimension(180, 60));
        addKidButton.setFont(new Font("Arial", Font.BOLD, 16));
        addKidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addKid(frame);
            }
        });
        panel.add(addKidButton, gbc);
    }

    /**
     * Creates and handles the theme change button.
     *
     * @param panel the panel where the theme change button will be added
     * @param gbc   the GridBagConstraints for layout control
     * @param frame the parent frame for theme change dialog
     */
    private void themeButton(JPanel panel, GridBagConstraints gbc, Page frame) {
        JButton themeButton = new JButton("Theme");
        themeButton.setPreferredSize(new Dimension(180, 60));
        themeButton.setFont(new Font("Arial", Font.BOLD, 16));
        themeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeTheme(frame, homepage);
            }
        });
        panel.add(themeButton, gbc);
    }

    /**
     * Copies the default profile image to the user directory if it does not exist.
     *
     * @param email the email of the parent user
     */
    private void copyDefaultImage(String email) {
        Path sourcePath = Paths.get("src/image/defaultImage.png");
        Path targetDirectory = Paths.get("./registerTable/" + email + "/userImage");
        Path targetPath = targetDirectory.resolve("defaultImage.png");
        try {
            if (Files.notExists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to copy default image: " + e.getMessage());
        }
    }

    /**
     * Adds a new kid to the parent user's account.
     *
     * @param frame the parent frame for handling dialog box
     */
    private void addKid(Page frame) {
        // Create and configure dialog boxes
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Kid");
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new FlowLayout());

        // Enter the kid’s email in the text field
        JTextField emailField = new JTextField(20);
        dialog.add(new JLabel("Enter kid's email:"));
        dialog.add(emailField);

        // Create confirm and exit buttons
        JButton confirmButton = new JButton("Confirm");
        JButton exitButton = new JButton("Exit");

        // Add event handling
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String kidEmail = emailField.getText();
                if (!kidEmail.isEmpty()) {
                    AccountInfoWriter aw = new AccountInfoWriter();
                    aw.writeRelationship(homepage.getEmail(), kidEmail);
                }
                dialog.dispose();
                frame.dispose();
                ProfilePageForParent pfp = new ProfilePageForParent(homepage);
                pfp.openPage();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(confirmButton);
        dialog.add(exitButton);

        // show dialog
        dialog.setVisible(true);


    }

    /**
     * Change theme.
     *
     * @param currentpage the currentpage
     * @param homepage    the homepage
     */
    public static void changeTheme(Page currentpage, Page homepage) {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("Theme Changer");
        frame.setSize(800, 600); // Set window size
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set default shutdown action
        frame.setLayout(new GridLayout(2, 2)); // Using the GridLayout layout manager

        String[] imageNames = {"Happy S.jpg", "Kungfu Panda.jpg", "Little Pony.jpg", "The Simpsons.jpg"};
        String basePath = "./Theme/"; // Assume that the image path has been corrected

        // Loop to add four JLabel to JFrame
        for (String imageName : imageNames) {
            JLabel label = new JLabel();
            label.setLayout(new BorderLayout()); // Set up JLabel to use BorderLayout
            label.setHorizontalAlignment(JLabel.CENTER); // Center images and text horizontally
            label.setVerticalAlignment(JLabel.CENTER); // Center images and text vertically
            try {
                // Read the original image
                BufferedImage originalImage = ImageIO.read(new File(basePath + imageName));
                // Resize image
                ImageIcon icon = new ImageIcon(originalImage.getScaledInstance(190, 190, Image.SCALE_SMOOTH));
                label.setIcon(icon);
            } catch (Exception e) {
                e.printStackTrace();
                label.setText("Image not found"); // If the image does not exist, an error message is displayed
            }

            JLabel textLabel = new JLabel(imageName.substring(0, imageName.lastIndexOf('.')), JLabel.CENTER); // 创建一个居中的文本标签
            textLabel.setHorizontalAlignment(JLabel.CENTER); // Set text to be centered horizontally
            label.add(textLabel, BorderLayout.SOUTH); // Add text label below image

            // Add mouse click event listener
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openAndCopyFiles(imageName.substring(0, imageName.lastIndexOf('.')));
                    frame.dispose();
                    currentpage.dispose();
                    ProfilePageForParent pfp = new ProfilePageForParent(homepage);
                    pfp.openPage();
                }
            });

            frame.add(label); // Add to JFrame
        }

        // Center the JFrame window on the screen
        frame.setLocationRelativeTo(null);

        // Set JFrame visible
        frame.setVisible(true);
    }

    // How to open the corresponding directory and copy the filesSet font size and bold
    /**
     * Opens the corresponding theme directory, copies theme files, and updates GUI.
     *
     * @param folderName the name of the theme folder
     */
    private static void openAndCopyFiles(String folderName) {
        Path sourceDir = Path.of("Theme", folderName);
        Path targetDir = Path.of("src", "image");

        try {
            Files.createDirectories(targetDir); // Make sure the target directory exists
            Files.list(sourceDir).forEach(path -> {
                try {
                    Path target = targetDir.resolve(path.getFileName());
                    Files.copy(path, target, StandardCopyOption.REPLACE_EXISTING); // Copy a file, replacing an existing file with the same name
                    System.out.println("Copied: " + path + " to " + target);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

