package br.com.alura.bytebank.repostory;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;
import br.com.alura.bytebank.domain.conta.Conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {
    private final ConnectionFactory factory;

    public ContaDAO() {
        this.factory = new ConnectionFactory();
    }

    public void salvar(Conta conta) throws SQLException {

        String sql = """
                insert into conta
                    (numero, saldo, cliente_nome, cliente_cpf, client_email)
                values (?, ?, ?, ?, ?);
                    """;


        PreparedStatement statement =null;
        Connection connection = null;
        try {
            connection = this.factory.connectionFactory();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, conta.getNumero());
            statement.setBigDecimal(2, BigDecimal.ZERO);
            statement.setString(3, conta.getTitular().getNome());
            statement.setString(4, conta.getTitular().getCpf());
            statement.setString(5, conta.getTitular().getEmail());
            System.out.println(statement.execute());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            statement.close();
            disconnect(connection);
        }
    }

    public Set<Conta> listarContas() throws SQLException {
        Set<Conta> contas = new HashSet<>();
        String sql = """
                select * from conta;
                """;
        Connection connection = this.factory.connectionFactory();
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            contas.add(new Conta(result.getInt(1), result.getBigDecimal(2),
                    new Cliente(new DadosCadastroCliente(result.getString(3),
                            result.getString(4),
                            result.getString(5)))));
        }
        statement.close();
        result.close();
        disconnect(connection);
        return contas;
    }


    public Conta buscarContarPorNumero(int numero) throws SQLException {
        Connection connection = factory.connectionFactory();
        PreparedStatement statement = connection.prepareStatement("""
                select * from conta 
                where numero = ?
                """);
        statement.setInt(1, numero);

        ResultSet result = statement.executeQuery();
        result.next();
        Conta conta = new Conta(result.getInt(1), result.getBigDecimal(2),
                new Cliente(new DadosCadastroCliente(result.getString(3),
                        result.getString(4),
                        result.getString(5))));

        statement.close();
        result.close();
        disconnect(connection);
        return conta;
    }

    public void disconnect(Connection connection) {
        try {
            connection.close();
            System.out.println("connection closed.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Conta conta) {
     String sql = """
             update conta
              set saldo = ? 
              where
              conta.numero = ?;
             """;
        PreparedStatement statement;
        Connection connection = factory.connectionFactory();
        try {
           statement  = connection.prepareStatement(sql);
           statement.setBigDecimal(1, conta.getSaldo());
           statement.setInt(2, conta.getNumero());
           statement.execute();

           statement.close();
           disconnect(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
