package util;

import com.google.gson.JsonObject;
import crypto.Crypto;
import crypto.CryptoFactory;

public class ServerConfig {

    private final String password;
    private final int port;
    private final String method;
    private final Crypto crypto;


    public static boolean checkJson(JsonObject obj) {
        return obj.has("port")
                && obj.has("password")
                && obj.has("method");
    }


    public ServerConfig(JsonObject obj) {
        this.password = obj.get("password").getAsString();
        this.port = obj.get("port").getAsInt();
        this.method = obj.get("method").getAsString();
        this.crypto = CryptoFactory.createCrypto(method, password);
    }

    public String toString() {
        return "\nPort: " + port + "\n"
                + "Method: " + method;
    }

    public String getMethod() {
        return method;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public Crypto getCrypto() {
        return crypto;
    }

}
