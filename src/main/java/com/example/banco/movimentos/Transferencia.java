package com.example.banco.movimentos;

import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Transferencia {
    private int nrTransferencia;
    private Conta contaOrigem;
    private Conta contaDestino;
    private double montante;
    private LocalDateTime data;

    //Metodo para transferir dinheiro entre duas contas
    //Recebe como parametro um cliente para fazer a confirmação da palavra-passe como uma forma de segurança
    public void transferir(Cliente cliente) {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.print("Conta origem: ");
        setContaOrigem(BdUtil.obterConta(scan.nextInt()));

        System.out.print("Conta destino: ");
        setContaDestino(BdUtil.obterConta(scan.nextInt()));

        if (!(getContaOrigem().getEstado() && getContaDestino().getEstado() && getContaOrigem().getCliente().getNrCliente() == cliente.getNrCliente())) {
            System.out.println("Conta(s) inválida(s) ou inativa(s)");
            return;
        }

        System.out.print(("Password: "));
        if (this.getContaOrigem().getCliente().getPassword().equals(scan.next())) {
            System.out.print("Total a transferir: ");
            if (this.setMontante(scan.nextDouble())) {
                System.out.println("A processar...");
                BdUtil.transferencia(this);
                System.out.println("Transferência realizada com sucesso");
            }
        }
        else {
            System.out.println("Password ou número de conta inválido");
        }
    }

    private boolean setMontante(double montante) {
        //if (montante > 0 && BdUtil.obterConta(this.getNrContaOrigem()).getSaldo() >= montante) {
            this.montante = montante;
            return true;
        /*}
        else {
            System.out.println("Montante inválido");
            return false;
        }*/
    }

    public double getMontante() {
        return this.montante;
    }

    public Conta getContaDestino(){
        return this.contaDestino;
    }

    private void setContaDestino(Conta conta) {
        this.contaDestino = conta;
    }

    public Conta getContaOrigem(){
        return this.contaOrigem;
    }

    private void setContaOrigem(Conta conta) {
        this.contaOrigem = conta;
    }
}
