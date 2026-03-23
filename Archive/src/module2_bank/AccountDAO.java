import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountDAO {

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
}