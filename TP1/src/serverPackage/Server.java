package serverPackage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server waiting for a client connection...");
        Socket socket = serverSocket.accept();
        System.out.println("A client is connected");

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        int nb = dis.readInt();
        System.out.println("Server received: " + nb);

        int x = nb * 5;
        System.out.println("Server computed: " + nb + " * 5 = " + x);

        dos.writeInt(x);
        System.out.println("Result sent to client: " + x);

        dis.close();
        dos.close();
        socket.close();
        serverSocket.close();
    }
}