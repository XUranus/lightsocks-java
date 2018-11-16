import Local.Local;
import Server.Server;
import Util.LocalConfig;
import Util.ServerConfig;
import Util.Util;


public class Main {
    public static void main(String [] args) {
        new ServerThread().start();
        new LocalThread().start();

        //System.out.println(ServerConfig.loadConfigFromFile("/home/xuranus/IdeaProjects/Test/serverConfig.json"));
        //System.out.println(LocalConfig.loadConfigFromFile("/home/xuranus/IdeaProjects/Test/localConfig.json"));
    }
}

class ServerThread extends Thread { //temp
    public void run() {
        ServerConfig serverConfig = ServerConfig.loadConfigFromFile("/home/xuranus/IdeaProjects/Test/serverConfig.json");
        if(serverConfig == null) {
            Util.log("error: failed to load server config, exit.");
            System.exit(1);
        }
        Server server = new Server(serverConfig.getPort(),serverConfig.getPassword());
        if(server == null) {
            Util.log("error: faild to create server socket,exit.");
            System.exit(1);
        }
        server.listen();
    }
}

class LocalThread extends Thread { //temp
    public void run() {
        LocalConfig localConfig = LocalConfig.loadConfigFromFile("/home/xuranus/IdeaProjects/Test/localConfig.json");
        if(localConfig == null) {
            Util.log("error: failed to load local config, exit.");
            System.exit(1);
        }
        Local local = new Local(localConfig.getHost(),localConfig.getHostPort(),localConfig.getLocalPort(),localConfig.getPassword());
        if(local == null) {
            Util.log("error: faild to create local socket,exit.");
            System.exit(1);
        }
        local.listen();
    }
}