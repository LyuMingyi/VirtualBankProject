package Handler;
import Page.*;
import User.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles the action of transitioning from the current page to the set task page in a user interface.
 * This class implements ActionListener to respond to GUI action events, managing the closure of the current page
 * and the opening of a new page dedicated to task setting.
 */
public class SetTaskHandler implements ActionListener {
    private User user;
    private Page homepage;
    private Page current;

    /**
     * Constructs a new SetTaskHandler with the specified homepage and current page.
     * This handler facilitates the smooth transition from any given page to the page where tasks can be set.
     *
     * @param homepage The homepage object, from which the user will transition to the set task page.
     * @param current The current page object that will be disposed when transitioning to the set task page.
     */
    public SetTaskHandler(Page homepage, Page current){
        this.homepage = homepage;
        this.current = current;

    }
    /**
     * Invoked when an action event is detected. This method handles the transition by disposing of the current
     * page and opening the set task page, thus facilitating task management within the application.
     *
     * @param e the event that triggers the transition.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        homepage.dispose();
        SetTaskPage st = new SetTaskPage(homepage);
        st.openPage();
        current.dispose();
    }
}
