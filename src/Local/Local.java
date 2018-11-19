package Local;

import Crypto.Cryptor;
import Util.Util;

import java.net.ServerSocket;
import java.net.Socket;

public class Local {
    private String hostAddr;
    private int hostPort;
    private int localPort;

    private ServerSocket localSocket;
    public static Cryptor cryptor;

    public Local(String host,int hostPort,int localPort) {
        this.hostAddr = host;
        this.hostPort = hostPort;
        this.localPort = localPort;
        localSocket = null;
    }


    public void listen() {
        try {
            localSocket = new ServerSocket(localPort);
            Util.log("local start listening...");
            while(true) {
                //Util.log("Local catch a socket");
                Socket appSocket = localSocket.accept();
                new LocalThread(hostAddr,hostPort,appSocket,this).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printHelpInfo() {
        System.out.println("\nUsage:");
        System.out.println("lightsocks-local [configFileName]\n");
    }
}
