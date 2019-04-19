package com.example.banco.cartao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.example.banco.conta.Conta;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

public class Cartao {
	private int nrCartao;
	private String tipoCartao;
	private int nrConta;
	private boolean ativo;
	private double saldo;
	
	//Metodo para desativar cartao
	public void desativarCartao(int nrCliente) {
		Scanner scan = new Scanner(System.in);
		
		System.out.println();
		System.out.print("Cartão a desativar: ");
		this.setNrCartao(scan.nextInt());

		try {
			ResultSet resultSet = BdUtil.select("SELECT ativo, c.idCliente\n" +
					"FROM cartao\n" +
					"INNER JOIN (\n" +
					"\tSELECT idCliente, nrconta\n" +
					"    FROM conta co\n" +
					") AS c\n" +
					"WHERE c.nrconta = cartao.nrConta AND nrCartao = " + this.getNrCartao() +";");

			while (resultSet.next()) {
				if (resultSet.getInt("idCliente") == nrCliente) {
					if (resultSet.getBoolean("ativo")) {
						BdUtil.execute("UPDATE cartao\n" +
								"SET ativo = false\n" +
								"WHERE nrCartao = "+ this.getNrCartao() +";");
					}
					else {
						System.out.println("Cartão já se encontra desativado");
					}
				}
				else {
					System.out.println("Cartão inexistente ou inválida");
				}
			}
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
			return;
		}
	}

	//Metodo para mostrar o detalhe de um dado cartao
	public void detalheCartao(Cartao cartao) {
		System.out.printf("\nNº cartão: %d\n", cartao.getNrCartao());
		System.out.printf("Conta: %d\n", cartao.getNrConta());
		System.out.printf("Tipo: %s\n", cartao.getTipoCartao());
		System.out.printf("Saldo %.2f\n", cartao.getSaldo());
		if (cartao.getAtivo()) {
			System.out.println("Estado: Ativo");
		}
		else {
			System.out.println("Estado: Inativo");
		}
	}

	//Metodo para listar cartao
	public void displayCartoes(int nrCliente) {
		try {
			ResultSet resultSet = BdUtil.select("SELECT * FROM cartao INNER JOIN conta ON cartao.nrConta = conta.nrconta WHERE conta.idCliente = " + nrCliente + ";");

			while (resultSet.next()) {
				detalheCartao(new Cartao(
					resultSet.getInt("nrConta"),
					resultSet.getInt("nrCartao"),
					resultSet.getString("tpCartao"),
					resultSet.getBoolean("ativo"),
					resultSet.getDouble("saldo")
				));
			}
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
			return;
		}
	}
	
	//Metodo para criar cartao
	public void criarCartao(int nrCliente) {
		Scanner scan = new Scanner(System.in);
		int tCartao;
		String password;

		System.out.println();
		System.out.print("Nº conta a associar:");
		this.setNrConta(scan.nextInt());

		try {
            ResultSet resultSet = BdUtil.select("SELECT idCliente, ativo, tpConta\n"+
                    "FROM conta\n" +
                    "WHERE nrconta = " + this.getNrConta() + ";");
            while (resultSet.next()) {
                if (resultSet.getInt("idCliente") == nrCliente) {
                    if (resultSet.getBoolean("ativo")) {
                        if (resultSet.getString("tpConta").equals("Ordem")){
                            break;
                        }
                        else {
                            System.out.println("Apenas pode associado cartões a uma conta Ordem");
                        }
                    }
                    else {
                        System.out.println("Conta inativa");
                    }
                }
                else {
                    System.out.println("Conta inválida ou inexistente");
                }
            }

            TIPO:
            do {
                System.out.println("1- Crédito");
                System.out.println("2- Débito");
                System.out.print("Opção: ");
                tCartao = scan.nextInt();

                switch (tCartao) {
                    case 1:
                       BdUtil.execute("INSERT INTO cartao (nrCartao, tpCartao, ativo, saldo, nrConta)\n" +
                               "VALUES (null, 'Crédito', true, 0, " + this.getNrConta() + ");");
                       System.out.println("Cartão criado com sucesso");
                       break TIPO;
                    case 2:
                        BdUtil.execute("INSERT INTO cartao (nrCartao, tpCartao, ativo, saldo, nrConta)\n" +
                                "VALUES (null, 'Débito', true, 0, " + this.getNrConta() + ");");
                        System.out.println("Cartão criado com sucesso");
                    break TIPO;
                    default:
                        System.out.println("Tipo inserido inválido"); }
            }  while (tCartao != 0);
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
		}
	}

	
	public String getTipoCartao() {
		return this.tipoCartao;
	}
	
	public int getNrConta() {
		return this.nrConta;
	}
	
	public int getNrCartao() {
		return this.nrCartao;
	}

	protected void setNrConta(int nrConta) {
		this.nrConta = nrConta;
	}

	protected void setTipoCartao(String tipoCartao) {
		if (tipoCartao.equals("Crédito") || tipoCartao.equals("Débito")) {
			this.tipoCartao = tipoCartao;
		}
	}
	
	private void setNrCartao(int nrCartao) {
		this.nrCartao = nrCartao;
	}
	
	public Cartao(int nrConta, int nrCartao, String tipoCartao, boolean ativo, double saldo) {
		this.setNrConta(nrConta);
		this.setTipoCartao(tipoCartao);
		this.setNrCartao(nrCartao);
		this.setAtivo(ativo);
		this.setSaldo(saldo);
	}

	public boolean getAtivo() {
		return this.ativo;
	}

	private void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public double getSaldo() {
		return this.saldo;
	}
	
	private void setSaldo(double saldo) {
		if (saldo >= 0) {
			this.saldo = saldo;
		}
		else {
			System.out.println("Saldo inserido inválido");
		}
	}
	
	public Cartao() {

	}
}
