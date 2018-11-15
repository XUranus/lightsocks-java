package Util;

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
}
