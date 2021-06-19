package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import socketThread.DecryptForward;
import socketThread.EncryptForward;
import socks.SocksConnectionRequest;
import socks.SocksConnectionResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ServerThread extends Thread{

    private final static Logger logger = LoggerFactory.getLogger(ServerThread.class);

    private Server mainThread;

    private Socket agentSocket; //被代理的socket
    private Socket remoteSocket; //真正的远端socket

    private InputStream agentIn;
    private OutputStream agentOut;

    private InputStream remoteIn;
    private OutputStream remoteOut;

    private final static int BUFFER_SIZE_DEFAULT = 1024;


    public ServerThread(Socket agentSocket,Server mainThread) {
        try {
            this.agentSocket = agentSocket;
            this.remoteSocket = null;

            this.mainThread = mainThread;

            this.agentIn = agentSocket.getInputStream();
            this.agentOut = agentSocket.getOutputStream();
            this.remoteIn = null;
            this.remoteOut = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void over() {
        try {
            agentIn.close();
            agentOut.close();
            agentSocket.close();

            remoteIn.close();
            remoteOut.close();
            remoteSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            agentSocket.setSoTimeout(180000);
            agentSocket.setKeepAlive(true);

            if(!handleSock5HandShake()) {
                logger.error("handleSock5HandShake failed");
                return;
            }

            if(!handleSock5Connection()) {
                logger.error("handleSock5Connection failed");
                return;
            }

            startForward();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startForward() {
        try {
            remoteSocket.setSoTimeout(180000);
            remoteSocket.setKeepAlive(true);

            new DecryptForward(agentIn,remoteOut,Server.crypto).start();
            new EncryptForward(remoteIn,agentOut,Server.crypto).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean handleSock5HandShake() {
       byte[] data = decryptRead(agentIn);
       if(data==null || data[0]!=0x05) return false;

       byte[] response = new byte[2];
       response[0] = 0x05;
       response[1] = 0x00;
       encryptWrite(agentOut,response);
       return true;
    }


    public boolean handleSock5Connection() {
        byte[] data = decryptRead(agentIn);
        assert data != null;
        SocksConnectionRequest request = SocksConnectionRequest.getNewInstance(data);
        if (request==null) return false;

        String remoteAddr = request.getAddress();
        int remotePort = request.getDstPort();
        try {
            logger.info("remote:" + remoteAddr+ ":" + remotePort);
            remoteSocket = new Socket(remoteAddr,remotePort);
            remoteOut = remoteSocket.getOutputStream();
            remoteIn = remoteSocket.getInputStream();
        } catch (Exception e) { //如果远端没有成功建立链接
            e.printStackTrace();
            return false;
        }

        byte[] response = SocksConnectionResponse.responseBytes();//TODO:这里可以根据上面是否成功链接返回不同报文
        encryptWrite(agentOut,response);

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
                    if (encryptDataSize >= 0) System.arraycopy(tempData, 0, encryptData, 0, encryptDataSize);
                }
                System.arraycopy(buffer,0,encryptData,encryptDataSize,len);
                encryptDataSize += len;
            }

            encryptData = Arrays.copyOfRange(encryptData,0,encryptDataSize);
            return Server.crypto.decrypt(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void encryptWrite(OutputStream out,byte[] rawData) {
        try {
            byte[] encryptData = Server.crypto.encrypt(rawData);
            out.write(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}