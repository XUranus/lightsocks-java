import Crypto.AESCryptor;
import Local.Local;
import Server.Server;
import Util.LocalConfig;
import Util.Util;

public class StartLocal {
    public static void main(String [] args) {
        if(args.length!=1) {
            Local.printHelpInfo();
            System.exit(0);
        }
        LocalConfig localConfig = LocalConfig.loadConfigFromFile(args[0]);
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
