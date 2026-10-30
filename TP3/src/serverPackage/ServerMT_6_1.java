package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TP6 : serveur multi-thread de calculatrice (communication par objets).
 * Le thread principal ne fait qu'accepter ; un ClientProcess_6_1 par client.
 */
public class ServerMT_6_1 {

    static final int PORT = 1234;

    public static void main(String[] args) {
        int numero = 0; // numero d'ordre de connexion
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Je suis un serveur en attente la connexion d'un client ");

            while (true) {
                Socket socket = serverSocket.accept(); // bloque jusqu'au prochain client
                numero++;
                System.out.println("Client n°" + numero + " connecté, adresse : "
                        + socket.getRemoteSocketAddress());
                new ClientProcess_6_1(socket, numero).start();
            }
        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }
}