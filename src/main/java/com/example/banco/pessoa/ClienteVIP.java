package com.example.banco.pessoa;

import com.example.banco.util.BdUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//Class clienteVIP
public class ClienteVIP extends Cliente{
    Scanner scanner = new Scanner(System.in);
    int nrGestor;

    ClienteVIP() {
        this.setTipoCliente("vip");
        do {
            System.out.println();
            System.out.print("Gestor de conta: ");
        } while (!this.setNrGestor(scanner.nextInt()));
    }

    boolean setNrGestor(int nrGestor) {
        try {
            ResultSet resultSet = BdUtil.select("SELECT nrGestor FROM gestor WHERE nrGestor =" + nrGestor + ";");

            while (resultSet.next()) {
                this.nrGestor = nrGestor;
                return true;
            }

            System.out.println("Gestor de conta inexistente");
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return false;
        }
        return  false;
    }

    public int getNrGestor() {
        return this.nrGestor;
    }
}
