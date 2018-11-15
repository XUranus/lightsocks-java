package SocketThread;

import Server.Server;
import Util.Util;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;

public class EncryptForward extends Thread {
    /*
    read from A and encrypt send to B
    * */

    private InputStream in;
    private OutputStream out;
    //private Object parent;

    private byte[] buffer;

    private static final int BUFFER_SIZE_MIN = 1024 * 128; // 缓冲区最小值，128K
    private static final int BUFFER_SIZE_MAX = 1024 * 512; // 缓冲区最大值，512K
    private static final int BUFFER_SIZE_STEP = 1024 * 128; // 缓冲区自动调整的步长值，128K

    public EncryptForward(InputStream in,OutputStream out) {
        buffer = new byte[BUFFER_SIZE_MIN];
        this.in = in;
        this.out = out;
    }

    public void run() {
        try {
            int len = 0;
            while((len=in.read(buffer))!=-1) {
                byte[] rawData = Arrays.copyOfRange(buffer,0,len);
                Util.log("EncryptForward 39 "+Util.bytesToHexString(rawData));
                Util.log("EncryptForward GET: "+Util.bytesToASCII(rawData));
                byte[] encryptData =  Server.cryptor.encrypt(rawData);
                if (encryptData == null) {
                    break; // 加密出错，退出
                }
                out.write(encryptData);
                Util.log("EncryptForward SENT: "+Util.bytesToASCII(encryptData));
                out.flush();
                if (len == buffer.length && len < BUFFER_SIZE_MAX) { // 自动调整缓冲区大小
                    buffer = new byte[len + BUFFER_SIZE_STEP];
                } else if (len < (buffer.length - BUFFER_SIZE_STEP) && (buffer.length - BUFFER_SIZE_STEP) >= BUFFER_SIZE_MIN) {
                    buffer = new byte[buffer.length - BUFFER_SIZE_STEP];
                }
            }
            Util.log("EncryptForward 51");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
