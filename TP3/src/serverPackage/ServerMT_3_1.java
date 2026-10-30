package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TP3-1 : serveur multi-thread.
 * Le thread principal ne fait qu'accepter les clients ;
 * le dialogue est confie a un ClientProcess_3_1 par client.
 */
public class ServerMT_3_1 {

    static final int PORT = 1234;

    public static void main(String[] args) {
        int numero = 0; // numero d'ordre de connexion
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Je suis un serveur en attente la connexion d'un client ");

            while (true) {
                Socket socket = serverSocket.accept(); // bloque jusqu'au prochain client
                numero++;

                // b) afficher l'adresse IP (et le port) + le numero d'ordre du client
                System.out.println("Client n°" + numero + " connecté, adresse : "
                        + socket.getRemoteSocketAddress());

                // un thread dedie par client, avec SA socket et SON numero
                new ClientProcess_3_1(socket, numero).start();
            }
            // pas de serverSocket.close() : la boucle est infinie
        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }
}