package com.example.banco.cartao;

import java.util.Scanner;

import com.example.banco.conta.Conta;
import com.example.banco.util.BdUtil;

public class Cartao {
	private int nrCartao;
	private int nrConta;
	private String tipoCartao;
	
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
}
