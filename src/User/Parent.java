package User;

import AccountType.Account;
import Task.Task;

import java.util.ArrayList;

/**
 * Represents a parent user in a system where users may have roles such as managing other users (children).
 * This class extends the generic User class, adding specific properties and methods for a parent, such as managing a list of children.
 */
public class Parent extends User {
    private ArrayList<String> kidsList;

    /**
     * Constructs a new Parent instance with specified details.
     * This constructor initializes a parent user with their email, name, list of accounts, list of tasks, and a list of their children's identifiers.
     *
     * @param email    The parent's email address, used as a unique identifier within the system.
     * @param name     The parent's full name.
     * @param accounts A list of Account objects representing the financial accounts the parent has access to.
     * @param taskList A list of Task objects representing tasks that may be assigned to their children or managed by the parent.
     * @param kidsList A list of strings representing the identifiers (usually emails) of the children under this parent's care.
     */
    public Parent(String email, String name, ArrayList<Account> accounts, ArrayList<Task> taskList,ArrayList<String> kidsList) {
        this.setEmail(email);
        this.setName(name);
        this.setAccount(accounts);
        this.setTaskList(taskList);
        this.setKidsList(kidsList);
    }

    /**
     * Retrieves the list of children's identifiers managed by this parent.
     * This list is typically used by the system for tasks such as assigning tasks, sending notifications, or managing permissions.
     *
     * @return An ArrayList of Strings representing the identifiers of the children under this parent's care.
     */
    public ArrayList<String> getKidsList(){
        return kidsList;
    }

    /**
     * Sets or updates the list of children's identifiers for this parent.
     * This method allows the dynamic updating of the children list, which can be necessary as the family situation changes.
     *
     * @param kids An ArrayList of Strings representing the new list of children's identifiers to be managed by the parent.
     */
    public void setKidsList(ArrayList<String> kids){
        this.kidsList = kids;
    }
}
