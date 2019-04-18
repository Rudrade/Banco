package com.example.banco.conta;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.example.banco.movimentos.Deposito;
import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;

public class ContaDeposito extends Conta{
	private int duracao;
	private int juros;

	ContaDeposito(Cliente cliente) {
		super(cliente);

		Scanner scan = new Scanner(System.in);

		this.setTipoConta("Depósito a prazo");
		this.setJuros(5);
		System.out.print("Duração da conta (1-5 anos):");
		if (this.setDuracao(scan.nextInt())) {
			System.out.print("Montante inicial:");
			if (this.setSaldo(scan.nextDouble())) {
				System.out.println("A processar...");
				BdUtil.criarConta(this);

				try {
					ResultSet resultSet = BdUtil.select("SELECT nrconta FROM conta ORDER BY nrconta DESC LIMIT 1;");
					while (resultSet.next()) {
						this.setNrConta(resultSet.getInt("nrconta"));
						break;
					}
				} catch (SQLException e) {
					System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
				}

				BdUtil.inserirDeposito(new Deposito(this, this.getSaldo()));
				System.out.println("Conta criada com sucesso");
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
