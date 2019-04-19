package com.example.banco.movimentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

import com.example.banco.util.BdUtil;

public class Levantamento {
    private int nrLevantamento;
    private int nrConta;
    private int nrCartao;
    private double montante;
    private Timestamp data;

    //Metodo para levantar dinheiro através de uma conta
    //Rece como parametro um cliente para fazer a confirmação da password como uma forma de segunraça
    public void levantar(int nrCliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.print("Conta a levantar:");
        this.setNrConta(scan.nextInt());

        try {
            ResultSet resultSet = BdUtil.select("SELECT tpConta, ativo, idCliente\n" +
                    "FROM conta\n" +
                    "WHERE nrconta = "+ this.getNrConta() + ";");

            while (resultSet.next()) {
                if (resultSet.getInt("idCliente") == nrCliente) {
                    if (resultSet.getBoolean("ativo")) {
                        if (resultSet.getString("tpConta").equals("Ordem")) {
                            break;
                        }
                        else {
                            System.out.println("Levantamentos são apenas permitidos através de uma conta a Ordem");
                            return;
                        }
                    }
                    else {
                        System.out.println("Conta inativa");
                        return;
                    }
                }
                else {
                    System.out.println("Conta inválida ou inexistente");
                    return;
                }
            }

            this.obterData();
            System.out.print("Motante a levantar: ");
            if(this.setMontante(scan.nextDouble())) {
                BdUtil.execute("INSERT INTO levantamento (nrLevantamento, nrConta, montante, data, nCartao)\n" +
                        "VALUES (null, " + this.getNrConta() + "," +  this.getMontante() + ", " + this.getData() + ", null);\n" +
                        "UPDATE conta\n" +
                        "SET saldo = saldo - " + this.getMontante() + "\n" +
                        "WHERE nrconta = " + this.getNrConta() +";");
                System.out.println("Levantamento feito com sucesso");
            }
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }
    }

    public void levantamentoDetalhe(Levantamento levantamento) {
        System.out.printf("Nº: %d\n", levantamento.getNrLevantamento());
        System.out.printf("Conta: %d\n", levantamento.getNrConta());
        if (levantamento.getNrCartao() != 0) {
        	System.out.printf("Cartão: %d\n", levantamento.getNrCartao());
        }
        System.out.printf("Montante: %.2f\n", levantamento.getMontante());
        System.out.println("Data: " + levantamento.getData());
        System.out.println("##########");
    }

    public void displayAll(int nrCliente) {
        try {
            ResultSet resultSet = BdUtil.select("SELECT * \n" +
                    "FROM levantamento;");

            while (resultSet.next()) {
                levantamentoDetalhe(new Levantamento(
                        resultSet.getInt("nrLevantamento"),
                        resultSet.getInt("nrConta"),
                        resultSet.getDouble("montante"),
                        resultSet.getInt("nCartao"),
                        resultSet.getTimestamp("data")
                ));
            }
        } catch (SQLException e){
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }
    }

    public Levantamento(int nrLevantamento, int nrConta, double montante, int nrCartao, Timestamp data) {
        this.setMontante(montante);
        this.setNrCartao(nrCartao);
        this.setNrLevantamento(nrLevantamento);
        this.setNrConta(nrConta);
        this.setData(data);
    }

    private void setNrLevantamento(int nrLevantamento) {
        this.nrLevantamento = nrLevantamento;
    }

    //Metodo para validar se o montante desejado pelo utilizador é válido
    //Faz uma query a base de dados de forma a verificar a quantidade de saldo da conta
    private boolean setMontante(double montante) {
        if (montante > 0) {
            this.montante = montante;
            return true;
        }
        else {
            System.out.println("Montante inserido inválido ou insuficiente");
            return false;
        }
    }

    public int getNrConta(){
        return this.nrConta;
    }

    private void setNrConta(int nrConta) {
        this.nrConta = nrConta;
    }

    public double getMontante() {
        return this.montante;
    }

    public Levantamento() {}

    public int getNrCartao() {
    	return this.nrCartao;
    }
    
    private void setNrCartao(int nrCartao) {
    	this.nrCartao = nrCartao;
    }
    
    public int getNrLevantamento() {
        return this.nrLevantamento;
    }
    private void setData(Timestamp data) {
        this.data = data;
    }

    private void obterData() {
        java.util.Date date = new java.util.Date();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
        this.data = timestamp;
    }

    public Timestamp getData() {
        return this.data;
    }
}