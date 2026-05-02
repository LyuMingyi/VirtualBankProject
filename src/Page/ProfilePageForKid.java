package Page;

import Handler.*;
import User.Kid;
import login.AccountInfoReader;

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
 * The type Profile page for kid.
 */
public class ProfilePageForKid extends Page {
    private Page homepage;
    private Kid kid;
    private JLabel profileImageLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel kidsLabel;

    /**
     * Instantiates a new Profile page for kid.
     *
     * @param homepage the homepage
     */
    public ProfilePageForKid(Page homepage) {
        this.homepage = homepage;
        AccountInfoReader ar = new AccountInfoReader();
        this.kid = ar.kidLoader(homepage.getEmail());
    }


    /**
     * Opens the profile page for the kid user.
     */
    public void openPage() {
        Page frame = new Page();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        GlassPane glassPane = new GlassPane("src/image/profileBackground.jpg", 0.5f);
        glassPane.setLayout(new BorderLayout());
        backgroundPanel.add(glassPane, BorderLayout.CENTER);
        JPanel topMenuBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topMenuBar.setOpaque(false);
        JButton homeButton = new JButton("🏠 Home");
        JButton taskButton = new JButton("💪 Task");
        JButton profileButton = new JButton("📭 Profile");
        JButton goalButton = new JButton("🎯 Goal");

        topMenuBar.add(homeButton);
        topMenuBar.add(taskButton);
        topMenuBar.add(goalButton);
        topMenuBar.add(profileButton);

        homeButton.addActionListener(new KidHomeHandler(homepage, frame));
        taskButton.addActionListener(new GetTaskHandler(homepage, frame));
        goalButton.addActionListener(new GoalHandler(homepage, frame));

        glassPane.add(topMenuBar, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        glassPane.add(centerPanel, BorderLayout.CENTER);

        handleProfileImage(centerPanel, gbc, kid.getEmail(), frame);
        addProfileLabels(centerPanel, gbc, kid.getName(), kid.getEmail());
        handleParent(centerPanel, gbc, kid.getParentEmail());


        themeButton(centerPanel, gbc, frame);

        frame.setVisible(true);
    }
    // Private helper methods for GUI components
    /**
     * Handles the display and update of the profile image.
     *
     * @param panel the panel where the profile image will be displayed
     * @param gbc   the GridBagConstraints for layout control
     * @param email the email of the kid user
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
     * Copies the default profile image to the user directory if it does not exist.
     *
     * @param email the email of the kid user
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
     * Adds name and email labels to the profile panel.
     *
     * @param panel the panel where the labels will be added
     * @param gbc   the GridBagConstraints for layout control
     * @param name  the name of the kid user
     * @param email the email of the kid user
     */
    private void addProfileLabels(JPanel panel, GridBagConstraints gbc, String name, String email) {
        nameLabel = new JLabel("<html><h1 style='font-size:18px;'><b>Name:</b> " + name + "</h1></html>");
        emailLabel = new JLabel("<html><h1 style='font-size:18px;'><b>Email:</b> " + email + "</h1></html>");
        panel.add(nameLabel, gbc);
        panel.add(emailLabel, gbc);
    }

    /**
     * Handles the display of parent information.
     *
     * @param panel       the panel where the parent information will be displayed
     * @param gbc         the GridBagConstraints for layout control
     * @param parentEmail the email of the parent associated with the kid user
     */
    private void handleParent(JPanel panel, GridBagConstraints gbc, String parentEmail) {
        JLabel parentLabel = new JLabel("<html><h1 style='font-size:18px;'><b>My Parent:</b></h1><br/>" + parentEmail + "</html>");
        panel.add(parentLabel, gbc);
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
                    ProfilePageForKid pfk = new ProfilePageForKid(homepage);
                    pfk.openPage();
                }
            });

            frame.add(label); // Add to JFrame
        }

        // Center the JFrame window on the screen
        frame.setLocationRelativeTo(null);

        // Set JFrame visible
        frame.setVisible(true);
    }
    /**
     * Opens the corresponding theme directory, copies theme files, and updates GUI.
     *
     * @param folderName the name of the theme folder
     */
    // How to open the corresponding directory and copy the files
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

