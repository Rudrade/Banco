package com.example.banco.conta;

import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;
import com.example.banco.util.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class ContaInvestimento extends Conta{
	private int juros;

	//Metodo para quando o tempo avança:
	// os juros são aplicados proporcionalmente
	public void avancar() {
		try {
			ResultSet resultSet = BdUtil.select("SELECT c.nrConta, c.juros, d.data, d.montante\n" +
					"FROM conta c\n" +
					"INNER JOIN (\n" +
					"\tSELECT montante, data, nrConta\n" +
					"    FROM deposito\n" +
					"\t) AS d\n" +
					"WHERE tpConta = 'Investimento' AND c.nrConta = d.nrConta;");

			while (resultSet.next()) {
				double diff = (double) TimeUnit.DAYS.convert(Data.getDataAtual().getTime() - resultSet.getDate(3).getTime(), TimeUnit.MILLISECONDS);
				double juros = (resultSet.getDouble(2) * diff) / 365;

				BdUtil.execute("UPDATE conta\n" +
						"SET saldo = saldo + (" + (resultSet.getDouble(4) * juros)+ ")\n" +
						"WHERE nrConta = " + resultSet.getInt(1) + ";");
			}
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
			return;
		}
	}

	protected ContaInvestimento(Cliente cliente) {
		super();
		this.setTipoConta("Investimento");
		this.setSaldo(50);
		this.setJuros(5);
	}

	private void setJuros(int juros) {
		this.juros = juros;
	}

	public ContaInvestimento() {}
}
