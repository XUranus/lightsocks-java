package server;


import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class LocalServer {

    private final static Logger logger = LoggerFactory.getLogger(LocalServer.class);

    public static Crypto crypto;

    private final String hostAddr;
    private final int hostPort;
    private final int localPort;

    public LocalServer(String host, int hostPort, int localPort) {
        // remote proxy server
        this.hostAddr = host;
        this.hostPort = hostPort;

        this.localPort = localPort;
    }

    public void listen() {
        try {
            ServerSocket localServerSocket = new ServerSocket(localPort);
            logger.info("local client start listening...");
            while (true) {
                Socket appSocket = localServerSocket.accept();
                logger.info("receive proxy request: 127.0.0.1:" + appSocket.getPort());
                Thread t = new LocalServerThread(hostAddr, hostPort, appSocket);//.start();
                t.start();
                //t.join();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
