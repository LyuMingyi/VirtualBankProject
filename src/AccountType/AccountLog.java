package AccountType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * The type Account log use to generate Log and safe to specific file
 */
public class AccountLog {

    /**
     * Set transfer log.
     *
     * @param senderEmail           the sender email
     * @param senderAccountNumber   the sender account number
     * @param receiverEmail         the receiver email
     * @param receiverAccountNumber the receiver account number
     * @param amount                the amount
     */
    public static void setTransferLog(String senderEmail,String senderAccountNumber,String receiverEmail, String receiverAccountNumber, double amount){
        String selfFilePath = "./registerTable/" + senderEmail + "/AccountFile/" + senderAccountNumber;
        String receiverFilePath = "./registerTable/" + receiverEmail + "/AccountFile/" + receiverAccountNumber;
        String time = getCurrentTimeString();
        String senderLog ="Transfer as sender" + "," + senderEmail + "," + senderAccountNumber + "," +"send to" + "," + receiverEmail + "," + receiverAccountNumber + "," + amount + "," + time;
        String receiverLog ="Transfer as receiver"+ "," + senderEmail + "," + senderAccountNumber+ "," + "send to" + "," + receiverEmail + "," + receiverAccountNumber + "," + amount + "," + time;
        appendTextToFile(selfFilePath,senderLog);
        appendTextToFile(receiverFilePath,receiverLog);
    }

    /**
     * Set parent set balance log.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param amount             the amount
     */
    public static void setParentSetBalanceLog(String ownerEmail,String ownerAccountNumber,double amount){
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String time = getCurrentTimeString();
        String parentSetBalanceLog = "Parent Set Balance" + "," + ownerEmail + "," + ownerAccountNumber + "," + amount + "," + time;
        appendTextToFile(ownerFilePath,parentSetBalanceLog);
    }

    /**
     * Set deposit log.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param amount             the amount
     */
    public static void setDepositLog(String ownerEmail,String ownerAccountNumber,double amount){
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String time = getCurrentTimeString();
        String depositLog = "Deposit" + "," + ownerEmail + "," + ownerAccountNumber + "," + amount + "," + time;
        appendTextToFile(ownerFilePath,depositLog);
    }

    /**
     * Set withdraw log.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param amount             the amount
     */
    public static void setWithdrawLog(String ownerEmail,String ownerAccountNumber,double amount){
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String time = getCurrentTimeString();
        String setBalanceLog = "Withdraw" + "," + ownerEmail + "," + ownerAccountNumber + "," + amount + "," + time;
        appendTextToFile(ownerFilePath,setBalanceLog);
    }

    /**
     * Set task log.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param taskId             the task id
     * @param amount             the amount
     */
    public static void setTaskLog(String ownerEmail,String ownerAccountNumber,String taskId,double amount){
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String time = getCurrentTimeString();
        String testLog = "Task" + "," + ownerEmail + "," + ownerAccountNumber+ "," + taskId+  "," + amount + "," + time;
        appendTextToFile(ownerFilePath,testLog);
    }

    /**
     * Set saving log.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param balance            the balance
     * @param startTime          the start time
     * @param endTime            the end time
     * @param interestRate       the interest rate
     * @param spanTime           the span time
     * @param interest           the interest
     */
    public static void setSavingLog(String ownerEmail, String ownerAccountNumber, double balance, String startTime, String endTime, double interestRate, int spanTime, double interest){
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String testLog = "Saving" + "," + ownerEmail + "," + ownerAccountNumber+ "," + balance +  ","+ interest +","+ startTime + ","+
                endTime + "," + spanTime + "," + interestRate;
        appendTextToFile(ownerFilePath,testLog);
    }

    /**
     * Sets deposit term finished.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     * @param interest           the interest
     * @param newBalance         the new balance
     */
    public static void setDepositTermFinished(String ownerEmail, String ownerAccountNumber,double interest ,double newBalance) {
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String time = getCurrentTimeString();
        String testLog = "DepositTermFinished" + "," + ownerEmail + "," + ownerAccountNumber+ "," + interest + "," + newBalance +  ","+ time;
        appendTextToFile(ownerFilePath,testLog);
    }

    /**
     * Sets reinforce deposit term finished.
     *
     * @param ownerEmail         the owner email
     * @param ownerAccountNumber the owner account number
     */
    public static void setReinforceDepositTermFinished(String ownerEmail, String ownerAccountNumber) {
        String ownerFilePath = "./registerTable/" + ownerEmail + "/AccountFile/" + ownerAccountNumber;
        String time = getCurrentTimeString();
        String testLog = "ReinforceDepositTermFinished" + "," + ownerEmail + "," + ownerAccountNumber+ "," + time;
        appendTextToFile(ownerFilePath,testLog);
    }


    /**
     * Append text to file to the third line of the file.
     *
     * @param filePath     the file path
     * @param textToAppend the text to append
     */
    public static void appendTextToFile(String filePath, String textToAppend) {
        List<String> lines = new ArrayList<>();

        // Read all existing lines from the file into a list
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Unable to read file: " + e.getMessage());
            return;
        }

        // Insert the new text at the position after the second line
        if (lines.size() >= 2) {
            lines.add(2, textToAppend); // Inserts text as the new third line
        } else {
            // If the file has less than 2 lines, append the text at the end
            lines.add(textToAppend);
        }

        // Write all lines, including the newly inserted one, back to the file
        try (FileWriter writer = new FileWriter(filePath, false)) { // 'false' to overwrite the file with new content
            for (String existingLine : lines) {
                writer.write(existingLine + "\n"); // Write each line followed by a newline character
            }
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + e.getMessage());
        }
    }


    /**
     * Gets current time string with specific form.
     *
     * @return the current time string
     */
    public static String getCurrentTimeString() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Create a datetime formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format current time
        return now.format(formatter);
    }


}

