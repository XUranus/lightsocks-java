package Crypto;

public interface Cryptor {
    public abstract byte[] encrypt(byte[] data);
    public abstract byte[] decrypt(byte[] data);
}
