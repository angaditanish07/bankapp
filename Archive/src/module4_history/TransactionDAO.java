import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TransactionDAO {

    public static ArrayList<Transaction> getTransactions(String email) {

        ArrayList<Transaction> list = new ArrayList<>();

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM transactions WHERE sender_email=? OR receiver_email=?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, email);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String sender = rs.getString("sender_email");
                String receiver = rs.getString("receiver_email");
                double amount = rs.getDouble("amount");
                String status = rs.getString("status");

                Transaction t = new Transaction(sender, receiver, amount, status);

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}