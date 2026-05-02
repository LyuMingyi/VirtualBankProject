package Test;

import AccountType.Account;
import AccountType.CurrentAccount;
import AccountType.SavingAccount;
import User.Parent;
import User.Kid;
import Task.Task;
import login.AccountInfoReader;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides a series of tests for the AccountInfoReader class, focusing on its functionality related to authentication,
 * data retrieval, and file handling. This includes tests for loading account details, validating user credentials,
 * and managing tasks and account lists.
 */
@Nested
class AccountInfoReaderTest {
    /**
     * Instance of AccountInfoReader used in all tests.
     */
    AccountInfoReader reader = new AccountInfoReader();

    /**
     * Tests successful authentication with correct credentials.
     */
    @org.junit.jupiter.api.Test
    public void testAuthenticateSuccess() {
        assertTrue(reader.authenticate("ReadTest1@qmul.ac.uk", "123abc"), "Authentication should succeed with correct credentials");
    }

    /**
     * Tests authentication failure due to incorrect password.
     */
    @org.junit.jupiter.api.Test
    public void testAuthenticateFailurePassword() {
        assertFalse(reader.authenticate("ReadTest1@qmul.ac.uk", "123456"),"Authentication should fail with wrong password");
    }

    /**
     * Tests authentication failure due to incorrect email.
     */
    @org.junit.jupiter.api.Test
    public void testAuthenticateFailureEmail() {
        assertFalse(reader.authenticate("readTest1@qmul.ac.uk", "123abc"),"Authentication should fail with wrong email");
    }

    /**
     * Tests authentication failure due to incorrect email and password.
     */
    @org.junit.jupiter.api.Test
    public void testAuthenticateFailureEmailAndPassword() {
        assertFalse( reader.authenticate("readTest1@qmul.ac.uk", "123456"),"Authentication should fail with wrong email and wrong passWord");
    }

    /**
     * Tests successful email lookup in the database.
     */
    @org.junit.jupiter.api.Test
    public void testFindEmailExists() {
        assertNotNull(reader.findEmail("ReadTest1@qmul.ac.uk","registerTable"),"Should find an email if it exists");
    }

    /**
     * Tests failure of email lookup when email does not exist.
     */
    @org.junit.jupiter.api.Test
    public void testFindEmailDoesNotExist() {
        assertNull(reader.findEmail("readTest1@qmul.ac.uk", "registerTable"),"Should not find an email if it does not exist");
    }

    /**
     * Tests successful loading of a parent object from a database.
     */
    @org.junit.jupiter.api.Test
    public void testParentLoader() {
        Parent parent = reader.parentLoader("ReadTest1@qmul.ac.uk");
        assertNotNull(parent,"Parent loader should load a parent object when data is correct");//This is the parent name in the info file
        assertEquals("ReadTest1", parent.getName(), "Names should match");
        assertTrue(parent.getKidsList().contains("ReadTest2@qmul.ac.uk"), "Kids list should contain Kid");
    }

    /**
     * Tests loading of a parent object to ensure incorrect data does not match.
     */
    @org.junit.jupiter.api.Test
    public void testParentNotLoader(){
        Parent parent = reader.parentLoader("ReadTest1@qmul.ac.uk");
        assertNotEquals("Wrong Test Name", parent.getName(), "Names should match");//This is the parent name in the info file
        assertFalse(parent.getKidsList().contains("Kid0"), "Kids list should not contain Kid1");
    }

    /**
     * Tests successful loading of a kid object from a database.
     */
    @org.junit.jupiter.api.Test
    public void testKidLoader() {
        Kid kid = reader.kidLoader("ReadTest2@qmul.ac.uk");
        assertNotNull(kid,"Kid loader should load a kid object when data is correct");
        assertEquals("ReadTest2", kid.getName(), "Kid's name should match");
        assertEquals("ReadTest2@qmul.ac.uk", kid.getEmail(), "Kid's email should match");
        assertEquals("ReadTest1@qmul.ac.uk", kid.getParentEmail(), "Parent email should match");
    }

    /**
     * Tests loading of a kid object to ensure incorrect data does not match.
     */
    @org.junit.jupiter.api.Test
    public void testKidNotLoader(){
        Kid kid = reader.kidLoader("ReadTest2@qmul.ac.uk");
        assertNotEquals("Wrong Test Kid", kid.getName(), "Kid's name should not match");
        assertNotEquals("Wrong kid@example.com", kid.getEmail(), "Kid's email should not match");
        assertNotEquals("Wrong parent@example.com", kid.getParentEmail(), "Parent email should not match");
    }

    /**
     * Test method for verifying that the account list loader properly loads the account list
     * and returns correct account details.
     */
    @org.junit.jupiter.api.Test
    public void testaccountListLoader() {
        assertNotNull(reader.accountListLoader("ReadTest1@qmul.ac.uk"),"Account list should not be null");
        assertFalse(reader.accountListLoader("ReadTest1@qmul.ac.uk").isEmpty(),"Account list should not be empty with valid data");
        assertEquals(1, reader.accountListLoader("ReadTest1@qmul.ac.uk").size(),"The number of accounts loaded should match");
        Account testAccount = reader.accountListLoader("ReadTest1@qmul.ac.uk").get(0); // 假定至少有一个账户
        assertEquals("ReadTest1@qmul.ac.uk", testAccount.getEmail(),"Check account email");
    }

