package com.example.banco.movimentos;

import com.example.banco.util.BdUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class Transferencia {
    private int nrTransferencia;
    private int nrContaOrigem;
    private int nrContaDestino;
    private double montante;
    private Timestamp data;

    //Metodo para transferir dinheiro entre duas contas
    //Recebe como parametro um cliente para fazer a confirmação da palavra-passe como uma forma de segurança
    public void transferir(int nrCliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.print("Conta origem: ");
        this.setNrContaOrigem(scan.nextInt());

        System.out.print("Conta destino: ");
        this.setNrContaDestino(scan.nextInt());

        if (this.getNrContaOrigem() == this.getNrContaDestino()) {
            System.out.println("Não pode transferir para a mesma conta");
            return;
        }

        try {
            ResultSet resultSet = BdUtil.select("SELECT distinct c.idCliente AS cliente1, c.ativo AS ativo1, c2.ativo AS ativo2, c.tpConta AS tipo1, c2.tpConta AS tipo2, c2.idCliente AS cliente2\n" +
                    "FROM conta c\n" +
                    "INNER JOIN conta c2 ON c.nrconta != c2.nrconta\n" +
                    "WHERE c.nrconta = "+ this.getNrContaOrigem() +" AND c2.nrconta = "+ this.getNrContaDestino() +";");

            while (resultSet.next()) {
                if (resultSet.getInt("cliente1") == nrCliente) {
                    if (resultSet.getBoolean("ativo1") && resultSet.getBoolean("ativo2")) {
                        if (resultSet.getString("tipo1").equals("Ordem")) {
                            break;
                        }
                        else if (resultSet.getInt("cliente2") == nrCliente){
                            if (resultSet.getString("tipo2").equals("Ordem")) {
                                break;
                            }
                        }
                    } else {
                        System.out.println("Pelo menos uma das contas está inativa");
                    }
                }
                System.out.println("Conta inválida ou inexistente");
                return;
            }

            System.out.print("Total a transferir: ");
            double mont = scan.nextDouble();

            ResultSet resultSet1 = BdUtil.select("SELECT saldo\n"+
                    "FROM conta\n"+
                    "WHERE nrconta = " + this.getNrContaOrigem() + ";");

            while (resultSet1.next()) {
                if (resultSet1.getDouble("saldo") < mont || mont <= 0) {
                    System.out.println("Saldo inválido ou insuficiente");
                    return;
                }
            }

            if (this.setMontante(scan.nextDouble())) {
                this.obterData();
                BdUtil.execute("INSERT INTO transferencia (nrTransferencia, data, nrContaOrigem, nrContaDestino, montante)\n" +
                        "VALUES (null,'" + this.getData() + "', " + this.getNrContaOrigem() + "," + this.getNrContaDestino() + "," + this.getMontante() + ");\n" +
                        "UPDATE conta\n" +
                        "SET saldo = saldo - " + this.getMontante() +"\n" +
                        "WHERE nrconta = " + this.getNrContaOrigem() + ";\n" +
                        "UPDATE conta\n" +
                        "SET saldo = saldo + " +this.getMontante() + "\n" +
                        "WHERE nrconta = " + this.getNrContaDestino() + ";");
                System.out.println("Transferência realizada com sucesso");
            }
        else {
            System.out.println("Password ou número de conta inválido");
        }
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }
    }

    public void transferenciaDetalhe(Transferencia transf) {
        System.out.printf("Nº: %d\n", transf.getNrTransferencia());
        System.out.printf("Conta Origem: %d\n", transf.getNrContaOrigem());
        System.out.printf("Conta Destino: %d\n", transf.getNrContaDestino());
        System.out.printf("Montante: %.2f\n", transf.getMontante());
        System.out.println("Data: " + transf.getData());
    }

    public void displayAll(int nrCliente) {
        try {
            ResultSet resultSet = BdUtil.select("SELECT DISTINCT t.nrTransferencia, t.nrContaOrigem, t.nrContaDestino, t.montante, t.data\n" +
                    "FROM transferencia t\n" +
                    "INNER JOIN (\n" +
                    "\tSELECT idCliente, nrconta\n" +
                    "    FROM conta\n" +
                    ") AS c\n" +
                    "WHERE c.idCliente = " + nrCliente + ";");

            System.out.println();
            while (resultSet.next()) {
                transferenciaDetalhe(new Transferencia(
                        resultSet.getInt("nrTransferencia"),
                        resultSet.getInt("nrContaOrigem"),
                        resultSet.getInt("nrContaDestino"),
                        resultSet.getDouble("montante"),
                        resultSet.getTimestamp("data")
                ));
                System.out.println("######");
            }
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }
    }

    private boolean setMontante(double montante) {
         if (montante > 0) {
            this.montante = montante;
            return true;
        }
        else {
            System.out.println("Montante inválido ou insuficiente");
            return false;
        }
    }

    public double getMontante() {
        return this.montante;
    }

    public int getNrContaDestino(){
        return this.nrContaDestino;
    }

    private void setNrContaDestino(int nrConta) {
        this.nrContaDestino = nrConta;
    }

    public int getNrContaOrigem(){
        return this.nrContaOrigem;
    }

    private void setNrContaOrigem(int nrConta) {
        this.nrContaOrigem = nrConta;
    }

    public int getNrTransferencia() {
        return this.nrTransferencia;
    }

    public Transferencia(int nrTransferencia, int nrContaOrigem, int nrContaDestino, double montante, Timestamp data) {
        this.setNrTransferencia(nrTransferencia);
        this.setNrContaOrigem(nrContaOrigem);
        this.setNrContaDestino(nrContaDestino);
        this.setMontante(montante);
        this.setData(data);
    }

    public Transferencia() {

    }

    private void setNrTransferencia(int nrTransferencia) {
        this.nrTransferencia = nrTransferencia;
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
