import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public boolean registerUser(User user) {

        try {

            Connection conn = DBConnection.getConnection();

            String query = "INSERT INTO users(name,mobile,email,vpa,upi_pin) VALUES(?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, user.getName());
            ps.setString(2, user.getMobile());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getVpa());
            ps.setString(5, "1234");

            ps.executeUpdate();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }

    public User getUserByVpa(String vpa) {

        try {

            Connection conn = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE vpa=?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, vpa);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("mobile"),
                        rs.getString("email"),
                        rs.getString("vpa"),
                        rs.getString("upi_pin")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }
}