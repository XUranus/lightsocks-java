package crypto;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

public class SimpleCrypto implements Crypto {

    private final static Logger logger = LoggerFactory.getLogger(SimpleCrypto.class);

    private final byte[] decodeKey;
    private final byte[] encodeKey;

    private SimpleCrypto(byte[] keyBytes) {
        this.decodeKey = new byte[256];
        this.encodeKey = new byte[256];

        System.arraycopy(keyBytes, 0, decodeKey, 0, keyBytes.length);

        for (int i = 0; i < decodeKey.length; i++) {
            int t = decodeKey[i];
            if (t < 0) t += 256;
            encodeKey[t] = (byte) i;
        }
    }

    public static SimpleCrypto getSimpleCrypto(String hexKeyString) {
        if (hexKeyString == null || hexKeyString.length() != 256 * 2) {
            logger.info("simple crypto key length must be 512!");
            return null;
        }
        hexKeyString = hexKeyString.toUpperCase();
        int length = hexKeyString.length() / 2;
        char[] hexChars = hexKeyString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }

        if (d.length != 256) return null;
        if (!keyValid(d)) return null;
        return new SimpleCrypto(d);
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    public static boolean keyValid(byte[] key) {
        byte[] allKey = new byte[256];
        for (int i = 0; i < allKey.length; i++)
            allKey[i] = (byte) i;

        for (byte k : allKey) {
            boolean flag = false;
            for (byte t : key) {
                if (t == k) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }


    public static byte[] generateKeyBytes() {
        byte[] key = new byte[256];
        for (int i = 0; i < key.length; i++)
            key[i] = (byte) i;

        Random random = new Random();
        for (int i = 0; i < key.length; i++) {
            int p = random.nextInt(256);//0 - 255
            byte t = key[i];//switch key[i] key[p]
            key[i] = key[p];
            key[p] = t;
        }
        return key;
    }

    public static String generateKeyString() {
        byte[] keyBytes = generateKeyBytes();
        return Util.bytesToHexString(keyBytes);
    }

    // this method has a side effect
    public void transform(byte[] data, byte[] map) {
        byte[] copy = Arrays.copyOf(data, data.length);
        for (int i = 0; i < copy.length; i++) {
            int v = copy[i];
            v = v >= 0 ? v : v + 256;
            data[i] = map[v];
        }
    }


    @Override
    public int readDecrypt(InputStream in, byte[] buffer) throws IOException {
        int len = in.read(buffer);
        if(len > 0) {
            transform(buffer, decodeKey);
        }
        return len;
    }

    @Override
    public int readEncrypt(InputStream in, byte[] buffer) throws IOException {
        int len = in.read(buffer);
        if(len > 0) {
            transform(buffer, encodeKey);
        }
        return len;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        byte[] copied = Arrays.copyOf(data, data.length);
        transform(copied, encodeKey);
        return copied;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        byte[] copied = Arrays.copyOf(data, data.length);
        transform(copied, decodeKey);
        return copied;
    }

}