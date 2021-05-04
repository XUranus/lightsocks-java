package crypto;

public class CryptoFactory {

    public static Crypto createCrypto(String method, String key) {
        method = method.toLowerCase();
        switch (method) {
            case "aes-256-cfb":
                return new AES_256_CFB(key);
            case "none":
                return new NoPasswordCrypto();
            case "simple":
                return SimpleCrypto.getSimplieCrypto(key);
        }
        return null;
    }
}
