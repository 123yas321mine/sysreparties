package serverPackage;

import java.io.*;
import java.net.*;

public class Server2 {
    public static void main(String[] args) throws IOException {
        InetAddress ip = InetAddress.getByName("192.168.1.10"); // IP réelle du serveur
        int port = 1234;

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(ip, port));
        System.out.println("Serveur en écoute sur " + ip.getHostAddress() + ":" + port);

        Socket socket = serverSocket.accept();
        System.out.println("Client connecté : " + socket.getInetAddress().getHostAddress());

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        int choix;
        while ((choix = dis.readInt()) != 0) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            System.out.println("Reçu : choix=" + choix + ", x=" + x + ", y=" + y);

            double res = 0;
            boolean ok = true;
            switch (choix) {
                case 1: res = x + y; break;
                case 2: res = x - y; break;
                case 3: res = x * y; break;
                case 4:
                    if (y == 0) ok = false;
                    else res = x / y;
                    break;
                default: ok = false;
            }

            dos.writeBoolean(ok);
            if (ok) {
                dos.writeDouble(res);
                System.out.println("Résultat envoyé : " + res);
            } else {
                dos.writeUTF("Opération impossible (division par zéro ou choix invalide)");
            }
        }

        System.out.println("Client déconnecté");
        dis.close();
        dos.close();
        socket.close();
        serverSocket.close();
    }
}