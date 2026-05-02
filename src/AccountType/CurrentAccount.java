package AccountType;

import java.io.FileWriter;
import java.io.IOException;import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Represents a current account type in a banking system.
 * This class extends a general Account class and includes functionalities specific to a current account,
 * such as initializing with a unique account number and managing account creation with time stamps.
 */
public class CurrentAccount extends Account {

    /**
     * Default constructor for creating a new CurrentAccount with an initial balance of 0.0 and a random account number.
     * The account number is prefixed with "CUR" and suffixed with ".txt", indicating a text file storage format.
     */
// 构造方法
    public CurrentAccount() {
        setBalance(0.0);
        setAccountNumber("CUR" + Random() + ".txt");
    }

    /**
     * Constructs a new CurrentAccount with specified details.
     * Allows setting initial balance, account name, account password, and a predefined account number.
     *
     * @param balance         the balance
     * @param accountName     the account name
     * @param accountPassword the account password
     * @param accountNumber   the account number
     */
    public CurrentAccount(double balance,String accountName,String accountPassword,String accountNumber){
        setBalance(0.0);
        setAccountNumber("CUR" + Random() + ".txt");
        this.setBalance(balance);
        this.setAccountName(accountName);
        this.setAccountPassword(accountPassword);
        this.setAccountNumber(accountNumber);
    }

    /**
     * Creates an account file in the system with initial details and a timestamp.
     * The method constructs a file path based on the provided email and writes the initial account balance and
     * creation timestamp into the file.
     *
     * @param email the email associated with the account, used to construct the file path
     * @return the filepath where the account details are stored
     */
    public String createAccount(String email) {
        String directoryPath = "./registerTable/" + email + "/AccountFile/";// Directory path based on the user's email
        String filepath = directoryPath + getAccountNumber();// Complete file path with the account number
        try (FileWriter writer = new FileWriter(filepath)) {
            // Write the initial balance and creation timestamp to the file
            writer.write("Balance:" + getBalance()+ "\n" + "Create:" +getCurrentTimeString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filepath;
    }

    /**
     * Generates a formatted string representing the current date and time.
     * This method utilizes the 'DateTimeFormatter' to format the current date and time as a string.
     *
     * @return a string representation of the current date and time, formatted as "yyyy-MM-dd HH:mm:ss"
     */
    public static String getCurrentTimeString() {
        // get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // create a date form
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // formalization the time form
        String formattedDateTime = now.format(formatter);

        // Returns a formatted datetime string
        return formattedDateTime;
    }

}
