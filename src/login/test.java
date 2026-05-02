package login;

import AccountType.Account;
import Task.Task;
import java.util.ArrayList;
import java.util.List;

import User.*;

/**
 * The {@code test} class serves as a testing environment for various methods and functionalities related to account and task management.
 * It includes methods for writing relationships, tasks, modifying tasks, deleting tasks, and loading account information.
 */
public class test {

    /**
     * The entry point of application, where various functionalities related to account and task management are tested.
     *
     * @param args the input arguments (not used)
     */
    public static void main(String[] args) {
        AccountInfoWriter ai = new AccountInfoWriter();
        // ai.writeRelationship("parent@qmul.ac.uk", "kid@qmul.ac.uk");
        //ai.writeTask("do housework", "parent@qmul.ac.uk", 10.00);
        //ai.writeTask("get 90 on math", "parent@qmul.ac.uk", 20.00);
//        ai.writeTask("wash the dishes", "parent@qmul.ac.uk", 4.00);
//        ai.writeTask("clean the garbage bin", "parent@qmul.ac.uk", 6.00);
//        ai.writeTask("wash father's clothes", "parent@qmul.ac.uk", 8.00);
//        ai.writeTask("make dinner for mom once", "parent@qmul.ac.uk", 10.00);
        // AccountInfoReader ar = new AccountInfoReader();
        // ArrayList<Task> task = ar.loadTask("parent@qmul.ac.uk");
        // ai.deliverTask("parent@qmul.ac.uk","kid@qmul.ac.uk", task.get(1));

        //ai.taskModifier("parent@qmul.ac.uk", "3", "make a new friend", 2.0);
        //ai.taskDeleter("parent@qmul.ac.uk", "10");

        /*AccountInfoReader ar = new AccountInfoReader();
        Parent p = ar.parentLoader("parent@qmul.ac.uk");
        ArrayList<String> kidsList = p.getKidsList();
        int i = 0;
        try{
            while (kidsList.get(i) != null){
                System.out.println(kidsList.get(i) + "\n");
                i++;
            }
        }catch (IndexOutOfBoundsException e){
            System.out.println("finish!");
        }

        ArrayList<String> kidNameList = ar.kidNameLoader(kidsList);
        int j = 0;
        try{
            while (kidNameList.get(j) != null){
                System.out.println(kidNameList.get(j) + "\n");
                j++;
            }
        }catch (IndexOutOfBoundsException e){
            System.out.println("finish!");
        }
    }*/

        AccountInfoReader ar = new AccountInfoReader();
        //ar.accountListLoader("kid@qmul.ac.uk");
        //Account account = ar.accountListLoader("kid@qmul.ac.uk").get(0);
        //account.getAccountName();
        List<String> list = ar.loadAccountList("kid@qmul.ac.uk");
        int i = 0;
        try{
            while(true) {
                System.out.println(list.get(i));
                i++;
            }
        }catch (IndexOutOfBoundsException e){
            System.out.println("finish!");
        }




    }
}
