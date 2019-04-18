package com.example.banco.movimentos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import com.example.banco.cartao.Cartao;
import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

public class Levantamento {
    private int nrLevantamento;
    private Conta conta;
    private Cartao cartao;
    private double montante;
    private LocalDateTime data;

    //Metodo para levantar dinheiro através de uma conta
    //Rece como parametro um cliente para fazer a confirmação da password como uma forma de segunraça
    public void levantar(Cliente cliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.print("Conta a levantar:");
        this.setConta(BdUtil.obterConta(scan.nextInt()));
        if (!(this.getConta().getTipoConta().equals("Ordem"))) {
            System.out.println("Apenas é permitido fazer levantamentos de uma conta a Ordem");
            return;
        }

        if (!(this.getConta().getEstado() && this.getConta().getCliente().getNrCliente() == cliente.getNrCliente())) {
            System.out.println("Conta inserida inativa ou inválida");
            return;
        }

        System.out.print("Password: ");
        if (this.getConta().getCliente().getPassword().equals(scan.next())) {
            System.out.print("Total a levantar: ");
            if (this.setMontante(scan.nextDouble())) {
                System.out.println("A processar...");
                BdUtil.inserirLevantamento(this);
                System.out.println("Levantamento feito com sucesso.");
            }
        }
        else {
            System.out.println("Password ou número de conta inválido");
        }
    }

    public void levantamentoDetalhe(Levantamento levantamento) {
        System.out.println("Tipo: Levantamento");
        System.out.printf("Nº: %d\n", levantamento.getNrLevantamento());
        System.out.printf("Conta: %d\n", levantamento.getConta().getNrConta());
        if (levantamento.getCartao() != null) {
        	System.out.printf("Cartão: %d\n", levantamento.getCartao().getNrCartao());
        }
        System.out.printf("Montante: %.2f\n", levantamento.getMontante());
    }

    public void displayAll(Cliente cliente) {
        ArrayList<Conta> contas = BdUtil.obterContas(cliente.getNrCliente());

        for (Conta conta : contas) {
            if (cliente.getNrCliente() == conta.getCliente().getNrCliente()) {
            	ArrayList<Levantamento> listaLevantamento = BdUtil.obterLevantamento(conta);
                for (Levantamento levantamento : listaLevantamento) {
                    levantamento.levantamentoDetalhe(levantamento);
                    System.out.println("-------------");
                }
                return;
            }
        }
    }

    public Levantamento(int nrLevantamento, Conta conta, double montante, Cartao cartao) {
        this.montante = montante;
        this.setConta(conta);
        this.setNrLevantamento(nrLevantamento);
    }

    private void setNrLevantamento(int nrLevantamento) {
        this.nrLevantamento = nrLevantamento;
    }

    //Metodo para validar se o montante desejado pelo utilizador é válido
    //Faz uma query a base de dados de forma a verificar a quantidade de saldo da conta
    private boolean setMontante(double montante) {
        if (montante > 0 && montante <= this.getConta().getSaldo()) {
            this.montante = montante;
            return true;
        }
        else {
            System.out.println("Montante inserido inválido ou insuficiente");
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

    public Cartao getCartao() {
    	return this.cartao;
    }
    
    private void setCartao(Cartao cartao) {
    	this.cartao = cartao;
    }
    
    public int getNrLevantamento() {
        return this.nrLevantamento;
    }
}