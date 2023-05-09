package br.com.alura.bytebank;

import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;
import br.com.alura.bytebank.domain.conta.Conta;
import br.com.alura.bytebank.domain.conta.ContaService;
import br.com.alura.bytebank.domain.conta.DadosAberturaConta;
import br.com.alura.bytebank.repostory.ContaDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

public class BytebankApplication {

    private static ContaService service = new ContaService(new ContaDAO());
    private static Scanner teclado = new Scanner(System.in).useDelimiter("\n");

    public static void main(String[] args) throws SQLException {
        var opcao = exibirMenu();
        while (opcao != 8) {
            try {
                switch (opcao) {
                    case 1:
                        listarContas();
                        break;
                    case 2:
                        abrirConta();
                        break;
                    case 3:
                        encerrarConta();
                        break;
                    case 4:
                        consultarSaldo();
                        break;
                    case 5:
                        realizarSaque();
                        break;
                    case 6:
                        realizarDeposito();
                        break;

                    case 7:
                        realizarTransferencia();
                        break;
                }
            } catch (RegraDeNegocioException | SQLException e) {
                System.out.println("Erro: " +e.getMessage());
                System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu");
                teclado.next();
            }
            opcao = exibirMenu();
        }
        System.out.println("Finalizando a aplicação.");
    }

    private static int exibirMenu() {
        System.out.println("""
                BYTEBANK - ESCOLHA UMA OPÇÃO:
                1 - Listar contas abertas
                2 - Abertura de conta
                3 - Encerramento de conta
                4 - Consultar saldo de uma conta
                5 - Realizar saque em uma conta
                6 - Realizar depósito em uma conta
                7 - Realizar transferencia entre contas
                8 - Sair
                """);
        return teclado.nextInt();
    }

    private static void listarContas() throws SQLException {
        System.out.println("Contas cadastradas:");
        var contas = service.listarContasAbertas();
        contas.stream().forEach(System.out::println);

        finalMessage();
    }

    private static void abrirConta() throws SQLException {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o nome do cliente:");
        var nome = teclado.next();

        System.out.println("Digite o cpf do cliente:");
        var cpf = teclado.next();

        System.out.println("Digite o email do cliente:");
        var email = teclado.next();

        service.abrir(new DadosAberturaConta(numeroDaConta, new DadosCadastroCliente(nome, cpf, email)));

        System.out.println("Conta aberta com sucesso!");
        finalMessage();
    }

    private static void encerrarConta() throws SQLException {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        service.encerrar(numeroDaConta);

        System.out.println("Conta encerrada com sucesso!");
        finalMessage();
    }

    private static void consultarSaldo() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();
        Conta conta = service.consultarSaldo(numeroDaConta);
        System.out.println("Titular: " + conta.getTitular().getNome());
        System.out.println("Saldo da conta: " + conta.getSaldo());

        finalMessage();
    }

    private static void realizarSaque() throws SQLException {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o valor do saque:");
        var valor = teclado.nextBigDecimal();

        service.realizarSaque(numeroDaConta, valor);
        System.out.println("Saque realizado com sucesso!");
        finalMessage();
    }

    private static void realizarDeposito() throws SQLException {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o valor do depósito:");
        var valor = teclado.nextBigDecimal();

        service.realizarDeposito(numeroDaConta, valor);

        System.out.println("Depósito realizado com sucesso!");
        finalMessage();
    }

    private static void realizarTransferencia() throws SQLException {
        System.out.println("Digite o número da conta do autor da transfêrencia:");
        var numeroContaAutor = teclado.nextInt();

        System.out.println("Digite o valor da transfêrencia:");
        var valor = teclado.nextBigDecimal();


        System.out.println("Digite o número da conta do beneficiado da transfêrencia:");
        var numeroContaBeneficiario = teclado.nextInt();

        BigDecimal valorTransferencia = service.realizarSaque(numeroContaAutor, valor);


        service.realizarDeposito(numeroContaBeneficiario, valorTransferencia);
        System.out.println("Transfêrencia realizada com sucesso.");
        finalMessage();
    }

    private static void finalMessage() {
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        teclado.next();
    }
}
