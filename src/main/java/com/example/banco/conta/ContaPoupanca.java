package com.example.banco.conta;

import com.example.banco.pessoa.Cliente;

public class ContaPoupanca extends Conta{
	
	public ContaPoupanca(Cliente cliente) {
		super();
		this.setTipoConta("Poupança");
		this.setJuros(5);
		this.setIdCliente(cliente.getNrCliente());
	}
}
