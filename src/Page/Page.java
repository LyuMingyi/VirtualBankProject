package Page;

import javax.swing.*;
import User.*;

/**
 * The type Page.
 */
public class Page extends JFrame {
    private String email;
    private User user;

    /**
     * Open page.
     */
    public void openPage() {};

    /**
     * Set email.
     *
     * @param user the user
     */
    public void setEmail(User user){
        this.email = user.getEmail();
    }

    /**
     * Get email string.
     *
     * @return the string
     */
    public String getEmail(){
        return email;
    }
}
