package com.example.banco.movimentos;

import com.example.banco.conta.Conta;
import com.example.banco.conta.ContaOrdem;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Levantamento {
    private int nrLevantamento;
    private Conta conta;
    private int nrCartao;
    private double montante;
    private LocalDateTime data;

    //Metodo para levantar dinheiro através de uma conta
    //Rece como parametro um cliente para fazer a confirmação da password como uma forma de segunraça
    public void levantar(Cliente cliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.print("Conta a levantar:");
        this.setConta(BdUtil.obterConta(scan.nextInt()));
        if (!(this.getConta().getEstado() && this.getConta().getCliente().getNrCliente() == cliente.getNrCliente())) {
            System.out.println("Conta inserida inativa ou inválida");
            return;
        }

        System.out.print("Password: ");
        if (this.getConta().getCliente().getPassword().equals(scan.next())) {
            System.out.print("Total a levantar: ");
            if(this.setMontante(scan.nextDouble())) {
                System.out.println("A processar...");
                BdUtil.inserirLevantamento(this);
                System.out.println("Levantamento feito com sucesso.");
            }
        }
        else {
            System.out.println("Password ou número de conta inválido");
        }
    }

    public Levantamento(int nrLevantamento, Conta conta, double montante) {
        this.setMontante(montante);
        this.setConta(conta);
        this.setNrLevantamento(nrLevantamento);
    }

    private void setNrLevantamento(int nrLevantamento) {
        this.nrLevantamento = nrLevantamento;
    }

    //Metodo para validar se o montante desejado pelo utilizador é válido
    //Faz uma query a base de dados de forma a verificar a quantidade de saldo da conta
    private boolean setMontante(double montante) {
        if (montante > 0) {
            //Conta conta = BdUtil.obterConta(this.getNrConta());
            //if (conta.getSaldo() >= montante) {
                this.montante = montante;
                return true;
            /*}
            else {
                System.out.println("Saldo insuficiente");
                return false;
            }*/
        }
        else {
            System.out.println("Montante inserido inválido.");
            return false;
        }
    }

    public Conta getConta(){
        return this.conta;
    }

    private void setConta(Conta conta) {
        this.conta = conta;
    }

    public double getMontante() {
        return this.montante;
    }

    public Levantamento() {}
}