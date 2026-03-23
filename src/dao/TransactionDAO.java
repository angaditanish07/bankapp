package dao;

import model.Transaction;
import util.DBConnection;
import util.AppLogger;
import java.sql.*;
import java.util.*;

public class TransactionDAO {

    /** Called inside an external JDBC transaction — connection provided by caller */
    public void save(Connection conn, String senderVpa, String receiverVpa,
                     double amount, String status, String note) throws SQLException {
        String sql = "INSERT INTO transactions(sender_vpa, receiver_vpa, amount, status, note) VALUES(?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, senderVpa);
        ps.setString(2, receiverVpa);
        ps.setDouble(3, amount);
        ps.setString(4, status);
        ps.setString(5, note);
        ps.executeUpdate();
    }

    /** All transactions filtered by VPA, keyword, and status */
    public static List<Transaction> getAll(String vpa, String keyword, String statusFilter) {
        List<Transaction> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM transactions WHERE (sender_vpa=? OR receiver_vpa=?) ");
        if (statusFilter != null && !statusFilter.equals("ALL"))
            sql.append("AND status=? ");
        if (keyword != null && !keyword.trim().isEmpty())
            sql.append("AND (sender_vpa LIKE ? OR receiver_vpa LIKE ? OR note LIKE ?) ");
        sql.append("ORDER BY txn_date DESC");

        try (Connection c = DBConnection.getNewConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int i = 1;
            ps.setString(i++, vpa); ps.setString(i++, vpa);
            if (statusFilter != null && !statusFilter.equals("ALL"))
                ps.setString(i++, statusFilter);
            if (keyword != null && !keyword.trim().isEmpty()) {
                String k = "%" + keyword.trim() + "%";
                ps.setString(i++, k); ps.setString(i++, k); ps.setString(i++, k);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String sv = rs.getString("sender_vpa");
                String type = sv.equals(vpa) ? "DEBIT" : "CREDIT";
                Transaction t = new Transaction(sv, rs.getString("receiver_vpa"),
                    rs.getDouble("amount"), rs.getString("status"), type, rs.getString("note"));
                t.setDate(rs.getTimestamp("txn_date"));
                list.add(t);
            }
        } catch (Exception e) { AppLogger.error(e); }
        return list;
    }

    public static List<Transaction> getRecent(String vpa, int limit) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE (sender_vpa=? OR receiver_vpa=?) ORDER BY txn_date DESC LIMIT ?";
        try (Connection c = DBConnection.getNewConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, vpa); ps.setString(2, vpa); ps.setInt(3, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String sv = rs.getString("sender_vpa");
                String type = sv.equals(vpa) ? "DEBIT" : "CREDIT";
                Transaction t = new Transaction(sv, rs.getString("receiver_vpa"),
                    rs.getDouble("amount"), rs.getString("status"), type, rs.getString("note"));
                t.setDate(rs.getTimestamp("txn_date"));
                list.add(t);
            }
        } catch (Exception e) { AppLogger.error(e); }
        return list;
    }

    public static double totalSent(String vpa) {
        return sumWhere("sender_vpa", vpa);
    }

    public static double totalReceived(String vpa) {
        return sumWhere("receiver_vpa", vpa);
    }

    private static double sumWhere(String col, String vpa) {
        String sql = "SELECT COALESCE(SUM(amount),0) FROM transactions WHERE " + col + "=? AND status='SUCCESS'";
        try (Connection c = DBConnection.getNewConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, vpa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { AppLogger.error(e); }
        return 0;
    }
}
