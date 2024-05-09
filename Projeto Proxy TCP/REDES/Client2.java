/*
    Um cliente TCP em Java;
    Escrito por Bruno Sime Ferreira Nunes;
    Última modificação em 07/05/2024;
 */

package REDES;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

public class Client2 {

    public static final Scanner input = new Scanner(System.in);
    static final String LOG_FILE = "log.txt";

    private static int getData(){

        System.out.println("Esta opção utiliza o cliente 2, por fora da proxy, para alterar dados no server.");
        System.out.printf("Insira o inteiro que voce deseja setar no servidor: ");
        int data = input.nextInt();
        return data;

    }

    public static void writeLog(String mensagem){

        try(BufferedWriter logger = new BufferedWriter(new FileWriter(LOG_FILE, true))){
            logger.write(String.format(LocalDateTime.now()+" - "+ mensagem));
            logger.newLine();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args, int option){

        final int DATA_SERVER_PORT = 1616;

        try {

            Socket cTwoSocket = new Socket("localhost",DATA_SERVER_PORT);
            writeLog("Client2.java :: O socket foi alocado.");

            BufferedReader cTwoInput = new BufferedReader(new InputStreamReader(cTwoSocket.getInputStream()));
            PrintWriter cTwoOutput =  new PrintWriter(cTwoSocket.getOutputStream(), true);
            writeLog("Client2.java :: Buffers alocados.");

            switch(option){
                case 3 -> {
                    writeLog("Client2.java :: Gerando numero aletório entre 0 e 100.");
                    Random random = new Random();
                    int data = random.nextInt(101);
                    writeLog(String.format("Client2.java :: Setando o numero aleatório %d no server.", data));
                    cTwoOutput.println(String.format("SET_DATA "+data));
                }

                case 1 -> {
                    int data = getData();
                    writeLog(String.format("Client2.java :: Setando o numero %d, enviado pelo usuário, no server.", data));
                    cTwoOutput.println(String.format("SET_DATA "+data));
                }
            }
            cTwoSocket.close();
            writeLog("Client2.java :: Socket do Cliente 2 fechado.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
