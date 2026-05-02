package Task;

/**
 * The {@code test} class contains the main method which serves as the entry point for the application.
 * This class is used to demonstrate the creation and manipulation of {@code Task} objects.
 */
public class test {
    /**
     * The entry point of application.
     * This method creates a {@code Task} object, prints its status and completion status, updates the task status, and prints the updated status.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Task task = new Task(1,"My_task",30.00);
        System.out.println(task.getStatus());
        System.out.println(task.getDone());
        task.setStatus("received");
        System.out.println(task.getStatus());
    }
}
