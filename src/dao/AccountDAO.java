package dao;

import model.BankAccount;
import util.DBConnection;
import util.AppLogger;
import java.sql.*;
import java.util.*;

public class AccountDAO {

    public boolean linkAccount(BankAccount acc) {
        String sql = "INSERT INTO bank_accounts(account_number, bank_name, balance, user_id, is_primary) VALUES(?,?,?,?,?)";
        try (Connection c = DBConnection.getNewConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, acc.getAccountNumber());
            ps.setString(2, acc.getBankName());
            ps.setDouble(3, acc.getBalance());
            ps.setInt(4, acc.getUserId());
            ps.setBoolean(5, acc.isPrimary());
            ps.executeUpdate();
            AppLogger.info("Account linked: " + acc.getAccountNumber());
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.warn("Duplicate account number: " + acc.getAccountNumber());
            return false;
        } catch (Exception e) {
            AppLogger.error(e);
            return false;
        }
    }

    public List<BankAccount> getByUserId(int userId) {
        List<BankAccount> list = new ArrayList<>();
        String sql = "SELECT * FROM bank_accounts WHERE user_id = ? ORDER BY is_primary DESC";
        try (Connection c = DBConnection.getNewConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) {
            AppLogger.error(e);
        }
        return list;
    }

    public BankAccount getPrimary(int userId) {
        String sql = "SELECT * FROM bank_accounts WHERE user_id = ? AND is_primary = TRUE LIMIT 1";
        try (Connection c = DBConnection.getNewConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception e) {
            AppLogger.error(e);
        }
        return null;
    }

    public void setPrimary(int accountId, int userId) {
        try (Connection c = DBConnection.getNewConnection()) {
            PreparedStatement ps1 = c.prepareStatement(
                "UPDATE bank_accounts SET is_primary = FALSE WHERE user_id = ?");
            ps1.setInt(1, userId);
            ps1.executeUpdate();
            PreparedStatement ps2 = c.prepareStatement(
                "UPDATE bank_accounts SET is_primary = TRUE WHERE account_id = ?");
            ps2.setInt(1, accountId);
            ps2.executeUpdate();
        } catch (Exception e) {
            AppLogger.error(e);
        }
    }

    private BankAccount map(ResultSet rs) throws SQLException {
        return new BankAccount(
            rs.getInt("account_id"),
            rs.getString("account_number"),
            rs.getString("bank_name"),
            rs.getDouble("balance"),
            rs.getInt("user_id"),
            rs.getBoolean("is_primary")
        );
    }
}
