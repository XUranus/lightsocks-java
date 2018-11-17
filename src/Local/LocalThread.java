package Local;

import SocketThread.DecryptForward;
import SocketThread.EncryptForward;
import Util.Util;

import java.io.*;
import java.net.Socket;

public class LocalThread extends Thread {
    private InputStream localIn;
    private OutputStream localOut;
    private InputStream hostIn;
    private OutputStream hostOut;

    private Socket hostSocket;
    private Socket localSocket;

    private Local mainThread;

    public LocalThread(String hostAddr,int hostPort, Socket localSocket,Local mainThread) {
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

            new EncryptForward(localIn,hostOut).start();
            new DecryptForward(hostIn,localOut).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

