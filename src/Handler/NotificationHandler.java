package Handler;

import Page.*;
import User.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles actions to manage notifications within the user interface.
 * This class listens to action events that trigger transitions to a notification page, disposing
 * of the current page and any other associated resources.
 */
public class NotificationHandler implements ActionListener {
    private User user;
    private Page homepage;
    private Page current;

    /**
     * Instantiates a new Notification handler.
     *
     * @param homepage the homepage
     * @param current  the current
     */
    public NotificationHandler(Page homepage, Page current){
        this.homepage = homepage;
        this.current = current;
    }
    /**
     * Responds to action events by disposing of the homepage and current page, and opening a new
     * NotificationPage.
     * This method is triggered when an action event is fired, effectively managing the transition
     * to a dedicated notification page and ensuring the user interface is refreshed appropriately.
     *
     * @param e the event that triggers this action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        homepage.dispose();
        NotificationPage np = new NotificationPage(homepage);
        np.openPage();
        current.dispose();
    }
}
