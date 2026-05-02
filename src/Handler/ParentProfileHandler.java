package Handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Page.*;

/**
 * Handles the action of transitioning to the profile page specifically designed for parents.
 * This class implements ActionListener to respond to GUI action events, managing the closure of the current page
 * and the opening of a new profile page tailored for parents.
 */
public class ParentProfileHandler implements ActionListener {
    private Page homepage;
    private Page currentpage;

    /**
     * Constructs a new ParentProfileHandler with the specified homepage and current page.
     * This handler facilitates the transition from any current page to the profile page designed for parents.
     *
     * @param homepage the homepage object of the user interface
     * @param currentpage the current page displayed to the user
     */
    public ParentProfileHandler(Page homepage, Page currentpage){
        this.homepage = homepage;
        this.currentpage = currentpage;
    }
    /**
     * Invoked when an action event is detected. This method handles the transition by disposing of the current
     * page and opening the ProfilePageForParent, thus facilitating profile management and viewing for parental users.
     *
     * @param e the event that triggers the transition.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ProfilePageForParent pfp = new ProfilePageForParent(homepage);
        currentpage.dispose();
        pfp.openPage();
    }
}
