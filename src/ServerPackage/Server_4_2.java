package ServerPackage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

public class Server_4_2 {
    private static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        byte[] buffer = new byte[1024];
        Set<SocketAddress> clients = new LinkedHashSet<>();

        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(PORT));
        System.out.println("Lancement du serveur sur le port " + PORT);

        while (true) {
            DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
            socket.receive(paquet);

            SocketAddress expediteur = paquet.getSocketAddress();
            String message = new String(paquet.getData(), 0, paquet.getLength(),
                    StandardCharsets.UTF_8);
            System.out.println("[" + expediteur + "] " + message);

            // Enregistre l'expéditeur s'il n'est pas déjà connu
            clients.add(expediteur);

            // Diffusion à tous les autres clients
            byte[] donnees = message.getBytes(StandardCharsets.UTF_8);
            for (SocketAddress client : clients) {
                if (!client.equals(expediteur)) {
                    socket.send(new DatagramPacket(donnees, donnees.length, client));
                }
            }
        }
    }
}