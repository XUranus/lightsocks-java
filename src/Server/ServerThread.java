package Server;

import SocketThread.DecryptForward;
import SocketThread.EncryptForward;
import Socks.SocksConnectionRequest;
import Socks.SocksConnectionResponse;
import Util.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Arrays;

public class ServerThread extends Thread{
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private String remoteAddr;
    private int remotePort;

    private final int BUFFER_SIZE_DEFAULT = 1024;


    public ServerThread(Socket socket) {
        try {
            this.socket = socket;
            in = socket.getInputStream();
            out = socket.getOutputStream();
            remoteAddr = "";
            remotePort = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void over() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            socket.setSoTimeout(180000);
            socket.setKeepAlive(true);

            if(handleSock5HandShake()) Util.log("handshake verify success");
            else Util.log("handshake verify failed");

            if(handleSock5Connection()) Util.log("connection verify success");
            else Util.log("connection verify failed");

            startForward();
           /* Util.log("start to foward...");
            byte[] tt =  decryptRead(in);
            Util.log("client sent : "+Util.bytesToASCII(tt));
            */

            //new EncryptForward()
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startForward() {
        try {
            Socket remoteSocket = new Socket(remoteAddr,remotePort);
            remoteSocket.setSoTimeout(180000);
            remoteSocket.setKeepAlive(true);

            Util.log(remoteSocket.getInetAddress()+":"+remoteSocket.getPort());

            new DecryptForward(in,remoteSocket.getOutputStream()).start();
            new EncryptForward(remoteSocket.getInputStream(),out).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean handleSock5HandShake() {
       byte[] data = decryptRead(in);
       if(data==null || data[0]!=0x05) return false;

       byte[] respose = new byte[2];
       respose[0] = 0x05;
       respose[1] = 0x00;
       encryptWrite(out,respose);
       return true;
    }


    public boolean handleSock5Connection() {
        byte[] data = decryptRead(in);
        //Util.log("handleConnection 83 "+Util.bytesToHexString(data));
        SocksConnectionRequest request = SocksConnectionRequest.getNewInstance(data);
        if (request==null) return false;

        remoteAddr = request.getAddress();
        remotePort = request.getDstPort();

        byte[] response = SocksConnectionResponse.responseBytes();
        encryptWrite(out,response);

        return true;
    }

    private byte[] decryptRead(InputStream in) {
        try {
            int len = 0,encryptDataSize = 0;
            byte[] buffer = new byte[BUFFER_SIZE_DEFAULT];
            byte[] encryptData = new byte[BUFFER_SIZE_DEFAULT];

            len = in.read(encryptData);
            encryptDataSize = len;
            if(len==-1) return null;

            while(len == buffer.length) {
                len = in.read(buffer);
                if(encryptData.length<len+encryptDataSize) { //扩容
                    byte[] tempData = encryptData;
                    encryptData = new byte[encryptData.length+BUFFER_SIZE_DEFAULT];
                    for(int i=0;i<encryptDataSize;i++) encryptData[i] = tempData[i];
                }
                System.arraycopy(buffer,0,encryptData,encryptDataSize,len);
                encryptDataSize += len;
            }

            encryptData = Arrays.copyOfRange(encryptData,0,encryptDataSize);
            byte[] decryptData = Server.cryptor.decrypt(encryptData);
            return decryptData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void encryptWrite(OutputStream out,byte[] rawData) {
        try {
            byte[] encryptData = Server.cryptor.encrypt(rawData);
            out.write(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}