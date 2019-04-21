package com.example.banco.pessoa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.example.banco.cartao.Cartao;
import com.example.banco.conta.Conta;
import com.example.banco.movimentos.Deposito;
import com.example.banco.movimentos.Levantamento;
import com.example.banco.movimentos.Transferencia;
import com.example.banco.util.BdUtil;

//Class Cliente
public class Cliente extends Pesssoa {
	private int nrCliente;
	private String password;
	private String tipoCliente;

	//Metodo para um cliente fazer login
	public void login() {
		Scanner scan = new Scanner(System.in);
		int nCliente, op;
		String password;

		LOGIN: while (true) {
			System.out.println();
			System.out.println("Menu - Login");
			System.out.print("Número cliente: ");
			nCliente = scan.nextInt();

			System.out.print("Password: ");
			password = scan.next();

			try {
				ResultSet resultSet = BdUtil.select("SELECT password, tpCliente FROM cliente WHERE idCliente = " + nCliente + ";");

				while (resultSet.next()) {
					if (resultSet.getString("password").equals(password)) {
						this.setNrCliente(nCliente);
						this.setTipoCliente(resultSet.getString("tpCliente"));
						System.out.println("Login com sucesso");
						break LOGIN;
					}
				}
			} catch (SQLException e) {
				System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
			}
		}

		SUBMENU:
		while (true) {
			System.out.println();
			System.out.println("Menu:");
			System.out.println("1- Listar contas");
			System.out.println("2- Listar cartões");
			System.out.println("3- Depositar");
			System.out.println("4- Levantamentar");
			System.out.println("5- Transferencia");
			System.out.println("6- Criar Conta");
			System.out.println("7- Desativar Conta");
			System.out.println("8- Criar cartão");
			System.out.println("9- Desativar cartão");
			System.out.println("10- Consultar Movimentos");
			System.out.println("0- Sair");
			System.out.print("Opção: ");
			op = scan.nextInt();
				switch (op) {
				case 1:
					new Conta().displayContas(this.getNrCliente());
					continue SUBMENU;
				case 2:
					new Cartao().displayCartoes(this.getNrCliente());
					continue SUBMENU;
				case 3:
					new Deposito().depositar(this.getNrCliente());
					continue SUBMENU;
				case 4:
					new Levantamento().levantar(this.getNrCliente());
					continue SUBMENU;
				case 5:
					new Transferencia().transferir(this.getNrCliente());
					continue SUBMENU;
				case 6:
					new Conta().criarConta(this.getNrCliente());
					continue SUBMENU;
				case 7:
					new Conta().desativarConta(this.getNrCliente());
					continue SUBMENU;
				case 8:
					new Cartao().criarCartao(this.getNrCliente());
					continue SUBMENU;
				case 9:
					new Cartao().desativarCartao(this.getNrCliente());
					continue SUBMENU;
				case 10:
					System.out.println();
					System.out.println("Tipo de visualização:");
					System.out.println("1- Depósitos");
					System.out.println("2- Levantamentos");
					System.out.println("3- Transferências");
					System.out.print("Opção:");
					switch (scan.nextInt()) {
						case 1:
							new Deposito().displayAll(this.getNrCliente());
							break;
						case 2:
							new Levantamento().displayAll(this.getNrCliente());
							break;
						case 3:
							new Transferencia().displayAll(this.getNrCliente());
							break;
					}
					continue SUBMENU;
				case 0:
					System.exit(0);
				default:
					System.out.println("Opção selecionada inválida");
			}
		}
	}

