public class BankAccount {

    private String accountNumber;
    private String bankName;
    private double balance;
    private String userEmail;

    public BankAccount(String accountNumber, String bankName, double balance, String userEmail) {
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.balance = balance;
        this.userEmail = userEmail;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public double getBalance() {
        return balance;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}