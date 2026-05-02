package User;
import AccountType.*;
import java.util.ArrayList;
import Task.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 * Represents a generic user in the system. This class serves as a base class for different types of users,
 * storing common information such as email, name, accounts, and tasks.
 */
public class User {
    private String email;
    private String name;
    private ArrayList<Account> accountList;
    private ArrayList<Task> taskList;

    private double taskMoney;

    /**
     * Retrieves the email address of the user.
     *
     * @return A string representing the user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return A string representing the user's name.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets account.
     *
     * @return the account
     */
    public ArrayList<Account> getAccount() {
        return accountList;
    }

    /**
     * Sets account.
     *
     * @param accounts the accounts
     */
    public void setAccount(ArrayList<Account> accounts) {
        this.accountList = accounts;
    }

    /**
     * Gets task list.
     *
     * @return the task list
     */
    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    /**
     * Sets task list.
     *
     * @param taskList the task list
     */
    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Adds a new task to the user's task list and creates a corresponding task file.
     * This method also handles directory creation if it does not exist and manages task file writing.
     *
     * @param taskName The name of the task to be added.
     * @param email    The email address of the user to whom the task is assigned, used for directory path construction.
     */
    public void addTask(String taskName, String email) {
        File dir = new File("registerTable" + email + "/Task");
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

        // Create new task and add to task list
        Task task = new Task(taskID, taskName, taskMoney);
        taskList.add(task);

        // Create a file in the directory and write task information
        File taskFile = new File(dir, taskID + ".txt");
        try {
            FileWriter writer = new FileWriter(taskFile);
            writer.write(task.getTaskID() + "\n");
            writer.write(task.getTaskName() + "\n");
            writer.write(task.getDone() + "\n");
            writer.write(task.getStatus() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
