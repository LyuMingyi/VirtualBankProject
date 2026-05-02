package Task;

/**
 * The {@code Task} class represents a task with an ID, name, status, and monetary value.
 * It includes methods to get and set the attributes of a task.
 * The status of a task can either be "received" or "toBeReceived".
 */
public class Task {
    private int taskID;
    private String taskName;
    private Boolean isDone;
    private String status; //represents the state of the task, either = received or toBeReceived.

    private double money;

    /**
     * Instantiates a new Task with default values
     */
    public Task(){}

    /**
     * Instantiates a new Task with the specified ID, name, and monetary value.
     * The task is initially set to not done and the status is "toBeReceived".
     *
     * @param taskID   the ID of the task
     * @param taskName the name of the task
     * @param money    the monetary value of the task
     */
    public Task(int taskID, String taskName, double money) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.isDone = false;
        this.status = "toBeReceived";
        this.money = money;
    }

    /**
     * Gets the monetary value of the task.
     *
     * @return the monetary value of the task
     */
    public double getMoney() {
        return money;
    }

    /**
     * Sets the monetary value of the task.
     *
     * @param money the monetary value to set
     */
    public void setMoney(double money) {
        this.money = money;
    }


    /**
     * Gets the ID of the task.
     *
     * @return the ID of the task
     */
    public int getTaskID() {
        return taskID;
    }

    /**
     * Sets the ID of the task.
     *
     * @param taskID the ID to set
     */
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    /**
     * Gets the name of the task.
     *
     * @return the name of the task
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Sets the name of the task.
     *
     * @param taskName the name to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Gets the completion status of the task.
     *
     * @return {@code true} if the task is done, {@code false} otherwise
     */
    public Boolean getDone() {
        return isDone;
    }

    /**
     * Sets the completion status of the task.
     *
     * @param done {@code true} if the task is done, {@code false} otherwise
     */
    public void setDone(Boolean done) {
        isDone = done;
    }

    /**
     * Gets the status of the task.
     * The status can either be "received" or "toBeReceived".
     *
     * @return the status of the task
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the task. The status must be either "received" or "toBeReceived".
     * If an invalid status is provided, an error message is printed.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        if(!status.equals("received") && !status.equals("toBeReceived")){
            System.out.println("the status is invalid!");
        }else{
            this.status = status;
        }
    }
}
