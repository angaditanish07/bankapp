package module4_history;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import module1_authentication.DBConnection;
import module3_payments.Transaction;

public class TransactionDAO {

    public boolean saveTransaction(String senderVpa, String receiverVpa, double amount, String status) {

        try {

            Connection conn = DBConnection.getConnection();

            String query = "INSERT INTO transactions(sender_vpa, receiver_vpa, amount, status) VALUES(?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, senderVpa);
            ps.setString(2, receiverVpa);
            ps.setDouble(3, amount);
            ps.setString(4, status);

            ps.executeUpdate();

            return true;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // used by TransactionHistoryFrame
    public static ArrayList<Transaction> getTransactions(String vpa) {

        ArrayList<Transaction> list = new ArrayList<>();

        try {

            Connection conn = DBConnection.getConnection();

            String query = "SELECT * FROM transactions WHERE sender_vpa=? OR receiver_vpa=?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, vpa);
            ps.setString(2, vpa);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                Transaction t = new Transaction(
                        rs.getString("sender_vpa"),
                        rs.getString("receiver_vpa"),
                        rs.getDouble("amount"),
                        rs.getString("status")
                );

                t.setDate(rs.getTimestamp("date"));

                list.add(t);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}