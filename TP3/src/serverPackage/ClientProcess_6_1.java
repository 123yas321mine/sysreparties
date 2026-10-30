package serverPackage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import modelPackage.Operation;

/**
 * Un thread = un client. Dialogue par objets + compteur global synchronise.
 */
public class ClientProcess_6_1 extends Thread {

    private Socket socket;
    private int numero;

    public ClientProcess_6_1(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {
        try {
            // ObjectOutputStream d'abord + flush, puis ObjectInputStream
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // envoyer a chaque client son numero d'ordre (1er objet recu par le client)
            oos.writeObject("Bienvenue, vous êtes le client n°" + numero);
            oos.flush();

            try {
                while (true) {
                    Object recu = ois.readObject();
                    if (!(recu instanceof Operation)) {
                        break; // "quit"
                    }
                    Operation op = (Operation) recu;
                    System.out.println("[client n°" + numero + "] Opération reçue : " + op);

                    String reponse = calculer(op);

                    // acces concurrent : incremente le compteur global et affiche sa valeur
                    Compteur.incrementer();

                    oos.writeObject(reponse);
                    oos.flush();
                    System.out.println("[client n°" + numero + "] Réponse envoyée : " + reponse);
                }
            } catch (EOFException e) {
                System.out.println("[client n°" + numero + "] déconnecté brutalement");
            }

            System.out.println("Client n°" + numero + " déconnecté (fin de la communication)");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur client n°" + numero + " : " + e.getMessage());
        } finally {
            try {
                socket.close(); // chaque thread ferme SA socket (jamais la ServerSocket)
            } catch (IOException e) {
                // rien a faire
            }
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