package AccountType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * use to kill an object and remove it from the filesystem
 *
 */
public class AccountBillInfo {

    private static final int YEARS_COUNT = 5;  // 2020-2024
    private static final int MONTHS_COUNT = 12;
    private static final int CATEGORIES_COUNT = 6;  // five kinds of tpyes and sumup
    private static final int TOTAL_INDEX = 12;  // sumUp index

    /**
     * Parse the log file and generate a corresponding statistical array
     *
     * @param email         the email
     * @param accountNumber the account number
     * @return 5x6x13In a 3D 5*6*13 double array, 5 represents the five years from 2020 to 2024, 6 represents six different types of data, tasks income is the first row, followed by receiver, sender, deposit, withdraw, sumUp, and thirteen represents the sum of twelve months
     */
    public static void parseFinancialLogs(String email, String accountNumber) {
        double[][][] data = new double[YEARS_COUNT][CATEGORIES_COUNT][MONTHS_COUNT + 1];  // Initialising arrays
        String filePath = "./registerTable/" + email + "/AccountFile/" + accountNumber;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read the first two lines
            reader.readLine();
            reader.readLine();
            // Read the rest of the line
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                String type = parts[0].trim();
                double amount;
                LocalDate date;

                // Read date and amount by selecting the correct field based on type
                switch (type) {
                    case "Task":
                        amount = Double.parseDouble(parts[4].trim());
                        date = LocalDate.parse(parts[5].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        break;
                    case "Deposit":
                    case "Withdraw":
                        amount = Double.parseDouble(parts[3].trim());
                        date = LocalDate.parse(parts[4].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        break;
                    case "Transfer as sender":
                    case "Transfer as receiver":
                        amount = Double.parseDouble(parts[6].trim());
                        date = LocalDate.parse(parts[7].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        break;
                    default:
                        continue; // Skip this line if it is not a known type
                }

                try {
                    updateDataArray(date, type, amount, data);
                } catch (DateTimeParseException e) {
                    System.out.println("cannot resolve the data: " + parts[7].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("there was a fault when reading file: " + e.getMessage());
        }

        calculateTotals(data);

        try {
            filePath = "./registerTable/" + email + "/AccountName&Password/" + accountNumber;
            writeArrayToFile(data, filePath);
        } catch (IOException e) {
            throw new RuntimeException("there was a fault when writing file: " + e.getMessage());
        }
    }
    /**
     * Updates the specified data array based on the provided date, type, and amount.
     * This method automatically locates the position within the data array using the year and month derived from the date,
     * and adjusts the corresponding amount based on the transaction type.
     *
     * @param date The specified date used to determine the year and month indices for the data update.
     * @param type The type of the transaction, which can be "Task", "Transfer as receiver", "Transfer as sender", "Deposit", or "Withdraw".
     * @param amount The amount of the transaction, which will be added or subtracted based on the type.
     * @param data The three-dimensional data array containing transaction amounts for different years, types, and months.
     */
    private static void updateDataArray(LocalDate date, String type, double amount, double[][][] data) {
        int yearIndex = date.getYear() - 2020;
        int monthIndex = date.getMonthValue() - 1;
        if (yearIndex < 0 || yearIndex >= YEARS_COUNT) return;

        int typeIndex = switch (type) {
            case "Task" -> 0;
            case "Transfer as receiver" -> 1;
            case "Transfer as sender" -> 2;
            case "Deposit" -> 3;
            case "Withdraw" -> 4;
            default -> -1;
        };

        if (typeIndex != -1) {
            data[yearIndex][typeIndex][monthIndex] += amount;
        }
    }
    /**
     * Calculates and updates the total amounts for each month and year within the provided data array.
     * This method iterates through each year and month, calculates the totals based on specific rules (e.g., income as positive, expenses as negative),
     * and stores the results in the last type index of the data array.
     *
     * @param data The three-dimensional data array where each element represents a specific year, month, and type amount; the function updates these amounts with their totals.
     */

    private static void calculateTotals(double[][][] data) {
        for (int year = 0; year < YEARS_COUNT; year++) {
            for (int month = 0; month < MONTHS_COUNT; month++) {
                double monthlySum = 0;
                for (int category = 0; category < CATEGORIES_COUNT - 1; category++) {
                    if(category == 0 || category == 1 || category == 3){
                        monthlySum += data[year][category][month];
                    }else {
                        monthlySum -= data[year][category][month];
                    }

                }
                data[year][CATEGORIES_COUNT - 1][month] = monthlySum;  // monthly total
            }
            for (int category = 0; category < CATEGORIES_COUNT; category++) {
                double annualSum = 0;
                for (int month = 0; month < MONTHS_COUNT; month++) {
                    if(category == 0 || category == 1 || category == 3){
                        annualSum += data[year][category][month];
                    }else {
                        annualSum -= data[year][category][month];
                    }
                }
                data[year][category][TOTAL_INDEX] = annualSum;  // annual total
            }
        }
    }
    /**
     * Writes the contents of a three-dimensional data array to the specified file path.
     * This method initially reads and saves the first two lines of the file, then clears the file, and writes the contents of the data array formatted as strings back to the file.
     * The first two lines of content are then rewritten to ensure that any other metadata or header information in the file is not overwritten.
     *
     * @param data The three-dimensional data array containing financial data for various years, types, and months.
     * @param filePath The path of the file where the data will be written.
     * @throws IOException If there is an error in file read/write operations, this exception is thrown.
     */
    private static void writeArrayToFile(double[][][] data, String filePath) throws IOException {
        List<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(filePath)));

        // Save the first two lines
        List<String> initialLines = new ArrayList<>();
        for (int i = 0; i < Math.min(2, lines.size()); i++) {
            initialLines.add(lines.get(i));
        }

        // Clear files
        Files.write(Paths.get(filePath), "".getBytes());

        StringBuilder sb = new StringBuilder();
        for (double[][] yearData : data) {
            for (double[] monthData : yearData) {
                sb.append(Arrays.toString(monthData)).append(System.lineSeparator());
            }
            sb.append(System.lineSeparator());
        }

        // Rewrite the first two lines
        Files.write(Paths.get(filePath), initialLines, StandardOpenOption.APPEND);

        // Append array contents
        Files.write(Paths.get(filePath), sb.toString().getBytes(), StandardOpenOption.APPEND);
    }


    /**
     * Reads a three-dimensional array from a file.
     * This method reads a three-dimensional array of double values from a specified file path.
     *
     * @param path the path of the file to read.
     * @return a three-dimensional array of double values.
     * @throws IOException if there is an input/output error during file reading.
     */
    public static double[][][] readArrayFromFile(String path) throws IOException {
        double[][][] data = new double[5][6][13]; // Create a three-dimensional array

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            int block = 0; // The index used to control the first dimension
            int row = 0; // The index used to control the second dimension

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("[") && line.endsWith("]")) {
                    line = line.substring(1, line.length() - 1); // Remove square brackets
                    if (line.isEmpty()) {
                        continue; // Skip empty bracket lines
                    }
                    String[] values = line.split(",\\s*"); // Separate the values by commas to remove possible white space
                    for (int col = 0; col < values.length; col++) {
                        data[block][row][col] = Double.parseDouble(values[col]);
                    }
                    row++; //After a row is read, the row index is added
                    if (row == 6) {
                        row = 0;
                        block++; // If the number of rows is full, add the block index
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printArray(data);
        //Print the array to verify
        return data;

    }


    /**
     * Prints the contents of a three-dimensional array.
     * Each value is printed space-separated, with each two-dimensional array separated by a newline.
     *
     * @param array the three-dimensional array to print.
     */
    public static void printArray(double[][][] array) {
        for (double[][] twoDArray : array) {
            for (double[] oneDArray : twoDArray) {
                for (double num : oneDArray) {
                    System.out.print(num + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    /**
     * Prints the contents of a two-dimensional array.
     * Each value is printed space-separated, with each one-dimensional array on a new line.
     *
     * @param array the two-dimensional array to print.
     */
    public static void printArray2D(double[][] array) {
        for (double[] oneDArray : array) {
            for (double num : oneDArray) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Creates and returns a fake three-dimensional array.
     * This method simulates reading from a file by generating random double values for a 5x6x12 array.
     *
     * @return a three-dimensional array filled with random double values.
     * @throws IOException if an input/output error is supposed to simulate.
     */
    public static double[][][] createFakeArrayFromFile() throws IOException {
        //  Read the implementation of the file, returning a three-dimensional array
        //        The example returns fake data
        double[][][] data = new double[5][6][12];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 12; k++) {
                    data[i][j][k] = Math.random() * 100;
                }
            }
        }
        return data;
    }


    /**
     * Reads log entries for a specific month from a file.
     * This method filters log entries by month and year from a specified file and returns them sorted by date.
     *
     * @param year the year of the logs to read.
     * @param month the month of the logs to read.
     * @param filePath the path of the log file.
     * @return an array of log entries for the specified month and year.
     */
    public static String[] readLogsForMonth(int year, int month, String filePath) {
        List<Map.Entry<LocalDate, String>> entries = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip header lines
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                String dateString = parts[parts.length - 1].trim(); // Assume date is always the last element

                try {
                    LocalDate date = LocalDate.parse(dateString, formatter);
                    if (date.getYear() == year && date.getMonthValue() == month) {
                        entries.add(new AbstractMap.SimpleImmutableEntry<>(date, line));
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Error parsing date in line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return entries.stream()
                .sorted(Map.Entry.<LocalDate, String>comparingByKey()) // Sort by date descending
                .map(Map.Entry::getValue) // Extract the log line
                .toArray(String[]::new);
    }
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Transforms log entries into a readable string format.
     * This method formats each log entry into a more readable form, specifically tailored for display.
     *
     * @param monthLog an array of log entries to format.
     * @return an array of formatted log entries.
     */
    public static String[] logTransfer(String[] monthLog) {
        ArrayList<String> lines = new ArrayList<String>();
        StringBuilder line = new StringBuilder();

        for (String log : monthLog) {
            line.setLength(0);
            String[] parts = log.split(",");
            if (log.startsWith("Task")) {
                line.append(parts[3]).append(" Award").append("\n").append("Finished At ").append(parts[5])
                        .append("\n").append("Amount:").append(parts[4]).append("$").append("\n\n");

            }else if (log.startsWith("Transfer as receiver")){
                line.append(parts[0]).append("\n").append("Transferred At: ").append(parts[7]).append("\n")
                        .append("Sender: ").append(parts[1]).append("\n").append("Amount: ")
                        .append(parts[6]).append("$").append("\n\n");

            }else if (log.startsWith("Transfer as sender")) {
                line.append(parts[0]).append("\n").append("Transferred At: ").append(parts[7]).append("\n")
                        .append("Receiver: ").append(parts[1]).append("\n").append("Account: ")
                        .append(parts[5]).append("\n").append("Amount: ").append(parts[6])
                        .append("$").append("\n\n");

            }else if (log.startsWith("Deposit")) {
                line.append(parts[0]).append("\n").append("Deposit At: ").append(parts[4]).append("\n")
                        .append("Amount: ").append(parts[3]).append("$").append("\n\n");

            }else if (log.startsWith("Withdraw")) {
                line.append(parts[0]).append("\n").append("Withdraw At: ").append(parts[4]).append("\n")
                        .append("Amount: ").append(parts[3]).append("$").append("\n\n");
            }
            lines.add(line.toString());
        }
        return lines.toArray(new String[0]);
    }


    /**
     * Creates a matrix from log entries.
     * This method processes an array of log entries to create a matrix representing different financial transactions over the days of a month.
     *
     * @param logs the array of log entries.
     * @return a two-dimensional array where each cell represents a specific day and transaction type total.
     */
    public static double[][] createLogMatrix(String[] logs) {
        double[][] matrix = new double[6][31]; // 6 rows and 31 columns matrix

        for (String log : logs) {
            String[] parts = log.split(",");
            double amount = Double.parseDouble(parts[parts.length - 2]); // The amount is always the penultimate element
            LocalDate date = LocalDate.parse(parts[parts.length - 1].trim(), DATE_FORMATTER);

            int day = date.getDayOfMonth() - 1; // 6 rows and 31 columns matrix ...
            if (log.startsWith("Task")) {
                matrix[0][day] += amount; // Tasks income
                matrix[5][day] += amount; // to add the sumup to the last line
            } else if (log.startsWith("Transfer as sender")) {
                matrix[2][day] += amount; // sender
                matrix[5][day] -= amount; //  to add the sumup to the last line
            } else if (log.startsWith("Deposit")) {
                matrix[3][day] += amount; // deposit
                matrix[5][day] += amount; //  to add the sumup to the last line
            } else if (log.startsWith("Withdraw")) {
                matrix[4][day] += amount; // withdraw
                matrix[5][day] -= amount; //  to add the sumup to the last line
            } else if (log.startsWith("Transfer as receiver")) {
                matrix[1][day] += amount; // reciver
                matrix[5][day] += amount; //  to add the sumup to the last line
            }


        }

        return matrix;
    }

    /**
     * The main entry point for the application.
     * This method is used to execute the program, demonstrating file reading, log processing, and array handling.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
//        parseFinancialLogs("kid@qmul.ac.uk","CUR18525953.txt");
//        double[][][] test = null;
//        try {
//            test = readArrayFromFile("./registerTable/" + "kid@qmul.ac.uk" + "/AccountName&Password/" + "CUR18525953.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        int year = 2024;
        int month = 12;
        String filePath = "./registerTable/" + "kid@qmul.ac.uk" + "/AccountFile/" + "CUR18525953.txt";
        String[] monthLogs = readLogsForMonth(2020, 1, filePath);
        for (String log : monthLogs) {
            System.out.println(log);
        }
        // Output the logs for verification
//        for (String log : monthLogs) {
//            System.out.println(log);
//        }

    }


}
