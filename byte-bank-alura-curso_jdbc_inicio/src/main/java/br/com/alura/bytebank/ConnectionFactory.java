package br.com.alura.bytebank;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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

            System.out.println("connection opened.");

//            return DriverManager.getConnection(url, properties);
            //Utilizando o Hikari para ter pool de conexão
            return createDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost/byte_bank");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }

}
