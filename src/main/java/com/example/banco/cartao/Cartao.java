package com.example.banco.cartao;

import java.util.ArrayList;
import java.util.Scanner;

import com.example.banco.conta.Conta;
import com.example.banco.util.BdUtil;

public class Cartao {
	private int nrCartao;
	private int nrConta;
	private String tipoCartao;
	
	//Metodo para desativar cartao
	public void desativarCartao(int nrCliente) {
		Scanner scan = new Scanner(System.in);
		int nCartao;
		String password;
		
		System.out.println();
		System.out.println("Cartão a desativar: ");
		nCartao = scan.nextInt();
		
		System.out.println("Password:");
		password = scan.next();
		
		if (BdUtil.obterCliente(nrCliente).getPassword() == password) {
			BdUtil.desativarCartao(nCartao);
		}
	}
	
	//Metodo para listar cartao
	public void displayCartao(int nrCliente) {
		ArrayList<Cartao> listaCartao = BdUtil.obterCartoes(nrCliente);
		
		for (Cartao cartao : listaCartao) {
			System.out.printf("\nNº cartão:%d\n", cartao.getNrCartao());
			System.out.printf("Conta:%d\n", cartao.getNrConta());
			System.out.printf("Tipo: %s\n", cartao.getTipoCartao());
		}
	}
	
	//Metodo para criar cartao
	public void criarCartao(Conta conta) {
		Scanner scan = new Scanner(System.in);
		int tCartao;
		String password = null;
		
		TIPO: do {
			System.out.println();
			System.out.println("1- Crédito");
			System.out.println("2- Débito");
			tCartao = scan.nextInt();
			
			switch (tCartao) {
			case 1:
				System.out.println("Password: ");
				password = scan.next();
				if (BdUtil.obterCliente(conta.getIdCliente()).getPassword().equals(password)) {
					BdUtil.criarCartao(new CartaoCredito(conta));
				}
				break TIPO;
			case 2:
				System.out.println("Password: ");
				password = scan.next();
				if (BdUtil.obterCliente(conta.getIdCliente()).getPassword().equals(password)) {
					BdUtil.criarCartao(new CartaoDebito(conta));
				}
				break TIPO;
			default:
				System.out.println("Tipo inserido inválido");
			}
		} while (tCartao != 0);
	}
	
	public String getTipoCartao() {
		return this.tipoCartao;
	}
	
	public int getNrConta() {
		return this.nrConta;
	}
	
	public int getNrCartao() {
		return this.nrCartao;
	}
	
	protected void setNrConta(int nrConta) {
		this.nrConta = nrConta;
	}
	
	protected void setTipoCartao(String tipoCartao) {
		if (tipoCartao.equals("Crédito") || tipoCartao.equals("Débito")) {
			this.tipoCartao = tipoCartao;
		}
	}
	
	private void setNrCartao(int nrCartao) {
		this.nrCartao = nrCartao;
	}
	
	public Cartao(int nrConta, int nrCartao, String tipoCartao) {
		this.setNrConta(nrConta);
		this.setTipoCartao(tipoCartao);
		this.setNrCartao(nrCartao);
	}
	
	public Cartao() {
		
	}
}
