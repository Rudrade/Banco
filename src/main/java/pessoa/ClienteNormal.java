package pessoa;

import conta.Conta;

import java.util.Scanner;

//Class cliente normal
public class ClienteNormal extends Cliente{
    public void loginNormal() {
        Scanner scan = new Scanner(System.in);
        int op;

        do {
            System.out.println();
            System.out.println("Menu:");
            System.out.println("1- Listar contas");
            System.out.println("2- Listar cartões");
            System.out.println("3- Alterar dados");
            System.out.println("0- Sair");
            op = scan.nextInt();

            switch (op) {
                case 1:
                    new Conta().displayConta(this);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("A opção selecionada é inválida");
            }
        } while (op != 0);
    }

    public ClienteNormal(int nrCliente) {
        this.setNrCliente(nrCliente);
    }

    public ClienteNormal() {
        super();
    }
}