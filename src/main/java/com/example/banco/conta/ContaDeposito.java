package com.example.banco.conta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.example.banco.movimentos.Deposito;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

public class ContaDeposito extends Conta{
	private int duracao;
	private int juros;

	ContaDeposito(int nrCliente) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Duração da conta (1-5 anos):");
		if (this.setDuracao(scan.nextInt())) {
			System.out.print("Montante inicial:");
			if (this.setSaldo(scan.nextDouble())) {
				try {
					BdUtil.execute("INSERT INTO conta (nrconta, juros, tpConta, ativo, saldo, idCliente)\n" +
							"VALUES (null, 5, 'Depósito a prazo', true, " + this.getSaldo() + ", " + nrCliente + ");");
					ResultSet resultSet = BdUtil.select("SELECT nrconta FROM conta ORDER BY nrconta DESC LIMIT 1;");
					while (resultSet.next()) {
						this.setNrConta(resultSet.getInt("nrconta"));
						break;
					}
					BdUtil.execute("INSERT INTO deposito (nrDeposito, nrConta, montante, data, nrCartao)\n" +
							"VALUES (null, " + this.getNrConta() + ", " + this.getSaldo() + ", null, null);");
					System.out.println("Conta criada com sucesso");
				} catch (SQLException e) {
					System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
					return;
				}
			}
		}
	}
	
	private boolean setDuracao(int duracao) {
		if (duracao >= 1 && duracao <= 5) {
			this.duracao = duracao;
			return true;
		}
		System.out.println("Duração inserida inválida");
		return false;
	}

	private void setJuros(int juros) {
		this.juros = juros;
	}
}
