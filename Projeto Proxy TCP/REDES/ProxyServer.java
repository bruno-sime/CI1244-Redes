/*
    Um servidor Proxy TCP multithread em Java;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class ProxyServer {
    static final String LOG_FILE = "log.txt";
    public static void main(String[] args) {
        final int DATA_SERVER_PORT = 1616;
        final int PROXY_SERVER_PORT = 1515;
        

        try {

            ServerSocket pSocket = new ServerSocket(PROXY_SERVER_PORT);
            System.out.printf("O servidor de proxy esta rodando na porta %d\n", PROXY_SERVER_PORT);
            writeLog("ProxyServer.java :: O servidor proxy foi alocado na porta 1515.");

            while (true){ 
                Socket cSocket = pSocket.accept();
                writeLog("ProxyServer.java :: Socket de cliente criado.");
                writeLog("ProxyServer.java :: Conexão de cliente recebida e aceita.");
                new ProxyThread(cSocket, DATA_SERVER_PORT).start();
                writeLog("ProxyServer.java :: Thread encaminhada para tratamento.");
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
    
    static class ProxyThread extends Thread{

        private Socket cSocket;
        private int dataServerPort;

        public ProxyThread(Socket cSocket, int dataServerPort){ //construtor;
            this.cSocket = cSocket;
            this.dataServerPort = dataServerPort;
        }

        public void run(){
            try {

                Socket sSocket = new Socket("localhost", dataServerPort); //conecta-se com o servidor de DADOS;

                //Buffers de leitura e escrita;
                BufferedReader cInput = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
                PrintWriter cOutput = new PrintWriter(cSocket . getOutputStream(), true);
                BufferedReader sInput = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
                PrintWriter sOutput = new PrintWriter(sSocket.getOutputStream(), true);
                writeLog("ProxyServer.java :: Buffers alocados");

                String requisicao = cInput.readLine();
                writeLog(String.format("ProxyServer.java :: Recebi a requisicao %s", requisicao));

                sOutput.println(requisicao);
                writeLog("ProxyServer.java :: Requisiçao enviada para o servidor.");

                String resposta = sInput.readLine();
                writeLog(String.format("ProxyServer.java :: Resposta do servidor: %s", resposta));

                cOutput.println(resposta);
                writeLog("ProxyServer.java :: Resposta enviada pelo servidor foi escrita no cliente.");

                cSocket.close();
                sSocket.close();
                writeLog("ProxyServer.java :: Sockets do cliente e do servidor encerrados.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
