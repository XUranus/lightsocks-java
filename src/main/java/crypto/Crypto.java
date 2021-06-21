package crypto;


import java.io.IOException;
import java.io.InputStream;

public interface Crypto {
    byte[] encrypt(byte[] data);
    byte[] decrypt(byte[] data);

    int readEncrypt(InputStream in, byte[] buffer) throws IOException;;
    int readDecrypt(InputStream in, byte[] buffer) throws IOException;
}
