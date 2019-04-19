package com.example.banco.conta;

import com.example.banco.pessoa.Cliente;

public class ContaInvestimento extends Conta{
	private int juros;

	protected ContaInvestimento(Cliente cliente) {
		super();
		this.setTipoConta("Investimento");
		this.setSaldo(50);
		this.setJuros(5);
	}

	private void setJuros(int juros) {
		this.juros = juros;
	}
}
