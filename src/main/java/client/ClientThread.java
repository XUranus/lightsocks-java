package client;

import socketThread.DecryptForward;
import socketThread.EncryptForward;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {

    private InputStream localIn;
    private OutputStream localOut;
    private InputStream hostIn;
    private OutputStream hostOut;

    private Socket hostSocket;
    private Socket localSocket;

    private Client mainThread;

    public ClientThread(String hostAddr, int hostPort, Socket localSocket, Client mainThread) {
        try {
            this.localSocket = localSocket;
            this.hostSocket = new Socket(hostAddr,hostPort);
            this.localIn = localSocket.getInputStream();
            this.localOut = localSocket.getOutputStream();
            this.hostIn = hostSocket.getInputStream();
            this.hostOut = hostSocket.getOutputStream();
            this.mainThread = mainThread;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {

            hostSocket.setSoTimeout(180000);
            localSocket.setSoTimeout(180000);

            hostSocket.setKeepAlive(true);
            localSocket.setKeepAlive(true);

            new EncryptForward(localIn,hostOut, Client.crypto).start();
            new DecryptForward(hostIn,localOut, Client.crypto).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

