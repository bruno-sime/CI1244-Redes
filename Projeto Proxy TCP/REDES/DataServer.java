/*
    Um servidor de dados TCP multithread em Java;
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
import java.net.*;
import java.time.LocalDateTime;

public class DataServer {

    private static int dado = 0;
    static final String LOG_FILE = "log.txt";

    public static void main(String[] args) {
        final int PORTA = 1616;//porta do servidor de dados;

        try {

            ServerSocket sSocket = new ServerSocket(PORTA); //aloca o socket que sera usado para o servidor escutar e receber as requisiçoes;
            System.out.printf("Servidor de DADOS escutando a porta %d\n", PORTA);
            writeLog("DataServer.java :: O servidor de dados foi alocado na porta 1616.");

            while(true){ // o servidor fica eternamente escutando a porta e aguardando requisiçoes;
                Socket cSocket = sSocket.accept(); //aloca um novo socket para processar este cliente;
                writeLog("DataServer.java :: Socket de cliente criado.");
                writeLog("DataServer.java :: Conexão de cliente recebida e aceita.");
                new ThreadServer(cSocket).start(); //designa este socket para uma nova thread;
                writeLog("DataServer.java :: Thread encaminhada para tratamento.");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLog(String mensagem){

        try(BufferedWriter logger = new BufferedWriter(new FileWriter(LOG_FILE, true))){
            logger.write(String.format(LocalDateTime.now()+" - "+ mensagem));
            logger.newLine();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    static class ThreadServer extends Thread{
        private Socket cSocket;
        public ThreadServer(Socket cSocket){
            this.cSocket = cSocket;
        }
        public void run(){
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
                PrintWriter output = new PrintWriter(cSocket.getOutputStream(), true);
                writeLog("DataServer.java :: Buffers alocados");


                String requisicao = input.readLine();
                writeLog(String.format("DataServer.java :: Recebi a requisicao %s", requisicao));
                String[] partes = requisicao.split(" ");
                String comando = partes[0]; // Aqui retiramos o comando que vamos utilizar na operação abaixo;

                //bloco de processamento da solicitaçao dos clientes;
                if(comando.equals("GET_DATA")){ //para o requisitar o dado;
                    if(cSocket.getInetAddress().getHostName().equals("localhost")){
                        output.println(dado);
                        writeLog("DataServer.java:: Valor retornado ao cliente.");
                    }else{
                        output.println("Acesso não autorizado!");
                    }
                } else if(comando.equals("SET_DATA")){ //o cliente2 cai aqui, para escrever no servidor por fora da proxy;
                    if(cSocket.getInetAddress().getHostName().equals("localhost")){ //verifica se o cliente que quer escrever pertence ao dominio localhost
                        dado = Integer.parseInt(partes[1]); //separa a parte da string que contem o dado e atribui à variavel;
                        output.println("Dado atualizado com sucesso!");
                        writeLog("DataServer.java:: Dado atualizado com sucesso!");

                    } else{
                        output.println("Acesso não autorizado!");
                    }
                } else {
                    output.println("Comando inválido!");
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}