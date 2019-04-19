package com.example.banco.movimentos;

import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Transferencia {
    private int nrTransferencia;
    private int nrContaOrigem;
    private int nrContaDestino;
    private double montante;
    private LocalDateTime data;

    //Metodo para transferir dinheiro entre duas contas
    //Recebe como parametro um cliente para fazer a confirmação da palavra-passe como uma forma de segurança
    public void transferir(int nrCliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.print("Conta origem: ");
        this.setNrContaOrigem(scan.nextInt());

        System.out.print("Conta destino: ");
        this.setNrContaDestino(scan.nextInt());

        try {
            ResultSet resultSet = BdUtil.select("SELECT distinct c.idCliente, c.ativo AS ativo1, c2.ativo AS ativo2\n" +
                    "FROM conta c\n" +
                    "INNER JOIN conta c2 ON c.nrconta != c2.nrconta\n" +
                    "WHERE c.nrconta = "+ this.getNrContaOrigem() +" AND c2.nrconta = "+ this.getNrContaDestino() +";");

            while (resultSet.next()) {
                if (resultSet.getInt("idCliente") == nrCliente) {
                    if (resultSet.getBoolean("ativo1") && resultSet.getBoolean("ativo2")) {
                        break;
                    }
                    else {
                        System.out.println("Pelo menos uma das contas está inativa");
                    }
                }
                else {
                    System.out.println("Conta inválida ou inexistente");
                }
            }

            System.out.print("Total a transferir: ");
            if (this.setMontante(scan.nextDouble())) {
                BdUtil.execute("INSERT INTO transferencia (nrTransferencia, data, nrContaOrigem, nrContaDestino, montante)\n" +
                        "VALUES (null, null, " + this.getNrContaOrigem() + "," + this.getNrContaDestino() + "," + this.getMontante() + ");\n" +
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
        System.out.println("Tipo: Transferência");
        System.out.printf("Nº: %d\n", transf.getNrTransferencia());
        System.out.printf("Conta Origem: %d\n", transf.getNrContaOrigem());
        System.out.printf("Conta Destino: %d\n", transf.getNrContaDestino());
        System.out.printf("Montante: %.2f\n", transf.getMontante());
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
                        resultSet.getDouble("montante")
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

    public Transferencia(int nrTransferencia, int nrContaOrigem, int nrContaDestino, double montante) {
        this.setNrTransferencia(nrTransferencia);
        this.setNrContaOrigem(nrContaOrigem);
        this.setNrContaDestino(nrContaDestino);
        this.setMontante(montante);
    }

    public Transferencia() {

    }

    private void setNrTransferencia(int nrTransferencia) {
        this.nrTransferencia = nrTransferencia;
    }
}
