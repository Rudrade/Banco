package com.example.banco;

import java.util.Scanner;

import pessoa.Cliente;

public class App {
    public static void main (String[] args) {
    	Scanner scan = new Scanner(System.in);
    	int op;
    	
    	do {
    		System.out.println();
    		System.out.println("Menu:");
    		System.out.println("1- Login");
    		System.out.println("2- Registo");
    		System.out.println("0- Sair");
    		op = scan.nextInt();
    		
    		switch (op) {
    			case 1:
    				break;
    			case 2:
    				Cliente cliente = new Cliente();
    				cliente.registarCliente();
    				break;
    			case 0:
    				System.exit(0);
    			default:
    				System.out.println("Opção selecionada é inválida");
    		}    		
    	} while (op != 0);
    }
}