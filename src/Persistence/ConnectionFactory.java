package Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:oracle:thin:@oracle.inf.poa.ifrs.edu.br:1521:XE", "0143138", "0143138");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}