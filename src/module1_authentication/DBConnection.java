package module1_authentication;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12821066",
                    "sql12821066",
                    "41kJ9iD1aA"
            );

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}