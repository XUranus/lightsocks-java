import Crypto.Cryptor;
import Server.Server;
import Util.ServerConfig;
import Util.Util;

public class StartServer {
    public static void main(String []args) {
        if(args.length!=1) {
            Server.printHelpInfo();
            System.exit(0);
        }
        ServerConfig serverConfig = ServerConfig.loadConfigFromFile(args[0]);
        if(serverConfig == null) {
            Util.log("error: failed to load server config, exit.");
            System.exit(1);
        }
        Server server = new Server(serverConfig.getPort());
        if(server == null) {
            Util.log("error: faild to create server socket,exit.");
            System.exit(1);
        }
        Cryptor cryptor = serverConfig.getCryptor();
        if(cryptor==null) {
            Util.log("error: failed to init cipher,exit.");
            System.exit(1);
        } else {
            server.cryptor = cryptor;
        }
        server.listen();
    }
}