	//Metodo para registar um novo cliente
	//Este metodo tambem insere os dados do cliente na base de dados
	public void registarCliente() {
		Scanner scan = new Scanner(System.in);
		int tipoCliente;
		
		TIPO: do {
			System.out.println();
			System.out.println("Menu - Registo cliente:");
			System.out.println("1- Normal");
			System.out.println("2- VIP");
			System.out.println("0- Cancelar");
			tipoCliente = scan.nextInt();
		
			switch (tipoCliente) {
				case 1:
					this.setTipoCliente("normal");
					break TIPO;
				case 2:
					this.setTipoCliente("vip");
					break TIPO;
				case 0:
					return;
				default:
					System.out.println("A opção selecionada não existe");
			}
		} while(tipoCliente != 0);
		
		System.out.println();
		System.out.print("Nome: ");
		this.setNome(scan.next());
		
		System.out.print("Morada: ");
		this.setMorada(scan.next());
		
		do {
			System.out.print("Telefone: ");
		} while (!this.setTelefone(scan.nextInt()));
		
		do {
			System.out.print("Email: ");
		} while (!this.setEmail(scan.next()));
		
		System.out.print("Profissão: ");
		this.setProfissao(scan.next());
		
		do {
			System.out.print("Password: ");
		} while (!this.setPassword(scan.next()));

		try {
			BdUtil.execute("INSERT INTO pessoa (idPessoa, nome, morada, telefone, email, profissao)\n" +
					"VALUES (null,'" + this.getNome() + "', '" + this.getMorada() + "', " + this.getTelefone() + ", '" + this.getEmail() + "', '" + this.getProfissao() + "');\n" +
					"INSERT INTO cliente (idCliente, tpCliente, password, idPessoa)\n" +
					"VALUES (null, '" + this.getTipoCliente() + "', '" + this.getPassword() + "', (SELECT idPessoa FROM pessoa ORDER BY idPessoa DESC LIMIT 1));\n" +
					"INSERT INTO conta (nrConta, saldo, juros, tpConta, ativo, idCliente)\n" +
					"VALUES (null, 0, 0, 'Ordem', true, (SELECT idCliente FROM cliente ORDER BY idCliente DESC LIMIT 1));\n" +
					"INSERT INTO cartao (nrCartao, saldo, tpCartao, ativo, nrConta)\n" +
					"VALUES (null, 0, 'Débito', true, (SELECT nrconta FROM conta ORDER BY nrconta DESC LIMIT 1));");

			ResultSet resultSet = BdUtil.select("SELECT idCliente FROM cliente ORDER BY idCliente DESC LIMIT 1");
			while (resultSet.next()) {
				System.out.printf("Cliente criado com o número: %d\n", resultSet.getInt("idCliente"));
				break;
			}
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
			return;
		}
	}

	//Metodo para obter um cliente na base de dados
	public Cliente obterCliente(int nrCliente) {
		try {
			ResultSet resultSet = BdUtil.select("SELECT * FROM cliente INNER JOIN pessoa ON cliente.idPessoa = pessoa.idPessoa WHERE cliente.idCliente = " + nrCliente + ";");

			while (resultSet.next()) {
				return new Cliente(
						resultSet.getInt("idCliente"),
						resultSet.getString("password"),
						resultSet.getString("tpCliente"),
						resultSet.getString("nome"),
						resultSet.getString("morada"),
						resultSet.getInt("telefone"),
						resultSet.getString("email"),
						resultSet.getString("profissao")
				);
			}
		} catch (SQLException e){
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
		}

		return null;
	}

	public Cliente (int nrCliente, String password, String tipoCliente, String nome, String morada, int telefone, String email, String profissao) {
		this.setNrCliente(nrCliente);
		this.setPassword(password);
		this.setTipoCliente(tipoCliente);
		this.setNome(nome);
		this.setMorada(morada);
		this.setTelefone(telefone);
		this.setEmail(email);
		this.setProfissao(profissao);
	}

	void setNrCliente(int nrCliente) {
		this.nrCliente = nrCliente;
	}

	public int getNrCliente() {
		return this.nrCliente;
	}

	boolean setPassword(String password) {
		if (password.length() >= 8) {
			this.password = password;
			return true;
		}
		else {
			System.out.println("Password inválida.");
			return false;
		}
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getTipoCliente() {
		return this.tipoCliente;
	}
	
	void setTipoCliente(String tipoCliente) {
		if (tipoCliente.equals("vip") || tipoCliente.equals("normal")) {
			this.tipoCliente = tipoCliente;
		}
	}

	public Cliente() {

	}
}