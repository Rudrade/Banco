package com.example.banco.movimentos;

import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Deposito {
    private int nrDeposito;
    private int nrConta;
    private double montante;
    private LocalDateTime data;

    //Metodo para depoistar dinheiro
    //Recebe como parametro um cliente para fazer a confirmação da palavra-passe como uma forma de segurança
    public void depositar(Cliente cliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        do {
            System.out.print("Conta a depositar: ");
        } while (!setNrConta(scan.nextInt(), cliente.getNrCliente()));

        System.out.print("Password: ");
        if (cliente.getPassword().equals(scan.next())) {
            System.out.print("Total a depositar: ");
            this.setMontante(scan.nextDouble());
            BdUtil.inserirDeposito(this);

            System.out.println("Deposito feito com sucesso.");
        }
        else {
            System.out.println("Password ou número de conta inválido");
        }
    }

    private void setMontante(double montante) {
        if (montante > 0) {
            this.montante = montante;
        }
    }

    //Metodo para inserir o número de conta
    //Faz a validação se o número da conta pretence ao número do cliente
    private boolean setNrConta(int nrConta, int nCliente) {
        ArrayList<Conta> listaContas;

        listaContas = BdUtil.obterContas(nCliente);

        for (Conta conta : listaContas) {
            if (conta.getIdCliente() == nCliente) {
                this.nrConta = nrConta;
                return true;
            }
        }

        System.out.println("A conta que inseriu é inválida");
        return false;
    }

    public double getMontante() {
        return this.montante;
    }

    public int getNrConta() {
        return this.nrConta;
    }

    public int getNrDeposito() {
        return this.nrDeposito;
    }

    public Deposito() {

    }
}
