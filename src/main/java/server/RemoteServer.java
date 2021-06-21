package server;


import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class RemoteServer {

    private final static Logger logger = LoggerFactory.getLogger(RemoteServer.class);

    public static Crypto crypto;

    private final int port;

    public RemoteServer(int port) {
        this.port = port;
    }

    public void listen() {
        try {
            logger.info("remote server start listening...");
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                new RemoteServerThread(socket).start();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
