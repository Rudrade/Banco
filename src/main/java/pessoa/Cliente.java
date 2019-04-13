package pessoa;

import java.util.Scanner;

import util.BdUtil;

//Class Cliente
public class Cliente extends Pesssoa {
	private String password;
	private String tipoCliente;
	
	//Metodo para registar um novo cliente
	//Neste metodo apenas selecionada o tipo de cliente a registar e conforme a escolha chama o registo do respetivo cliente.
	public void registarCliente() {
		Scanner scan = new Scanner(System.in);
		int tipoCliente;
		
		do {
			System.out.println();
			System.out.println("Menu - Registo cliente:");
			System.out.println("1- Normal");
			System.out.println("2- VIP");
			System.out.println("0- Cancelar");
			tipoCliente = scan.nextInt();
		
			switch (tipoCliente) {
				case 1:
					ClienteNormal cliente = new ClienteNormal();
					cliente.registoClienteNormal();					
					BdUtil.registarCliente(cliente);
					break;
				case 2:
					break;
				case 0:
					return;
				default:
					System.out.println("A opção selecionada não existe");
			}
		} while(tipoCliente != 0);
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