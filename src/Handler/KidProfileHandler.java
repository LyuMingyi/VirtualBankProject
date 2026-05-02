package Handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Page.*;

/**
 * Handles the action of transitioning to a profile page specifically designed for kids.
 * This class listens to GUI action events, and directs the user interface to a kid-oriented profile page,
 * disposing of the current page in the process.
 */
public class KidProfileHandler implements ActionListener {
    private Page homepage;
    private Page currentpage;

    /**
     * Constructs a new KidProfileHandler with the specified homepage and current page.
     *
     * @param homepage the homepage object of the user interface
     * @param currentpage the current page displayed to the user
     */
    public KidProfileHandler(Page homepage, Page currentpage){
        this.homepage = homepage;
        this.currentpage = currentpage;
    }
    /**
     * Responds to action events by disposing the current page and opening a new Profile Page tailored for kids.
     * This method is triggered when an action event is fired, managing the transition to the ProfilePageForKid,
     * thus providing an interface suited for the needs and capabilities of younger users.
     *
     * @param e the event that triggers this action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ProfilePageForKid pfk = new ProfilePageForKid(homepage);
        currentpage.dispose();
        pfk.openPage();
    }
}