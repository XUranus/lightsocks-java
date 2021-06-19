public class Test {

    @org.junit.Test
    public void test() throws Exception{

        Thread t1 = new Thread(()->{
            Main.main(new String[]{
                    "java",
                    "-jar",
                    "lightsocks.jar",
                    "--server",
                    "-c",
                    "/home/xuranus/workspace/lightsocks-java/serverConfig.json"
            });
        });

        Thread t2 = new Thread(()->{
            Main.main(new String[]{
                    "java",
                    "-jar",
                    "lightsocks.jar",
                    "--client",
                    "-c",
                    "/home/xuranus/workspace/lightsocks-java/localConfig.json"
            });
        });

        t1.start();
        Thread.sleep(1000);
        t2.start();

        t2.join();

    }
}