import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/upi_system";
        String user = "root";
        String password = "root1234";

        try {

            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                System.out.println("Database Connected Successfully!");
            }

        } catch (Exception e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }

    }
}