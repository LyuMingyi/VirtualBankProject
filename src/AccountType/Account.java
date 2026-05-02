package AccountType;


import login.AccountInfoReader;

import java.io.File;

/**
 * The type Account used to saving money and transaction.
 */
public class Account {

    private double balance;
    private String accountName;
    private String accountPassword;
    private String accountNumber;
    private String email;

    /**
     * to create a random string base on a random number as an account's file name.
     *
     * @return the string
     */
    public String Random() {
        int randomNumber = (int) (Math.random() * 100000000);
        return String.format("%08d", randomNumber);
    }

    /**
     * Set balance.
     *
     * @param balance the balance
     */
    public void setBalance(double balance){
        this.balance = balance;
    }

    /**
     * Get balance double.
     *
     * @return the double
     */
    public double getBalance(){
        return balance;
    }

    /**
     * Set account number.
     *
     * @param accountNumber the account number
     */
    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }

    /**
     * Get account number string.
     *
     * @return the string
     */
    public String getAccountNumber(){
        return accountNumber;
    }

    /**
     * Set account name.
     *
     * @param accountName the account name
     */
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    /**
     * Get account name string.
     *
     * @return the string
     */
    public String getAccountName(){
        return accountName;
    }

    /**
     * Sets account password.
     *
     * @param accountPassword the account password
     */
    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    /**
     * Get account password string.
     *
     * @return the string
     */
    public String getAccountPassword(){
        return accountPassword;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get email string.
     *
     * @return the string
     */
    public String getEmail(){
        return email;
    }

    /**
     * Transfer used to transfer money between two accounts and create log
     *
     * @param email         the email
     * @param accountNumber the account number
     * @param amount        the amount
     * @return the boolean
     */
    public boolean transfer(String email, String accountNumber, double amount) {
            double balance = getBalance();
            if (amount > balance) {
                System.out.println("The balance is insufficient：");
                System.out.println("Current balance：" + balance);
                return false;
            } else {
                String otherFilePath = "./registerTable/" + email + "/AccountFile/" + accountNumber;
                String selfFilePath =  "./registerTable/" + this.email + "/AccountFile/" + this.accountNumber;
                if(AccountModifier.addBalance(otherFilePath,amount) && AccountModifier.reduceBalance(selfFilePath,amount,this)){

                    AccountLog.setTransferLog(this.email,this.accountNumber,email,accountNumber,amount);
                    //Creating a log
                    System.out.println("transfer successfully");
                    return true;
                }
            }
            System.out.println("transfer failed");
            return false;
    }

    /**
     * Parent could use this method to set balance in reinforce.
     *
     * @param amount the amount
     * @return the boolean
     */
    public boolean parentSetBalance(double amount){
        String selfFilePath =  "./registerTable/" + this.email + "/AccountFile/" + this.accountNumber;
        if(AccountModifier.setBalance(selfFilePath,amount,this)){

            AccountLog.setParentSetBalanceLog(this.email,this.accountNumber,amount);
            //Creating a log
            System.out.println("The parent-mandated modification was successful");
            return true;
        }
        System.out.println("The parent-mandated modification was failed");
        return false;
    }

    /**
     * get an amount and add it to the account balance as user deposit balance
     *
     * @param amount the amount
     * @return the boolean
     */
    public boolean deposit(double amount){
        String selfFilePath = "./registerTable/" + this.email + "/AccountFile/" + this.accountNumber;
        System.out.println(selfFilePath);
        if(AccountModifier.addBalance(selfFilePath,amount,this)){
            AccountLog.setDepositLog(this.email,this.accountNumber,amount);
            //Creating a log
            System.out.println("modified successfully");
            return true;
        }else {
            System.out.println("modified failed");
            return false;
        }

    }

    /**
     * get an amount and reduce it from the account balance as user withdraw the balance
     *
     * @param amount the amount
     * @return the boolean
     */
    public boolean withdraw(double amount){
        String selfFilePath =  "./registerTable/" + this.email + "/AccountFile/" + this.accountNumber;
        if(AccountModifier.reduceBalance(selfFilePath,amount,this)){
            //Creating a log
            AccountLog.setWithdrawLog(this.email,this.accountNumber,amount);
            System.out.println("modified successfully");
            return true;
        }else {
            System.out.println("modified failed");
            return false;
        }

    }

    /**
     * Task finish boolean.
     *
     * @param taskId the task id
     * @param amount the amount
     * @return the boolean
     */
//The taskID here is just for the purpose of generating logs, only a string is required
    public boolean taskFinish(String taskId,double amount){
        String selfFilePath =  "./registerTable/" + this.email + "/AccountFile/" + this.accountNumber;
        if(AccountModifier.addBalance(selfFilePath,amount,this)){
            //creating log
            AccountLog.setTaskLog(this.email,this.accountNumber,taskId,amount);
            System.out.println("modified successfully");
            return true;
        }else {
            System.out.println("modified failed");
            return false;
        }

    }
    /**
     * use to kill an object and remove it from the filesystem
     *
     */
    private void killAccount() {
        // Construct file paths
        String accountFilePath = "./registerTable/" + this.email + "/AccountFile/" + this.accountNumber + ".txt";
        String accountPasswordPath = "./registerTable/" + this.email + "/AccountName&Password/" + this.accountNumber + ".txt";
        AccountInfoReader accountInfoReader = new AccountInfoReader();
        // Delete account file
        accountInfoReader.deleteFile(accountFilePath);
        // Delete password file
        accountInfoReader.deleteFile(accountPasswordPath);
        // Clear the references (not strictly necessary, but a good practice if the object is to be disposed of)
        this.email = null;
        this.accountNumber = null;
        // Any other cleanup code can go here
    }





}
