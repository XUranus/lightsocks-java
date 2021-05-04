import client.Client;
import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;
import util.LocalConfig;
import util.Mode;
import util.ServerConfig;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private static void printHelpInfo() {
        System.out.println("Usage: java -jar lightsocks.jar -c config.json --client | --server");
    }

    public static void main(String[] args) {

        String configPath = null;
        Mode mode = Mode.Unknown;

        for(int i=0;i<args.length;i++) {
            if(args[i].equals("-c") && i + 1 < args.length) {
                configPath = args[i + 1];
                i++;
            } else if(args[i].equals("--client")) {
                mode = Mode.Client;
            } else if(args[i].equals("--server")) {
                mode = Mode.Server;
            } else if(args[i].equals("-h")) {
                printHelpInfo();
            }
        }

        if(mode == Mode.Unknown || configPath == null) {
            printHelpInfo();
        }


        if(mode == Mode.Client) {
            LocalConfig localConfig = LocalConfig.loadConfigFromFile(configPath);
            if(localConfig == null) {
                logger.info("error: failed to load local config, exit.");
                System.exit(1);
            }
            Client local = new Client(localConfig.getHost(),localConfig.getHostPort(),localConfig.getLocalPort());
            if(local == null) {
                logger.info("error: failed to create local socket,exit.");
                System.exit(1);
            }
            Crypto crypto = localConfig.getCrypto();
            if(crypto==null) {
                logger.info("error: failed to init cipher,exit.");
                System.exit(1);
            } else {
                local.crypto = crypto;
            }
            local.listen();

        } else if (mode == Mode.Server) {

            ServerConfig serverConfig = ServerConfig.loadConfigFromFile(configPath);
            if(serverConfig == null) {
                logger.info("error: failed to load server config, exit.");
                System.exit(1);
            }
            Server server = new Server(serverConfig.getPort());
            if(server == null) {
                logger.info("error: failed to create server socket,exit.");
                System.exit(1);
            }
            Crypto crypto = serverConfig.getCrypto();
            if(crypto==null) {
                logger.info("error: failed to init cipher,exit.");
                System.exit(1);
            } else {
                server.crypto = crypto;
            }
            server.listen();
        }

    }
}