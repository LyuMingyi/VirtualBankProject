package login;

import AccountType.Account;
import AccountType.CurrentAccount;
import AccountType.SavingAccount;
import Page.*;
import Task.Task;
import User.Kid;
import User.Parent;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code AccountInfoReader} class implements the {@code LoginCheck} interface and is responsible for reading and managing account information from a structured file system.
 * It provides functionalities to authenticate users, load user-specific data such as accounts, tasks, and other related information.
 */
public class AccountInfoReader implements LoginCheck {

    /**
     * Path to the directory containing registration tables.
     */
    static String registerDirectoryPath = "./registerTable";

    @Override
    public boolean authenticate(String email, String password) {
        String emailDirectoryPath = findEmail(email, registerDirectoryPath);
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(emailDirectoryPath, "info.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");  // Assuming each column is separated by a comma
                if (data[0].trim().equals(email) && data[2].trim().equals(password)) {
                    //This creates an object for successful login
                    if (data[3].trim().equals("Parent")) {
                        Parent parent = parentLoader(email);
                        homePageForParent hg = new homePageForParent(parent);
                        hg.openPage();
                    } else {
                        Kid kid = kidLoader(email);
                        homePageForKid hg = new homePageForKid(kid);
                        hg.openPage();
                    }

                    System.out.println("Successfully logged");
                    System.out.println("Welcome, " + email);
                    //open homeGUI


                    return true;
                } else {
                    System.out.println("password is wrong!");
                }
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Loads a Parent object with associated accounts, tasks, and kids list based on the email provided.
     *
     * @param email the email address to load the parent information
     * @return the loaded Parent object or null if an error occurs
     */
    public Parent parentLoader(String email) {
        ArrayList<Account> accountList = accountListLoader(email);
        ArrayList<Task> taskList = loadTask(email);
        ArrayList<String> kidsList = new ArrayList<>();

        String emailDirectoryPath = findEmail(email, registerDirectoryPath);
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(emailDirectoryPath, "info.txt")))) {
            String line;
            line = reader.readLine();
            String[] data = line.split(",");  // Assuming each column is separated by a comma
            while ((line = reader.readLine()) != null) {
                kidsList.add(line);
            }
            Parent parent;
            parent = new Parent(data[0].trim(),data[1].trim(),accountList,taskList,kidsList);
            return parent;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return null;
    }

    /**
     * Loads a Kid object with associated accounts and tasks based on the email provided.
     *
     * @param email the email address to load the kid information
     * @return the loaded Kid object or null if an error occurs
     */
    public Kid kidLoader(String email) {
        String emailDirectoryPath = findEmail(email, registerDirectoryPath);
        ArrayList<Account> accountList = accountListLoader(emailDirectoryPath);
        ArrayList<Task> taskList = loadTask(emailDirectoryPath);
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(emailDirectoryPath, "info.txt")))) {
            String line;
            line = reader.readLine();
            String[] data = line.split(",");  // Assuming each column is separated by a comma
            line = reader.readLine();
            Kid kid = new Kid(data[0].trim(),data[1].trim(),accountList,taskList,line);
            return kid;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return null;
    }

    /**
     * Loads a list of Account objects associated with the given email.
     *
     * @param emailPath the path to the email directory containing account files
     * @return a list of Account objects
     */
    public ArrayList<Account> accountListLoader(String emailPath) {
        String AccountPath = "./registerTable/" + emailPath + "/AccountFile/";
        String[] files = getTxtFileNames(AccountPath);
        ArrayList<Account> tmp = new ArrayList<>();
        if(files != null) {
            for (int i = 0; i < files.length; i++) {
                Account account = new Account();
                account = accountLoader(emailPath,files[i]);
                account.setEmail(emailPath);
                tmp.add(account);
            }
        }
        return tmp;
    }

    /*public Account accountLoader(String emailPath,String fileName) {
        String accountFilePath = "./registerTable/" + emailPath + "/AccountFile/" + fileName;
        String nameWithPasswordPath = "./registerTable/" + emailPath + "/AccountName&Password/" + fileName;
        String prefix = fileName.substring(0, 3); // 获取前三个字符
        double tmpBalance = 0.0;
        if (prefix.equals("CUR")) {
            File file = new File(accountFilePath);
            try (Scanner scanner = new Scanner(file)) {
                String line = scanner.nextLine();
                if (line.startsWith("Balance:")) {
                    String balanceStr = line.split(":")[1].trim();  // 分割字符串并去除空格
                    tmpBalance = Double.parseDouble(balanceStr);  // 将字符串转换为double
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件未找到: " + e.getMessage());
            }

            File file2 = new File(nameWithPasswordPath);
            String[] credentials = new String[2];
            try (Scanner scanner = new Scanner(file2)) {
                if (scanner.hasNextLine()) {
                    credentials[0] = scanner.nextLine();  // 读取第一行
                }
                if (scanner.hasNextLine()) {
                    credentials[1] = scanner.nextLine();  // 读取第二行
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件未找到: " + e.getMessage());
            }
            return new CurrentAccount(tmpBalance,credentials[0],credentials[1],fileName);
        } else if (prefix.equals("FIX")) {
            File file = new File(accountFilePath);
            try (Scanner scanner = new Scanner(file)) {
                String line = scanner.nextLine();
                if (line.startsWith("Balance:")) {
                    String balanceStr = line.split(":")[1].trim();  // 分割字符串并去除空格
                    tmpBalance = Double.parseDouble(balanceStr);  // 将字符串转换为double
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件未找到: " + e.getMessage());
            }

            File file2 = new File(nameWithPasswordPath);
            String[] credentials = new String[2];
            try (Scanner scanner = new Scanner(file2)) {
                if (scanner.hasNextLine()) {
                    credentials[0] = scanner.nextLine();  // 读取第一行
                }
                if (scanner.hasNextLine()) {
                    credentials[1] = scanner.nextLine();  // 读取第二行
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件未找到: " + e.getMessage());
            }
            return new SavingAccount(tmpBalance,credentials[0],credentials[1],fileName);
        }
        return null;
    }*/

    /**
     * Get txt file names string [ ].
     *
     * @param directoryPath the directory path
     * @return the string [ ]
     */
    public static String[] getTxtFileNames(String directoryPath) {
        File directory = new File(directoryPath);
        // 检查目录是否存在并且是否为一个目录
        if (directory.exists() && directory.isDirectory()) {
            // 使用 FilenameFilter 来筛选出扩展名为 .txt 的文件
            FilenameFilter textFileFilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            };
            // 返回筛选后的文件名数组
            return directory.list(textFileFilter);
        } else {
            return null;
        }
    }

    /**
     * Find email string.
     *
     * @param email         the email
     * @param registerTable the register table
     * @return the string
     */
    public String findEmail(String email,String registerTable){
        // 创建一个代表registerTable路径的File对象
        File directory = new File(registerTable);

        // 确保提供的路径是一个目录
        if (directory.isDirectory()) {
            // 获取目录下所有文件及文件夹的名称
            String[] subdirs = directory.list();

            // 检查每一个名称是否与email匹配
            for (String subdir : subdirs) {
                File folder = new File(directory, subdir);
                // 确保是文件夹且名称与email相匹配
                if (folder.isDirectory() && subdir.equals(email)) {
                    return folder.getAbsolutePath();  // 返回匹配的文件夹的绝对路径
                }
            }
        }
        return null;  // 如果没有找到，返回null
    }

    /**
     * Loads task information for a specific user.
     *
     * @param email the user's email
     * @return a list of Task objects
     */
    public ArrayList<Task> loadTask(String email) {
        ArrayList<Task> tasks = new ArrayList<>();
        File dir = new File("./registerTable/" + email + "/Task/");
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        int taskID = Integer.parseInt(reader.readLine());
                        String taskName = reader.readLine();
                        boolean isDone = Boolean.parseBoolean(reader.readLine());
                        String status = reader.readLine();
                        double money = Double.parseDouble(reader.readLine());
                        reader.close();

                        Task task = new Task(); // 假设Task类有默认构造函数
                        task.setTaskID(taskID);
                        task.setTaskName(taskName);
                        task.setDone(isDone);
                        task.setStatus(status);
                        task.setMoney(money);

                        tasks.add(task);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return tasks;
    }

    /**
     * Loads the names of kids associated with a given parent's email.
     *
     * @param email the parent's email
     * @return a list of kids' names
     */
    public List<String> loadKidsName(String email) {
        List<String> kidsName = new ArrayList<>();
        File infoFile = new File("./registerTable/" + email + "/info.txt");
        if (infoFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(infoFile));
                // 跳过第一行
                reader.readLine();
                String line;
                while ((line = reader.readLine()) != null) {
                    kidsName.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return kidsName;
    }

    /**
     * Reads specific user information from a file.
     *
     * @param email the user's email
     * @param idx the index of the data to retrieve
     * @return the data string
     */
    public String readInfo(String email,int idx){
        // 读取孩子信息文件
        File InfoFile = new File("./registerTable/" + email + "/info.txt");
        if (InfoFile.exists()) {
            try {
                BufferedReader Reader = new BufferedReader(new FileReader(InfoFile));
                String firstLine = Reader.readLine();
                Reader.close();

                // 获取第一个字符串（孩子的id）
                String[] parts = firstLine.split(",");
                if (parts.length >= 2) {
                   return parts[idx].trim();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Kid name loader array list.
     *
     * @param kidList the kid list
     * @return the array list
     */
    public ArrayList<String> kidNameLoader(ArrayList<String> kidList) {
        ArrayList<String> kidNameList = new ArrayList<>();
        for (String kid : kidList) {
            String filePath = "./registerTable/" + kid + "/info.txt";
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split(",", 4);
                    if (parts.length > 1) { // 确保有第二个字符串
                        kidNameList.add(parts[1]); // 添加第二个字符串到列表中
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return kidNameList;
    }


    /**
     * Loads account list based on the user's email.
     *
     * @param email the user's email
     * @return the list of accounts as String
     */
    public List<String> loadAccountList(String email) {
        List<String> accountList = new ArrayList<>();
        Path dirPath = Paths.get("./registerTable/" + email + "/AccountFile/");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    accountList.add(path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    /**
     * Deletes a file at the specified path.
     *
     * @param filePath the path to the file to be deleted
     */
    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("File deleted successfully: " + filePath);
            } else {
                System.out.println("Failed to delete the file: " + filePath);
            }
        } else {
            System.out.println("File does not exist: " + filePath);
        }
    }

    /**
     * Account loader account.
     *
     * @param emailPath the email path
     * @param fileName  the file name
     * @return the account
     */
    public Account accountLoader(String emailPath,String fileName) {
        String accountFilePath = "./registerTable/" + emailPath + "/AccountFile/" + fileName;
        String nameWithPasswordPath = "./registerTable/" + emailPath + "/AccountName&Password/" + fileName;
        String prefix = fileName.substring(0, 3); // 获取前三个字符
        double tmpBalance = 0.0;
        if (prefix.equals("CUR")) {
            File file = new File(accountFilePath);
            try (Scanner scanner = new Scanner(file)) {
                String line = scanner.nextLine();
                if (line.startsWith("Balance:")) {
                    String balanceStr = line.split(":")[1].trim();  // 分割字符串并去除空格
                    tmpBalance = Double.parseDouble(balanceStr);  // 将字符串转换为double
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件未找到: " + e.getMessage());
            }

            File file2 = new File(nameWithPasswordPath);
            String[] credentials = new String[2];
            try (Scanner scanner = new Scanner(file2)) {
                if (scanner.hasNextLine()) {
                    credentials[0] = scanner.nextLine();  // 读取第一行
                }
                if (scanner.hasNextLine()) {
                    credentials[1] = scanner.nextLine();  // 读取第二行
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件未找到: " + e.getMessage());
            }
            return new CurrentAccount(tmpBalance,credentials[0],credentials[1],fileName);
        } else if (prefix.equals("FIX")) {
            File file = new File(accountFilePath);
            File file2 = new File(nameWithPasswordPath);
            String[] credentials = new String[2];
            try (Scanner scanner = new Scanner(file2)) {
                if (scanner.hasNextLine()) {
                    credentials[0] = scanner.nextLine();  // 读取第一行
                }
                if (scanner.hasNextLine()) {
                    credentials[1] = scanner.nextLine();  // 读取第二行
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件未找到: " + e.getMessage());
            }
            try (Scanner scanner = new Scanner(file)) {
                String line = scanner.nextLine();
                if (line.startsWith("Balance:")) {
                    String balanceStr = line.split(":")[1].trim();  // 分割字符串并去除空格
                    tmpBalance = Double.parseDouble(balanceStr);  // 将字符串转换为double
                }
                line = scanner.nextLine();
                if (line.startsWith("Saving")){
                    String[] parts = line.split(",");
                    double rate = Double.parseDouble(parts[8].trim());
                    int spanDays = Integer.parseInt(parts[7].trim());
                    double interest = Double.parseDouble(parts[4].trim());
                    return new SavingAccount(tmpBalance,credentials[0],credentials[1],fileName,rate,spanDays,interest,parts[5].trim(),parts[6].trim());
                }else{
                    return new SavingAccount(tmpBalance,credentials[0],credentials[1],fileName,0,0,0,null,null);
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件未找到: " + e.getMessage());
            }
        }
        return null;
    }
}



