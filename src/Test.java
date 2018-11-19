import Local.Local;
import Server.Server;
import Util.LocalConfig;
import Util.ServerConfig;
import Util.Util;


public class Test {
    public static void main(String [] args) {
        new ServerThread().start();
        new LocalThread().start();
    }
}

class ServerThread extends Thread { //temp
    public void run() {
        String[] args = new String[2];
        args[0] = "-c";
        args[1] = "/home/xuranus/IdeaProjects/Test/serverConfig.json";
        StartServer.main(args);
    }
}

class LocalThread extends Thread { //temp
    public void run() {
        String[] args = new String[2];
        args[0] = "-c";
        args[1] = "/home/xuranus/IdeaProjects/Test/localConfig.json";
        StartLocal.main(args);
    }
}