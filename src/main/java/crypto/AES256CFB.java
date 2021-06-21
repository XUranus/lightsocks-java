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

    //240-255 => 256
    /**
     * ----------------------------------
     * | len (1) | bytes with 0 padding
     * ----------------------------------
     *  1 =< len <= 254
     */
    private final int AES_MAX_LEN  = 254;
    private final int AES_BLOCK_SIZE = 256;


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

//    //240-255 => 256
//    /**
//     * ----------------------------------
//     * | len (1) | bytes with 0 padding
//     * ----------------------------------
//     *  1 =< len <= 254
//     */
//    private final int AES_MAX_LEN  = 254;//raw
//    private final int AES_PADDING_SIZE = 256;

    @Override
    public int readEncrypt(InputStream in, byte[] buffer) throws IOException {
        // temp buffer size must be smaller than en encrypted buffer (byte[] buffer)
        assert buffer.length >= AES_BLOCK_SIZE;
        byte[] tmpBuffer = new byte[AES_MAX_LEN];
        int len = in.read(tmpBuffer);
        if (len > 0) {
            byte[] finalTempBuffer = new byte[len + 1];
            finalTempBuffer[0] = (byte) len;
            System.arraycopy(tmpBuffer, 0, finalTempBuffer, 1, len);
            byte[] encrypted = encrypt(finalTempBuffer);
            System.arraycopy(encrypted, 0, buffer, 0, encrypted.length);
            return encrypted.length;
        }
        return len;
    }

    //    //240-255 => 256
//    /**
//     * ----------------------------------
//     * | len (1) | bytes with 0 padding
//     * ----------------------------------
//     *  1 =< len <= 254
//     */
//    private final int AES_MAX_LEN  = 254;//raw
//    private final int AES_PADDING_SIZE = 256;

    @Override
    public int readDecrypt(InputStream in, byte[] buffer) throws IOException {
        assert buffer.length >= AES_MAX_LEN;

        byte[] tmpBuffer = new byte[AES_BLOCK_SIZE];
        int len = in.read(tmpBuffer);

        if(len > 0) {
            byte[] decrypted =  decrypt(Arrays.copyOf(tmpBuffer, len));
            int size = decrypted[0] & 0xFF;
            byte[] realDecrypted = new byte[size];
            System.arraycopy(decrypted, 1, realDecrypted, 0, size);
            System.arraycopy(realDecrypted, 0, buffer, 0, size);
            return size;
        }

        return len; // 0 or -1
    }

    public static byte[] padding(byte[] data) {
        byte[] data2 = new byte[data.length + 1];
        System.arraycopy(data, 0, data2, 1, data.length);
        data2[0] = (byte) data.length;
        return data2;
    }
}
