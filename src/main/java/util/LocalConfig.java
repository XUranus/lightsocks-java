package util;

import com.google.gson.JsonObject;
import crypto.Crypto;
import crypto.CryptoFactory;

public class LocalConfig {
    /*
    * Singleton Pattern
    * exist only one
    * */
    private static LocalConfig instance = null;

    private final String password;
    private final String host;
    private final int hostPort;
    private final int localPort;
    private final String method;
    private final Crypto crypto;

    public static LocalConfig loadConfigFromFile(String filepath) {
        if(instance !=null) return instance;

        JsonObject jsonObject = Util.getJsonObjectFromFile(filepath);
        if(jsonObject == null || !checkJson(jsonObject)) {
            return null;
        }else {
            instance = new LocalConfig(jsonObject);
        }
        return instance;
    }

    private static boolean checkJson(JsonObject obj) {
        return obj.has("host")
                && obj.has("hostPort")
                && obj.has("password")
                && obj.has("localPort")
                && obj.has("method");
    }


    private LocalConfig(JsonObject obj) {
        this.password = obj.get("password").getAsString();
        this.hostPort = obj.get("hostPort").getAsInt();
        this.host = obj.get("host").getAsString();
        this.localPort = obj.get("localPort").getAsInt();
        this.method = obj.get("method").getAsString();
        this.crypto = CryptoFactory.createCrypto(method,password);
    }

    public String toString() {
        if(instance == null) return "LocalConfig Not Initilized.";
        else return
                "\nHost: "+host+"\n"
        +"HostPort: "+hostPort+"\n"
        +"LocalPort: "+localPort+"\n"
        +"Method: "+method+"\n"
        +"Password: "+password+"\n";
    }

    public int getHostPort() {
        return hostPort;
    }

    public int getLocalPort() {
        return localPort;
    }

    public String getHost() {
        return host;
    }

    public String getMethod() {
        return method;
    }

    public String getPassword() {
        return password;
    }

    public Crypto getCrypto() {return crypto;}
}
