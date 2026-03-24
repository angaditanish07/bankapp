package util;

import java.sql.*;

public class DBConnection {

    private static final String URL  = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12821066?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "sql12821066";
    private static final String PASS = "41kJ9iD1aA";
    private static Connection instance;

    private DBConnection() {}

    /** Shared singleton — for simple reads/writes */
    public static synchronized Connection getInstance() {
        try {
            if (instance == null || instance.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (Exception e) {
            AppLogger.error(e);
        }
        return instance;
    }

    /** Fresh connection — use for JDBC transactions (PaymentService) */
    public static Connection getNewConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            AppLogger.error(e);
            return null;
        }
    }
}