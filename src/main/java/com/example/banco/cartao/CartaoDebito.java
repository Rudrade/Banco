package com.example.banco.cartao;

import com.example.banco.conta.Conta;

public class CartaoDebito extends Cartao {
	protected CartaoDebito(Conta conta) {
		super();
		this.setTipoCartao("Débito");
	}
}
