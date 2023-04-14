package br.com.alura.bytebank.repostory;

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

import br.com.alura.bytebank.ConnectionFactory;

public class ContaDAO {
    private final Connection connection;

    public ContaDAO() {
        this.connection = new ConnectionFactory().connectionFactory();
    }

    public void salvar(Conta conta){
        String sql = """
                insert into conta
                    (numero, saldo, cliente_nome, cliente_cpf, client_email)
                values (?, ?, ?, ?, ?);
                    """;


        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setInt(1, conta.getNumero());
            statement.setBigDecimal(2, BigDecimal.ZERO);
            statement.setString(3, conta.getTitular().getNome());
            statement.setString(4, conta.getTitular().getCpf());
            statement.setString(5, conta.getTitular().getEmail());
            System.out.println(statement.execute());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Set<Conta> listarContas() throws SQLException {
        Set<Conta> contas = new HashSet<>();
        String sql = """
                select * from conta;
                """;
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            contas.add(new Conta(result.getInt(1), result.getBigDecimal(2),
                    new Cliente(new DadosCadastroCliente(result.getString(3),
                            result.getString(4),
                            result.getString(5)))));
        }
        return contas;
    }

    public void disconnect() throws SQLException {
        this.connection.close();
    }
}
