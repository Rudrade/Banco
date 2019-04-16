package com.example.banco.conta;

import java.util.ArrayList;
import java.util.Scanner;

import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

public class Conta {
    private int nrConta;
    private double saldo;
    private double juros;
    private String tipoConta;
    private int idCliente;
    
    //Metodo para desativar conta
    public void desativarConta(Cliente cliente) {
    	Scanner scan = new Scanner(System.in);
    	int nConta;
    	String password;
    	
    	System.out.println();
    	System.out.print("Número da conta:");
    	nConta = scan.nextInt();
    	
    	System.out.print("Password:");
    	password = scan.next();
    	
    	if (BdUtil.obterCliente(cliente.getNrCliente()).getPassword().equals(password)) {
    		BdUtil.desativarConta(nConta);
    	}
    	else {
    		System.out.println("Conta ou password inválido");
    	}
    }
    
    //Metodo para perguntar qual o tipo de conta a criar
    public void criarConta(Cliente cliente) {
    	Scanner scan = new Scanner(System.in);
    	int tipoConta;
    	
    	CRIAR: do {
    		System.out.println();
    		System.out.println("1- Poupança");
    		System.out.println("2- Depósito a prazo");
    		tipoConta = scan.nextInt();
    		
    		switch (tipoConta) {
			case 1:
				ContaPoupanca conta = new ContaPoupanca(cliente);
				BdUtil.criarConta(conta);
				System.out.println("Conta criada com sucesso.");
				break CRIAR;
			case 2:
				ContaDeposito contaD = new ContaDeposito(cliente);
				BdUtil.criarConta(contaD);
				System.out.println("Conta criada com sucesso.");
				break CRIAR;
			default:
				System.out.println("Opção inserida inválida");
			}
    	} while (tipoConta != 0);
    }
    
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
    	this.setSaldo(0);
    }

    protected void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    protected void setJuros(double juros) {
        this.juros = juros;
    }

    protected void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    private void setNrConta(int nrConta) {
        this.nrConta = nrConta;
    }

    public void setTipoConta(String tipoConta) {
        if (tipoConta.equals("Ordem") || tipoConta.equals("Investimento") || tipoConta.equals("Poupança") || tipoConta.equals("Depósito a prazo")) {
            this.tipoConta = tipoConta;
        }
    }

    public int getIdCliente() {
        return this.idCliente;
    }

    
    public String getTipoConta(){
        return this.tipoConta;
    }

    public double getJuros() {
        return this.juros;
    }

    public double getSaldo() {
        return this.saldo;
    }

    public int getNrConta() {
        return this.nrConta;
    }
}
