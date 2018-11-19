package Crypto;

public interface Cryptor {
    public abstract byte[] encrypt(byte[] data);
    public abstract byte[] decrypt(byte[] data);

    public static Cryptor createNewCryptor(String method,String key) {
        method = method.toLowerCase();
        switch (method) {
            case "aes-256-cfb":
                return new AES_256_CFB(key);
            case "none":
                return new NoPasswordCryptor();
            case "simple":
                return SimpleCrypto.getSimplieCrypto(key);
        }
        return null;
    }
}
