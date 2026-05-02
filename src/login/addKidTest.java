package login;

/**
 * The type Add kid test.
 */
public class addKidTest {


    /**
     * The main method is the entry point of the application. It initializes {@code AccountInfoReader}
     * and {@code AccountInfoWriter} objects and uses them to establish parent-child relationships
     * between accounts.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        AccountInfoReader ar = new AccountInfoReader();
        AccountInfoWriter aw = new AccountInfoWriter();
        aw.writeRelationship("parent@qmul.ac.uk", "Tom@qmul.ac.uk");
        aw.writeRelationship("parent@qmul.ac.uk", "Jack@qmul.ac.uk");
        aw.writeRelationship("parent@qmul.ac.uk", "Bob@qmul.ac.uk");
    }
}
