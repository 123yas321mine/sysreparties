package clientPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelPackage.Operation;

/**
 * TP6 : client pour le serveur multi-thread (communication par objets).
 * Utilisation : sans argument = localhost ; sinon 1er argument = IP du serveur.
 */
public class Client_6_1 {

    static final int PORT = 1234;

    // nombre, operateur (+ - * /), nombre ; espaces facultatifs
    static final Pattern SYNTAXE = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");

    public static void main(String[] args) {
        String adresse = args.length > 0 ? args[0] : "localhost";
        try {
            System.out.println("Je suis un client pas encore connecté…");
            InetAddress ip = InetAddress.getByName(adresse);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, PORT), 5000);
            System.out.println("je suis un client connecté à " + ip.getHostAddress() + ":" + PORT);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner sc = new Scanner(System.in);

            // lire le numero d'ordre envoye par le serveur (1er objet)
            System.out.println((String) ois.readObject());

            while (true) {
                System.out.print("Donnez une opération (ex : 55 * 25) ou quit : ");
                String saisie = sc.nextLine().trim();
                if (saisie.equals("quit")) {
                    break;
                }

                Matcher m = SYNTAXE.matcher(saisie);
                if (!m.matches()) {
                    System.out.println("Syntaxe invalide, rien n'est envoyé.");
                    continue;
                }

                Operation op = new Operation(
                        Double.parseDouble(m.group(1)),
                        m.group(2).charAt(0),
                        Double.parseDouble(m.group(3)));
                oos.writeObject(op);
                oos.flush();

                String reponse = (String) ois.readObject();
                System.out.println("Résultat : " + reponse);
            }

            oos.writeObject("quit"); // prevenir le serveur
            oos.flush();
            System.out.println("Fin de la communication");
            sc.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}