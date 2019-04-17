package com.example.banco.util;

import java.sql.*;
import java.util.ArrayList;

import com.example.banco.cartao.Cartao;
import com.example.banco.conta.Conta;
import com.example.banco.movimentos.Deposito;
import com.example.banco.movimentos.Levantamento;
import com.example.banco.movimentos.Transferencia;
import com.example.banco.pessoa.Cliente;

//Class utilitária para acesso à base de dados
public class BdUtil {
	private static final String BD_URL		= "jdbc:mysql://137.74.114.78:3306/banco?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String BD_USER		= "admin";
	private static final String BD_PASSWORD	= "XjAnxgL:9SK=QW*}";

	//Metodo para obter um cartao
	public static Cartao obterCartao(int nrCartao) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM cartao WHERE nrCartao = ?;");
			ResultSet rs;

			stmt.setInt(1, nrCartao);
			rs = stmt.executeQuery();

			while (rs.next()) {
				return new Cartao(
						BdUtil.obterConta(rs.getInt("nrConta")),
						rs.getInt("nrCartao"),
						rs.getString("tpCartao"),
						rs.getBoolean("ativo")
				);
			}

			return null;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}

		return null;
	}

	//Metodo para obter transferencia
	public static ArrayList<Transferencia> obterTransferencia(Conta conta) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT nrTransferencia, nrContaOrigem, nrContaDestino, montante FROM transferencia INNER JOIN conta ON transferencia.nrContaOrigem = conta.nrconta WHERE idCliente = ?;");
			ResultSet rs;
			ArrayList<Transferencia> lista = new ArrayList<>();

			stmt.setInt(1, conta.getCliente().getNrCliente());
			rs = stmt.executeQuery();

			while (rs.next()) {
				Transferencia transferencia = new Transferencia(rs.getInt("nrTransferencia"),
						BdUtil.obterConta(rs.getInt("nrContaOrigem")),
						BdUtil.obterConta(rs.getInt("nrContaDestino")),
						rs.getDouble("montante"));
				lista.add(transferencia);
			}

			close(connection, stmt, rs);
			return lista;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
		return null;
	}

	//Metodo para obter levantamento
	public static ArrayList<Levantamento> obterLevantamento(Conta conta) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT nrLevantamento, levantamento.nrConta, montante FROM levantamento INNER JOIN conta ON levantamento.nrConta = conta.nrconta WHERE idCliente = ?;");
			ResultSet rs;
			ArrayList<Levantamento> lista = new ArrayList<>();

			stmt.setInt(1, conta.getCliente().getNrCliente());
			rs = stmt.executeQuery();

			while (rs.next()) {
				lista.add(new Levantamento(
					rs.getInt("nrLevantamento"),
					BdUtil.obterConta(rs.getInt("nrConta")),
					rs.getDouble("montante")
				));
			}

			close(connection, stmt, rs);
			return lista;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
		return null;
	}

	//Metodo para obter deposito
	public static ArrayList<Deposito> obterDepositos(Conta conta) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT nrDeposito, deposito.nrConta, montante, deposito.nrCartao FROM deposito INNER JOIN conta ON deposito.nrConta = conta.nrconta INNER JOIN cartao ON deposito.nrCartao = cartao.nrCartao WHERE idCliente = ?;");
			ResultSet rs;
			ArrayList<Deposito> lista = new ArrayList<>();

			stmt.setInt(1, conta.getCliente().getNrCliente());
			rs = stmt.executeQuery();

			while (rs.next()) {
				lista.add(new Deposito(
						rs.getInt("nrDeposito"),
						BdUtil.obterConta(rs.getInt("nrConta")),
						rs.getDouble("montante"),
						BdUtil.obterCartao(rs.getInt("nrCartao"))
				));
			}

			close(connection, stmt, rs);
			return lista;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
		return null;
	}

	//Metodo para desativar cartao
	public static void desativarCartao(int nrCartao) {
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement("UPDATE cartao SET ativo = false WHERE nrCartao = ?;");
			
			stmt.setInt(1, nrCartao);
			stmt.execute();
			
			stmt.close();
			conn.close();
			
			System.out.println("Cartão desativado com sucesso");
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
	}
	
	//Metodo para obter cartoes
	public static ArrayList<Cartao> obterCartoes(int nrCliente) {
		try {
			ArrayList<Cartao> listaCartao = new ArrayList<Cartao>();
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT cartao.nrConta, tpCartao, cartao.ativo, nrCartao, idCliente FROM cartao INNER JOIN conta ON cartao.nrConta = conta.nrConta WHERE conta.idCliente = ?;");
			ResultSet rs = null;
			
			stmt.setInt(1, nrCliente);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				listaCartao.add(new Cartao(
						obterConta(rs.getInt("nrConta")),
						rs.getInt("nrCartao"),
						rs.getString("tpCartao"),
						rs.getBoolean("ativo")
				));
			}	
			
			rs.close();
			stmt.close();
			conn.close();
			
			return listaCartao;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
		
		return null;
	}
	
	//Metodo para criar um cartao
	public static void criarCartao(Cartao cartao) {
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO cartao (nrConta, tpCartao, ativo, nrCartao) VALUES (?, ?, true, ?);");
			
			stmt.setInt(1, cartao.getConta().getNrConta());
			stmt.setString(2, cartao.getTipoCartao());
			stmt.setString(3, null);
			stmt.execute();

			close(conn, stmt);
			System.out.println("Cartão criado com sucesso.");
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
	}

	//Metodo para desativar uma conta
	public static void desativarConta(Conta conta) {
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement("UPDATE conta SET ativo = false WHERE nrConta = ?;");
			
			stmt.setInt(1, conta.getNrConta());
			stmt.execute();
			
			close(conn, stmt);
			System.out.println("Conta desativada com sucesso");
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
	}

	//Metodo para criar uma conta
	public static void criarConta(Conta conta) {
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO conta (nrConta, saldo, juros, tpConta, idCliente, ativo) VALUES (?, ?, ?, ?, ?, true);");
			
			stmt.setString(1, null);
			stmt.setDouble(2, conta.getSaldo());
			if (conta.getTipoConta().equals("Ordem")) {
				stmt.setString(3, null);
			}
			else {
				stmt.setDouble(3, 5);
			}
			stmt.setString(4, conta.getTipoConta());
			stmt.setInt(5, conta.getCliente().getNrCliente());
			stmt.execute();
			
			close(conn, stmt);
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
	}

	//Metodo para transferir entre contas
	public static void transferencia(Transferencia transferencia) {
		try {
			Connection connection = getConnection();
			PreparedStatement statementTransferencia = connection.prepareStatement("INSERT INTO transferencia (nrTransferencia, nrContaOrigem, nrContaDestino, montante, data) VALUES (?, ?, ?, ?, ?);");
			PreparedStatement statementContaOrigem = connection.prepareStatement("UPDATE conta SET saldo = ? WHERE nrconta = ?;");
			PreparedStatement statementContaDestino = connection.prepareStatement("UPDATE conta SET saldo = ? WHERE nrconta = ?;");
			double saldoOrigem = transferencia.getContaOrigem().getSaldo() - transferencia.getMontante();
			double saldoDestino = transferencia.getContaDestino().getSaldo() + transferencia.getMontante();

			statementTransferencia.setString(1, null);
			statementTransferencia.setInt(2, transferencia.getContaOrigem().getNrConta());
			statementTransferencia.setInt(3, transferencia.getContaDestino().getNrConta());
			statementTransferencia.setDouble(4, transferencia.getMontante());
			statementTransferencia.setDate(5, null);

			statementContaOrigem.setDouble(1, saldoOrigem);
			statementContaOrigem.setInt(2, transferencia.getContaOrigem().getNrConta());

			statementContaDestino.setDouble(1, saldoDestino);
			statementContaDestino.setInt(2, transferencia.getContaDestino().getNrConta());

			statementTransferencia.execute();
			statementContaOrigem.execute();
			statementContaDestino.execute();

			close(connection, statementContaDestino, statementContaOrigem, statementTransferencia);
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
	}

	//Metodo para levantar dinheiro
	public static void inserirLevantamento(Levantamento levantamento) {
		try {
			Connection connection = getConnection();
			PreparedStatement statementLevatamento = connection.prepareStatement("INSERT INTO levantamento (nrLevantamento, nrConta, nCartao, montante, data) VALUES (?, ?, ?, ?, ?);");
			PreparedStatement statementConta = connection.prepareStatement("UPDATE conta SET saldo = ? WHERE nrconta = ?;");
			double saldoAlteardo = levantamento.getConta().getSaldo() - levantamento.getMontante();

			statementLevatamento.setString(1, null);
			statementLevatamento.setInt(2, levantamento.getConta().getNrConta());
			statementLevatamento.setString(3, null);
			statementLevatamento.setDouble(4, levantamento.getMontante());
			statementLevatamento.setDate(5, null);

			statementConta.setDouble(1, saldoAlteardo);
			statementConta.setInt(2, levantamento.getConta().getNrConta());

			statementLevatamento.execute();
			statementConta.execute();

			close(connection, statementConta, statementLevatamento);
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
	}

	//Metodo para inserir deposito
	public static void inserirDeposito(Deposito deposito) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmtDeposito = connection.prepareStatement("INSERT INTO deposito (nrDeposito, nrConta, montante, nrCartao) VALUES (?, ?, ?, ?);");
			PreparedStatement stmtConta = connection.prepareStatement("UPDATE conta SET saldo = ? WHERE nrconta = ?;");
			double saldoAlterado;

			stmtDeposito.setString(1, null);
			if (deposito.getCartao() != null) {
				stmtDeposito.setInt(2, deposito.getCartao().getConta().getNrConta());
				stmtDeposito.setInt(4, deposito.getCartao().getNrCartao());
				saldoAlterado = deposito.getCartao().getConta().getSaldo();
				stmtConta.setInt(2, deposito.getCartao().getConta().getNrConta());
			}
			else {
				stmtDeposito.setInt(2, deposito.getConta().getNrConta());
				stmtDeposito.setString(4, null);
				saldoAlterado = deposito.getConta().getSaldo();
				stmtConta.setInt(2, deposito.getConta().getNrConta());
			}
			stmtDeposito.setDouble(3,deposito.getMontante());
			stmtDeposito.execute();

			stmtConta.setDouble(1, saldoAlterado += deposito.getMontante());

			stmtConta.execute();

			close(connection, stmtConta, stmtDeposito);
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
	}

	//Metodo para obter dados de uma dada conta devolvendo uma conta com todos os seus atributos
	public static Conta obterConta (int nrConta) {
		try {
			Connection connection = getConnection();
			Conta conta = null;
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM conta WHERE nrConta = ?;");
			ResultSet rs;

			stmt.setInt(1, nrConta);
			rs = stmt.executeQuery();

			while (rs.next()) {
				conta = new Conta(
						rs.getInt("nrconta"),
						rs.getDouble("saldo"),
						rs.getString("tpConta"),
						rs.getBoolean("ativo"),
						obterCliente(rs.getInt("idCliente"))
				);
			}

			close(connection, stmt, rs);
			return conta;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}

		return null;
	}

	//Metodo para obter dados de um dado cliente devolvendo um cliente com todos os seus atributos
	public static Cliente obterClienteTel (int telemovel) {
		try {
			Connection connection = getConnection();
			Cliente cliente = null;
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM cliente INNER JOIN pessoa ON cliente.idPessoa = pessoa.idPessoa WHERE pessoa.telefone = ?;");
			ResultSet rs;

			stmt.setInt(1, telemovel);
			rs = stmt.executeQuery();

			while (rs.next()) {
				cliente = new Cliente(
						rs.getInt("idCliente"),
						rs.getString("password"),
						rs.getString("tpCliente"),
						rs.getString("nome"),
						rs.getString("morada"),
						rs.getInt("telefone"),
						rs.getString("email"),
						rs.getString("profissao")
				);
			}

			rs.close();
			stmt.close();
			connection.close();
			return cliente;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}

		return null;
	}

	//Metodo para obter dados de um dado cliente devolvendo um cliente com todos os seus atributos
	public static Cliente obterCliente (int nrCliente) {
		try {
			Connection connection = getConnection();
			Cliente cliente = null;
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM cliente INNER JOIN pessoa ON cliente.idPessoa = pessoa.idPessoa WHERE cliente.idCliente = ?;");
			ResultSet rs;

			stmt.setInt(1, nrCliente);
			rs = stmt.executeQuery();

			while (rs.next()) {
				cliente = new Cliente(
						rs.getInt("idCliente"),
						rs.getString("password"),
						rs.getString("tpCliente"),
						rs.getString("nome"),
						rs.getString("morada"),
						rs.getInt("telefone"),
						rs.getString("email"),
						rs.getString("profissao")
				);
			}

			close(connection, stmt, rs);
			return cliente;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}

		return null;
	}

	//Metodo para fazer o display de todas as contas de um dado cliente
	public static ArrayList<Conta> obterContas(int nrCliente) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM conta WHERE idCliente = ?;");
			ResultSet rs;
			ArrayList<Conta> listaContas = new ArrayList<>();

			stmt.setInt(1, nrCliente);
			rs = stmt.executeQuery();

			while (rs.next()) {
				listaContas.add(
					new Conta(
						rs.getInt("nrConta"),
						rs.getDouble("saldo"),
						rs.getString("tpConta"),
						rs.getBoolean("ativo"),
						obterCliente(rs.getInt("idCliente"))
					)
				);
			}

			close(connection, stmt, rs);
			return listaContas;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro ao obter a lista de contas: %s\n", e.getMessage());
		}

		return null;
	}

	//Metodo para fazer  o display de todos os clientes
	public static void displayClientes() {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT cliente.idCliente, pessoa.nome FROM cliente INNER JOIN pessoa ON cliente.idPessoa = pessoa.idPessoa;");
			ResultSet rs = stmt.executeQuery();

			System.out.println();
			System.out.println("ID \tNome");
			System.out.println("--------------");
			while (rs.next()) {
				System.out.printf("%d:\t%s\n", rs.getInt("idCliente"), rs.getString("nome"));
			}

			rs.close();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro ao ligar à base de dados: %s\n", e.getMessage());
		}
	}

	//Metodo para fazer uma pesquisa a base de dados, pesquisando o utilizador e password.
	//Caso tenha entrado é feito um novo registo na base de dados
	public static String login(int nrCliente, String password) {
		try {
			Connection connection = getConnection();
			ResultSet rs = null;
			PreparedStatement stmtLogin = connection.prepareStatement("SELECT password, tpCliente FROM cliente WHERE idCliente = ?;");

			stmtLogin.setInt(1, nrCliente);
			rs = stmtLogin.executeQuery();

			while (rs.next()) {
				if (password.equals(rs.getString("password"))) {
					System.out.println("Login com sucesso");
					return rs.getString("tpCliente");
				}

				System.out.println("Password ou número de cliente errado");
			}

			rs.close();
			stmtLogin.close();
			connection.close();
		} catch (SQLException e) {
			System.out.printf("Erro ao fazer login: %s\n", e.getMessage());
		}

		return null;
	}

	//Metodo para registar um cliente na base de dados com todos os dados obrigatórios
	public static void registarCliente(Cliente cliente) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmtPessoa = connection.prepareStatement("INSERT INTO pessoa (idPessoa, nome, morada, telefone, email, profissao) VALUES (?, ?, ?, ?, ?, ?);");
			PreparedStatement stmtCliente = connection.prepareStatement("INSERT INTO cliente (idCliente, idPessoa, tpCliente, password) VALUES (?, ?, ?, ?);");
			PreparedStatement stmtPessoaCliente = connection.prepareStatement("SELECT idPessoa FROM pessoa WHERE telefone = ?");
			
			stmtPessoa.setString(1, null);
			stmtPessoa.setString(2, cliente.getNome());
			stmtPessoa.setString(3, cliente.getMorada());
			stmtPessoa.setInt(4, cliente.getTelefone());
			stmtPessoa.setString(5, cliente.getEmail());
			stmtPessoa.setString(6, cliente.getProfissao());
			
			stmtPessoa.execute();
			stmtPessoa.close();
			
			stmtPessoaCliente.setInt(1, cliente.getTelefone());
			ResultSet rs = stmtPessoaCliente.executeQuery();
			int idPessoa = 0;
			
			while (rs.next()) {
				idPessoa = rs.getInt("idPessoa");
			}
			
			stmtCliente.setString(1, null);
			stmtCliente.setInt(2, idPessoa);
			stmtCliente.setString(3, cliente.getTipoCliente());
			stmtCliente.setString(4, cliente.getPassword());
			
			stmtCliente.execute();
			stmtCliente.close();

			close(connection, stmtCliente, stmtPessoa, stmtPessoaCliente, rs);
		} catch (SQLException e) {
			System.out.printf("Erro ao registar cliente: %s\n", e.getMessage());
		}
	}
	
	//Metodo para criar uma ligação à base de dados
	private static Connection getConnection() {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(BD_URL, BD_USER, BD_PASSWORD);
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro ao ligar à base de dados: %s\n", e.getMessage());
		}
		
		return conn;
	}

	private static void close(Connection conn) throws  SQLException{
		conn.close();
	}

	private static void close(Connection conn, PreparedStatement stmt) throws  SQLException{
		conn.close();
		stmt.close();
	}

	private static void close(Connection conn, PreparedStatement stmt, ResultSet rs) throws  SQLException{
		conn.close();
		stmt.close();
		rs.close();
	}

	private static void close(Connection conn, PreparedStatement stmt1, PreparedStatement stmt2) throws  SQLException {
		conn.close();
		stmt1.close();
		stmt2.close();
	}

	private static void close(Connection conn, PreparedStatement stmt1, PreparedStatement stmt2, PreparedStatement stmt3) throws  SQLException {
		conn.close();
		stmt1.close();
		stmt2.close();
		stmt3.close();
	}

	private static void close(Connection conn, PreparedStatement stmt1, PreparedStatement stmt2, PreparedStatement stmt3, ResultSet rs) throws  SQLException {
		conn.close();
		stmt1.close();
		stmt2.close();
		stmt3.close();
		rs.close();
	}

	private BdUtil() {
	}
}