import crypto.Crypto;
import crypto.SimpleCrypto;
import org.junit.Test;

import java.io.InputStreamReader;

public class SimpleCryptoTest {

    //@Test
    public void generateKey() {
        String key = SimpleCrypto.generateKeyString();

        Crypto crypto = SimpleCrypto.getSimpleCrypto(key);
        String str = "this is for test";
        System.out.println(str);
        assert crypto != null;
        byte[] bytes = crypto.encrypt(str.getBytes());
        System.out.println(new String(crypto.decrypt(bytes)));

    }


}
