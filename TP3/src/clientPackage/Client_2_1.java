package clientPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TP2-1 : client qui envoie une operation complete (ex : 55 * 25).
 * Utilisation : sans argument = localhost ; sinon 1er argument = IP du serveur.
 */
public class Client_2_1 {

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
            socket.connect(new InetSocketAddress(ip, PORT), 5000); // delai max 5 s
            System.out.println("je suis un client connecté à " + ip.getHostAddress() + ":" + PORT);

            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("Donnez une opération (ex : 55 * 25) ou quit : ");
                String saisie = sc.nextLine().trim();
                if (saisie.equals("quit")) {
                    break;
                }

                // Validation de la syntaxe AVANT l'envoi
                Matcher m = SYNTAXE.matcher(saisie);
                if (!m.matches()) {
                    System.out.println("Syntaxe invalide, rien n'est envoyé.");
                    continue;
                }

                // a) envoi des trois elements, un par ligne
                pw.println(m.group(1)); // premier operande
                pw.println(m.group(2)); // operateur
                pw.println(m.group(3)); // deuxieme operande

                // d) lecture et affichage du resultat
                String reponse = br.readLine();
                if (reponse == null) { // le serveur s'est arrete
                    System.out.println("Le serveur a fermé la connexion.");
                    break;
                }
                System.out.println("Résultat : " + reponse);
            }

            pw.println("quit"); // prevenir le serveur
            System.out.println("Fin de la communication");
            sc.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}