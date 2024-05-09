/*
    Uma main que facilita a interação com os servidores e clientes do trabalho de Redes;
    Escrito por Bruno Sime Ferreira Nunes (GRR20190568);
 */

import REDES.Client1;
import REDES.Client2;
import REDES.DataServer;
import REDES.ProxyServer;
import static java.lang.Thread.sleep;
import java.util.Scanner;

public class Main {

    public static Scanner input = new Scanner(System.in);
    private static void menu(){ // implementa um menu que permite ao usuário interagir com o codigo;
        
        System.out.print("\033\143");
        System.out.printf("#######################################################################################\n");
        System.out.printf("#                        MENU INTERATIVO DO TRABALHO DE REDES                         #\n");
        System.out.printf("#    ESCOLHA UMA OPÇÃO:                                                               #\n");
        System.out.printf("#                                                                                     #\n");
        System.out.printf("#        1 - Setar um valor INTEIRO no servidor (por fora da proxy);                  #\n");
        System.out.printf("#        2 - Resgatar um valor INTEIRO no servidor (utilizando a proxy);              #\n");
        System.out.printf("#        3 - Setar um valor INTEIRO ALEATORIO no servidor (por fora da proxy);        #\n");
        System.out.printf("#        0 - Terminei meus testes, terminar a execução;                               #\n");
        System.out.printf("#                                                                                     #\n");
        System.out.printf("#######################################################################################\n");
        
    }


    public static void main(String[] args) throws InterruptedException {

        Thread dServer =  new Thread(()->DataServer.main(args)); //alocamos uma nova thread para receber o nosso servidor de DADOS;
        dServer.start(); // Servidor de dados iniciado;
        System.out.printf("Servidor de dados inicializado pela MAIN;\n");
        Thread pServer = new Thread(()->ProxyServer.main(args)); //alocamos uma nova thread para receber o nosso servidor de PROXY;
        pServer.start(); // Proxy iniciada;
        System.out.printf("Servidor de Proxy inicializado pela MAIN;\n");
        sleep(3000);
        int opcao;
        do{
            menu();
            opcao = input.nextInt();
            input.nextLine();
            switch(opcao){
                case 1 -> Client2.main(args, opcao);
                case 2 -> {
                    Client1.main(args);
                    System.out.printf("Aguarde 5 segundos...");
                    sleep(5000);
                }
                case 3 -> Client2.main(args, opcao);
            }

        }while(opcao != 0);
        System.out.printf("Encerrando a execução!\n");
        pServer.interrupt();
        System.out.printf("Servidor Proxy interrompido\n");
        dServer.interrupt();
        System.out.printf("Servidor Dados interrompido\n");
    }
}
