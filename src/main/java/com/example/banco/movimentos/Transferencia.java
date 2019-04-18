package com.example.banco.movimentos;

import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public void transferenciaDetalhe(Transferencia transf) {
        System.out.println("Tipo: Transferência");
        System.out.printf("Nº: %d\n", transf.getNrTransferencia());
        System.out.printf("Conta Origem: %d\n", transf.getContaOrigem().getNrConta());
        System.out.printf("Conta Destino: %d\n", transf.getContaDestino().getNrConta());
        System.out.printf("Montante: %.2f\n", transf.getMontante());
    }

    public void displayAll(Cliente cliente) {
        ArrayList<Conta> contas = BdUtil.obterContas(cliente.getNrCliente());

        for (Conta conta : contas) {
            if (cliente.getNrCliente() == conta.getCliente().getNrCliente()) {
                ArrayList<Transferencia> lista = BdUtil.obterTransferencia(conta);
                for (Transferencia transferencia : lista) {
                    transferencia.transferenciaDetalhe(transferencia);
                    System.out.println("-------------");
                }
                return;
            }
        }
    }

    private boolean setMontante(double montante) {
         if (montante > 0 && montante <= getContaOrigem().getSaldo()) {
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

    public int getNrTransferencia() {
        return this.nrTransferencia;
    }

    public Transferencia(int nrTransferencia, Conta contaOrigem, Conta contaDestino, double montante) {
        setNrTransferencia(nrTransferencia);
        setContaOrigem(contaOrigem);
        setContaDestino(contaDestino);
        setMontante(montante);
    }

    public Transferencia() {

    }

    private void setNrTransferencia(int nrTransferencia) {
        this.nrTransferencia = nrTransferencia;
    }
}
