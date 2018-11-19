import Crypto.Cryptor;
import Local.Local;
import Server.Server;
import Util.ServerConfig;
import Util.Util;

public class StartServer {
    public static void main(String []args) {
        String filename = "/etc/serverConfig.json";
        for(int i=0;i<args.length;i++) {
            if(args[i].equals("-c") && i+1<args.length) {
                filename = args[i+1];
                break;
            } else if(args[i].equals("-h")) {
                Local.printHelpInfo();
            }
        }

        ServerConfig serverConfig = ServerConfig.loadConfigFromFile(filename);
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
