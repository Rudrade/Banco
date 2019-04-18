package com.example.banco.pessoa;

import java.util.Scanner;

import com.example.banco.cartao.Cartao;
import com.example.banco.conta.Conta;
import com.example.banco.conta.ContaOrdem;
import com.example.banco.movimentos.Deposito;
import com.example.banco.movimentos.Levantamento;
import com.example.banco.movimentos.Transferencia;
import com.example.banco.util.BdUtil;

import javax.sound.midi.Soundbank;

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

			System.out.println("A processar...");
			switch(BdUtil.login(nCliente, password)) {
                case "normal":
                	this.setTipoCliente("Normal");
                	this.setNrCliente(nCliente);
                	this.setPassword(password);
                    break;
                case "vip":
                    break;
                case "administrador":
                    Administrador.menuAdmin();
                    break;
				default:
					continue LOGIN;
            }

            SUBMENU: while(true) {
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
						new Conta(this).displayContas();
						continue SUBMENU;
					case 2:
						new Cartao().displayCartoes(this);
						continue SUBMENU;
					case 3:
						new Deposito().depositar(this);
						continue SUBMENU;
					case 4:
						new Levantamento().levantar(this);
						continue SUBMENU;
					case 5:
						new Transferencia().transferir(this);
						continue SUBMENU;
					case 6:
						new Conta(this).criarConta();
						continue SUBMENU;
					case 7:
						new Conta(this).desativarConta();
						continue SUBMENU;
					case 8:
						new Cartao().criarCartao(this);
						continue SUBMENU;
					case 9:
						new Cartao().desativarCartao();
						continue SUBMENU;
					case 10:
						System.out.println("A processar...");
						new Deposito().displayAll(this);/*
						new Levantamento().displayAll(this);
						new Transferencia().displayAll(this);*/
						break;
					case 0:
						System.exit(0);
					default:
						System.out.println("Opção selecionada inválida");
				}
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

		System.out.println("A processar...");
		BdUtil.registarCliente(this);
		Cliente cliente = BdUtil.obterClienteTel(getTelefone());
		BdUtil.criarConta(new ContaOrdem(cliente));
		System.out.printf("Cliente registado com sucesso, atribuido o número: %d\n", cliente.getNrCliente());
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