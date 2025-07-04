import java.util.Scanner;

class BankAccount {
    private double balance; 

    public BankAccount(double initialBalance) {
        if (initialBalance >= 0) {
            this.balance = initialBalance;
        } else {
            this.balance = 0; 
            System.out.println("Initial balance cannot be negative. Setting to 0.");
        }
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Successfully deposited: $" + String.format("%.2f", amount));
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        if (amount > balance) { 
            System.out.println("Insufficient Balance. Current balance: $" + String.format("%.2f", balance));
            return false;
        }
        balance -= amount;
        System.out.println("Successfully withdrew: $" + String.format("%.2f", amount));
        return true;
    }
}

class ATM {
    private BankAccount userAccount; 

    public ATM(BankAccount account) {
        this.userAccount = account;
    }

    public void displayMenu() {
        System.out.println("\n--- Welcome to the ATM ---");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Exit");
        System.out.print("Please choose an option: ");
    }


    public void checkBalance() {

        System.out.println("Your current balance is: $" + String.format("%.2f", userAccount.getBalance()));
    }

    public void processDeposit(Scanner scanner) {
        System.out.print("Enter amount to deposit: $");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            System.out.println("Invalid deposit amount. Please enter a positive value.");
        } else {
            userAccount.deposit(amount);
        }
    }

    public void processWithdrawal(Scanner scanner) {
        System.out.print("Enter amount to withdraw: $");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount. Please enter a positive value.");
        } else {
            userAccount.withdraw(amount); 
        }
    }
}

public class ATMInterface {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter initial account balance: $");
        double initialBalance = scanner.nextDouble();
        BankAccount account = new BankAccount(initialBalance);

        ATM atm = new ATM(account);

        boolean running = true;
        while (running) {
            atm.displayMenu(); 
            int choice = -1;
            try {
                choice = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 - 4.");
                scanner.next();  
                continue;  
            }

            switch (choice) {
                case 1:
                    atm.checkBalance();  
                    break;
                case 2:
                    atm.processDeposit(scanner); 
                    break;
                case 3:
                    atm.processWithdrawal(scanner); 
                    break;
                case 4:
                    running = false;
                    System.out.println("Thank you for using the ATM.!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }

        scanner.close(); 
    }
}
