package com.example.banco.pessoa;

import com.example.banco.util.BdUtil;

import java.util.Scanner;

public class Administrador extends Pesssoa {
    public static void menuAdmin() {
        Scanner scan = new Scanner(System.in);
        int op;

        do {
            System.out.println();
            System.out.println("Menu:");
            System.out.println("1- Listar clientes");
            System.out.println("0- Sair");
            op = scan.nextInt();

            switch (op) {
                case 1:
                    BdUtil.displayClientes();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Opção selecionada inválida");
            }
        } while (op != 0);
    }
}
