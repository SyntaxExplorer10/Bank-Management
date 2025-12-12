package BankManagement;

import java.sql.*;
import java.util.Scanner;

public class ATMSimulation {

    static final String URL = "jdbc:mysql://localhost:3306/ccsha";
    static final String USER = "root";
    static final String PASS = "";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Driver not found!");
        }

        while (true) {
            System.out.println("\n===== ATM BANK MANAGEMENT =====");
            System.out.println("1. New Account");
            System.out.println("2. Edit Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Check Balance");
            System.out.println("6. Mini-Statement");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> newAccount();
                case 2 -> editAccount();
                case 3 -> deposit();
                case 4 -> withdraw();
                case 5 -> checkBalance();
                case 6 -> miniStatement();
                case 7 -> System.exit(0);
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ---------------- NEW ACCOUNT ----------------
    static void newAccount() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Account Number: ");
            String acc = sc.next();

            PreparedStatement check = con.prepareStatement(
                    "SELECT * FROM accounts WHERE account_number=?");
            check.setString(1, acc);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                System.out.println("Account already exists!");
                return;
            }

            sc.nextLine();
            System.out.print("Enter Holder Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Initial Balance: ");
            double bal = sc.nextDouble();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO accounts(account_number, holder_name, balance) VALUES(?,?,?)");

            ps.setString(1, acc);
            ps.setString(2, name);
            ps.setDouble(3, bal);
            ps.executeUpdate();

            System.out.println("Account Created Successfully!");

        } catch (SQLException e) {
            printSQLError(e);
        }
    }

    // ---------------- EDIT DETAILS ----------------
    static void editAccount() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Account Number: ");
            String acc = sc.next();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM accounts WHERE account_number=?");
            ps.setString(1, acc);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Account not found!");
                return;
            }

            System.out.println("\nOld Name: " + rs.getString("holder_name"));

            sc.nextLine();
            System.out.print("Enter New Name: ");
            String name = sc.nextLine();

            PreparedStatement update = con.prepareStatement(
                    "UPDATE accounts SET holder_name=? WHERE account_number=?");

            update.setString(1, name);
            update.setString(2, acc);
            update.executeUpdate();

            System.out.println("Account Updated!");

        } catch (SQLException e) {
            printSQLError(e);
        }
    }

    // ---------------- DEPOSIT ----------------
    static void deposit() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Account Number: ");
            String acc = sc.next();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM accounts WHERE account_number=?");
            ps.setString(1, acc);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Account not found!");
                return;
            }

            System.out.print("Enter Deposit Amount: ");
            double amt = sc.nextDouble();

            double newBal = rs.getDouble("balance") + amt;

            PreparedStatement update = con.prepareStatement(
                    "UPDATE accounts SET balance=? WHERE account_number=?");

            update.setDouble(1, newBal);
            update.setString(2, acc);
            update.executeUpdate();

            saveTransaction(con, acc, "Deposited: $" + amt);

            System.out.println("Deposit Successful!");

        } catch (SQLException e) {
            printSQLError(e);
        }
    }

    // ---------------- WITHDRAW ----------------
    static void withdraw() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Account Number: ");
            String acc = sc.next();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM accounts WHERE account_number=?");
            ps.setString(1, acc);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Account not found!");
                return;
            }

            System.out.print("Enter Withdrawal Amount: ");
            double amt = sc.nextDouble();

            double bal = rs.getDouble("balance");

            if (amt > bal) {
                System.out.println("Insufficient Balance!");
                return;
            }

            double newBal = bal - amt;

            PreparedStatement update = con.prepareStatement(
                    "UPDATE accounts SET balance=? WHERE account_number=?");

            update.setDouble(1, newBal);
            update.setString(2, acc);
            update.executeUpdate();

            saveTransaction(con, acc, "Withdrawn: $" + amt);

            System.out.println("Withdrawal Successful!");

        } catch (SQLException e) {
            printSQLError(e);
        }
    }

    // ---------------- CHECK BALANCE ----------------
    static void checkBalance() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Account Number: ");
            String acc = sc.next();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT balance FROM accounts WHERE account_number=?");
            ps.setString(1, acc);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Current Balance: $" + rs.getDouble("balance"));
            } else {
                System.out.println("Account not found!");
            }

        } catch (SQLException e) {
            printSQLError(e);
        }
    }

    // ---------------- MINI STATEMENT ----------------
    static void miniStatement() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Account Number: ");
            String acc = sc.next();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT t1,t2,t3,t4,t5 FROM accounts WHERE account_number=?");
            ps.setString(1, acc);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Account not found!");
                return;
            }

            System.out.println("\n===== LAST 5 TRANSACTIONS =====");
            for (int i = 1; i <= 5; i++) {
                String t = rs.getString("t" + i);
                if (t != null)
                    System.out.println(t);
            }

        } catch (SQLException e) {
            printSQLError(e);
        }
    }

    // ---------------- SAVE TRANSACTION ----------------
    static void saveTransaction(Connection con, String acc, String text) throws SQLException {

        PreparedStatement ps = con.prepareStatement(
                "SELECT t1,t2,t3,t4,t5 FROM accounts WHERE account_number=?");
        ps.setString(1, acc);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) return;

        String[] t = new String[5];
        for (int i = 0; i < 5; i++)
            t[i] = rs.getString("t" + (i+1));

        // shift old transactions
        for (int i = 4; i > 0; i--)
            t[i] = t[i-1];
        t[0] = text;

        PreparedStatement upd = con.prepareStatement(
                "UPDATE accounts SET t1=?, t2=?, t3=?, t4=?, t5=? WHERE account_number=?");

        for (int i = 0; i < 5; i++)
            upd.setString(i+1, t[i]);

        upd.setString(6, acc);
        upd.executeUpdate();
    }

    // ---------------- SQL ERROR LOG ----------------
    static void printSQLError(SQLException e) {
        System.out.println("\n--- SQL ERROR ---");
        System.out.println("Message: " + e.getMessage());
        System.out.println("State: " + e.getSQLState());
        System.out.println("Code: " + e.getErrorCode());
        e.printStackTrace();
        System.out.println("------------------\n");
    }

}
