package com.example.banco.conta;

import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import java.util.ArrayList;

public class Conta {
    private int nrConta;
    private double saldo;
    private double juros = 0.0;
    private String tipoConta;
    private int idCliente;

    //Metodo para fazer o display de todas as contas de um dado cliente
    public void displayConta(Cliente cliente) {
        ArrayList<Conta> listaContas = BdUtil.obterContas(cliente.getNrCliente());

        for (Conta conta : listaContas) {
            System.out.println();
            System.out.printf("Nº Conta: %d\n", conta.getNrConta());
            System.out.printf("Saldo: %.2f\n", conta.getSaldo());
            System.out.printf("Juros: %.2f%%\n", conta.getJuros());
            System.out.printf("Tipo: %s\n", conta.getTipoConta());
        }
    }

    public Conta(int nrConta, double saldo, double juros, String tipoConta, int idCliente) {
        this.setNrConta(nrConta);
        this.setSaldo(saldo);
        this.setJuros(juros);
        this.setTipoConta(tipoConta);
        this.setIdCliente(idCliente);
    }

    public Conta() {

    }

    private void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    private void setJuros(double juros) {
        this.juros = juros;
    }

    private void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    private void setNrConta(int nrConta) {
        this.nrConta = nrConta;
    }

    public void setTipoConta(String tipoConta) {
        if (tipoConta.equals("Ordem") || tipoConta.equals("Investimento") || tipoConta.equals("Poupança")) {
            this.tipoConta = tipoConta;
        }
    }

    public int getIdCliente() {
        return this.idCliente;
    }

    String getTipoConta(){
        return this.tipoConta;
    }

    double getJuros() {
        return this.juros;
    }

    public double getSaldo() {
        return this.saldo;
    }

    public int getNrConta() {
        return this.nrConta;
    }
}
