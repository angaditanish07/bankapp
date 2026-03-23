package module1_authentication;
import module1_authentication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public boolean registerUser(User user) {

        try {

            Connection conn = DBConnection.getConnection();
            
            if (conn == null) {
                System.out.println("ERROR: Database connection is null in registerUser");
                return false;
            }

            String query = "INSERT INTO users(name,mobile,email,vpa,upi_pin) VALUES(?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, user.getName());
            ps.setString(2, user.getMobile());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getVpa());
            ps.setString(5, "1234");

            ps.executeUpdate();
            System.out.println("Registration successful for VPA: " + user.getVpa());

            return true;

        } catch (Exception e) {
            System.out.println("Registration error from DB:");
            e.printStackTrace();
        }

        return false;
    }

    public User getUserByVpa(String vpa) {

        try {

            Connection conn = DBConnection.getConnection();
            
            if (conn == null) {
                System.out.println("ERROR: Database connection is null in getUserByVpa");
                return null;
            }

            String query = "SELECT * FROM users WHERE vpa=?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, vpa);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                double balance = 0.0;
                try {
                    balance = rs.getDouble("balance");
                } catch (Exception e) {
                    System.out.println("Balance column not found, using default 0.0");
                }

                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("mobile"),
                        rs.getString("email"),
                        rs.getString("vpa"),
                        rs.getString("upi_pin"),
                        balance
                );
            }

        } catch (Exception e) {

            System.out.println("ERROR in getUserByVpa: ");
            e.printStackTrace();

        }

        return null;
    }
}