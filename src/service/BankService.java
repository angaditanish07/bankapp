package service;

import dao.AccountDAO;
import model.BankAccount;
import util.AppLogger;
import java.util.List;

public class BankService {

    private final AccountDAO accountDAO = new AccountDAO();

    public boolean linkAccount(BankAccount account) {
        // First account for this user automatically becomes primary
        List<BankAccount> existing = accountDAO.getByUserId(account.getUserId());
        if (existing.isEmpty()) account.setPrimary(true);
        boolean result = accountDAO.linkAccount(account);
        AppLogger.info("Link account result for userId " + account.getUserId() + ": " + result);
        return result;
    }

    public List<BankAccount> getAccounts(int userId) {
        return accountDAO.getByUserId(userId);
    }

    public BankAccount getPrimaryAccount(int userId) {
        return accountDAO.getPrimary(userId);
    }

    public void setPrimary(int accountId, int userId) {
        accountDAO.setPrimary(accountId, userId);
        AppLogger.info("Primary account set: accountId=" + accountId + " userId=" + userId);
    }
}
