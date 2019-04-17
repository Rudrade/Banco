package com.example.banco.conta;

import java.util.Scanner;

import com.example.banco.pessoa.Cliente;

public class ContaDeposito extends Conta{
	private int duracao;
	private int juros;

	protected ContaDeposito(Cliente cliente) {
		super(cliente);
		Scanner scan = new Scanner(System.in);
		this.setTipoConta("Depósito a prazo");
		this.setJuros(5);
		System.out.println("Duração da conta (1-5 anos)");
		this.setDuracao(scan.nextInt());
	}
	
	private void setDuracao(int duracao) {
		if (duracao >= 1 && duracao <= 5) {
			this.duracao = duracao;
		}
	}

	private void setJuros(int juros) {
		this.juros = juros;
	}
}
