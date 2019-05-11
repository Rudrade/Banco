package com.example.banco.conta;

import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;
import com.example.banco.util.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class ContaPoupanca extends Conta{
	private int juros;

	public ContaPoupanca(Cliente cliente) {
		super();
		this.setTipoConta("Poupança");
		this.setJuros(5);
	}

	public void avancarJuros() {
		try {
			ResultSet resultSet = BdUtil.select("SELECT * FROM conta WHERE tpConta = 'Poupança';");

			while (resultSet.next()) {
				if (resultSet.getDate("dataCriacao") != null) {
					double diff = (double) TimeUnit.DAYS.convert(Data.getDataAtual().getTime() - resultSet.getDate("dataCriacao").getTime(), TimeUnit.MILLISECONDS);
					if (diff >= 365 && resultSet.getInt("saldo") > 0) {
						BdUtil.execute("UPDATE conta SET saldo = saldo + " + ((5 * diff) / 365) + " WHERE nrconta = " + resultSet.getInt("nrconta"));
					}
				}
			}
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
			return;
		}
	}

	private void setJuros(int juros) {
		this.juros = juros;
	}

	public ContaPoupanca(int nrConta, double saldo, String tipoConta, boolean ativo, int nrCliente, int nrAgencia, int juros) {
		super(nrConta, saldo, tipoConta, ativo, nrCliente, nrAgencia);
		this.setJuros(juros);
	}

	public int getJuros() {
		return this.juros;
	}

	public ContaPoupanca() {}
}
