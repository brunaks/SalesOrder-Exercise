package Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnectionFactory {
    public static Connection getConnection() {
        try {
            String database_url = System.getenv("ORACLE_URL");
            String database_user = System.getenv("ORACLE_USER");
            String database_password = System.getenv("ORACLE_PASSWORD");
            return DriverManager.getConnection(database_url, database_user, database_password);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}