package Handler;
import Page.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The type Goal handler.
 */
public class GoalHandler implements ActionListener {
    private Page homepage;
    private Page currentpage;

    /**
     * Instantiates a new Goal handler.
     *
     * @param homepage    the homepage
     * @param currentpage the currentpage
     */
    public GoalHandler(Page homepage, Page currentpage){
        this.homepage = homepage;
        this.currentpage = currentpage;


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GoalPage gp = new GoalPage(homepage, currentpage);
        gp.openPage();
        currentpage.dispose();
    }
}
