package Handler;

import Page.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles the action of delivering tasks through the user interface.
 * This class listens to GUI action events and triggers the transition to a task delivery page.
 */
public class deliverTaskHandler implements ActionListener {
    private Page homepage;

    private Page setTaskPage;

    /**
     * Instantiates a new Deliver task handler.
     *
     * @param homepage    the homepage
     * @param setTaskPage the set task page
     */
    public deliverTaskHandler(Page homepage, Page setTaskPage){
        this.homepage = homepage;
        this.setTaskPage = setTaskPage;
    }
    /**
     * Responds to action events by opening the Deliver Task Page.
     * This method is triggered when an action event is fired, leading to the instantiation and display
     * of the DeliverTaskPage to facilitate the delivery of tasks by the user.
     *
     * @param e the event that triggers this action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        DeliverTaskPage dtp = new DeliverTaskPage(homepage, setTaskPage);
        dtp.openPage();
    }
}
