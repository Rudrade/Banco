package pessoa;

import java.util.Scanner;

import util.BdUtil;

//Class Cliente
public class Cliente extends Pesssoa {
	private String password;
	private String tipoCliente;

	//Metodo para um cliente fazer login
	public static void login() {
		Scanner scan = new Scanner(System.in);
		int nrCliente;
		String password;

		while (true) {
			System.out.println();
			System.out.println("Menu - Login");
			System.out.print("Número cliente: ");
			nrCliente = scan.nextInt();

			System.out.print("Password: ");
			password = scan.next();

			switch(BdUtil.login(nrCliente, password)) {
                case "normal":
                    break;
                case "vip":
                    break;
                case "administrador":
                    Administrador.menuAdmin();
                    break;
            }
			break;
		}
	}

	//Metodo para registar um novo cliente
	//Este metodo tambem insere os dados do cliente na base de dados
	public static void registarCliente() {
		Scanner scan = new Scanner(System.in);
		int tipoCliente;
		Cliente cliente = new Cliente();
		
		TIPO: do {
			System.out.println();
			System.out.println("Menu - Registo cliente:");
			System.out.println("1- Normal");
			System.out.println("2- VIP");
			System.out.println("0- Cancelar");
			tipoCliente = scan.nextInt();
		
			switch (tipoCliente) {
				case 1:
					cliente.setTipoCliente("normal");
					break TIPO;
				case 2:
					cliente.setTipoCliente("vip");
					break TIPO;
				case 0:
					return;
				default:
					System.out.println("A opção selecionada não existe");
			}
		} while(tipoCliente != 0);
		
		System.out.println();
		System.out.print("Nome: ");
		cliente.setNome(scan.next());
		
		System.out.print("Morada: ");
		cliente.setMorada(scan.next());
		
		do {
			System.out.print("Telefone: ");
		} while (!cliente.setTelefone(scan.nextInt()));
		
		do {
			System.out.print("Email: ");
		} while (!cliente.setEmail(scan.next()));
		
		System.out.print("Profissão: ");
		cliente.setProfissao(scan.next());
		
		do {
			System.out.print("Password: ");
		} while (!cliente.setPassword(scan.next()));
		
		BdUtil.registarCliente(cliente);
	}
	
	boolean setPassword(String password) {
		if (password.length() >= 8) {
			this.password = password;
			return true;
		}
		else {
			System.out.println("Password inválida.");
			return false;
		}
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getTipoCliente() {
		return this.tipoCliente;
	}
	
	void setTipoCliente(String tipoCliente) {
		if (tipoCliente.equals("vip") || tipoCliente.equals("normal")) {
			this.tipoCliente = tipoCliente;
		}
	}
}