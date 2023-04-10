package br.com.alura.bytebank.repostory;

import br.com.alura.bytebank.domain.conta.Conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            this.connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
