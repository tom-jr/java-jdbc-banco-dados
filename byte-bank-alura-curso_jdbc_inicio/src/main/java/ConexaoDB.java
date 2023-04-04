import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoDB {
    public static void main(String[] args) {
        try {
            String url = "jdbc:postgresql://localhost/byte_bank";
            Properties properties = new Properties();
            properties.setProperty("user","postgres");
            properties.setProperty("password","postgres");
            properties.setProperty("sll","true");

            Connection connection = DriverManager.getConnection(url, properties);

            System.out.println("OKAY");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
