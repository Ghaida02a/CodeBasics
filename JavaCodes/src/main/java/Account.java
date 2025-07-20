import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Account {
    //attributes
    private String accountNumber;
    private String holderName;
    private double balance;
    private List<String> transactionHistory;


    // Constructor (initialize new Account objects)
    public Account(String accountNumber, String holderName) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = 0.0; // Default balance
        this.transactionHistory = new ArrayList<>();
    }

    // Getter for accountNumber
    public String getAccountNumber() {
        return accountNumber;
    }

    // Getter for holderName
    public String getHolderName() {
        return holderName;
    }

    // Getter for balance
    public double getBalance() {
        return balance;
    }

    // Method to deposit money
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            transactionHistory.add(LocalDate.now() + " | Deposited OMR " + amount);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    // Method to withdraw money
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Error: Amount must be positive.");
        } else if (amount > balance) {
            System.out.println("Error: The withdrawal amount is more than your current balance.");
        } else {
            balance -= amount; // Deduct the amount from balance
            transactionHistory.add(LocalDate.now() + " | Withdrew OMR " + amount);
        }
    }

    // Method to view balances
    public void viewBalance() {
        System.out.println("Account Holder: " + holderName);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Current Balance: OMR " + balance);
        System.out.println("Account status: Active");
    }

    // Method to get transaction history
    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}
