package com.example.banco.pessoa;

import com.example.banco.conta.Conta;
import com.example.banco.movimentos.Deposito;
import com.example.banco.movimentos.Levantamento;
import com.example.banco.movimentos.Transferencia;

import java.util.Scanner;

//Class cliente normal
public class ClienteNormal extends Cliente{
    public void loginNormal() {
        Scanner scan = new Scanner(System.in);
        int op;

        do {
            System.out.println();
            System.out.println("Menu:");
            System.out.println("1- Listar contas");
            System.out.println("2- Listar cartões");
            System.out.println("3- Depositar");
            System.out.println("4- Levantamentar");
            System.out.println("5- Transferencia");
            System.out.println("9- Alterar dados");
            System.out.println("0- Sair");
            op = scan.nextInt();

            switch (op) {
                case 1:
                    new Conta().displayConta(this);
                    break;
                case 2:
                    break;
                case 3:
                    new Deposito().depositar(this);
                    break;
                case 4:
                    new Levantamento().levantarConta(this);
                    break;
                case 5:
                    new Transferencia().transferir(this);
                    break;
                case 9:
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("A opção selecionada é inválida");
            }
        } while (op != 0);
    }

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