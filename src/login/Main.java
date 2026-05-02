package login;

import java.awt.*;

/**
 * The {@code Main} class serves as the entry point for the application.
 * It initializes and displays the login form using {@code LoginForm}.
 */
public class Main {
    /**
     * The main method serves as the entry point of the application.
     * It schedules the initialization and visibility of {@code LoginForm} on the Event Dispatch Thread (EDT).
     *
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            LoginForm lf = new LoginForm();
            lf.setVisible(true);

        });
    }
}