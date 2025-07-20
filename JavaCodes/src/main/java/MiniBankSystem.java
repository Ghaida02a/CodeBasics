import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class MiniBankSystem {
    // Static collections
    public static int accountCounter = 1;
    static Map<String, Account> accounts = new HashMap<>(); // Stores all accounts by account number
    static Map<String, List<String>> transactionHistories = new HashMap<>(); // Stores transaction histories by account number
    private static String loggedInAccount; // Currently logged-in account number

    private static final String bank_Name = "Bank Muscat";
    private static double minimum_Balance = 0.00;

    public static void main(String[] args) {
        System.out.println("Welcome to " + bank_Name);
        Scanner inputScanner = new Scanner(System.in); // Create a single Scanner instance
        loadFromFile();
        boolean isRunning = true; // Initialize isRunning to true
        while (isRunning) { // Loop asking until it is not run
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. View Balances");
            System.out.println("6. View Transaction History");
            System.out.println("7. LogOut");
            System.out.println("8. Exit");
            System.out.print("Please select what you want to do: ");

            int select = inputScanner.nextInt();
            inputScanner.nextLine(); // Clear the buffer
            switch (select) {
                case 1:
                    createAccount(inputScanner);
                    break;
                case 2:
                    logIn(inputScanner);
                    break;
                case 3:
                    depositMoney(inputScanner);
                    break;
                case 4:
                    withdrawMoney(inputScanner);
                    break;
                case 5:
                    viewBalances();
                    break;
                case 6:
                    viewTransactionHistory();
                    break;
                case 7:
                    logOut();
                    break;
                case 8:
                    saveToFile(); // Save accounts to file
                    isRunning = false;
                    System.out.println("Thank you for banking with us!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        inputScanner.close(); // Close the scanner at the end
    }

    public static void createAccount(Scanner inputScanner) {
        // Generate a unique account number
        String accountNumber = generateAccountNumber();
        // Check if account number already exists in the list
        while (accounts.containsKey(accountNumber)) {
            accountNumber = generateAccountNumber(); // Regenerate if it already exists
        }

        System.out.print("Enter your name: ");
        String holderName = inputScanner.nextLine();

        // Create a new account using the constructor
        Account newAccount = new Account(accountNumber, holderName);
        accounts.put(accountNumber, newAccount); // Save account number and holder name to the hashmap
        transactionHistories.put(accountNumber, new ArrayList<>());

        System.out.println("Account created successfully!");
        System.out.println("Added a new account holder: " + holderName);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("The Account is Active");
    }

    public static void logIn(Scanner inputScanner) {
        System.out.print("\nEnter account number: "); //ask for account number
        String accountNumber = inputScanner.nextLine();

        if (!accounts.containsKey(accountNumber)) {
            System.out.println("Account not found!");
            return;
        }
        //if it is found, print logged in message
        loggedInAccount = accountNumber;
        System.out.println("Logged in successfully as " + accounts.get(accountNumber).getHolderName());
    }

    public static void depositMoney(Scanner inputScanner) {
        if (loggedInAccount != null) {
            Account account = accounts.get(loggedInAccount);
            System.out.println("Current balance: OMR " + account.getBalance());
            System.out.print("Enter amount to deposit: OMR ");
            double amountOfDeposit = inputScanner.nextDouble();
            inputScanner.nextLine(); // Clear the buffer

            if (amountOfDeposit > 0) {
                account.deposit(amountOfDeposit);//call deposit method from Account class
                System.out.println("Success! The balance after deposit is OMR " + account.getBalance());
                transactionHistories.get(loggedInAccount).add(LocalDate.now() + " | Deposited OMR " + amountOfDeposit);//add the process to transactionHistories list
            }
            else {
                System.out.println("Cannot deposit money. Invalid number");
            }
        }
        else {
            System.out.println("Please Sign in or Login first");
        }
    }

    public static void withdrawMoney(Scanner inputScanner) {
        if (loggedInAccount != null) {
            Account account = accounts.get(loggedInAccount);
            System.out.println("Current balance: OMR " + account.getBalance());
            System.out.print("Enter amount to withdraw: OMR ");
            double amount = inputScanner.nextDouble();
            inputScanner.nextLine(); // Clear the buffer

            if (amount <= 0) {
                System.out.println("Error: Amount must be positive.");
            }
            else if (amount > account.getBalance()) {
                System.out.println("Error: The withdrawal amount is more than your current balance.");
            }
            else {
                account.withdraw(amount);//call withdraw method from Account class
                System.out.println("Success! Withdrew OMR " + amount);
                System.out.println("New balance: OMR " + account.getBalance());
                transactionHistories.get(loggedInAccount).add(LocalDate.now() + " | Withdrew OMR " + amount);//add the process to transactionHistories list
            }
        }
        else {
            System.out.println("Please Sign in or Login first");
        }
    }

    public static void viewBalances() {
        if (loggedInAccount != null) {
            Account account = accounts.get(loggedInAccount);
            account.viewBalance();
        } else {
            System.out.println("Please Sign in or Login first");
        }
    }

    public static void viewTransactionHistory() {
        if (loggedInAccount != null) {
            List<String> history = transactionHistories.get(loggedInAccount);
            if (history.isEmpty()) {
                System.out.println("No transactions found.");
            }
            else {//get the list of what loggedInAccount user did
                System.out.println("Transaction History:");
                for (String transaction : history) {
                    System.out.println(transaction);
                }
            }
        }
        else {
            System.out.println("Please Sign in or Login first");
        }
    }

    public static void logOut() {
        if (loggedInAccount != null) {
            System.out.println("Logged out from account: " + loggedInAccount);
            loggedInAccount = null;
        }
        else {
            System.out.println("No account is logged in");
        }
    }

    public static void saveToFile() {//save everything to text file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("allAccounts.txt"))) {
            for (Account account : accounts.values()) {
                writer.write("===== Account Details =====");
                writer.newLine();

                writer.write("Account Number: " + account.getAccountNumber());
                writer.newLine();

                writer.write("Holder Name: " + account.getHolderName());
                writer.newLine();

                writer.write("Balance: OMR " + account.getBalance());
                writer.newLine();

                writer.write("----- Transaction History -----");
                writer.newLine();

                // Write each transaction on a new line
                List<String> transactions = account.getTransactionHistory();
                if (transactions.isEmpty()) {
                    writer.write("No transactions yet.");
                }
                else {
                    for (String transaction : transactions) {
                        writer.write(transaction);
                        writer.newLine();
                    }
                }
                writer.newLine(); // Blank line between accounts
            }
            System.out.println("Accounts saved successfully to allAccounts.txt");
        }
        catch (IOException e) {//if file can't be saved
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    public static void loadFromFile() {//load all accounts and its data
        try (BufferedReader reader = new BufferedReader(new FileReader("allAccounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("===== Account Details =====")) {
                    String accountNumber = null;
                    String holderName = null;
                    double balance = 0.0;
                    List<String> transactions = new ArrayList<>();

                    // Read the next lines to get account details
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        if (line.startsWith("Account Number: ")) {
                            accountNumber = line.substring("Account Number: ".length()).trim();
                        } else if (line.startsWith("Holder Name: ")) {
                            holderName = line.substring("Holder Name: ".length()).trim();
                        } else if (line.startsWith("Balance: OMR ")) {
                            balance = Double.parseDouble(line.substring("Balance: OMR ".length()).trim());
                        } else if (line.startsWith("----- Transaction History -----")) {
                            // Read transaction history
                            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                                transactions.add(line);
                            }
                        }
                    }
                    // Create the account and add it to the map
                    Account account = new Account(accountNumber, holderName);
                    account.deposit(balance); // Set the balance using deposit method
                    for (String transaction : transactions) {
                        account.getTransactionHistory().add(transaction);
                    }
                    accounts.put(account.getAccountNumber(), account);
                    transactionHistories.put(account.getAccountNumber(), transactions); // Initialize transaction history
                }
            }
            System.out.println("Accounts loaded successfully from allAccounts.txt");
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    private static String generateAccountNumber() {// Generate a unique account number
        return "" + accountCounter++;
    }
}
