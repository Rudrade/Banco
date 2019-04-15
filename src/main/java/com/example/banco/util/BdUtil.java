package com.example.banco.util;

import java.sql.*;
import java.util.ArrayList;

import com.example.banco.conta.Conta;
import com.example.banco.movimentos.Deposito;
import com.example.banco.movimentos.Levantamento;
import com.example.banco.pessoa.Cliente;

//Class utilitária para acesso à base de dados
public class BdUtil {
	private static final String BD_URL		= "jdbc:mysql://137.74.114.78:3306/banco?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String BD_USER		= "admin";
	private static final String BD_PASSWORD	= "XjAnxgL:9SK=QW*}";

	//Metodo para levantar dinheiro
	public static void inserirLevantamentoConta(Levantamento levantamento) {
		try {
			Connection connection = getConnection();
			PreparedStatement statementLevatamento = connection.prepareStatement("INSERT INTO levantamento (nrLevantamento, nrConta, nrCartao, montante, data) VALUES (?, ?, ?, ?, ?);");
			PreparedStatement statementConta = connection.prepareStatement("UPDATE conta SET saldo = ? WHERE nrconta = ?;");
			double saldoAlteardo = obterConta(levantamento.getNrConta()).getSaldo() - levantamento.getMontante();

			statementLevatamento.setString(1, null);
			statementLevatamento.setInt(2, levantamento.getNrConta());
			statementLevatamento.setString(3, null);
			statementLevatamento.setDouble(4, levantamento.getMontante());
			statementLevatamento.setDate(5, null);

			statementConta.setDouble(1, saldoAlteardo);
			statementConta.setInt(2, levantamento.getNrConta());

			statementLevatamento.execute();
			statementConta.execute();

			statementConta.close();
			statementLevatamento.close();
			connection.close();
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}
	}

	//Metodo para inserir deposito
	public static void inserirDeposito(Deposito deposito) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmtDeposito = connection.prepareStatement("INSERT INTO deposito (nrDeposito, nrConta, montante) VALUES (?, ?, ?);");
			PreparedStatement stmtConta = connection.prepareStatement("UPDATE conta SET saldo = ? WHERE nrconta = ?;");
			double saldoAlterado = obterConta(deposito.getNrConta()).getSaldo() + deposito.getMontante();

			stmtDeposito.setString(1, null);
			stmtDeposito.setInt(2, deposito.getNrConta());
			stmtDeposito.setDouble(3,deposito.getMontante());
			stmtDeposito.execute();

			stmtConta.setDouble(1, saldoAlterado);
			stmtConta.setInt(2, deposito.getNrConta());
			stmtConta.execute();

			stmtConta.close();
			stmtDeposito.close();
			connection.close();
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
						rs.getDouble("juros"),
						rs.getString("tpConta"),
						rs.getInt("idCliente")
				);
			}

			rs.close();
			stmt.close();
			connection.close();
			return conta;
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

			rs.close();
			stmt.close();
			connection.close();
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
			ResultSet rs = null;
			ArrayList<Conta> listaContas = new ArrayList<Conta>();

			stmt.setInt(1, nrCliente);
			rs = stmt.executeQuery();

			while (rs.next()) {
				listaContas.add(
						new Conta(
								rs.getInt("nrConta"),
								rs.getDouble("saldo"),
								rs.getDouble("juros"),
								rs.getString("tpConta"),
								rs.getInt("idCliente")
						)
				);
			}

			rs.close();
			stmt.close();
			connection.close();
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
			PreparedStatement stmtDataHora = connection.prepareStatement("INSERT INTO login (idCliente, dataLogin) VALUES (?, ?);");

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
			stmtDataHora.close();
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
			
			System.out.println("Cliente registado com sucesso");

			rs.close();
			stmtCliente.close();
			stmtPessoa.close();
			stmtPessoaCliente.close();
			connection.close();
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

	private BdUtil() {
	}
}