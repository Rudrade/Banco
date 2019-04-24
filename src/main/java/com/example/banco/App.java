package com.example.banco;

import java.util.Scanner;

import com.example.banco.pessoa.Cliente;
import com.example.banco.util.BdUtil;
import com.example.banco.util.Data;

public class App {
	static {
		BdUtil.getInstance();
		new Data();
	}

    public static void main (String[] args) {
    	Cliente cliente = new Cliente();
    	Scanner scan = new Scanner(System.in);
    	int op;
    	
    	do {
    		System.out.println();
    		Data.printData();
    		System.out.println("Menu:");
    		System.out.println("1- Login com cartão");
    		System.out.println("2- Login com conta");
    		System.out.println("3- Registo cliente");
    		System.out.println("4- Opções data");
    		System.out.println("0- Sair");
    		System.out.print("Opção: ");
    		op = scan.nextInt();
    		
    		switch (op) {
    			case 1:
    				cliente.login();
    				break;
    			case 3:
    				cliente.registarCliente();
    				break;
				case 4:
					Data.menu();
					break;
    			case 0:
    				System.exit(0);
    			default:
    				System.out.println("Opção selecionada é inválida");
    		}    		
    	} while (op != 0);
    	
    	scan.close();
    }
}