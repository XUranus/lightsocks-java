package Local;

import Crypto.AESCryptor;
import Crypto.Cryptor;
import Server.Server;

import java.net.ServerSocket;
import java.net.Socket;

public class Local {
    private String hostAddr;
    private int hostPort;
    private int localPort;

    private String password;
    private ServerSocket localSocket;
    public static Cryptor cryptor;

    public Local(String host,int hostPort,int localPort,String password) {
        this.hostAddr = host;
        this.hostPort = hostPort;
        this.localPort = localPort;
        this.password = password;
        localSocket = null;
        cryptor = new AESCryptor(password); //default
        System.out.println("Local created");
    }


    public void listen() {
        try {
            localSocket = new ServerSocket(localPort);
            System.out.println("Local Listening");
            while(true) {
                System.out.println("Local catch a socket");
                Socket appSocket = localSocket.accept();
                new LocalThread(hostAddr,hostPort,appSocket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
