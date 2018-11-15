import Server.Server;

public class StartServer {
    public static void main(String []args) {
        Server server = new Server(8090,"password");
        server.listen();
    }
}
