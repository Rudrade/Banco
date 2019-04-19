package com.example.banco.conta;

import com.example.banco.pessoa.Cliente;

public class ContaOrdem extends Conta {

    public ContaOrdem(Cliente cliente) {
        super();
        this.setTipoConta("Ordem");
        this.setSaldo(0.0);
    }
}
