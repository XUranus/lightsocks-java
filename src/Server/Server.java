package Server;

import Crypto.AESCryptor;
import Crypto.Cryptor;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private String password;
    private ServerSocket serverSocket;
    public static Cryptor cryptor;

    public Server(int port,String password) {
        this.port = port;
        this.password = password;
        cryptor = new AESCryptor(password);
        System.out.println("Server created");
    }

    public void listen() {
       try {
           System.out.println("Server Listening...");
           serverSocket = new ServerSocket(port);
           while(true) {
               System.out.println("Server catch a socket");
               Socket socket = serverSocket.accept();
               new ServerThread(socket).start();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
}
