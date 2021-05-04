package client;


import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    private final static Logger logger = LoggerFactory.getLogger(Client.class);

    private String hostAddr;
    private int hostPort;
    private int localPort;

    private ServerSocket localSocket;
    public static Crypto crypto;

    public Client(String host, int hostPort, int localPort) {
        this.hostAddr = host;
        this.hostPort = hostPort;
        this.localPort = localPort;
        localSocket = null;
    }


    public void listen() {
        try {
            localSocket = new ServerSocket(localPort);
            logger.info("local start listening...");
            while(true) {
                //Util.log("Local catch a socket");
                Socket appSocket = localSocket.accept();
                new ClientThread(hostAddr,hostPort,appSocket,this).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}