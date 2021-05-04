package crypto;


public interface Crypto {

    public abstract byte[] encrypt(byte[] data);

    public abstract byte[] decrypt(byte[] data);
}
