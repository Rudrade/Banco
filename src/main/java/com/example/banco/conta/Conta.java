package com.example.banco.conta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import javax.xml.transform.Result;

public class Conta {
    private int nrConta;
    private double saldo;
    private String tipoConta;
    private int nrCliente;
    private boolean ativo;

    //Metodo para desativar conta
    public void desativarConta(int nrCliente) {
    	Scanner scan = new Scanner(System.in);
    	String password;
    	
    	System.out.println();
    	System.out.print("Número da conta:");
    	this.setNrConta(scan.nextInt());

    	try {
            ResultSet resultSet = BdUtil.select("SELECT idCliente, ativo\n" +
                    "FROM conta\n" +
                    "WHERE nrconta = " + this.getNrConta() + ";");
            while (resultSet.next()) {
                if (resultSet.getInt("idCliente") == nrCliente) {
                    if (resultSet.getBoolean("ativo")) {
                        BdUtil.execute("UPDATE conta\n" +
                                "SET ativo = false\n" +
                                "WHERE nrconta = "+ this.getNrConta() +";");
                        System.out.println("Conta desativada com sucesso");
                    }
                    else {
                        System.out.println("Conta já se encontra desativada");
                    }
                }
                else {
                    System.out.println("Conta inválida ou inexistente");
                }
            }
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }
    }

    //Metodo para perguntar qual o tipo de conta a criar
    public void criarConta(int nrCliente) {
    	Scanner scan = new Scanner(System.in);
    	int tipoConta;
    	String tipoCliente = null;

    	try {
            ResultSet resultSet = BdUtil.select("SELECT tpCliente FROM cliente WHERE idCliente = " + nrCliente + ";");

            while (resultSet.next()) {
                tipoCliente = resultSet.getString("tpCliente");
            }
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }

    	CRIAR: do {
    		System.out.println();
    		System.out.println("1- Poupança");
    		System.out.println("2- Depósito a prazo");
    		if (tipoCliente.equals("vip")) {
    		    System.out.println("3- Investimento");
            }
    		System.out.print("Opção: ");
    		tipoConta = scan.nextInt();
    		
    		switch (tipoConta) {
			    case 1:
			        try {
                        BdUtil.execute("INSERT INTO conta (nrconta, saldo, juros, tpConta, ativo, idCliente)\n" +
                                "VALUES (null, 0, 5, 'Poupança', true," + nrCliente + ");");
                        System.out.println("Conta criada com sucesso.");
                    } catch (SQLException e) {
                        System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
                        return;
                    }
				    break CRIAR;
			    case 2:
			        new ContaDeposito(nrCliente);
				    break CRIAR;
                case 3:
                    if (tipoCliente.equals("vip")) {
                        try {
                            BdUtil.execute("INSERT INTO conta (nrconta, saldo, juros, tpConta, ativo, idCliente)\n" +
                                    "VALUES (null, 0, 5, 'Investimento', true," + nrCliente + ");");
                            System.out.println("Conta criada com sucesso.");
                        } catch (SQLException e) {
                            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
                            return;
                        }
                    }
                    else {
                        System.out.println("Opção inserida inválida");
                        continue;
                    }
                    break CRIAR;
			    default:
				    System.out.println("Opção inserida inválida");
			}
    	} while (tipoConta != 0);
    }

    //Metodo para fazer o display de todas as contas de um dado cliente
    public void displayContas(int nrCliente) {
        try {
            ResultSet resultSet = BdUtil.select("SELECT * FROM conta WHERE idCliente = " + nrCliente + ";");

            while (resultSet.next()) {
                detalheConta(new Conta(
                        resultSet.getInt("nrConta"),
                        resultSet.getDouble("saldo"),
                        resultSet.getString("tpConta"),
                        resultSet.getBoolean("ativo"),
                        nrCliente
                ));
            }
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }
    }

    //Metodo para mostrar o detalhe de uma dada conta
    public void detalheConta(Conta conta) {
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

    public Conta(int nrConta, double saldo, String tipoConta, boolean ativo, int nrCliente) {
        this.setNrConta(nrConta);
        this.setSaldo(saldo);
        this.setTipoConta(tipoConta);
        this.setAtivo(ativo);
        this.setNrCliente(nrCliente);
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

    protected void setNrCliente(int nrCliente) {
        this.nrCliente = nrCliente;
    }

    public int getNrCliente() {
        return this.nrCliente;
    }

    public Conta() {

    }
}
