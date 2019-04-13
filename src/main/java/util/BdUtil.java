package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pessoa.Cliente;

//Class utilitária para acesso à base de dados
public class BdUtil {
	private static final String BD_URL		= "jdbc:mysql://137.74.114.78:3306/banco?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String BD_USER		= "admin";
	private static final String BD_PASSWORD	= "XjAnxgL:9SK=QW*}";
	
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