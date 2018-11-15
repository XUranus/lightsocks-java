package SocketThread;

import Server.Server;
import Util.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class DecryptForward extends Thread {
    /*
read from A and encrypt send to B
* */
    private InputStream in;
    private OutputStream out;

    private byte[] buffer;

    private static final int BUFFER_SIZE_MIN = 1024 * 128; // 缓冲区最小值，128K
    private static final int BUFFER_SIZE_MAX = 1024 * 512; // 缓冲区最大值，512K
    private static final int BUFFER_SIZE_STEP = 1024 * 128; // 缓冲区自动调整的步长值，128K

    public DecryptForward(InputStream in,OutputStream out) {
        this.in = in;
        this.out = out;
        buffer = new byte[BUFFER_SIZE_MIN];
    }

    public void run() {
        try {
            int len = 0;
            while((len=in.read(buffer))!=-1) {
                byte[] encryptData = Arrays.copyOfRange(buffer,0,len);
                byte[] decryptData =  Server.cryptor.decrypt(encryptData);

                //Util.log("DecryptForward GET: "+Util.bytesToASCII(encryptData));

                if (decryptData == null) {
                    break; // 出错，退出
                }

                out.write(decryptData);
                //Util.log("DecryptForward SENT: "+Util.bytesToASCII(decryptData));
                out.flush();

                if (len == buffer.length && len < BUFFER_SIZE_MAX) { // 自动调整缓冲区大小
                    buffer = new byte[len + BUFFER_SIZE_STEP];
                } else if (len < (buffer.length - BUFFER_SIZE_STEP) && (buffer.length - BUFFER_SIZE_STEP) >= BUFFER_SIZE_MIN) {
                    buffer = new byte[buffer.length - BUFFER_SIZE_STEP];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
