package BankManagement;

import java.util.ArrayList;
import java.util.Scanner;

class BankAccount {
    private String accountNumber;
    private String holderName;
    private double balance;
    private ArrayList<String> transactions; // To store last 5 transactions

    // Constructor
    public BankAccount(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    // Method to check balance
    public void checkBalance() {
        System.out.println("Current balance: $" + balance);
        recordTransaction("Checked balance");
    }

    // Method to deposit money
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("$" + amount + " deposited successfully.");
            recordTransaction("Deposited: $" + amount);
        } else {
            System.out.println("Invalid amount. Deposit must be greater than zero.");
        }
    }

    // Method to withdraw money
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("$" + amount + " withdrawn successfully.");
            recordTransaction("Withdrew: $" + amount);
        } else {
            System.out.println("Invalid amount or insufficient balance.");
        }
    }

    // Method to view mini-statement (last 5 transactions)
    public void viewMiniStatement() {
        System.out.println("Last 5 transactions:");
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
    }

    // Helper method to record transactions in the last 5 transactions
    private void recordTransaction(String transaction) {
        if (transactions.size() == 5) {
            transactions.remove(0); // Remove the oldest transaction if there are already 5
        }
        transactions.add(transaction);
    }
}

public class ATMSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create a sample account
        BankAccount account = new BankAccount("12345", "John Doe", 1000.00);

        int option;
        do {
            // Display menu options
            System.out.println("\nATM Banking System");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Mini-Statement");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            option = scanner.nextInt();

            // Perform corresponding operation based on user input
            switch (option) {
                case 1:
                    account.checkBalance();
                    break;
                case 2:
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;
                case 3:
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawalAmount = scanner.nextDouble();
                    account.withdraw(withdrawalAmount);
                    break;
                case 4:
                    account.viewMiniStatement();
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 5);

        scanner.close();
    }
}


