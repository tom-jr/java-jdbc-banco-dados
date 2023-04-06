import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    public Connection connectionFactory() {
        try {
            String url = "jdbc:postgresql://localhost/byte_bank";
            Properties properties = new Properties();
            properties.setProperty("user", "postgres");
            properties.setProperty("password", "postgres");
            properties.setProperty("sll", "true");

            return DriverManager.getConnection(url, properties);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
