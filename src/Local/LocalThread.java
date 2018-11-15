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

    public LocalThread(String hostAddr,int hostPort, Socket localSocket) {
        try {
            this.localSocket = localSocket;
            this.hostSocket = new Socket(hostAddr,hostPort);
            localIn = localSocket.getInputStream();
            localOut = localSocket.getOutputStream();
            hostIn = hostSocket.getInputStream();
            hostOut = hostSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Util.log("LocalThread 34");

            //byte[] buf = new byte[100];
            //int len = localIn.read(buf);
            //Util.log("temp "+Util.bytesToHexString(buf,len));

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

