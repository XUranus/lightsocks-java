import client.Client;
import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;
import util.LocalConfig;
import util.Mode;
import util.ServerConfig;
import util.Util;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    private static void printHelpInfo() {
        System.out.println("Usage: java -jar lightsocks.jar -c config.json --client | --server");
    }

    public static void main(String[] args) {
        logger.info("starting.");
        String configPath = null;
        Mode mode = Mode.Unknown;

        for(int i=0;i<args.length;i++) {
            if(args[i].equals("-c") && i + 1 < args.length) {
                configPath = args[i + 1];
                i++;
            } else if(args[i].equals("-h")) {
                printHelpInfo();
                System.exit(0);
            }
        }


        try {
            if(Util.getJsonObjectFromFile(configPath).get("mode").getAsString().equalsIgnoreCase("server")) {
                mode = Mode.Server;
            } else if (Util.getJsonObjectFromFile(configPath).get("mode").getAsString().equalsIgnoreCase("local")) {
                mode = Mode.Client;
            }
        } catch (Exception e) {
            logger.error("", e);
            System.exit(1);
        }


        if(mode == Mode.Client) {
            LocalConfig localConfig = LocalConfig.loadConfigFromFile(configPath);
            if(localConfig == null) {
                logger.info("error: failed to load local config, exit.");
                System.exit(1);
            } else {
                logger.info(localConfig.toString());
            }

            Client local = new Client(localConfig.getHost(),localConfig.getHostPort(),localConfig.getLocalPort());
            Crypto crypto = localConfig.getCrypto();
            if(crypto==null) {
                logger.info("error: failed to init cipher,exit.");
                System.exit(1);
            } else {
                Client.crypto = crypto;
            }
            local.listen();

        } else if (mode == Mode.Server) {

            ServerConfig serverConfig = ServerConfig.loadConfigFromFile(configPath);
            if(serverConfig == null) {
                logger.info("error: failed to load server config, exit.");
                System.exit(1);
            } else {
                logger.info(serverConfig.toString());
            }

            Server server = new Server(serverConfig.getPort());
            Crypto crypto = serverConfig.getCrypto();
            if(crypto==null) {
                logger.info("error: failed to init cipher,exit.");
                System.exit(1);
            } else {
                Server.crypto = crypto;
            }
            server.listen();
        }


    }
}