import crypto.AES256CFB;
import crypto.Crypto;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class AES256CFBTest {
    @Test
    public void test() {
        Crypto crypto = new AES256CFB("1234567887654321");
        String str = "114514啊啊啊啊啊啊1919810";

        System.out.println(str);
        System.out.println(new String(crypto.decrypt(crypto.encrypt(str.getBytes()))));

        for(int i = 1; i < 1000; i++) {
            byte[] b = make(i);
            byte[] e = crypto.encrypt(b);
            System.out.println(i + " -> " + e.length);
        }
    }

    public byte[] make(int len) {
        return new byte[len];
    }
}

//0-15  24
//16-31 44
//32-47 64
//48-63 88
//64-79 108
//80-95 128
//96-111 148
//112-127
//128-