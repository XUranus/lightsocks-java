import Local.Local;
import Server.Server;


public class Main {
    public static void main(String [] args) {
        new ServerThread().start();
        new LocalThread().start();
    }
}

class ServerThread extends Thread {
    public void run() {
        Server server = new Server(8090,"password");
        server.listen();
    }
}

class LocalThread extends Thread {
    public void run() {
        Local local = new Local("127.0.0.1",8090,8080,"password");
        local.listen();
    }
}