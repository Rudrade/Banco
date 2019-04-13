package pessoa;

import java.util.Scanner;

//Class cliente normal
public class ClienteNormal extends Cliente{
    public static void loginNormal() {
        Scanner scan = new Scanner(System.in);
        int op;

        do {
            System.out.println();
            System.out.println("Menu:");
            System.out.println("1- ");
            System.out.println("0- Sair");
            op = scan.nextInt();
        } while (op != 0);
    }
}