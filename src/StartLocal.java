import Crypto.Cryptor;
import Local.Local;
import Util.LocalConfig;
import Util.Util;

public class StartLocal {
    public static void main(String [] args) {
        String filename = "/etc/localConfig.json";
        for(int i=0;i<args.length;i++) {
            if(args[i].equals("-c") && i+1<args.length) {
                filename = args[i+1];
                break;
            } else if(args[i].equals("-h")) {
                Local.printHelpInfo();
            }
        }


        LocalConfig localConfig = LocalConfig.loadConfigFromFile(filename);
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
