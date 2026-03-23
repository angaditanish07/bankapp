package module2_bank;
import module1_authentication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class AccountDAO {
    public static BankAccount getAccountByEmail(String email) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM bank_accounts WHERE user_email=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new BankAccount(
                        rs.getString("account_number"),
                        rs.getString("bank_name"),
                        rs.getDouble("balance"),
                        rs.getString("user_email")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static boolean linkBankAccount(BankAccount account) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO bank_accounts(account_number, bank_name, balance, user_email) VALUES(?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getBankName());
            stmt.setDouble(3, account.getBalance());
            stmt.setString(4, account.getUserEmail());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static void updateBalance(BankAccount acc) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE bank_accounts SET balance=? WHERE account_number=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setDouble(1, acc.getBalance());
            stmt.setString(2, acc.getAccountNumber());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}