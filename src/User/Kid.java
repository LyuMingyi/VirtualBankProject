package User;

import AccountType.Account;
import Task.Task;

import java.util.ArrayList;

/**
 * Represents a child user in a system where users may have different roles and attributes.
 * This class extends the generic User class, adding specific properties that relate to a child user, such as a parent's email.
 * It manages the child's accounts and tasks which can be assigned or supervised by a parent.
 */
public class Kid extends User {
    private String parentEmail;

    /**
     * Constructs a new Kid instance with specified details.
     * This constructor initializes a child user with their email, name, list of accounts, list of tasks, and parent's email.
     *
     * @param email       The child's email address, used as a unique identifier within the system.
     * @param name        The child's full name.
     * @param accounts    A list of Account objects representing the financial accounts the child has access to.
     * @param taskList    A list of Task objects representing tasks assigned to or created by the child.
     * @param parentEmail The parent's email address, used for linking the child to their parent's account and for notifications.
     */
    public Kid(String email, String name, ArrayList<Account> accounts, ArrayList<Task> taskList,String parentEmail){
        this.setEmail(email);
        this.setName(name);
        this.setAccount(accounts);
        this.setTaskList(taskList);
        this.setParentEmail(parentEmail);
    }
//    public Kid(){}

    /**
     * Retrieves the parent's email address.
     * This method is typically used when the system needs to send notifications or requires parental approval.
     *
     * @return The email address of the parent associated with this child.
     */
    public String getParentEmail(){
        return parentEmail;
    }

    /**
     * Sets or updates the parent's email address for this child.
     * This method allows updating the parent's email, which can be used for notifications or linking the child to a new parent.
     *
     * @param parentEmail The new parent email to be associated with this child.
     */
    public void setParentEmail(String parentEmail){
        this.parentEmail = parentEmail;
    }
}
