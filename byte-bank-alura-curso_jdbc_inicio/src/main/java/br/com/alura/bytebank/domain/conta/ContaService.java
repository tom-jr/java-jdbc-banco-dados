package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.repostory.ContaDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaService {
    private final ContaDAO dao;

    private Set<Conta> contas = new HashSet<>();

    public ContaService(ContaDAO dao) {
        this.dao = dao;
    }

    public Set<Conta> listarContasAbertas() throws SQLException {
        return dao.listarContas();
    }

    public Conta consultarSaldo(Integer numeroDaConta) {
        Conta conta = null;
        try {
            conta = dao.buscarContarPorNumero(numeroDaConta);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conta;
    }

    public void abrir(DadosAberturaConta dadosDaConta) throws SQLException {
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);
        if (contas.contains(conta)) {
            throw new RegraDeNegocioException("Já existe outra conta aberta com o mesmo número!");
        }

        contas.add(conta);
        ContaDAO dao = new ContaDAO();
        dao.salvar(conta);
    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) throws SQLException {
        var conta = dao.buscarContarPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        conta.sacar(valor);
        dao.atualizarSaldo(conta);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) throws SQLException {
        Conta conta = dao.buscarContarPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }

        conta.depositar(valor);
        dao.atualizarSaldo(conta);
    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        contas.remove(conta);
    }

    private Conta buscarContaPorNumero(Integer numero) {
        return contas
                .stream()
                .filter(c -> c.getNumero() == numero)
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Não existe conta cadastrada com esse número!"));
    }
}
