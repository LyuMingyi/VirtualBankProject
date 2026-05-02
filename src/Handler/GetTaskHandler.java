package Handler;

import Page.*;
import User.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles the retrieval of tasks through the user interface.
 * This class listens to GUI action events and triggers the transition to a page where tasks can be retrieved.
 */
public class GetTaskHandler implements ActionListener {
    private User user;
    private Page homepage;
    private Page currentpage;

    /**
     * Instantiates a new Get task handler.
     *
     * @param homepage    the homepage
     * @param currentpage the currentpage
     */
    public GetTaskHandler(Page homepage, Page currentpage){
        this.homepage = homepage;
        this.currentpage = currentpage;
    }
    /**
     * Responds to action events by disposing the current page and opening the Get Task Page.
     * This method is triggered when an action event is fired, leading to the closure of the current page
     * and the opening of the GetTaskPage to facilitate task retrieval by the user.
     *
     * @param e the event that triggers this action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        currentpage.dispose();
        GetTaskPage st = new GetTaskPage(homepage);
        st.openPage();
    }
}
