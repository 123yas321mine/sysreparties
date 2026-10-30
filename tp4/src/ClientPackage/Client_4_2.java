package ClientPackage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client_4_2 {
    private static final String HOTE_SERVEUR = "localhost";
    private static final int PORT_SERVEUR = 1234;

    public static void main(String[] args) throws Exception {
        Scanner clavier = new Scanner(System.in);
        DatagramSocket socket = new DatagramSocket();
        InetAddress adresseServeur = InetAddress.getByName(HOTE_SERVEUR);

        System.out.print("Entrez votre nom d'utilisateur : ");
        String nom = clavier.nextLine().trim();

        // Thread de réception : tourne en parallèle de la saisie clavier
        Thread reception = new Thread(() -> {
            byte[] buffer = new byte[1024];
            try {
                while (!socket.isClosed()) {
                    DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paquet);
                    String msg = new String(paquet.getData(), 0, paquet.getLength(),
                            StandardCharsets.UTF_8);
                    System.out.println(msg);
                }
            } catch (Exception e) {
                if (!socket.isClosed()) {
                    System.err.println("[CLIENT] Erreur réception : " + e.getMessage());
                }
            }
        });
        reception.setDaemon(true);
        reception.start();

        // Premier message : enregistre le client auprès du serveur
        envoyer(socket, adresseServeur, "[" + nom + "] : s'est connecté");
        System.out.println("Tapez vos messages ('exit' pour quitter).");

        while (true) {
            String texte = clavier.nextLine();
            if (texte.equalsIgnoreCase("exit")) {
                break;
            }
            envoyer(socket, adresseServeur, "[" + nom + "] : " + texte);
        }

        socket.close(); // débloque aussi le receive() du thread
        clavier.close();
        System.out.println("Client fermé.");
    }

    private static void envoyer(DatagramSocket socket, InetAddress adresse, String message)
            throws Exception {
        byte[] donnees = message.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(donnees, donnees.length, adresse, PORT_SERVEUR));
    }
}