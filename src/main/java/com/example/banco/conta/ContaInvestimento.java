package com.example.banco.conta;

public class ContaInvestimento extends Conta{
	protected ContaInvestimento() {
		super();
		this.setTipoConta("Investimento");
		this.setSaldo(50);
		this.setJuros(5);
	}
}
