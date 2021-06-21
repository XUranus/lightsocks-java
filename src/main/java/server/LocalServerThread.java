package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class LocalServerThread extends Thread {


    private final static Logger logger = LoggerFactory.getLogger(LocalServerThread.class);

    // socket of remote proxy server
    private Socket remoteHostSocket;

    private Socket localAppSocket;


    private final InputStream localAppIn;
    private final OutputStream localAppOut;
    private final InputStream remoteHostIn;
    private final OutputStream remoteHostOut;


    public LocalServerThread(String hostAddr, int hostPort, Socket localAppSocket) throws IOException {
        try {
            this.localAppSocket = localAppSocket;
            this.remoteHostSocket = new Socket(hostAddr, hostPort);
        } catch (IOException e) {
            logger.error("", e);
            this.localAppSocket.close();
            this.remoteHostSocket.close();
        }

        this.localAppIn = localAppSocket.getInputStream();
        this.localAppOut = localAppSocket.getOutputStream();
        this.remoteHostIn = remoteHostSocket.getInputStream();
        this.remoteHostOut = remoteHostSocket.getOutputStream();
    }

    public void run() {
        try {
            remoteHostSocket.setSoTimeout(60 * 60 * 24);
            localAppSocket.setSoTimeout(60 * 60 * 24);

            remoteHostSocket.setKeepAlive(true);
            localAppSocket.setKeepAlive(true);

            Thread encryptThread = new StreamForwardThread(localAppIn, remoteHostOut, LocalServer.crypto, true);
            Thread decryptThread = new StreamForwardThread(remoteHostIn, localAppOut, LocalServer.crypto, false);

            encryptThread.start();
            decryptThread.start();

            encryptThread.join();
            decryptThread.join();

        } catch (Exception e) {
            logger.error("", e);
        }

        // close all stream and socket
        try {
            this.localAppIn.close();
            this.localAppOut.close();
            this.remoteHostIn.close();
            this.remoteHostOut.close();
        } catch (IOException e) {
            logger.error("", e);
        }

    }


}

