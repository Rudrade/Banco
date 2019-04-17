package com.example.banco.movimentos;

import com.example.banco.cartao.Cartao;
import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Deposito {
    private int nrDeposito;
    private Conta conta;
    private Cartao cartao;
    private double montante;
    private LocalDateTime data;

    //Metodo para depositar dinheiro
    //Recebe como parametro um cliente para fazer a confirmação da palavra-passe como uma forma de segurança
    public void depositar(Cliente cliente) {
        Scanner scan = new Scanner(System.in);
        int tipoDeposito;

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
                    this.setConta(BdUtil.obterConta(scan.nextInt()));
                    if (!(this.getConta().getEstado() && this.getConta().getCliente().getNrCliente() == cliente.getNrCliente())) {
                        System.out.println("Conta inserida inativa ou inválida");
                        return;
                    }

                    if(!(this.getConta().getTipoConta().equals("Ordem") || this.getConta().getTipoConta().equals("Investimento"))) {
                        System.out.println("Depósitos apenas podem ser realizados com uma conta Ordem ou conta Investimento");
                        return;
                    }
                    break TIPO;//Fim tipo deposito 1 - conta
                case 2:
                    System.out.print("Cartão a depositar: ");
                    this.setCartao(BdUtil.obterCartao(scan.nextInt()));

                    if (!(this.getCartao().getAtivo() && this.getCartao().getConta().getCliente().getNrCliente() == cliente.getNrCliente())) {
                        System.out.println("Cartão inativo ou inexistente");
                        return;
                    }

                    if (!this.getCartao().getTipoCartao().equals("Débito")) {
                        System.out.println("Depósitos apenas podem ser realizados com um cartão de débito");
                        return;
                    }
                    break TIPO;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção selecionada é inválida");
            }
        }

        System.out.print("Total a depositar: ");
        if(this.setMontante(scan.nextDouble())) {
            System.out.print("Password: ");
            if (cliente.getPassword().equals(scan.next())) {
                System.out.println("A processar...");
                BdUtil.inserirDeposito(this);

                System.out.println("Deposito feito com sucesso.");
            }
            else {
                System.out.println("Password inválida");
            }
        }
    }

    public void depositoDetalhe() {
        System.out.println("Tipo: Depósito");
        System.out.printf("Nº: %d\n", this.getNrDeposito());
        System.out.printf("Conta: %d\n", this.getConta().getNrConta());
        if (this.getCartao() != null) {
            System.out.printf("Cartão: %d\n", this.getCartao().getNrCartao());
        }
        System.out.printf("Montante: %.2f\n", this.getMontante());
    }

    public void displayAll(Cliente cliente) {
        ArrayList<Conta> contas = BdUtil.obterContas(cliente.getNrCliente());

        for (Conta conta : contas) {
            if (cliente.getNrCliente() == conta.getCliente().getNrCliente()) {
                for (Deposito deposito : BdUtil.obterDepositos(conta)) {
                    deposito.depositoDetalhe();
                    System.out.println("-------------");
                }
                return;
            }
        }
    }

    public Deposito(int nrDeposito, Conta conta, double montante, Cartao cartao) {
        this.setMontante(montante);
        this.setConta(conta);
        this.setNrDeposito(nrDeposito);
        this.setCartao(cartao);
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

    public Conta getConta(){
        return this.conta;
    }

    private void setConta(Conta conta) {
        this.conta = conta;
    }

    private void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Cartao getCartao() {
        return this.cartao;
    }

    public Deposito() {

    }
}
