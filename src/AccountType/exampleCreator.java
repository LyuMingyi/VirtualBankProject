package AccountType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;


/**
 * A utility class to create and manage examples of financial transactions for demonstration or testing purposes.
 * This class simulates transactions like transfers, deposits, withdrawals, and tasks, and logs them into account files.
 */
public class exampleCreator {
    private static final String[] logTypes = {"Transfer as sender", "Transfer as receiver", "Deposit", "Withdraw", "Task"};
    private static final Random random = new Random();


    /**
     * The main method to generate transaction logs.
     * It cycles through each year and month within a specified range, randomly generating transactions for a set number of days per month.
     *
     * @param args the input arguments (not used in this application)
     */
    public static void main(String[] args) {
        String childEmail = "kid@qmul.ac.uk";
        String childAccountNumber = "CUR18525953.txt";
        String otherEmail = "parent@qmul.ac.uk";
        String otherAccountNumber = "CUR58010701.txt";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Random random = new Random();

        for (int year = 2020; year <= 2024; year++) { // Flips the position level of the next cycle relative to the end of the first cycle ...
            for (int month = 1; month <= 12; month++) { // Cycle every month
                int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth(); // The number of days in the month
                int numberOfDaysToGenerate = 20; // A random date of 3 days is generated each month

                for (int i = 0; i < numberOfDaysToGenerate; i++) {
                    int day = random.nextInt(daysInMonth) + 1; //  A random day of the month is chosen
                    LocalDate date = LocalDate.of(year, month, day);
                    LocalDateTime dateTime = date.atStartOfDay(); // Convert to the time at the start of the day
                    String formattedDateTime = dateTime.format(formatter);
                    double randomAmount = 10 + (1000 - 10) * random.nextDouble();
                    String formattedAmount = String.format("%.2f", randomAmount);
                    randomAmount = Double.parseDouble(formattedAmount);
                    String logType = logTypes[random.nextInt(logTypes.length)];

                    switch (logType) {
                        case "Transfer as sender":
                            setTransferLog(childEmail, childAccountNumber, otherEmail, otherAccountNumber, randomAmount, formattedDateTime);
                            break;
                        case "Transfer as receiver":
                            setTransferLog(otherEmail, otherAccountNumber, childEmail, childAccountNumber, randomAmount, formattedDateTime);
                            break;
                        case "Deposit":
                            setDepositLog(childEmail, childAccountNumber, randomAmount, formattedDateTime);
                            break;
                        case "Withdraw":
                            setWithdrawLog(childEmail, childAccountNumber, randomAmount, formattedDateTime);
                            break;
                        case "Task":
                            String taskId = "Task-" + (i + 1);
                            setTaskLog(childEmail, childAccountNumber, taskId, randomAmount, formattedDateTime);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Logs a transfer transaction as both a sender and a receiver.
     * This method logs the transaction in both the sender's and the receiver's account files.
     *
     * @param senderEmail           the email of the account sending the funds
     * @param senderAccountNumber   the account number of the sender
     * @param receiverEmail         the email of the account receiving the funds
     * @param receiverAccountNumber the account number of the receiver
     * @param amount                the transaction amount
     * @param date                  the date and time of the transaction formatted as a string
     */
    public static void setTransferLog(String senderEmail, String senderAccountNumber, String receiverEmail, String receiverAccountNumber, double amount,String date){
        String selfFilePath = "./registerTable/" + senderEmail + "/AccountFile/" + senderAccountNumber;
        String receiverFilePath = "./registerTable/" + receiverEmail + "/AccountFile/" + receiverAccountNumber;
        String senderLog ="Transfer as sender" + "," + senderEmail + "," + senderAccountNumber + "," +"send to" + "," + receiverEmail + "," + receiverAccountNumber + "," + amount + "," + date;
        String receiverLog ="Transfer as receiver"+ "," + senderEmail + "," + senderAccountNumber+ "," + "send to" + "," + receiverEmail + "," + receiverAccountNumber + "," + amount + "," + date;
        appendTextToFile(selfFilePath,senderLog);
        appendTextToFile(receiverFilePath,receiverLog);
    }

    /**
     * Set parent set balance log.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param amount             the amount
     * @param date               the date
     */
    public static void setParentSetBalanceLog(String ownerEmail,String ownerAccountNumber,double amount,String date){
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String parentSetBalanceLog = "Parent Set Balance" + "," + ownerEmail + "," + ownerAccountNumber + "," + amount + "," + date;
        appendTextToFile(ownerFilePath,parentSetBalanceLog);
    }

    /**
     * Set deposit log.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param amount             the amount
     * @param date               the date
     */
    public static void setDepositLog(String ownerEmail,String ownerAccountNumber,double amount,String date){
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String depositLog = "Deposit" + "," + ownerEmail + "," + ownerAccountNumber + "," + amount + "," + date;
        appendTextToFile(ownerFilePath,depositLog);
    }

    /**
     * Set withdraw log.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param amount             the amount
     * @param date               the date
     */
    public static void setWithdrawLog(String ownerEmail,String ownerAccountNumber,double amount,String date){
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String setBalanceLog = "Withdraw" + "," + ownerEmail + "," + ownerAccountNumber + "," + amount + "," + date;
        appendTextToFile(ownerFilePath,setBalanceLog);
    }

    /**
     * Set task log.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param taskId             the task id
     * @param amount             the amount
     * @param date               the date
     */
    public static void setTaskLog(String ownerEmail,String ownerAccountNumber,String taskId,double amount,String date){
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String testLog = "Task" + "," + ownerEmail + "," + ownerAccountNumber + "," + taskId  + "," + amount + "," + date;
        appendTextToFile(ownerFilePath,testLog);
    }

/**
 * Appends text to a specific position in a file or creates new content if the file is too short.
 * This method reads all lines from the specified file, inserts the text at the second line position,
 * and then writes back all the content to the file, effectively modifying the original file content.
 *
 * @param filePath     the file path where the text is to be appended. This is the full path to the file.
 * @param
 **/
    public static void appendTextToFile(String filePath, String textToAppend) {
        ArrayList<String> lines = new ArrayList<>();

        // Read all the lines of the file first
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Unable to read file: " + e.getMessage());
            return;
        }

        // Insert the new text in the second line position
        if (lines.size() >= 2) {
            lines.add(2, textToAppend); // Insert in the second line
        } else {
            // If the file doesn't have enough lines, it's still safe to add text
            lines.add(textToAppend);
        }

        // Write the modified content back to the file
        try (FileWriter writer = new FileWriter(filePath, false)) { // false Represents non-appendic mode, i.e., rewrites the entire file
            for (String line : lines) {
                writer.write(line);
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + e.getMessage());
        }
    }

}