    /**
     * Test method for verifying that current accounts are correctly loaded and instance-checked.
     */
    @org.junit.jupiter.api.Test
    public void testCurrentAccountLoading() {
        Account account = reader.accountLoader("ReadTest1@qmul.ac.uk", "CUR58010701.txt");
        assertNotNull(account,"Account should not be null");
        assertTrue(account instanceof CurrentAccount,"Account should be instance of CurrentAccount");
        assertEquals("ReadTest1", account.getAccountName(),"Account name should be ReadTest1");
        assertEquals(9999999.0, account.getBalance(), 0.01,"Account balance should be 9999999.0");
    }

    /**
     * Test method for verifying that saving accounts are correctly loaded and instance-checked.
     */
    @org.junit.jupiter.api.Test
    public void testSavingAccountLoading() {
        Account account = reader.accountLoader("ReadTest2@qmul.ac.uk", "FIX88921203.txt");
        assertNotNull(account,"Account should not be null");
        assertTrue(account instanceof SavingAccount,"Account should be instance of SavingAccount");
        assertEquals("ReadTest2", account.getAccountName(),"Account name should be ReadTest2");
        assertEquals(100.0, account.getBalance(), 0.01,"Account balance should be 100.0");
    }

    /**
     * Test method for verifying task loading functionality with validation on task properties.
     */
    @org.junit.jupiter.api.Test
    public void testLoadTask() {
        assertNotNull(reader.loadTask("ReadTest1@qmul.ac.uk"),"Task loading should not return null");
        assertFalse( reader.loadTask("ReadTest1@qmul.ac.uk").isEmpty(),"Task list should not be empty with valid data");
        assertEquals(1,reader.loadTask("ReadTest1@qmul.ac.uk").size(),"The number of tasks loaded should match");
        // Test the properties of the Task object returned
        Task testTask = reader.loadTask("ReadTest1@qmul.ac.uk").get(0);
        assertEquals(1, testTask.getTaskID(),"Task ID should match");
        assertEquals("clean the garbage bin", testTask.getTaskName(),"Task name should match");
        assertEquals("received", testTask.getStatus(),"Task status should match");
        assertEquals(false, testTask.getDone(),"Task completion should match");
        assertEquals(6.0, testTask.getMoney(), 0.001,"Task money should match");
    }

    /**
     * Test method for verifying that the account list can be loaded successfully.
     */
    @org.junit.jupiter.api.Test
    public void testLoadAccountList() {
        List<String> accounts = reader.loadAccountList("ReadTest1@qmul.ac.uk");
        assertNotNull(accounts,"Loaded account list should not be null");
        assertTrue(!accounts.isEmpty(),"Should load some accounts");
    }

    /**
     * Test method for verifying that kids' names can be correctly loaded from account data.
     */
    @org.junit.jupiter.api.Test
    public void testLoadKidsName() {
        List<String> kidsNames = reader.loadKidsName("ReadTest1@qmul.ac.uk");//View from parent's account
        assertNotNull(kidsNames,"Loaded kids names list should not be null");
        assertFalse(kidsNames.isEmpty(),"Kids names list should not be empty with valid data");
    }

    /**
     * Test method for verifying that kids' names are correctly loaded using a list of email addresses.
     */
    @org.junit.jupiter.api.Test
    public void testKidNameLoader() {//What is read is the name of the second block in the first line of the info file.
        ArrayList<String> kidList = new ArrayList<>(Arrays.asList("ReadTest2@qmul.ac.uk"));//A child's email address, why is it the child's? Because it is a kidlist. Generally, it is created by reading the member variables of the parent class, that is, the assignment in the parentload function. In fact, the result of the assignment is the child's email address.
        ArrayList<String> kidNames = reader.kidNameLoader(kidList);
        String firstKidName = null;
        if (!kidNames.isEmpty()) {
           firstKidName = kidNames.get(0);
        }
        assertNotNull(kidNames,"Should return a list, not null");
        assertFalse(kidNames.isEmpty(),"Should return names for valid emails");
        assertEquals("ReadTest2",firstKidName,"KidName should match");
    }

    /**
     * Test method for verifying the retrieval of TXT file names within a directory.
     */
    @org.junit.jupiter.api.Test
    public void testTxtFilesFound() {
        // The test expects to find a txt file
        String[] files = reader.getTxtFileNames("./registerTable/ReadTest1@qmul.ac.uk/Task/");
        assertNotNull(files,"File array should not be null");
        assertEquals(1, files.length,"Should find exactly two txt files");
        assertArrayEquals(new String[]{"1.txt"}, files,"File names should match");
    }
    /**
     * Test method for checking behavior when directory does not exist.
     */
    @org.junit.jupiter.api.Test
    public void testDirectoryDoesNotExist() {
        // Test a non-existent directory
        String nonExistingDirectory = "ReadTest4@qmul.ac.uk";
        String[] files = reader.getTxtFileNames(nonExistingDirectory);
        assertNull(files,"File array should be null");
    }
}