package Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() {
        try {
            String database_url = System.getenv("ORACLE_URL");
            String database_user = System.getenv("ORACLE_USER");
            String database_password = System.getenv("ORACLE_PASSWORD");
            return DriverManager.getConnection(database_url, database_user, database_password);
            //return DriverManager.getConnection("jdbc:oracle:thin:@oracle.inf.poa.ifrs.edu.br:1521:XE", "usr24", "usr24");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}