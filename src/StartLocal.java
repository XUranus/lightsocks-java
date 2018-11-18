import Crypto.AESCryptor;
import Crypto.Cryptor;
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
        Local local = new Local(localConfig.getHost(),localConfig.getHostPort(),localConfig.getLocalPort());
        if(local == null) {
            Util.log("error: failed to create local socket,exit.");
            System.exit(1);
        }
        Cryptor cryptor = localConfig.getCryptor();
        if(cryptor==null) {
            Util.log("error: failed to init cipher,exit.");
            System.exit(1);
        } else {
            local.cryptor = cryptor;
        }
        local.listen();
    }

}
