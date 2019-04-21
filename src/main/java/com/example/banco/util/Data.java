package com.example.banco.util;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;

public class Data {
    private static Date dataAtual;

    static {
        dataAtual = criarData();
    }

    public static void printData() {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(format.format(getDataAtual()));
    }

    public static Date getDataAtual() {
        return dataAtual;
    }

    private static Date criarData() {
        try {
            ResultSet resultSet = BdUtil.select("SELECT currentDate FROM data LIMIT 1;");

            while (resultSet.next()) {
                return resultSet.getDate("currentDate");
            }
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            System.exit(0);
        }
        return null;
    }

    private static void insertDataAtual(String data) {
        try {
            BdUtil.execute("UPDATE data SET currentDate = '" + data + "';");
            System.out.println("Tempo avançado com sucesso");
        } catch (SQLException e) {
            System.out.printf("Ocorreu um erro: %s\n", e.getMessage());
            return;
        }
    }

    private static void reset() {

    }

    private static void avancar() {
            Calendar calendar = Calendar.getInstance();
            Scanner scanner = new Scanner(System.in);
            int dias;

            System.out.print("Tempo a avançar (dias): ");
            dias = scanner.nextInt();

            calendar.setTime(dataAtual);
            calendar.add(Calendar.DATE, dias);
            dataAtual = calendar.getTime();

            DateFormat outputFormatter = new SimpleDateFormat("yyyy/MM/dd");
            String output = outputFormatter.format(dataAtual);
            insertDataAtual(output);
    }

    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        int op;

        while (true) {
            System.out.println();
            System.out.println("1- Avançar");
            System.out.println("2- Reset");
            System.out.println("0- Voltar ao menu anterior");
            System.out.print("Opção: ");
            op = scanner.nextInt();

            switch (op) {
                case 1:
                    avancar();
                    break;
                case  2:
                    reset();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }
}
