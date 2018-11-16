package Util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
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

    public static void log(Object obj) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("["+ df.format(new Date()) +"]"+obj.toString());
    }

    public static JsonObject getJsonObjectFromFile(String filepath) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        String jsonStr = "";
        String lineStr;
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(filepath)));
            while((lineStr = in.readLine())!=null) {
                jsonStr = jsonStr.concat(lineStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
        return jsonObject;
    }
}
