import crypto.Crypto;
import server.LocalServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.RemoteServer;
import util.*;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);


    public static void checkCrypto(Crypto crypto) {
        if(crypto == null) {
            logger.error("unknown crypto");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        logger.info("starting...");
        String configPath = null;
        Mode mode = Mode.Unknown;


        // load config path
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-c") && i + 1 < args.length) {
                configPath = args[i + 1];
                i++;
            } else if (args[i].equals("-h")) {
                Util.printHelpInfo();
                System.exit(0);
            }
        }

        // decide running mode
        mode = ConfigLoader.loadConfigMode(configPath);
        if (mode == Mode.Local) {
            LocalConfig config = ConfigLoader.loadAsLocalConfig(configPath);

            if (config == null) {
                logger.info("failed to load local config.");
                System.exit(1);
            } else {
                logger.info(config.toString());
            }

            checkCrypto(config.getCrypto());

            LocalServer localServer = new LocalServer(config.getHost(), config.getHostPort(), config.getLocalPort());
            LocalServer.crypto = config.getCrypto();
            localServer.listen();

        } else if (mode == Mode.Server) {
            ServerConfig config = ConfigLoader.loadAsServerConfig(configPath);

            if (config == null) {
                logger.info("failed to load server config.");
                System.exit(1);
            } else {
                logger.info(config.toString());
            }

            checkCrypto(config.getCrypto());

            RemoteServer remoteServer = new RemoteServer(config.getPort());
            RemoteServer.crypto = config.getCrypto();
            remoteServer.listen();

        } else {
            logger.error("unknown running mode");
            System.exit(1);
        }


    }
}