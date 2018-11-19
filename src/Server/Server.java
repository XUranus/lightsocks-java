package Server;

import Crypto.Cryptor;
import Util.Util;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    public static Cryptor cryptor;

    public Server(int port) {
        this.port = port;
    }

    public void listen() {
       try {
           Util.log("server start listening...");
           serverSocket = new ServerSocket(port);
           while(true) {
               Socket socket = serverSocket.accept();
               new ServerThread(socket,this).start();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    public static void printHelpInfo() {
        System.out.println("\nUsage:");
        System.out.println("lightsocks-server [configFileName]\n");
    }
}
