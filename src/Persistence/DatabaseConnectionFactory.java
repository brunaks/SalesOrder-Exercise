package Persistence;

import java.sql.Connection;

public class DatabaseConnectionFactory {

    public static Connection getConnection() {
        return PostgresqlConnectionFactory.getConnection();
    }
}
