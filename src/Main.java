import Local.Local;
import Server.Server;
import Util.LocalConfig;
import Util.ServerConfig;
import Util.Util;


public class Main {
    public static void main(String [] args) {
        new ServerThread().start();
        new LocalThread().start();
    }
}

class ServerThread extends Thread { //temp
    public void run() {
        String[] args = new String[1];
        args[0] = "/home/xuranus/IdeaProjects/Test/serverConfig.json";
        StartServer.main(args);
    }
}

class LocalThread extends Thread { //temp
    public void run() {
        String[] args = new String[1];
        args[0] = "/home/xuranus/IdeaProjects/Test/localConfig.json";
        StartLocal.main(args);
    }
}