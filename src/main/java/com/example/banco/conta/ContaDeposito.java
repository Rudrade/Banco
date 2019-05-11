package com.example.banco.conta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.example.banco.util.BdUtil;
import com.example.banco.util.Data;

public class ContaDeposito extends Conta{
	private int duracao;
	private int juros;
	private Date data;

	ContaDeposito(int nrCliente) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Duração da conta (1-5 anos):");
		if (this.setDuracao(scan.nextInt())) {
			System.out.print("Montante inicial: ");
			if (this.setSaldo(scan.nextDouble())) {
				try {
					ResultSet resultSet1 = BdUtil.select("SELECT saldo FROM conta WHERE tpConta = 'Ordem' and idCliente = " + nrCliente + ";");

					while (resultSet1.next()) {
						if (resultSet1.getDouble("saldo") < this.getSaldo()) {
							System.out.println("Saldo insuficiente na conta a ordem");
							return;
						}
					}

					this.setValidacao();
					DateFormat outputFormatter = new SimpleDateFormat("yyyy/MM/dd");
					String output = outputFormatter.format(this.data.getTime());

					BdUtil.execute("INSERT INTO conta (nrconta, juros, tpConta, ativo, saldo, idCliente, duracao, dataCriacao, nrAgencia)\n" +
							"VALUES (null, 5, 'Depósito a prazo', true, " + this.getSaldo() + ", " + nrCliente + ",'" + output + "','" + Data.obterDataString(Data.getDataAtual()) + "', (SELECT nrAgencia FROM cliente WHERE idCliente = " + nrCliente + "));");
					ResultSet resultSet = BdUtil.select("SELECT nrconta FROM conta ORDER BY nrconta DESC LIMIT 1;");
					while (resultSet.next()) {
						this.setNrConta(resultSet.getInt("nrconta"));
						break;
					}
					BdUtil.execute("INSERT INTO deposito (nrDeposito, nrConta, montante, data, nrCartao)\n" +
							"VALUES (null, " + this.getNrConta() + ", " + this.getSaldo() + ", null, null);\n" +
							"UPDATE conta AS co, (SELECT nrconta FROM conta WHERE tpConta = 'Ordem' AND idCliente = " + nrCliente + ") AS c SET co.saldo = co.saldo - " + this.getSaldo() + " WHERE co.nrconta = c.nrconta;");
					System.out.println("Conta criada com sucesso");
				} catch (SQLException e) {
					System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
					return;
				}
			}
		}
	}

	//Metodo para quando o tempo avança:
	//é depositado 10% da conta depósito na conta ordem correspondente
	//
	public void avancar() {
		try {
			ResultSet resultSet = BdUtil.select("SELECT * FROM conta WHERE tpConta = 'Depósito a prazo';");

			while (resultSet.next()) {
				double diff = (double) TimeUnit.DAYS.convert(resultSet.getDate("duracao").getTime() - Data.getDataAtual().getTime(), TimeUnit.MILLISECONDS);
				while (diff != 0) {
					if (diff >= 365 && resultSet.getInt("saldo") > 0) {
						ResultSet resultSet1 = BdUtil.select("SELECT co.nrconta, c.saldo\n" +
								"FROM conta c\n" +
								"INNER JOIN (\n" +
								"\tSELECT nrconta, saldo, idCliente\n" +
								"    FROM conta\n" +
								"    WHERE tpConta = 'Ordem'\n" +
								") AS co\n" +
								"WHERE c.idCliente = co.idCliente AND c.nrconta = " + resultSet.getInt(1) + ";");
						while (resultSet1.next()) {
							BdUtil.execute("UPDATE conta\n" +
									"SET saldo = saldo + " + (resultSet1.getDouble(2) * 0.10) + "\n" +
									"WHERE nrconta = " + resultSet1.getInt(1) + ";");
						}
						diff -= 365;
					}
					else {
						break;
					}
				}
			}
		} catch (SQLException e) {
			System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
			return;
		}
	}

	//Metodo para mostrar o detalhe de uma dada conta
	public void detalheConta() {
		System.out.println();
		System.out.printf("Nº Conta: %d\n", this.getNrConta());
		System.out.printf("Saldo: %.2f\n", this.getSaldo());
		System.out.printf("Tipo: %s\n", this.getTipoConta());
		try {
			System.out.printf("Data validade: %s\n", Data.obterDataS(this.getData()));
		} catch (NullPointerException e) {}
		if (this.getEstado()) {
			System.out.println("Estado: Ativo");
		}
		else {
			System.out.println("Estado: Inativo");
		}
	}

	private void setValidacao() {
		Calendar calendar = Calendar.getInstance();
		Date date = Data.getDataAtual();

		calendar.setTime(date);
		calendar.add(Calendar.YEAR, this.getDuracao());
		this.data = calendar.getTime();
	}

	private boolean setDuracao(int duracao) {
		if (duracao >= 1 && duracao <= 5) {
			this.duracao = duracao;
			return true;
		}
		System.out.println("Duração inserida inválida");
		return false;
	}

	public int getDuracao() {
		return this.duracao;
	}

	private void setJuros(int juros) {
		this.juros = juros;
	}

	public Date getData() {
		return this.data;
	}

	ContaDeposito(int nrConta, double saldo, String tipoConta, int nrCliente, boolean ativo, int juros, Date data) {
		this.setNrConta(nrConta);
		this.setSaldo(saldo);
		this.setTipoConta(tipoConta);
		this.setNrCliente(nrCliente);
		this.setJuros(juros);
		this.setAtivo(ativo);
		this.setData(data);
	}

	private void setData(Date data){
		this.data = data;
	}

	public ContaDeposito() {};
}
