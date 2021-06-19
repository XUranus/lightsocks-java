package server;


import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import socketThread.EncryptForward;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    private final int port;
    public static Crypto crypto;

    public Server(int port) {
        this.port = port;
    }

    public void listen() {
       try {
           logger.info("server start listening...");
           ServerSocket serverSocket = new ServerSocket(port);
           while(true) {
               Socket socket = serverSocket.accept();
               new ServerThread(socket,this).start();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

}
