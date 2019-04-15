package com.example.banco.pessoa;

import java.util.Scanner;

import com.example.banco.util.BdUtil;

//Class Cliente
public class Cliente extends Pesssoa {
	private int nrCliente;
	private String password;
	private String tipoCliente;

	//Metodo para um cliente fazer login
	public void login() {
		Scanner scan = new Scanner(System.in);
		Cliente clienteLogin = null;
		int nCliente;
		String password;

		while (true) {
			System.out.println();
			System.out.println("Menu - Login");
			System.out.print("Número cliente: ");
			nCliente = scan.nextInt();

			System.out.print("Password: ");
			password = scan.next();

			switch(BdUtil.login(nCliente, password)) {
                case "normal":
                	clienteLogin = BdUtil.obterCliente(nCliente);
                	new ClienteNormal(
                			clienteLogin.getNrCliente(),
							clienteLogin.getPassword(),
							clienteLogin.getTipoCliente(),
							clienteLogin.getIdPessoa(),
							clienteLogin.getNome(),
							clienteLogin.getMorada(),
							clienteLogin.nrCliente,
							clienteLogin.getEmail(),
							clienteLogin.getProfissao()
					).loginNormal();
                    break;
                case "vip":
                    break;
                case "administrador":
                    Administrador.menuAdmin();
                    break;
            }
			break;
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
		
		BdUtil.registarCliente(this);
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