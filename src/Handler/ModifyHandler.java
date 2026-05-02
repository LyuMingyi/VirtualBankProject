package Handler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.swing.border.EmptyBorder;
import Page.*;

/**
 * Handles mouse interactions for modifying tasks through a graphical user interface.
 * This class extends MouseAdapter and is specifically designed to respond to mouse events,
 * such as double-clicking on an item in a list to open a modification page for the selected task.
 */
public class ModifyHandler extends MouseAdapter {
    private DefaultListModel<String> listModel;
    private Page homepage;
    private Page setTaskPage;

    /**
     * Instantiates a new Modify handler.
     *
     * @param listModel   the list model
     * @param homepage    the homepage
     * @param setTaskPage the set task page
     */
//private JList list;
    public ModifyHandler(DefaultListModel<String> listModel, Page homepage, Page setTaskPage){
        this.listModel = listModel;
        this.homepage = homepage;
        this.setTaskPage = setTaskPage;
    }
    /**
     * Handles mouse click events, specifically double clicks on an item in the list, to open a page
     * where the selected task can be modified.
     * If the user double-clicks on an item, this method retrieves the task details from the list model,
     * identifies the clicked index, and opens the ModifyPage for the selected task.
     *
     * @param evt the mouse event that triggers this method
     */
    public void mouseClicked(MouseEvent evt) {
        JList list = (JList)evt.getSource();
        if (evt.getClickCount() == 2) { // 双击
            int index = list.locationToIndex(evt.getPoint());
            String selectedTaskDetails = listModel.get(index);
            ModifyPage modifyPage = new ModifyPage(selectedTaskDetails, homepage, setTaskPage);
            modifyPage.openPage();
        }
    }
}
