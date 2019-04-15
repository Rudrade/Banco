package com.example.banco.movimentos;

import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Levantamento {
    private int nrLevantamento;
    private int nrConta;
    private int nrCartao;
    private double montante;
    private LocalDateTime data;

    //Metodo para levantar dinheiro através de uma conta
    //Rece como parametro um cliente para fazer a confirmação da password como uma forma de segunraça
    public void levantarConta(Cliente cliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        do {
            System.out.print("Conta a levantar:");
        } while (!setNrConta(scan.nextInt(), cliente.getNrCliente()));

        System.out.print("Password: ");
        if (cliente.getPassword().equals(scan.next())) {
            System.out.print("Total a levantar: ");
            if(this.setMontante(scan.nextDouble())) {
                BdUtil.inserirLevantamentoConta(this);

                System.out.println("Levantamento feito com sucesso.");
            }
        }
        else {
            System.out.println("Password ou número de conta inválido");
        }
    }

    //Metodo para inserir o número de conta
    //Faz a validação se o número da conta pretence ao número do cliente
    private boolean setNrConta(int nrConta, int nCliente) {
        if (BdUtil.obterConta(nrConta).getIdCliente() == nCliente) {
            this.nrConta = nrConta;
            return true;
        }

        System.out.println("A conta que inseriu é inválida");
        return false;
    }

    //Metodo para validar se o montante desejado pelo utilizador é válido
    //Faz uma query a base de dados de forma a verificar a quantidade de saldo da conta
    private boolean setMontante(double montante) {
        if (montante > 0) {
            Conta conta = BdUtil.obterConta(this.getNrConta());
            if (conta.getSaldo() >= montante) {
                this.montante = montante;
                return true;
            }
            else {
                System.out.println("Saldo insuficiente");
                return false;
            }
        }
        else {
            System.out.println("Montante inserido inválido.");
            return false;
        }
    }

    public int getNrConta() {
        return this.nrConta;
    }

    public double getMontante() {
        return this.montante;
    }
}