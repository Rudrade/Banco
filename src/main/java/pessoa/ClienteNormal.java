package pessoa;

import java.util.Scanner;

//Class cliente normal
public class ClienteNormal extends Cliente{
	public ClienteNormal() {
		this.setTipoCliente("normal");
	}
	
	//Metodo para registar um cliente normal e inserir-lo na base de dados
	public ClienteNormal registoClienteNormal()  {
		Scanner scan = new Scanner(System.in);
		
		System.out.println();
		System.out.print("Nome: ");
		this.setNome(scan.next());
		
		System.out.print("Morada: ");
		this.setMorada(scan.next());
		
		do {
			System.out.print("Telefone: ");
		} while (!this.setTelefone(scan.nextInt()));
		
		do {
			System.out.print("Email: ");
		} while (!this.setEmail(scan.next()));
		
		System.out.print("Profissão: ");
		this.setProfissao(scan.next());
		
		do {
			System.out.print("Password: ");
		} while (!this.setPassword(scan.next()));
			
		return this;
	}
}