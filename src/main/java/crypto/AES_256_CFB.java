package crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES_256_CFB implements Crypto {
    private final String key;
    public AES_256_CFB(String key) {
        this.key = key;
    }

    public byte[] encrypt(byte[] data){
        Base64.Encoder encoder =  Base64.getEncoder();
        byte[] crypted = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(data);
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return encoder.encode(crypted);
    }

    public byte[] decrypt(byte[] data){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] output = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(decoder.decode(data));
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return output;
    }

}
