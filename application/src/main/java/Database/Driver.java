/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/****
 * This will be deprecated soon and is only temporary
 */
public abstract class Driver {
    private static String IP = "localhost";
    private static String PORT = "3306";
    private static String DB_NAME = "dcs";
    private static String USER = "root";
    private static String PASS = "root";

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static String DB_URL = "jdbc:mysql://" + IP + ":" + PORT + "/" + DB_NAME + "?useSSL=false";

    public static Connection getConnection() {
        Connection conn;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("[DB] Connection estabilished.");
        } catch (Exception e) {
            conn = null;
            System.err.println("[DB] Failed to estabilish connection!" + e.getLocalizedMessage());
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            System.err.println("[DB] Impossible to close connection!" + e.getLocalizedMessage());
        }
        System.out.println("[DB] Connection closed.");
    }

    public static void transactionRollback(Connection conn) {
        try {
            System.out.println("[DB] Rolling back.");
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.err.println("Transaction rollback NOT possible!" + e.getLocalizedMessage());
        }
    }
}