package serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TP2-1 : serveur pour UN seul client.
 * Le client envoie 3 lignes (operande 1, operateur, operande 2),
 * le serveur calcule et renvoie le resultat.
 */
public class Server_2_1 {

    static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Je suis un serveur en attente la connexion d'un client ");
            Socket socket = serverSocket.accept();
            System.out.println("un client est connecté : " + socket.getRemoteSocketAddress());

            // Flux de communication (octets) puis flux de traitement (texte)
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true); // true = envoi automatique a chaque println

            // a) Le client envoie 3 lignes : operande 1, operateur, operande 2
            String operande1 = br.readLine();
            while (operande1 != null && !operande1.equals("quit")) {
                String operateur = br.readLine();
                String operande2 = br.readLine();
                if (operateur == null || operande2 == null) {
                    break; // le client s'est deconnecte au milieu d'une operation
                }
                System.out.println("Opération reçue : " + operande1 + " " + operateur + " " + operande2);

                // b) Le serveur calcule
                String reponse;
                try {
                    reponse = calculer(Double.parseDouble(operande1), operateur,
                            Double.parseDouble(operande2));
                } catch (NumberFormatException e) {
                    reponse = "Erreur : opérande invalide";
                }

                // c) Le serveur envoie le resultat
                pw.println(reponse);
                System.out.println("Réponse envoyée : " + reponse);

                operande1 = br.readLine(); // operation suivante, ou "quit"
            }

            System.out.println("Fin de la communication");
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }

    // Effectue l'operation et renvoie le texte a afficher chez le client
    static String calculer(double a, String operateur, double b) {
        double r;
        switch (operateur) {
            case "+":
                r = a + b;
                break;
            case "-":
                r = a - b;
                break;
            case "*":
                r = a * b;
                break;
            case "/":
                if (b == 0) {
                    return "Erreur : division par zéro impossible";
                }
                r = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu";
        }
        return formater(a) + " " + operateur + " " + formater(b) + " = " + formater(r);
    }

    // 1375.0 s'affiche 1375 ; 3.5 reste 3.5
    static String formater(double v) {
        if (v == Math.rint(v) && !Double.isInfinite(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }
}