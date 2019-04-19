package com.example.banco.movimentos;

import com.example.banco.util.BdUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class Deposito {
    private int nrDeposito;
    private int nrConta;
    private int nrCartao;
    private double montante;
    private Timestamp data;

    //Metodo para depositar dinheiro
    //Recebe como parametro um cliente para fazer a confirmação da palavra-passe como uma forma de segurança
    public void depositar(int nrCliente) {
        Scanner scan = new Scanner(System.in);
        int tipoDeposito;

        try {
            TIPO: while (true) {
                System.out.println();
                System.out.println("Depositar através:");
                System.out.println("1- Conta");
                System.out.println("2- Cartão");
                System.out.println("0- Sair");
                System.out.print("Opção:");
                tipoDeposito = scan.nextInt();

                switch (tipoDeposito) {
                    case 1:
                        System.out.print("Conta a depositar: ");
                        this.setNrConta(scan.nextInt());
                        ResultSet resultSet = BdUtil.select("SELECT idCliente, tpConta, ativo FROM conta WHERE nrconta = " + this.getNrConta() + ";");

                        while (resultSet.next()) {
                            if (resultSet.getInt("idCliente") == nrCliente) {
                                if (resultSet.getBoolean("ativo")) {
                                    if (resultSet.getString("tpConta").equals("Ordem") || resultSet.getString("tpConta").equals("Investimento")) {
                                        break TIPO;
                                    }
                                }
                            }
                        }
                        System.out.println("Conta inválida");
                        continue TIPO;
                    case 2:
                        System.out.print("Cartão a depositar: ");
                        this.setNrCartao(scan.nextInt());
                        ResultSet resultSet1 = BdUtil.select("SELECT c.ativo AS cartaoAtivo, c.tpCartao, co.ativo AS contaAtivo , co.idCliente, c.nrConta\n" +
                                "FROM cartao c\n" +
                                "INNER JOIN (\n" +
                                "\tSELECT co.idCliente, co.ativo, co.nrconta\n" +
                                "    FROM conta co\n" +
                                ") AS co\n" +
                                "ON c.nrConta = co.nrconta\n" +
                                "WHERE c.nrCartao = " + this.getNrCartao() +";");
                       while (resultSet1.next()) {
                           if (resultSet1.getInt("idCliente") == nrCliente) {
                               if (resultSet1.getBoolean("cartaoAtivo") && resultSet1.getBoolean("contaAtivo")) {
                                   if (resultSet1.getString("tpCartao").equals("Débito")) {
                                       this.setNrConta(resultSet1.getInt("nrConta"));
                                       break TIPO;
                                   }
                               }
                           }
                       }
                       System.out.println("Cartão inválido");
                       continue TIPO;
                    case 0:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opção selecionada é inválida");
                }
            }

            System.out.print("Total a depositar: ");
            if (this.setMontante(scan.nextDouble())) {
                String nCartaoS;

                if (this.getNrCartao() != 0) {
                    nCartaoS = String.valueOf(this.getNrCartao());
                    BdUtil.execute("UPDATE cartao\n" +
                            "SET saldo = saldo + " + this.getMontante() + "\n" +
                            "WHERE nrCartao = " + this.getNrCartao() + ";");
                }
                else {
                    nCartaoS = "null";
                }

                this.obterData();
                BdUtil.execute("INSERT INTO deposito (nrDeposito, nrConta, montante, data, nrCartao)\n" +
                        "VALUES (null, " + this.getNrConta() + "," +  this.getMontante() + ",'" + this.getData() + "',"+ nCartaoS + ");\n" +
                        "UPDATE conta\n" +
                        "SET saldo = saldo + " + this.getMontante() + "\n" +
                        "WHERE nrconta = " + this.getNrConta() +";");
                System.out.println("Depósito feito com sucesso");
            }
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }
    }

    public void depositoDetalhe(Deposito deposito) {
        System.out.printf("Nº: %d\n", deposito.getNrDeposito());
        System.out.printf("Conta: %d\n", deposito.getNrConta());
        if (deposito.getNrCartao() != 0) {
            System.out.printf("Cartão: %d\n", deposito.getNrCartao());
        }
        System.out.printf("Montante: %.2f\n", deposito.getMontante());
        System.out.println("Data: " + deposito.getData());
        System.out.println("#########");
    }

    public void displayAll(int nrCliente) {
        try {
            ResultSet resultSet = BdUtil.select("select d.nrDeposito, d.nrConta, d.montante, d.data, d.nrCartao\n" +
                    "from deposito d\n" +
                    "inner join conta c\n" +
                    "on d.nrConta = c.nrconta\n" +
                    "where c.idCliente = " + nrCliente + ";");

            while (resultSet.next()) {
                depositoDetalhe(new Deposito(
                        resultSet.getInt("nrDeposito"),
                        resultSet.getInt("nrConta"),
                        resultSet.getDouble("montante"),
                        resultSet.getInt("nrCartao"),
                        resultSet.getTimestamp("data")
                ));
            }
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }
    }

    public Deposito(int nrDeposito, int nrConta, double montante, int nrCartao, Timestamp data) {
        this.setMontante(montante);
        this.setNrConta(nrConta);
        this.setNrDeposito(nrDeposito);
        this.setNrCartao(nrCartao);
        this.setData(data);
    }

    private void setNrDeposito(int nrDeposito) {
        this.nrDeposito = nrDeposito;
    }

    private boolean setMontante(double montante) {
        if (montante > 0) {
            this.montante = montante;
            return true;
        }

        System.out.println("Montante inserido não é válido");
        return false;
    }

    public double getMontante() {
        return this.montante;
    }

    public int getNrDeposito() {
        return this.nrDeposito;
    }

    public int getNrConta(){
        return this.nrConta;
    }

    private void setNrConta(int nrConta) {
        this.nrConta = nrConta;
    }

    private void setNrCartao(int nrCartao) {
        this.nrCartao = nrCartao;
    }

    public int getNrCartao() {
        return this.nrCartao;
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

    public Deposito() {

    }
}
