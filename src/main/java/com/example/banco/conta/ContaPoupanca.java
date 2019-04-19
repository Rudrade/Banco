package com.example.banco.conta;

import com.example.banco.pessoa.Cliente;

public class ContaPoupanca extends Conta{
	private int juros;

	public ContaPoupanca(Cliente cliente) {
		super();
		this.setTipoConta("Poupança");
		this.setJuros(5);
	}

	private void setJuros(int juros) {
		this.juros = juros;
	}

	public int getJuros() {
		return this.juros;
	}
}
