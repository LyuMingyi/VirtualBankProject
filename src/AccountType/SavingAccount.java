package AccountType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a savings account with a fixed interest rate and term.
 * This class handles the operations related to the depositing of funds,
 * setting terms, and calculating interest over those terms.
 */
public class SavingAccount extends Account {
    private double interestRate;
    private int spanDays;
    private String startTime;
    private String endTime;
    private double interest;

    /**
     * Default constructor that initializes the saving account with a unique account number.
     */
    public SavingAccount() {
        setBalance(0.0);
        setAccountNumber("FIX" + Random() + ".txt");
    }

    /**
     * Selects the interest rate based on the deposit term.
     * Different terms have different fixed annual interest rates.
     *
     * @param term the deposit term, which affects the interest rate
     * @return the annual interest rate for the specified term
     */
    public static double chooseInterest(String term) {
        return switch (term.toLowerCase()) {
            //case "test" -> 10;
            case "one_week", "two_weeks" -> 0.015; // 1.5% annual rate for very short term
            case "one_month" -> 0.02; // 2% annual rate for one month
            case "three_months" -> 0.025; // 2.5% annual rate for three months
            case "six_months" -> 0.03; // 3% annual rate for six months
            case "one_year" -> 0.035; // 3.5% annual rate for one year
            case "two_years" -> 0.04; // 4% annual rate for two years
            case "three_years" -> 0.045; // 4.5% annual rate for three years
            default -> 0.0; // No interest if the term does not match
        };
    }
    /**
     * Chooses the number of days based on the deposit term.
     *
     * @param term the deposit term, which affects the duration in days
     * @return the number of days for the deposit term
     */
    private static int chooseDays(String term) {
        return switch (term.toLowerCase()) {
            //case "test" -> -365;
            case "one_week" -> 7;
            case "two_weeks" -> 14;
            case "one_month" -> 30;
            case "three_months" -> 90;
            case "six_months" -> 180;
            case "one_year" -> 365;
            case "two_years" -> 730;
            case "three_years" -> 1095;
            default -> 0; // Return 0 if the term does not match
        };
    }

    /**
     * Sets the deposit term details including the start time, end time, interest rate, and span.
     * Calculates and logs the expected interest based on the initial balance.
     *
     * @param term the term for which the account is set, e.g., "one_year"
     */
//set and intialize the deposit Term
    public void setDepositTerm(String term) {
        spanDays = chooseDays(term);                      // span day of deposit term
        interestRate = chooseInterest(term);                    //the interest rate of deposit term
        interest = interestRate * ( spanDays / 365.0) * this.getBalance();               //sum up interest
        String startTime = getCurrentSavingTimeString();  //start time set up
        setStartTime(startTime);
        setEndTime(calculateEndDate(startTime, spanDays));     //end up time set up
        AccountLog.setSavingLog(getEmail(),getAccountNumber(),getBalance(),startTime,endTime, interestRate, spanDays,interest);
    }

    /**
     * Ends the deposit term and adds the calculated interest to the account balance.
     * Only modifies the account if the current date is after or on the end date.
     */
//At the end of the period, you will receive interest
    public void depositTermFinished(){
        String path = "./registerTable/" + this.getEmail() + "/AccountFile/" + this.getAccountNumber();
        if(compareDates(getCurrentSavingTimeString(),getEndTime()) == 0 || compareDates(getCurrentSavingTimeString(),getEndTime()) == -1){
            double newBalance = getBalance() + getInterest();//Total interest plus principal
            setBalance(newBalance);
            System.out.println(this.getBalance());
            AccountModifier.addBalance(path, getInterest());
            AccountLog.setDepositTermFinished(getEmail(),getAccountNumber(),getInterest(),newBalance);
            initialSavingAccount();
        }else{
            //end up forehead
            System.out.println("12345678");
        }
    }

