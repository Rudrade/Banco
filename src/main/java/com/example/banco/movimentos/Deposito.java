package com.example.banco.movimentos;

import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Deposito {
    private int nrDeposito;
    private Conta conta;
    private double montante;
    private LocalDateTime data;

    //Metodo para depositar dinheiro
    //Recebe como parametro um cliente para fazer a confirmação da palavra-passe como uma forma de segurança
    public void depositar(Cliente cliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.print("Conta a depositar: ");
        this.setConta(BdUtil.obterConta(scan.nextInt()));
        if (!(this.getConta().getEstado() && this.getConta().getCliente().getNrCliente() == cliente.getNrCliente())) {
            System.out.println("Conta inserida inativa ou inválida");
            return;
        }

        System.out.print("Password: ");
        if (cliente.getPassword().equals(scan.next())) {
            System.out.print("Total a depositar: ");
            this.setMontante(scan.nextDouble());
            System.out.println("A processar...");
            BdUtil.inserirDeposito(this);

            System.out.println("Deposito feito com sucesso.");
        }
        else {
            System.out.println("Password ou número de conta inválido");
        }
    }

    public void depositoDetalhe(Deposito deposito) {
        System.out.println();
        System.out.printf("Nº: %d\n", deposito.getNrDeposito());
        System.out.printf("Conta: %s\n", deposito.getConta().getNrConta());
        System.out.printf("Montante: %.2f\n", deposito.getMontante());
    }

    public Deposito(int nrDeposito, Conta conta, double montante) {
        this.setMontante(montante);
        this.setConta(conta);
        this.setNrDeposito(nrDeposito);
    }

    private void setNrDeposito(int nrDeposito) {
        this.nrDeposito = nrDeposito;
    }

    private void setMontante(double montante) {
        if (montante > 0) {
            this.montante = montante;
        }
    }

    public double getMontante() {
        return this.montante;
    }

    public int getNrDeposito() {
        return this.nrDeposito;
    }

    public Conta getConta(){
        return this.conta;
    }

    private void setConta(Conta conta) {
        this.conta = conta;
    }

    public Deposito() {

    }
}
