import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        try {

            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/upi_system",
                    "root",
                    "root1234");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }
}