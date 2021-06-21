package util;

import com.google.gson.JsonObject;

public class ConfigLoader {

    public static Mode loadConfigMode(String filepath) {
        JsonObject jsonObject = Util.getJsonObjectFromFile(filepath);
        if (jsonObject == null || jsonObject.get("mode") == null) {
            return Mode.Unknown;
        }

        String mode = jsonObject.get("mode").getAsString();
        if("server".equals(mode)) {
            return Mode.Server;
        } else if("local".equals(mode)) {
            return Mode.Local;
        } else {
            return Mode.Unknown;
        }
    }

    public static LocalConfig loadAsLocalConfig(String filepath) {
        JsonObject jsonObject = Util.getJsonObjectFromFile(filepath);
        if(LocalConfig.checkJson(jsonObject)) {
            return new LocalConfig(jsonObject);
        } else {
            return null;
        }
    }

    public static ServerConfig loadAsServerConfig(String filepath) {
        JsonObject jsonObject = Util.getJsonObjectFromFile(filepath);
        if(ServerConfig.checkJson(jsonObject)) {
            return new ServerConfig(jsonObject);
        } else {
            return null;
        }

    }


}
