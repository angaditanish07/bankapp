package model;

public class BankAccount {

    private int    accountId;
    private String accountNumber;
    private String bankName;
    private double balance;
    private int    userId;
    private boolean primary;

    public BankAccount(int accountId, String accountNumber, String bankName,
                       double balance, int userId, boolean primary) {
        this.accountId     = accountId;
        this.accountNumber = accountNumber;
        this.bankName      = bankName;
        this.balance       = balance;
        this.userId        = userId;
        this.primary       = primary;
    }

    /** Convenience constructor for a new (unsaved) account */
    public BankAccount(String accountNumber, String bankName, double balance, int userId) {
        this(0, accountNumber, bankName, balance, userId, false);
    }

    public int    getAccountId()     { return accountId; }
    public String getAccountNumber() { return accountNumber; }
    public String getBankName()      { return bankName; }
    public double getBalance()       { return balance; }
    public int    getUserId()        { return userId; }
    public boolean isPrimary()       { return primary; }

    public void setBalance(double balance) { this.balance = balance; }
    public void setPrimary(boolean primary){ this.primary = primary; }

    @Override
    public String toString() {
        return bankName + "  •  " + accountNumber + (primary ? "  ★ Primary" : "");
    }
}
