package com.example.banco.pessoa;

//Class cliente normal
public class ClienteNormal extends Cliente{

    public ClienteNormal(int nrCliente, String password, String tipoCliente, int idPessoa, String nome, String morada, int telefone, String email, String profissao) {
        this.setNrCliente(nrCliente);
        this.setPassword(password);
        this.setTipoCliente(tipoCliente);
        this.setIdPessoa(idPessoa);
        this.setNome(nome);
        this.setMorada(morada);
        this.setTelefone(telefone);
        this.setEmail(email);
        this.setProfissao(profissao);
    }

    public ClienteNormal() {
        super();
    }
}