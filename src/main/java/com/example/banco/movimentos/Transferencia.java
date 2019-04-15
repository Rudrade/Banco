package com.example.banco.movimentos;

import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

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
    public void transferir(Cliente cliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        do {
            System.out.print("Conta origem: ");
        } while (!setNrContaOrigem(scan.nextInt(), cliente.getNrCliente()));

        do {
            System.out.print("Conta destino: ");
        } while (!setNrContaDestino(scan.nextInt()));

        System.out.print(("Password: "));
        if (cliente.getPassword().equals(scan.next())) {
            System.out.print("Total a transferir: ");
            if (this.setMontante(scan.nextDouble())) {
                BdUtil.transferencia(this);

                System.out.println("Transferência realizada com sucesso");
            }
        }
        else {
            System.out.println("Password ou número de conta inválido");
        }
    }

    //Metodo para inserir o número de conta origem
    //Faz a validação se o número da conta pretence ao número do cliente
    private boolean setNrContaOrigem(int nrContaOrigem, int nCliente) {
        if (BdUtil.obterConta(nrContaOrigem).getIdCliente() == nCliente) {
            this.nrContaOrigem = nrContaOrigem;
            return true;
        }

        System.out.println("A conta que inseriu é inválida");
        return false;
    }

    //Metodo para inserir o número de conta destino
    //Faz a validação se o número da conta existe
    private boolean setNrContaDestino(int nrContaDestino) {
        if (BdUtil.obterConta(nrContaDestino).getNrConta() == nrContaDestino) {
            this.nrContaDestino = nrContaDestino;
            return true;
        }

        System.out.println("A conta que inseriu é inválida");
        return false;
    }

    private boolean setMontante(double montante) {
        if (montante > 0 && BdUtil.obterConta(this.getNrContaOrigem()).getSaldo() >= montante) {
            this.montante = montante;
            return true;
        }
        else {
            System.out.println("Montante inválido");
            return false;
        }
    }

    public double getMontante() {
        return this.montante;
    }

    public int getNrContaDestino() {
        return this.nrContaDestino;
    }

    public int getNrContaOrigem() {
        return this.nrContaOrigem;
    }
}
