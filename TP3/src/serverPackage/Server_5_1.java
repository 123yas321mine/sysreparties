package serverPackage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import modelPackage.Operation;

/**
 * TP5 : serveur pour UN client, communication par objets.
 */
public class Server_5_1 {

    static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Je suis un serveur en attente la connexion d'un client ");
            Socket socket = serverSocket.accept();
            System.out.println("un client est connecté : " + socket.getRemoteSocketAddress());

            // IMPORTANT : creer ObjectOutputStream d'abord, puis flush(), puis ObjectInputStream
            // (sinon les deux cotes attendent l'en-tete de l'autre : blocage)
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            try {
                while (true) {
                    // c) le serveur recoit l'objet
                    Object recu = ois.readObject();
                    if (!(recu instanceof Operation)) {
                        break; // "quit"
                    }
                    Operation op = (Operation) recu;

                    // d) il recupere les donnees de l'objet
                    System.out.println("Opération reçue : " + op);

                    // e) il effectue le calcul
                    String reponse = calculer(op);

                    // f) il renvoie le resultat
                    oos.writeObject(reponse);
                    oos.flush();
                    System.out.println("Réponse envoyée : " + reponse);
                }
            } catch (EOFException e) {
                System.out.println("Le client s'est déconnecté brutalement");
            }

            System.out.println("Fin de la communication");
            socket.close();
            serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }

    static String calculer(Operation op) {
        double a = op.getOperande1();
        double b = op.getOperande2();
        double r;
        switch (op.getOperateur()) {
            case '+':
                r = a + b;
                break;
            case '-':
                r = a - b;
                break;
            case '*':
                r = a * b;
                break;
            case '/':
                if (b == 0) {
                    return "Erreur : division par zéro impossible";
                }
                r = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu";
        }
        return formater(a) + " " + op.getOperateur() + " " + formater(b) + " = " + formater(r);
    }

    // 1375.0 s'affiche 1375 ; 3.5 reste 3.5
    static String formater(double v) {
        if (v == Math.rint(v) && !Double.isInfinite(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }
}