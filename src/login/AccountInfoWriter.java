package login;

import Task.Task;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.*;
import User.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The {@code AccountInfoWriter} class is responsible for writing and updating user data in the system.
 * It handles operations such as creating new user accounts, writing task information, and managing relationships between accounts.
 */
public class AccountInfoWriter {

    /**
     * Writes account information into the system. This method checks for duplicate accounts before creating a new one.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @param id       the ID of the user
     * @param group    the group (e.g., 'parent' or 'kid')
     * @return true if the account was successfully created, false otherwise
     */
    public Boolean writeAccountInfo(String email, String password, String id, String group) {
        String registerDirectoryPath = "./registerTable";
        File emailDirectory = new File(registerDirectoryPath, email);

        // Creating an email folder
        if (!examDuplicate(email)) {
            emailDirectory.mkdirs();
        }

        // Create info.txt file in the email folder
        File infoFile = new File(emailDirectory, "info.txt");

        // Create AccountFile, AccountName&Password and Task folders.
        File accountFileDir = new File(emailDirectory, "AccountFile");
        if (!accountFileDir.exists()) {
            accountFileDir.mkdirs();
        }
        File accountNamePasswordDir = new File(emailDirectory, "AccountName&Password");
        if (!accountNamePasswordDir.exists()) {
            accountNamePasswordDir.mkdirs();
        }
        File taskDir = new File(emailDirectory, "Task");
        if (!taskDir.exists()) {
            taskDir.mkdirs();
        }

        // Using the try-with-resources statement to automatically close FileWriter
        try (FileWriter writer = new FileWriter(infoFile, true)) {  // ‘true’ means write the file in append mode
            if (email != null && password != null && id != null && group != null) {
                writer.write(email + "," + password + "," + id + "," + group + System.lineSeparator());
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
            return false;
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Testing the write function, using sample data
        AccountInfoWriter accountInfoWriter = new AccountInfoWriter();
        accountInfoWriter.writeAccountInfo("user1@example.com", "password123", "UserID123","parent");
    }

    /**
     * Checks for duplicate emails in the system to prevent overlapping accounts.
     *
     * @param email the email to check
     * @return true if the email already exists, false otherwise
     */
    public boolean examDuplicate(String email){
        String registerDirectoryPath = "./registerTable";
        String emailFilePath = findEmail(email,registerDirectoryPath);
        if(emailFilePath == null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Finds the directory path for the given email in the registration table.
     *
     * @param email         the email
     * @param registerTable the register table
     * @return the directory path as a string if found, null otherwise
     */
    public String findEmail(String email,String registerTable){
        // Create a File object representing the path to the registerTable.
        File directory = new File(registerTable);

        // Make sure the path provided is a directory
        if (directory.isDirectory()) {
            // Get the names of all files and folders in a directory
            String[] subdirs = directory.list();

            // Check that each name matches the email
            for (String subdir : subdirs) {
                File folder = new File(directory, subdir);
                // Make sure it's a folder and the name matches the email.
                if (folder.isDirectory() && subdir.equals(email)) {
                    return folder.getAbsolutePath();  // Returns the absolute path to the matching folder
                }
            }
        }
        return null;  // Returns null if not found
    }

    /**
     * Writes a task to the user's task list.
     *
     * @param taskName the name of the task
     * @param email    the email of the user
     * @param money    the monetary value of the task
     */
// Creates a task and writes it to the user's taskList.
    public void writeTask(String taskName, String email, double money) {
        File dir = new File("./registerTable/" + email + "/Task/");
        int taskID = 1;
        if (dir.exists()) {
            // Get all files in a directory and find the largest file name
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    int currentID = Integer.parseInt(file.getName().replace(".txt", ""));
                    if (currentID >= taskID) {
                        taskID = currentID + 1;
                    }
                }
            }
        } else {
            // If the directory does not exist, create it
            dir.mkdirs();
        }

        // Create a new task and add it to the task list
        Task task = new Task(taskID, taskName, money);
        // Creating a file in a directory and writing task information
        File taskFile = new File(dir, taskID + ".txt");
        try {
            FileWriter writer = new FileWriter(taskFile);
            writer.write(task.getTaskID() + "\n");
            writer.write(task.getTaskName() + "\n");
            writer.write(task.getDone() + "\n");
            writer.write(task.getStatus() + "\n");
            writer.write(task.getMoney() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establishes a parent-child relationship in the system by writing to their respective files.
     *
     * @param parentEmail the email of the parent
     * @param kidsEmail   the email of the child
     */
    public void writeRelationship(String parentEmail, String kidsEmail) {
        // Write information about parents
        String parentFilePath = "./registerTable/" + parentEmail + "/info.txt";
        writeInfoForPar(parentFilePath, kidsEmail, "this kid has been writed");

        // Write child information
        String kidsFilePath = "./registerTable/" + kidsEmail + "/info.txt";
        writeInfoForKid(kidsFilePath, parentEmail, "this kid has a parent");
    }

    private void writeInfoForPar(String filePath, String emailToWrite, String errorMessage) {
        try {
            Path path = Paths.get(filePath);
            // Make sure the file exists
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }

            List<String> lines = Files.readAllLines(path);
            // Check if the same mailbox has already been written to
            if (lines.contains(emailToWrite)) {
                System.out.println(errorMessage);
                return;
            }

            // Find the latest empty line and write
            boolean written = false;
            for (int i = lines.size() - 1; i >= 0; i--) {
                if (lines.get(i).trim().isEmpty()) {
                    lines.set(i, emailToWrite);
                    written = true;
                    break;
                }
            }
            if (!written) {
                lines.add(emailToWrite); // If no empty line is found, add at the end
            }

            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void writeInfoForKid(String filePath, String emailToWrite, String errorMessage) {
        try {
            Path path = Paths.get(filePath);
            // Make sure the file exists
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }

            List<String> lines = Files.readAllLines(path);

            // For the child's information, check if there are already two rows of data
            if (filePath.contains("/info.txt") && lines.size() >= 2) {
                System.out.println(errorMessage);
                return;
            }

            // Find the latest empty line and write
            boolean written = false;
            for (int i = lines.size() - 1; i >= 0; i--) {
                if (lines.get(i).trim().isEmpty()) {
                    lines.set(i, emailToWrite);
                    written = true;
                    break;
                }
            }
            if (!written) {
                lines.add(emailToWrite); // If no empty line is found, add at the end
            }

            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Delivers a task from a parent to a child.
     *
     * @param parentEmail the email of the parent
     * @param kidEmail    the email of the child
     * @param task        the task to be delivered
     */
    public void deliverTask(String parentEmail, String kidEmail, Task task) {
        String parentTaskPath = "./registerTable/" + parentEmail + "/Task/";
        String kidTaskPath = "./registerTable/" + kidEmail + "/Task/";
        String taskFileName = task.getTaskID() + ".txt";
        File parentTaskFile = new File(parentTaskPath + taskFileName);

        if (parentTaskFile.exists()) {
            try {
                // Read the contents of the file and change the fourth line to ‘received’.
                List<String> lines = Files.readAllLines(Paths.get(parentTaskPath, taskFileName));
                lines.set(3, "received"); // The index of the fourth row is 3
                Files.write(Paths.get(parentTaskPath, taskFileName), lines);

                // Make sure your child's task catalogue exists
                File kidTaskDir = new File(kidTaskPath);
                if (!kidTaskDir.exists()) {
                    kidTaskDir.mkdirs();
                }

                // Copying files to the child's task directory
                File kidTaskFile = new File(kidTaskPath + taskFileName);
                Files.copy(parentTaskFile.toPath(), kidTaskFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Modifies an existing task's details.
     *
     * @param email    the email of the user whose task is to be modified
     * @param taskID   the ID of the task to modify
     * @param taskName the new name of the task
     * @param award    the new monetary reward for the task
     */
    public void taskModifier(String email, String taskID, String taskName, Double award) {
        String filePath = "./registerTable/" + email + "/Task/" + taskID + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String[] lines = new String[5];
            for (int i = 0; i < 5; i++) {
                lines[i] = reader.readLine();
            }

            // Modify the second and fifth lines
            lines[1] = taskName;
            lines[4] = award.toString();

            // Write the modified lines back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Deletes a task from the user's task file.
     *
     * @param email  the email of the user
     * @param taskID the ID of the task to delete
     */
    public void taskDeleter(String email, String taskID) {
        // Build file path
        String path = "./registerTable/" + email + "/Task/" + taskID + ".txt";
        // Creating File Objects
        File file = new File(path);
        // Check if the file exists and is not a directory
        if (file.exists() && !file.isDirectory()) {
            // If the file exists, delete it
            if (file.delete()) {
                System.out.println("文件 " + taskID + ".txt 已被删除");
            } else {
                System.out.println("文件 " + taskID + ".txt 删除失败");
            }
        } else {
            System.out.println("文件 " + taskID + ".txt 不存在");
        }
    }

    /**
     * Submits a task for verification by a parent, creating a notification for the parent about the task submission.
     *
     * @param kidEmail    the email of the child who completed the task
     * @param parentEmail the email of the parent to notify
     * @param taskDetail  detailed information about the task
     */
    public void taskSubmit(String kidEmail, String parentEmail, String taskDetail) {
        // Create AccountInfoReader and Kid objects within the method
        AccountInfoReader ar = new AccountInfoReader();
        Kid kid = ar.kidLoader(kidEmail);
        String kidName = kid.getName();

        // Extract Task ID using regular expression from taskDetail
        Pattern pattern = Pattern.compile("Task ID:</font></b> (\\d+)<br>");
        Matcher matcher = pattern.matcher(taskDetail);
        String taskID = "";
        if (matcher.find()) {
            taskID = matcher.group(1);
        }

        // Get current date and time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = dtf.format(now);

        // Construct notification content
        String notificationContent = kidName + " completed Task ID: " + taskID + " on " + dateTime + ".\n" +
                /*"Task Requirement: " + stripHtml(taskDetail) + "\n" +*/
                "From: " + kidEmail + "\n" +
                "Task Award: " + extractAward(taskDetail);

        // Create file path
        Path path = Paths.get("./registerTable/" + parentEmail + "/notification_" + taskID + ".txt");

        // Write to file
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, notificationContent.getBytes());
            System.out.println("Notification file created successfully.");
        } catch (IOException e) {
            System.err.println("Error while creating notification file: " + e.getMessage());
        }
    }

    // Extract award from taskDetail
    private String extractAward(String taskDetail) {
        Pattern pattern = Pattern.compile("Award: ([\\d.]+\\$)");
        Matcher matcher = pattern.matcher(taskDetail);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "N/A";
    }

    // Remove HTML tags from taskDetail
    private String stripHtml(String html) {
        return html.replaceAll("<[^>]*>", "");
    }


}

