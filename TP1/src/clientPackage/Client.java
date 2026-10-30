package clientPackage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("Client not connected yet");
        Socket socket = new Socket("localhost", 1234);
        System.out.println("Client connected");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an integer:");
        int x = scanner.nextInt();

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        dos.writeInt(x);
        System.out.println("Number sent to server: " + x);

        int xx = dis.readInt();
        System.out.println("Result received from server: " + xx);

        dis.close();
        dos.close();
        socket.close();
        scanner.close();
    }
}
