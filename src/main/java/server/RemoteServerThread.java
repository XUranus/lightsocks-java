package server;

import crypto.AES256CFB;
import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import socks.SocksConnectionRequest;
import socks.SocksConnectionResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;


/**
 *  Client(Agent)   ||                                  ||  Server
 *                  ||  ---handshake( 0x05 )-------->   ||
 *                  ||  <---handshake( 0x05 0x00) ---   ||
 *                  ||  ---connection meta --------->   ||
 *                  ||  <---------------------------    ||
 */

public class RemoteServerThread extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(RemoteServerThread.class);

    private Socket agentSocket; // the proxied local app socket
    private InputStream agentIn;
    private OutputStream agentOut;

    private String remoteAddr;
    private int remotePort;

    private final static int SOCKS_CONNECTION_MAX = 512; //1 + 1 + 1 + 1 + (1 + 255) + 2 = 262

    /**
     * proxy agentSocket request to remoteSocket
     */

    public RemoteServerThread(Socket agentSocket) {
        try {
            this.agentSocket = agentSocket;
            this.agentIn = agentSocket.getInputStream();
            this.agentOut = agentSocket.getOutputStream();


        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void run() {
        try {
            agentSocket.setSoTimeout(60 * 60 * 24);
            agentSocket.setKeepAlive(true);

            if (!handleSock5Handshake(agentIn, agentOut)) {
                logger.error("handleSock5HandShake failed");
                agentSocket.close();
                return;
            }

            if (!handleSock5Connection(agentIn, agentOut)) {
                logger.error("handleSock5Connection failed");
                agentSocket.close();
                return;
            }

            startForward();

        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private boolean handleSock5Handshake(InputStream in, OutputStream out) {
        byte[] data = decryptReadAllBytes(in);
        if (data == null || data[0] != 0x05) {
            // not a sock5 header
            return false;
        }

        byte[] response = new byte[2];
        response[0] = 0x05;
        // response sock5 hand shake
        return encryptWriteAllBytes(out, response);
    }


    public boolean handleSock5Connection(InputStream in, OutputStream out) throws InterruptedException {
        byte[] data = decryptReadAllBytes(in);
        if(data == null) {
            return false;
        }

        SocksConnectionRequest request = SocksConnectionRequest.fromBytes(data);
        if (request == null) {
            return false;
        }

        remoteAddr = request.getAddress();
        remotePort = request.getDstPort();
        logger.info("proxy to remote: " + remoteAddr + ":" + remotePort);

        byte[] response = SocksConnectionResponse.responseBytes();
        //TODO: respond different bytes corresponding weather is succeed
        return encryptWriteAllBytes(out, response);
    }

    private void startForward() {
        Socket remoteSocket = null;
        OutputStream remoteOut = null;
        InputStream remoteIn = null;
        try {
            remoteSocket = new Socket(remoteAddr, remotePort);
            remoteOut = remoteSocket.getOutputStream();
            remoteIn = remoteSocket.getInputStream();

            remoteSocket.setSoTimeout(60 * 60 * 24);
            remoteSocket.setKeepAlive(true);

            Thread decryptThread = new StreamForwardThread(agentIn, remoteOut, RemoteServer.crypto, false);
            Thread encryptThread = new StreamForwardThread(remoteIn, agentOut, RemoteServer.crypto, true);

            decryptThread.start();
            encryptThread.start();

            decryptThread.join();
            encryptThread.join();

        }  catch (Exception e) {
            logger.error("", e);
        }

        // close all stream and socket
        try {
            this.agentIn.close();
            this.agentOut.close();

        } catch (IOException e) {
            logger.error("", e);
        }
    }

    private byte[] decryptReadAllBytes(InputStream in) {
        Crypto crypto = RemoteServer.crypto;
        int len;
        byte[] buffer = new byte[SOCKS_CONNECTION_MAX + 10];
        try {
            len = crypto.readDecrypt(in, buffer);
            if(len == buffer.length) {
                logger.warn("reach buffer limit");
            }
            return Arrays.copyOf(buffer, len);

        } catch (IOException e) {
            logger.error("", e);
            return null;
        }
    }


    private boolean encryptWriteAllBytes(OutputStream out, byte[] rawBuffer) {
        try {
            if(RemoteServer.crypto instanceof AES256CFB) {
                rawBuffer = AES256CFB.padding(rawBuffer);
            }
            byte[] encryptBuffer = RemoteServer.crypto.encrypt(rawBuffer);
            out.write(encryptBuffer);
            out.flush();
            return true;
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }
    }


}