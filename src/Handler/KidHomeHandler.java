package Handler;

import Page.*;
import User.*;
import login.AccountInfoReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles the transition to the homepage specifically designed for kids.
 * This class listens to GUI action events, retrieves kid-specific account information, and directs to a
 * kid-oriented homepage, disposing of the current page.
 */
public class KidHomeHandler implements ActionListener {
    private Page homepage;
    private Page currentPage;

    /**
     * Instantiates a new Kid home handler.
     *
     * @param homepage    the homepage
     * @param currentPage the current page
     */
    public KidHomeHandler(Page homepage, Page currentPage){
        this.homepage = homepage;
        this.currentPage = currentPage;
    }
    /**
     * Responds to action events by loading kid-specific data, transitioning to the kid's homepage, and
     * disposing of the current page.
     * This method is triggered when an action event is fired, using the email associated with the homepage
     * to load kid data and open a new homepage tailored for kids, enhancing the user experience for younger users.
     *
     * @param e the event that triggers this action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String email = homepage.getEmail();
        AccountInfoReader ar = new AccountInfoReader();
        Kid kid = ar.kidLoader(email);
        homePageForKid hp = new homePageForKid(kid);
        hp.openPage();
        currentPage.dispose();
    }
}
