package Handler;

import Page.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Handles mouse interactions for submitting tasks through a graphical user interface.
 * This class extends MouseAdapter and is specifically designed to respond to mouse events,
 * such as double-clicking on an item in a list to open a submission page for the selected task.
 */
public class SubmitHandler extends MouseAdapter {
    private DefaultListModel<String> listModel;
    private Page homepage;
    private Page getTaskPage;

    /**
     * Constructs a new SubmitHandler with the specified list model, homepage, and task retrieval page.
     *
     * @param listModel the list model that contains task items
     * @param homepage the homepage of the user interface
     * @param getTaskPage the page where tasks are retrieved and can be submitted
     */
    public SubmitHandler(DefaultListModel<String> listModel, Page homepage, Page getTaskPage){
        this.listModel = listModel;
        this.homepage = homepage;
        this.getTaskPage = getTaskPage;
    }
    /**
     * Handles mouse click events, specifically double clicks on an item in the list, to open a page
     * where the selected task can be submitted.
     * If the user double-clicks on an item, this method retrieves the task details from the list model,
     * identifies the clicked index, and opens the SubmitPage for the selected task.
     *
     * @param evt the mouse event that triggers this method
     */
    public void mouseClicked(MouseEvent evt) {
        JList list = (JList)evt.getSource();
        if (evt.getClickCount() == 2) { // 双击
            int index = list.locationToIndex(evt.getPoint());
            String selectedTaskDetails = listModel.get(index);
            SubmitPage submitPage = new SubmitPage(selectedTaskDetails, homepage, getTaskPage);
            submitPage.openPage();
        }
    }
}
