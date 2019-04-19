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
	private static final String BD_URL		= "jdbc:mysql://137.74.114.78:3306/banco?allowMultiQueries=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String BD_USER		= "admin";
	private static final String BD_PASSWORD	= "XjAnxgL:9SK=QW*}";
	private static BdUtil instancia = new BdUtil();
	private static Connection connection = getConnection();

	public static ResultSet select(String sql) throws SQLException {
		ResultSet resultSet;
		Statement statement = connection.createStatement();

		resultSet = statement.executeQuery(sql);

		return resultSet;
	}

	public static void execute(String sql) throws SQLException {
		Statement statement = connection.createStatement();
		statement.execute(sql);
	}

	//Metodo para deovlver uma instancia
	public static BdUtil getInstance() {
		return instancia;
	}
	
	//Metodo para criar uma ligação à base de dados
	private static Connection getConnection() {		
		try {
			connection = DriverManager.getConnection(BD_URL, BD_USER, BD_PASSWORD);
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro ao ligar à base de dados: %s\n", e.getMessage());
			System.exit(0);
		}
		
		return connection;
	}

	private BdUtil() {
		connection = getConnection();
	}
}