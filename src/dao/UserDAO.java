package dao;

import model.User;
import util.DBConnection;
import util.AppLogger;
import java.sql.*;

public class UserDAO {

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users(name, mobile, email, vpa, upi_pin, balance) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBConnection.getNewConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getMobile());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getVpa());
            ps.setString(5, user.getUpiPin());
            ps.setDouble(6, 0.0);
            ps.executeUpdate();
            AppLogger.info("User registered: " + user.getVpa());
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.warn("Duplicate VPA/email on register: " + user.getVpa());
            return false;
        } catch (Exception e) {
            AppLogger.error(e);
            return false;
        }
    }

    public User getUserByVpa(String vpa) {
        String sql = "SELECT * FROM users WHERE vpa = ?";
        try (Connection c = DBConnection.getNewConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, vpa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception e) {
            AppLogger.error(e);
        }
        return null;
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
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

    public boolean vpaExists(String vpa) {
        String sql = "SELECT 1 FROM users WHERE vpa = ?";
        try (Connection c = DBConnection.getNewConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, vpa);
            return ps.executeQuery().next();
        } catch (Exception e) {
            AppLogger.error(e);
            return false;
        }
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getString("mobile"),
            rs.getString("email"),
            rs.getString("vpa"),
            rs.getString("upi_pin"),
            rs.getDouble("balance")
        );
    }
}
