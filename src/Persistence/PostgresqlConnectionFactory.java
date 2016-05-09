package Persistence;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Bruna Koch Schmitt on 09/05/2016.
 */
public class PostgresqlConnectionFactory {
    public static Connection getConnection() {
        try {
            URI databaseUri = new URI(System.getenv("DATABASE_URL"));

            String username = databaseUri.getUserInfo().split(":")[0];
            String password = databaseUri.getUserInfo().split(":")[1];

            String databaseUrl = "jdbc:postgresql://" + databaseUri.getHost() + ':' + databaseUri.getPort() + databaseUri.getPath();

            return DriverManager.getConnection(databaseUrl, username, password);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
