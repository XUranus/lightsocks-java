package crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoFactory {

    private final static Logger logger = LoggerFactory.getLogger(CryptoFactory.class);


    public static Crypto createCrypto(String method, String key) {
        logger.info("loading crypto [" + method + "]");
        method = method.toLowerCase();
        switch (method) {
            case "aes-256-cfb":
                return new AES256CFB(key);
            case "none":
                return new NoneCrypto();
            case "simple":
                return SimpleCrypto.getSimpleCrypto(key);
        }
        return null;
    }
}
