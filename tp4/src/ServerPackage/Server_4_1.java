package ServerPackage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class Server_4_1 {
    private static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        byte[] buffer = new byte[1024];

        // Liaison explicite via InetSocketAddress (toutes les interfaces, port 1234)
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(PORT));
        System.out.println("Lancement du serveur sur le port " + PORT);

        while (true) {
            DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
            socket.receive(paquet); // bloquant

            SocketAddress expediteur = paquet.getSocketAddress();
            String message = new String(paquet.getData(), 0, paquet.getLength(),
                    StandardCharsets.UTF_8);

            System.out.println("[" + expediteur + "] " + message);
        }
    }
}
//ETAPE3
