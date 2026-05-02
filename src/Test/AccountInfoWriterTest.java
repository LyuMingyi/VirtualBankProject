package Test;

import Task.Task;
import login.AccountInfoWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of the AccountInfoWriter class, which handles writing account and task-related information to files.
 * This includes verifying the creation of accounts, checking duplicate detection, writing tasks, and managing relationships.
 */
class AccountInfoWriterTest {
    /**
     * The Writer instance used for testing.
     */
    AccountInfoWriter writer = new AccountInfoWriter();

    /**
     * Tests the behavior of writing account information with null values.
     * Ensures that the account is not created when critical information is missing.
     */
    @Test
    void testWriteAccountInfoWithNullValues() {
        assertFalse(writer.writeAccountInfo("usertestWrite@example.com", null, "12345", "group1"), "Account should not be created with null password");
    }

    /**
     * Tests the successful writing of account information to the system.
     * Validates that the account can be created with proper details.
     */
    @Test
    void testWriteAccountInfo() {
        assertTrue(writer.writeAccountInfo("usertestWrite@example.com", "123asd", "12345", "group1"), "Account should be created successfully");
    }

    /**
     * Tests the detection of non-existent emails as duplicates.
     * Ensures that an email not previously registered is correctly identified as not a duplicate.
     */
    @Test
    void testExamDuplicateNonExistent() {
        assertFalse(writer.examDuplicate("nonexistent@example.com"), "Nonexistent email should not be detected as duplicate");
    }

    /**
     * Tests the detection of existing emails as duplicates.
     * Verifies that an email already registered is identified as a duplicate.
     */
    @Test
    void testExamDuplicateExistent() {
        assertTrue(writer.examDuplicate("usertestWrite@example.com"), "Existent email should be detected as duplicate");
    }

    /**
     * Tests successful writing of a task to the file system.
     * Checks that a task file is created after the write operation.
     */
    @Test
    void testWriteTaskSuccessfully(){
        writer.writeTask("Homework", "usertestWrite@example.com", 50.0);
        File taskFile = new File("./registerTable/usertestWrite@example.com/Task/1.txt");
        assertTrue(taskFile.exists(), "Task file should exist after successful write");
    }

    /**
     * Tests the creation of tasks with unique IDs to avoid conflicts.
     * Validates that each new task is stored under a new file.
     */
    @Test
    void testWriteTaskConflictID() {
        writer.writeAccountInfo("usertestWrite3@example.com", "pass123", "12345", "group1");
        writer.writeTask("Homework", "usertestWrite3@example.com", 50.0);
        writer.writeTask("Homework", "usertestWrite3@example.com", 100.0);
        File taskFile2 = new File("./registerTable/usertestWrite3@example.com/Task/2.txt");
        assertTrue(taskFile2.exists(), "Second task file should exist after writing another task with a new ID");
    }

    /**
     * Tests the behavior of writing duplicate relationship entries.
     * Ensures that duplicate relationship entries are not written multiple times to the file.
     */
    @Test
    void testWriteRelationshipDuplicate() {
        writer.writeAccountInfo("parentTest@example.com", "pass123", "12345", "group1");
        writer.writeAccountInfo("kidTest@example.com", "pass123", "12345", "group1");
        writer.writeRelationship("parentTest@example.com", "kidTest@example.com");
        writer.writeRelationship("parentTest@example.com", "kidTest@example.com");
        writer.writeRelationship("parentTest@example.com", "kidTest@example.com");
        File parentFile = new File("./registerTable/parentTest@example.com/info.txt");
        try {
            assertEquals(2, Files.readAllLines(Paths.get(parentFile.getPath())).size(), "Only one relationship entry should exist even after duplicate writes");
        } catch (IOException e) {
            fail("IOException during reading the file: " + e.getMessage());
        }
    }

    /**
     * Tests the task delivery system by copying a task from a parent to a kid and updating the status.
     * Verifies that the task file exists in the kid's directory and the status is updated in the parent's file.
     */
    @Test
    void testDeliverTask() {
        writer.writeTask("Homework", "parentTest@example.com", 50.0);
        Task task = new Task(1, "Homework", 50.0);
        writer.deliverTask("parentTest@example.com", "kidTest@example.com", task);

        File parentTaskFile = new File("./registerTable/parentTest@example.com/Task/1.txt");
        File kidTaskFile = new File("./registerTable/kidTest@example.com/Task/1.txt");
        assertTrue(kidTaskFile.exists(), "Task file should be copied to kid directory");
        try {
            List<String> lines = Files.readAllLines(Paths.get(parentTaskFile.toURI()));
            assertEquals("received", lines.get(3), "Parent file should be updated to 'received'");
        } catch (IOException e) {
            fail("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Tests the modification of task details in an existing task file.
     * Verifies that the task details such as name and reward are updated correctly.
     */
    @Test
    void testTaskModifier() {
        writer.writeAccountInfo("usertestWrite2@example.com", "pass123", "12345", "group1");
        writer.writeTask("Homework", "usertestWrite2@example.com", 50.0);
        writer.taskModifier("usertestWrite2@example.com", "1", "Updated Homework", 75.0);

        File taskFile = new File("./registerTable/usertestWrite2@example.com/Task/1.txt");
        assertTrue(taskFile.exists(), "Task file should exist after modification");
        try {
            List<String> lines = Files.readAllLines(Paths.get(taskFile.toURI()));
            assertEquals("Updated Homework", lines.get(1), "Task name should be updated");
            assertEquals("75.0", lines.get(4), "Task reward should be updated");
        } catch (IOException e) {
            fail("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Tests the submission of a task from a kid to a parent, involving the creation of a notification file.
     * Verifies that the notification file contains correct details regarding task completion and reward.
     */
    @Test
    void testTaskSubmit() {
        writer.writeAccountInfo("kidTest2@example.com", "pass123", "12345", "group1");
        writer.writeAccountInfo("parentTest2@example.com", "pass123", "12345", "group1");
        String taskDetail = "Task ID:</font></b> 1<br>Award: 50$";
        writer.taskSubmit("kidTest2@example.com", "parentTest2@example.com", taskDetail);

        Path notificationPath = Paths.get("./registerTable/parentTest2@example.com/notification_1.txt");
        assertTrue(Files.exists(notificationPath), "Notification file should be created");
        try {
            String content = Files.readString(notificationPath);
            assertTrue(content.contains("completed Task ID: 1 on"), "Notification should mention task completion");
            assertTrue(content.contains("50$"), "Notification should correctly record the award");
        } catch (IOException e) {
            fail("Error reading notification file: " + e.getMessage());
        }
    }

}