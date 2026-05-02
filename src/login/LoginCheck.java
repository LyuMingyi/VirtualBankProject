package login;

/**
 * The interface Login check.
 */
public interface LoginCheck {
    /**
     * Authenticate boolean.
     *
     * @param email    the email
     * @param password the password
     * @return the boolean
     */
    boolean authenticate(String email, String password);
}
