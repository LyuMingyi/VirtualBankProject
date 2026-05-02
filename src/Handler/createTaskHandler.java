package Handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Page.*;

/**
 * Handles the action of creating a new task from the user interface.
 * This class listens to GUI action events and triggers the transition to a task creation page.
 */
public class createTaskHandler implements ActionListener {
    private Page homepage;
    private Page setTaskPage;

    /**
     * Instantiates a new Create task handler.
     *
     * @param homepage    the homepage
     * @param setTaskPage the set task page
     */
    public createTaskHandler(Page homepage, Page setTaskPage){
        this.homepage = homepage;
        this.setTaskPage = setTaskPage;
    }
    /**
     * Responds to action events by opening the Create Task Page.
     * This method is triggered when an action event is fired, leading to the instantiation and display
     * of the CreateTaskPage to facilitate new task creation by the user.
     *
     * @param e the event that triggers this action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        CreateTaskPage ctp = new CreateTaskPage(homepage, setTaskPage);
        ctp.openPage();
    }
}
