package com.example.banco.cartao;

import com.example.banco.conta.Conta;

public class CartaoCredito extends Cartao{
	public CartaoCredito(Conta conta) {
		super(conta);
		this.setTipoCartao("Crédito");
	}
}
