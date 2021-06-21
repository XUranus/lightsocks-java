package crypto;

import java.io.IOException;
import java.io.InputStream;

public class NoneCrypto implements Crypto {

    /**
     * extremely unsafe method, for test only
     */

    @Override
    public byte[] encrypt(byte[] data) {
        return data;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return data;
    }


    @Override
    public int readDecrypt(InputStream in, byte[] buffer) throws IOException {
        return in.read(buffer);
    }

    @Override
    public int readEncrypt(InputStream in, byte[] buffer) throws IOException {
        return in.read(buffer);
    }
}
