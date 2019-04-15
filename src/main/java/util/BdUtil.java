package util;

import java.sql.*;
import java.util.ArrayList;

import conta.Conta;
import pessoa.Cliente;

//Class utilitária para acesso à base de dados
public class BdUtil {
	private static final String BD_URL		= "jdbc:mysql://137.74.114.78:3306/banco?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String BD_USER		= "admin";
	private static final String BD_PASSWORD	= "XjAnxgL:9SK=QW*}";

	//Metodo para fazer o display de todas as contas de um dado cliente
	public static ArrayList<Conta> obterContas(int nrCliente) {
		try {
			PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM conta WHERE idCliente = ?;");
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

			return listaContas;
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro ao obter a lista de contas: %s", e.getCause());
		}

		return null;
	}

	//Metodo para fazer  o display de todos os clientes
	public static void displayClientes() {
		try {
			PreparedStatement stmt = getConnection().prepareStatement("SELECT cliente.idCliente, pessoa.nome FROM cliente INNER JOIN pessoa ON cliente.idPessoa = pessoa.idPessoa;");
			ResultSet rs = stmt.executeQuery();

			System.out.println();
			System.out.println("ID \tNome");
			System.out.println("--------------");
			while (rs.next()) {
				System.out.printf("%d:\t%s\n", rs.getInt("idCliente"), rs.getString("nome"));
			}
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro ao ligar à base de dados: %s", e.getCause());
		}
	}

	//Metodo para fazer uma pesquisa a base de dados, pesquisando o utilizador e password.
	//Caso tenha entrado é feito um novo registo na base de dados
	public static String login(int nrCliente, String password) {
		try {
			ResultSet rs = null;
			PreparedStatement stmtLogin = getConnection().prepareStatement("SELECT password, tpCliente FROM cliente WHERE idCliente = ?;");
			PreparedStatement stmtDataHora = getConnection().prepareStatement("INSERT INTO login (idCliente, dataLogin) VALUES (?, ?);");

			stmtLogin.setInt(1, nrCliente);

			rs = stmtLogin.executeQuery();

			while (rs.next()) {
				if (password.equals(rs.getString("password"))) {
					stmtDataHora.setInt(1, nrCliente);
					//Código para obter, tratar e inserir data no statement
					/*LocalDate data = LocalDate.now();
					Date sqlData = Date.valueOf(data);
					stmtDataHora.setDate(2, sqlData);
					//*****************************************
					stmtDataHora.execute();*/
					System.out.println("Login com sucesso");
					return rs.getString("tpCliente");
				}

				System.out.println("Password ou número de cliente errado");
			}
		} catch (SQLException e) {
			System.out.printf("Erro ao fazer login: %s", e.getCause());
		}

		return null;
	}

	//Metodo para registar um cliente na base de dados com todos os dados obrigatórios
	public static void registarCliente(Cliente cliente) {
		try {
			PreparedStatement stmtPessoa = getConnection().prepareStatement("INSERT INTO pessoa (idPessoa, nome, morada, telefone, email, profissao) VALUES (?, ?, ?, ?, ?, ?);");
			PreparedStatement stmtCliente = getConnection().prepareStatement("INSERT INTO cliente (idCliente, idPessoa, tpCliente, password) VALUES (?, ?, ?, ?);");
			PreparedStatement stmtPessoaCliente = getConnection().prepareStatement("SELECT idPessoa FROM pessoa WHERE telefone = ?");
			
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
		} catch (SQLException e) {
			System.out.printf("Erro ao registar cliente: %s\n", e.getMessage());
		}
	}
	
	//Metodo para criar uma conexão à base de dados
	private static Connection getConnection() {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(BD_URL, BD_USER, BD_PASSWORD);
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro ao ligar à base de dados: %s", e.getMessage());
		}
		
		return conn;
	}
	
	private BdUtil() {
	}
}