package com.example.banco.cartao;

import java.util.ArrayList;
import java.util.Scanner;

import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

public class Cartao {
	private int nrCartao;
	private String tipoCartao;
	private Conta conta;
	private boolean ativo;
	private double saldo;
	
	//Metodo para desativar cartao
	public void desativarCartao() {
		Scanner scan = new Scanner(System.in);
		int nCartao;
		String password;
		
		System.out.println();
		System.out.print("Cartão a desativar: ");
		nCartao = scan.nextInt();
		
		System.out.print("Password:");
		password = scan.next();

		System.out.println("A processar...");
		if (getConta().getCliente().getPassword().equals(password)) {
			BdUtil.desativarCartao(nCartao);
		}
	}

	//Metodo para mostrar o detalhe de um dado cartao
	public void detalheCartao(Cartao cartao) {
		System.out.printf("\nNº cartão: %d\n", cartao.getNrCartao());
		System.out.printf("Conta: %d\n", cartao.getConta().getNrConta());
		System.out.printf("Tipo: %s\n", cartao.getTipoCartao());
		System.out.printf("Saldo %.2f\n", cartao.getSaldo());
		if (cartao.getAtivo()) {
			System.out.println("Estado: Ativo");
		}
		else {
			System.out.println("Estado: Inativo");
		}
	}

	//Metodo para listar cartao
	public void displayCartoes(Cliente cliente) {
		System.out.println("A processar...");
		ArrayList<Cartao> listaCartao = BdUtil.obterCartoes(cliente.getNrCliente());
		
		for (Cartao cartao : listaCartao) {
			cartao.detalheCartao(cartao);
		}
	}
	
	//Metodo para criar cartao
	public void criarCartao(Cliente cliente) {
		Scanner scan = new Scanner(System.in);
		int tCartao, nConta;
		String password;

		System.out.println();
		System.out.print("Nº conta a associar:");
		nConta = scan.nextInt();

		this.setConta(BdUtil.obterConta(nConta));		
		if (!this.getConta().getEstado() || this.getConta().getCliente().getNrCliente() != cliente.getNrCliente()) {
			System.out.println("Conta inativa ou inexistente");
			return;
		}

		if (!this.getConta().getTipoConta().equals("Ordem" )) {
			System.out.println("Apenas pode ser associado cartões a uma conta do tipo Ordem");
			return;
		}
		
		TIPO: do {
			System.out.println("1- Crédito");
			System.out.println("2- Débito");
			System.out.print("Opção: ");
			tCartao = scan.nextInt();

			System.out.println("Password: ");
			password = scan.next();

			if (conta.getCliente().getPassword().equals(password)) {
				System.out.println("A processar...");
				switch (tCartao) {
					case 1:
						BdUtil.criarCartao(new CartaoCredito(this.getConta()));
						break TIPO;
					case 2:
						BdUtil.criarCartao(new CartaoDebito(this.getConta()));
						break TIPO;
					default:
						System.out.println("Tipo inserido inválido");
				}
			}
		} while (tCartao != 0);
	}
	
	public String getTipoCartao() {
		return this.tipoCartao;
	}
	
	public Conta getConta() {
		return this.conta;
	}
	
	public int getNrCartao() {
		return this.nrCartao;
	}
	
	protected void setConta(int nrConta) {
		this.conta = BdUtil.obterConta(nrConta);
	}

	protected void setConta(Conta conta) {
		this.conta = conta;
	}

	protected void setTipoCartao(String tipoCartao) {
		if (tipoCartao.equals("Crédito") || tipoCartao.equals("Débito")) {
			this.tipoCartao = tipoCartao;
		}
	}
	
	private void setNrCartao(int nrCartao) {
		this.nrCartao = nrCartao;
	}
	
	public Cartao(Conta conta, int nrCartao, String tipoCartao, boolean ativo, double saldo) {
		this.setConta(conta);
		this.setTipoCartao(tipoCartao);
		this.setNrCartao(nrCartao);
		this.setAtivo(ativo);
		this.setSaldo(saldo);
	}

	public boolean getAtivo() {
		return this.ativo;
	}

	private void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Cartao(Conta conta) {
		this.setConta(conta);
	}

	public double getSaldo() {
		return this.saldo;
	}
	
	private void setSaldo(double saldo) {
		if (saldo >= 0) {
			this.saldo = saldo;
		}
		else {
			System.out.println("Saldo inserido inválido");
		}
	}
	
	public Cartao() {

	}
}
