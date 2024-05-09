/*
    Um cliente TCP em Java;
    Escrito por Bruno Sime Ferreira Nunes;
    Última modificação em 07/05/2024;
 */

package REDES;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client1 {

    public static void main(String[] args){

        final int PROXY_SERVER_PORT = 1515;

        try {

            Socket cOneSocket = new Socket("localhost", PROXY_SERVER_PORT);

            BufferedReader cOneInput = new BufferedReader(new InputStreamReader(cOneSocket.getInputStream())); //buffer do cliente da proxy de envio
            PrintWriter cOneOutput =  new PrintWriter(cOneSocket.getOutputStream(), true); //buffer de recebimento do cliente da proxy

            cOneOutput.println("GET_DATA");

            String resposta = cOneInput.readLine();
            
            System.out.println("Valor obtivo via proxy: " + resposta);
        
            cOneSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