    /**
     * Forces the termination of the deposit term without adding any accrued interest.
     */
//Mandatory end with no interest
    public void reinforceTermFinished(){
        initialSavingAccount();
        AccountLog.setReinforceDepositTermFinished(getEmail(),getAccountNumber());
    }
    /**
     * Resets the savings account to its initial state with no active deposit term.
     */
    private void initialSavingAccount() {
        interestRate = 0;
        interest = 0;
        startTime = null;
        endTime = null;
    }

     /**
     * Compares two date strings and returns the result.
     *
     * @param dateStr1 the first date string
     * @param dateStr2 the second date string
     * @return an integer indicating the order of the dates
     */
    public int compareDates(String dateStr1, String dateStr2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse(dateStr1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateStr2, formatter);

        return dateTime1.compareTo(dateTime2);
    }


    /**
     * Calculates the end date from a given start date and a number of days.
     *
     * @param startDateStr the start date in string format
     * @param days the number of days to add to the start date
     * @return the end date as a formatted string
     */
    public String calculateEndDate(String startDateStr, int days) {
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Parse the start date string
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);

        // Add the specified number of days to the start date
        LocalDateTime endDate = startDate.plusDays(days);

        // Format the end date to a string and return
        return endDate.format(formatter);
    }

    /**
     * Returns the current date and time as a string.
     *
     * @return the current date and time formatted as "yyyy-MM-dd HH:mm:ss"
     */
    private String getCurrentSavingTimeString() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Create a datetime formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format current time
        return now.format(formatter);
    }

    /**
     * Constructs a SavingAccount with specified details.
     *
     * @param balance the initial balance
     * @param accountName the account holder's name
     * @param accountPassword the account password
     * @param accountNumber the account number
     * @param interestRate the initial interest rate
     * @param spanDays the number of days for the deposit term
     * @param interest the calculated interest to be added at the end of the term
     * @param startTime the start date of the deposit term
     * @param endTime the end date of the deposit term
     */
    public SavingAccount(double balance, String accountName, String accountPassword, String accountNumber, double interestRate, int spanDays, double interest, String startTime, String endTime) {
        this.setBalance(balance);
        this.setAccountName(accountName);
        this.setAccountPassword(accountPassword);
        this.setAccountNumber(accountNumber);
        this.setInterestRate(interestRate);
        this.setInterest(interest);
        this.setSpanDays(spanDays);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
    }
    /**
     * Creates a new account file with initial balance and creation date.
     *
     * @param email the email associated with the account
     * @return the path to the created account file
     */
    public String creatAccount(String email) {
        String directoryPath = "./registerTable/" + email + "/AccountFile/";
        String filepath = directoryPath + getAccountNumber();
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write("Balance:" + getBalance()+ "\n" +  "Create:" +getCurrentTimeString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filepath;
    }

    /**
     * Gets the current time formatted as "yyyy-MM-dd HH:mm:ss".
     *
     * @return the formatted current time
     */
    public static String getCurrentTimeString() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Create a datetime formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format current time
        String formattedDateTime = now.format(formatter);

        // Returns a formatted datetime string
        return formattedDateTime;
    }

    /**
     * Sets interest rate.
     *
     * @param fdrate the fdrate
     */
    public void setInterestRate(double fdrate) {
        this.interestRate = fdrate;
    }

    /**
     * Gets interest rate.
     *
     * @return the interest rate
     */
    public double getInterestRate() {
        return this.interestRate;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets interest.
     *
     * @return the interest
     */
    public double getInterest() {
        return interest;
    }

    /**
     * Sets interest.
     *
     * @param interest the interest
     */
    public void setInterest(double interest) {
        this.interest = interest;
    }

    /**
     * Gets span days.
     *
     * @return the span days
     */
    public int getSpanDays() {
        return spanDays;
    }

    /**
     * Sets span days.
     *
     * @param spanDays the span days
     */
    public void setSpanDays(int spanDays) {
        this.spanDays = spanDays;
    }
}


