package ClientPackage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * ETAPE 2 - CLIENT UDP (envoi uniquement)
 * Demande un nom d'utilisateur, puis envoie chaque ligne saisie au serveur
 * sous la forme "[NomUtilisateur] : message".
 */
public class Client_4_1 {

    private static final String HOTE_SERVEUR = "localhost"; // ou IP du serveur
    private static final int PORT_SERVEUR = 1234;

    public static void main(String[] args) {

        // Les ressources sont déclarées AVANT le try (et initialisées à null) :
        // ainsi elles restent visibles dans le bloc finally pour être fermées.
        Scanner clavier = null;
        DatagramSocket socket = null;

        try {
            // Scanner sur System.in pour lire le clavier
            clavier = new Scanner(System.in);

            // Constructeur sans argument : le système choisit un port LOCAL libre
            // (éphémère). Le client n'a pas besoin d'un port fixe.
            socket = new DatagramSocket();

            // Résolution du nom d'hôte en adresse IP (faite une seule fois)
            InetAddress adresseServeur = InetAddress.getByName(HOTE_SERVEUR);

            System.out.print("Entrez votre nom d'utilisateur : ");
            String nom = clavier.nextLine().trim();
            System.out.println("Tapez vos messages ('exit' pour quitter).");

            while (true) {
                String texte = clavier.nextLine();
                if (texte.equalsIgnoreCase("exit")) {
                    break; // sortie de la boucle -> passage au finally
                }

                // Préfixe imposé par l'énoncé : [Nom] : message
                String message = "[" + nom + "] : " + texte;

                // UDP n'envoie que des octets : on convertit la chaîne (UTF-8 pour
                // gérer correctement les accents)
                byte[] donnees = message.getBytes(StandardCharsets.UTF_8);

                // Un datagramme d'envoi contient : données + longueur + IP + port
                // de destination (pas de connexion : chaque paquet est autonome).
                DatagramPacket paquet = new DatagramPacket(
                        donnees, donnees.length, adresseServeur, PORT_SERVEUR);

                // send() n'est pas bloquant et ne garantit PAS la livraison (UDP)
                socket.send(paquet);
            }
            System.out.println("Client fermé.");

        } catch (Exception e) {
            System.err.println("[CLIENT] Erreur : " + e.getMessage());

        } finally {
            // Le finally s'exécute TOUJOURS (sortie normale ou erreur) :
            // c'est ici qu'on libère les ressources manuellement.
            // Le test != null évite une NullPointerException si la création
            // du Scanner ou du socket a échoué dans le try.
            if (socket != null) {
                socket.close();
            }
            if (clavier != null) {
                clavier.close();
            }
        }
    }
}
//ETAPE3