package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Util {

    private final static Logger logger = LoggerFactory.getLogger(Util.class);

    public static String bytesToHexString(byte[] bArray, int len) {
        StringBuffer sb = new StringBuffer(len);
        String sTemp;
        for (int i = 0; i < len; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String bytesToASCII(byte[] bArray, int len) {
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            sb.append((char) bArray[i]);
        }
        return sb.toString();
    }

    public static String bytesToASCII(byte[] bArray) {
        return bytesToASCII(bArray,bArray.length);
    }

    public static String bytesToHexString(byte[] bArray) {
        return bytesToHexString(bArray,bArray.length);
    }

    public static JsonObject getJsonObjectFromFile(String filepath) {
        JsonParser jsonParser = new JsonParser();
        String jsonStr = "";
        String lineStr;
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(filepath)));
            while((lineStr = in.readLine())!=null) {
                jsonStr = jsonStr.concat(lineStr);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return jsonParser.parse(jsonStr).getAsJsonObject();
    }

}
