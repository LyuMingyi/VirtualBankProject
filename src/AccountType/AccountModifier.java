package AccountType;import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The type Account modifier.
 */
public  class AccountModifier {
    /**
     * Read balance double from specific amount of money and send back as a double array.
     *
     * @param otherFilePath the other file path
     * @return the double
     * @throws IOException the io exception
     */
    public static double readBalance(String otherFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(otherFilePath))) {
            String balanceLine = reader.readLine(); // Read first line
            if (balanceLine != null && balanceLine.startsWith("Balance:")) {
                String balanceStr = balanceLine.split(":")[1].trim();
                return Double.parseDouble(balanceStr);
            }
            throw new IOException("Balance information is missing or incorrect format.");
        }
    }

    /**
     * Write balance to a specific file path.
     *
     * @param otherFilePath the other file path
     * @param newBalance    the new balance
     * @throws IOException the io exception
     */
    public static void writeBalance(String otherFilePath, double newBalance) throws IOException {
        // Read all lines from the file into a list
        List<String> lines = Files.readAllLines(Paths.get(otherFilePath));

        // Update the first line with the new balance
        if (!lines.isEmpty()) {
            lines.set(0, "Balance: " + newBalance);
        } else {
            // If the file was empty, add the new balance line
            lines.add("Balance: " + newBalance);
        }

        // Write all lines back to the file, preserving all but updating the first line
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(otherFilePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Add balance and modified file content.
     *
     * @param otherFilePath the other file path
     * @param amount        the amount
     * @return the boolean
     */
    public static boolean addBalance(String otherFilePath,double amount){
        try {
            double beneficiaryBalance = readBalance(otherFilePath);
            beneficiaryBalance += amount;
            writeBalance(otherFilePath,beneficiaryBalance);//Modify the amount in the file path
            //Write history generation here
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add balance and modified file content.
     *
     * @param otherFilePath the other file path
     * @param amount        the amount
     * @param owner         the owner
     * @return the boolean
     */
    public static boolean addBalance(String otherFilePath,double amount,Account owner){
        try {
            double beneficiaryBalance = readBalance(otherFilePath);
            beneficiaryBalance += amount;
            writeBalance(otherFilePath,beneficiaryBalance);//Modify the amount in the file path
            System.out.println(beneficiaryBalance);
            owner.setBalance(owner.getBalance() + amount);//Modify the amount in the real class
            //Write history generation here
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reduce balance and modified file content.
     *
     * @param selfFilePath the self file path
     * @param amount       the amount
     * @param moneySender  the money sender
     * @return the boolean
     */
    public static boolean reduceBalance(String selfFilePath,double amount,Account moneySender){
        try {
            double beneficiaryBalance = readBalance(selfFilePath);
            beneficiaryBalance -= amount;
            writeBalance(selfFilePath,beneficiaryBalance);//Modify the amount in the file path
            moneySender.setBalance(moneySender.getBalance() - amount);//Modify the amount in a real-world class
            //Write history generation here
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set balance and modified file content.
     *
     * @param selfFilePath the self file path
     * @param amount       the amount
     * @param owner        the owner
     * @return the boolean
     */
    public static boolean setBalance(String selfFilePath,double amount,Account owner){
        try {
            double beneficiaryBalance = readBalance(selfFilePath);
            beneficiaryBalance = amount;
            writeBalance(selfFilePath,beneficiaryBalance);//Modify the amount in the file path
            owner.setBalance(amount);//Modify the amount in a real-world class
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
