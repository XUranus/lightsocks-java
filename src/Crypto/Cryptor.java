package Crypto;

public interface Cryptor {
    public abstract byte[] encrypt(byte[] data);
    public abstract byte[] decrypt(byte[] data);

    public static Cryptor createNewCryptor(String method,String password) {
        switch (method) {
            case "AES-256":
                return new AESCryptor(password);
            case "none":
                return new NoPasswordCryptor();
        }
        return null;
    }
}
