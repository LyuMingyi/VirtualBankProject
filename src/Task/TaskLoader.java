package Task;

/**
 * The {@code TaskLoader} class is responsible for creating and initializing {@code Task} objects.
 * It provides methods to instantiate new tasks with specified parameters.
 */
public class TaskLoader {
    /**
     * Instantiates a new Task loader.
     */
    public TaskLoader(){}

    /**
     * Loads a new {@code Task} with the specified ID and name.
     * The task is initially set to not done and its status is set to "toBeReceived".
     *
     * @param taskID   the ID of the task
     * @param taskName the name of the task
     * @return the newly created {@code Task}
     */
    public Task load(int taskID, String taskName){
        Task task = new Task();
        task.setTaskID(taskID);
        task.setTaskName(taskName);
        task.setDone(false);
        task.setStatus("toBeReceived");
        return task;
    }
}
