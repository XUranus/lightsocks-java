import crypto.Crypto;
import crypto.CryptoFactory;
import java.io.*;
import java.util.Date;
import java.util.Objects;

public class Test {

    //@org.junit.Test
    public void test() throws Exception{

        String serverConfigPath = Objects.requireNonNull(Test.class.getClassLoader().getResource("serverConfig.json")).getPath();
        String localConfigPath =  Objects.requireNonNull(Test.class.getClassLoader().getResource("localConfig.json")).getPath();

        System.out.println("using config path:" + serverConfigPath + ", " + localConfigPath);

        Thread t1 = new Thread(()->{
            Main.main(new String[]{"-c", serverConfigPath});
        });

        Thread t2 = new Thread(()->{
            Main.main(new String[]{"-c", localConfigPath});
        });

        t1.start();
        t2.start();

//        t1.join();
//        t2.join();

    }

    //@org.junit.Test
    public void benchmark() throws IOException, InterruptedException {
        String serverConfigPath = Objects.requireNonNull(Test.class.getClassLoader().getResource("serverConfig.json")).getPath();
        String localConfigPath =  Objects.requireNonNull(Test.class.getClassLoader().getResource("localConfig.json")).getPath();

        System.out.println("using config path:" + serverConfigPath + ", " + localConfigPath);

        Thread t1 = new Thread(()->{
            Main.main(new String[]{"-c", serverConfigPath});
        });

        Thread t2 = new Thread(()->{
            Main.main(new String[]{"-c", localConfigPath});
        });

        t1.start();
        t2.start();

        pureBenchmark(1000, false);

        System.getProperties().put("socksProxySet","true");
        System.getProperties().put("socksProxyHost","localhost");
        System.getProperties().put("socksProxyPort","8888");

        pureBenchmark(1000, true);

    }

    public void pureBenchmark(int times,boolean proxySet) throws IOException {
        long current = new Date().getTime();
        for(int i = 0; i < times; i++) {
            Runtime.getRuntime().exec("curl www.baidu.com");
        }
        long sec = (new Date().getTime() - current)/1000;
        System.out.println( (proxySet? "with proxy: " : "without proxy: ") + times / sec + " connection per/sec");
    }

    //@org.junit.Test
    public void testAES256CFB() {
        String str = "123456789012345612456789012345612345678901234561";
        Crypto crypto = CryptoFactory.createCrypto("aes-256-cfb", "1234567887654321");
        assert crypto != null;
        byte[] en = crypto.encrypt(str.getBytes());
        byte[] de = crypto.decrypt(en);
        System.out.println(str.getBytes().length + " -> " + en.length + " -> " + de.length);
        System.out.println(new String(de));
    }
}



/*
    without proxy: 20 connection per/sec
    proxy without nio, 15 connection per/sec
 */