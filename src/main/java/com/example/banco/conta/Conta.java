package com.example.banco.conta;

import java.util.ArrayList;
import java.util.Scanner;

import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

public class Conta {
    private int nrConta;
    private double saldo;
    private String tipoConta;
    private Cliente cliente;
    private boolean ativo;

    //Metodo para desativar conta
    public void desativarConta() {
    	Scanner scan = new Scanner(System.in);
    	String password;
    	
    	System.out.println();
    	System.out.print("Número da conta:");
    	setNrConta(scan.nextInt());
    	
    	System.out.print("Password:");
    	password = scan.next();

        System.out.println("A processar...");
    	String passwordVerf = BdUtil.obterCliente(getCliente().getNrCliente()).getPassword();

    	if (password.equals(passwordVerf)) {
    	    if (BdUtil.obterConta(getNrConta()).getCliente().getNrCliente() == getCliente().getNrCliente()) {
    	        BdUtil.desativarConta(this);
            }
        }
    }
    
    //Metodo para perguntar qual o tipo de conta a criar
    public void criarConta() {
    	Scanner scan = new Scanner(System.in);
    	int tipoConta;
    	
    	CRIAR: do {
    		System.out.println();
    		System.out.println("1- Poupança");
    		System.out.println("2- Depósito a prazo");
    		System.out.print("Opção: ");
    		tipoConta = scan.nextInt();
    		
    		switch (tipoConta) {
			    case 1:
			        System.out.println("A processar...");
			        BdUtil.criarConta(new ContaPoupanca(this.getCliente()));
			        System.out.println("Conta criada com sucesso.");
				    break CRIAR;
			    case 2:
				    new ContaDeposito(this.getCliente());
				    break CRIAR;
			    default:
				    System.out.println("Opção inserida inválida");
			}
    	} while (tipoConta != 0);
    }
    
    //Metodo para fazer o display de todas as contas de um dado cliente
    public void displayContas() {
        System.out.println("A processar...");
        ArrayList<Conta> listaContas = BdUtil.obterContas(getCliente().getNrCliente());

        for (Conta conta : listaContas) {
            System.out.println();
            System.out.printf("Nº Conta: %d\n", conta.getNrConta());
            System.out.printf("Saldo: %.2f\n", conta.getSaldo());
            System.out.printf("Tipo: %s\n", conta.getTipoConta());
            if (conta.getEstado()) {
                System.out.println("Estado: Ativo");
            }
            else {
                System.out.println("Estado: Inativo");
            }
        }
    }

    public Conta(int nrConta, double saldo, String tipoConta, boolean ativo, Cliente cliente) {
        this.setNrConta(nrConta);
        this.setSaldo(saldo);
        this.setTipoConta(tipoConta);
        this.setAtivo(ativo);
        this.setCliente(cliente);
    }

    public Conta(Cliente cliente) {
    	this.setCliente(cliente);
    }

    boolean setSaldo(double saldo) {
        if (saldo >= 0) {
            this.saldo = saldo;
            return true;
        }
        else {
            System.out.println("Saldo inserido inválido");
            return false;
        }
    }

    void setNrConta(int nrConta) {
        this.nrConta = nrConta;
    }

    public void setTipoConta(String tipoConta) {
        if (tipoConta.equals("Ordem") || tipoConta.equals("Investimento") || tipoConta.equals("Poupança") || tipoConta.equals("Depósito a prazo")) {
            this.tipoConta = tipoConta;
        }
    }
    
    public String getTipoConta(){
        return this.tipoConta;
    }

    public double getSaldo() {
        return this.saldo;
    }

    public int getNrConta() {
        return this.nrConta;
    }

    public boolean getEstado() {
        return this.ativo;
    }

    private void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    protected void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return this.cliente;
    }
}
