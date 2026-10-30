package clientPackage;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) throws IOException {
        InetAddress ip = InetAddress.getByName("192.168.1.10"); // IP de la machine SERVEUR
        int port = 1234;

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port));
        System.out.println("Connecté au serveur " + ip.getHostAddress() + ":" + port);

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Addition");
            System.out.println("2. Soustraction");
            System.out.println("3. Multiplication");
            System.out.println("4. Division");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            int choix = sc.nextInt();

            if (choix == 0) {
                dos.writeInt(0);
                break;
            }
            if (choix < 1 || choix > 4) {
                System.out.println("Choix invalide");
                continue;
            }

            System.out.print("x = ");
            double x = sc.nextDouble();
            System.out.print("y = ");
            double y = sc.nextDouble();

            dos.writeInt(choix);
            dos.writeDouble(x);
            dos.writeDouble(y);

            boolean ok = dis.readBoolean();
            if (ok) {
                System.out.println("Résultat : " + dis.readDouble());
            } else {
                System.out.println("Erreur : " + dis.readUTF());
            }
        }

        dis.close();
        dos.close();
        socket.close();
        sc.close();
    }
}