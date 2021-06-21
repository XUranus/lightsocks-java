package crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.StreamForwardThread;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Base64;

public class AES256CFB implements Crypto {


    private final static Logger logger = LoggerFactory.getLogger(AES256CFB.class);

    private final String key;


    public AES256CFB(String key) {
        this.key = key;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        if (data == null) return null;
        byte[] encrypted;
        try {
            SecretKeySpec sKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sKey);
            encrypted = cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
        return encrypted;
    }

    // length of bytes must can be times of 16
    @Override
    public byte[] decrypt(byte[] data) {
        if (data == null) return null;
        byte[] decrypted;
        try {
            SecretKeySpec sKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, sKey);
            decrypted = cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
        return decrypted;
    }

    @Override
    public int readEncrypt(InputStream in, byte[] buffer) throws IOException {
        // temp buffer size must be smaller than en encrypted buffer (byte[] buffer)
        byte[] tmpBuffer = new byte[buffer.length - 1];
        int len = in.read(tmpBuffer);
        if (len > 0) {
            byte[] finalTempBuffer = new byte[len];
            System.arraycopy(tmpBuffer, 0, finalTempBuffer, 0, len);
            byte[] encrypted = encrypt(finalTempBuffer);
            System.arraycopy(encrypted, 0, buffer, 0, encrypted.length);
            return encrypted.length;
        }
        return len;
    }

    @Override
    public int readDecrypt(InputStream in, byte[] buffer) throws IOException {
        // buffer size must bigger than AES_BLOCK_SIZE

        int DEFAULT_BLOCK_SIZE = 1024 * 256; // 256 K
        byte[] tmpBuffer = new byte[DEFAULT_BLOCK_SIZE];
        int len = in.read(tmpBuffer);
        if (len < 0) {
            return len;
        }

        if(len > 0) {
            logger.warn("AES decrypt read bytes = " + len);
            byte[] decrypted = decrypt(Arrays.copyOf(tmpBuffer, len));
            System.arraycopy(decrypted,0, buffer, 0, decrypted.length);
            return decrypted.length;
        }


        return len; // 0 or -1
    }

}
