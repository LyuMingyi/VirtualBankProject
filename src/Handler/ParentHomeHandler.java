package Handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Page.*;
import User.*;
import login.AccountInfoReader;

/**
 * Handles the action of transitioning to the homepage specifically designed for parents.
 * This class listens to GUI action events, retrieves parent-specific account information, and directs to a
 * parent-oriented homepage, disposing of the current page.
 */
public class ParentHomeHandler implements ActionListener {
    private Page homepage;
    private Page currentPage;

    /**
     * Instantiates a new Parent home handler.
     *
     * @param homepage    the homepage
     * @param currentPage the current page
     */
    public ParentHomeHandler(Page homepage, Page currentPage){
        this.homepage = homepage;
        this.currentPage = currentPage;
    }
    /**
     * Responds to action events by loading parent-specific data, transitioning to the parent's homepage, and
     * disposing of the current page.
     * This method is triggered when an action event is fired, using the email associated with the homepage
     * to load parent data and open a new homepage tailored for parents, enhancing the user experience for parental users.
     *
     * @param e the event that triggers this action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String email = homepage.getEmail();
        AccountInfoReader ar = new AccountInfoReader();
        Parent parent = ar.parentLoader(email);
        homePageForParent hp = new homePageForParent(parent);
        hp.openPage();
        currentPage.dispose();
    }
}
