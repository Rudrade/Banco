package com.example.banco.pessoa;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

//Class Pessoa
public abstract class Pesssoa {
	private int idPessoa;
	private String nome;
	private String morada;
	private int telefone;
	private String email;
	private String profissao;
	
	void setProfissao(String profissao) {
		this.profissao = profissao;
	}
	
	boolean setEmail (String email) {
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
			this.email = email;
			return true;
		} catch (AddressException ex) {
			return false;
		}
	}
	
	boolean setTelefone (int telefone) {
		String telefoneS = String.valueOf(telefone);
		
		if ((telefoneS.length() == 9) && (telefoneS.startsWith("91") || telefoneS.startsWith("93") || telefoneS.startsWith("96") || telefoneS.startsWith("92"))) {
			this.telefone = telefone;
			return true;
		}
		else {
			System.out.println("Número de telefone inválido");
			return false;
		}
	}

	void setIdPessoa (int idPessoa) {
		this.idPessoa = idPessoa;
	}

	void setMorada (String morada) {
		this.morada = morada;
	}
	
	void setNome (String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public String getMorada() {
		return morada;
	}

	public int getTelefone() {
		return this.telefone;
	}

	public String getEmail() {
		return email;
	}

	public String getProfissao() {
		return profissao;
	}

	public int getIdPessoa() {
		return this.idPessoa;
	}
}
