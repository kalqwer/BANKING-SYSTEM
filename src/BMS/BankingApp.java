package BMS;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String URL = "jdbc:mysql://localhost:3306/bankingsystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            User user = new User(connection, scanner);
            Account accounts = new Account(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            while (true) {
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice1 = scanner.nextInt();

                switch (choice1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        String email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User Logged In!");

                            if (!accounts.account_exist(email)) {
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if (scanner.nextInt() == 1) {
                                    long accountNumber = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + accountNumber);
                                } else {
                                    break;
                                }
                            }

                            long accountNumber = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your choice: ");
                                choice2 = scanner.nextInt();

                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(accountNumber);
                                        break;
                                    case 2:
                                        accountManager.credit_money(accountNumber);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(accountNumber);
                                        break;
                                    case 4:
                                        accountManager.getBalance(accountNumber);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password!");
                        }
                        break;  // Ensure to break out of the case block
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
