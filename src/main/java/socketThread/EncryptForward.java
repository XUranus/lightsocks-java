package socketThread;


import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Arrays;

public class EncryptForward extends Thread {
    /*
    read from A and encrypt send to B
    * */

    private final static Logger logger = LoggerFactory.getLogger(EncryptForward.class);

    private InputStream in;
    private OutputStream out;
    //private Object parent;

    private byte[] buffer;
    private Crypto crypto;

    private boolean isRunning;

    private static final int BUFFER_SIZE_MIN = 1024 * 128; // 缓冲区最小值，128K
    private static final int BUFFER_SIZE_MAX = 1024 * 512; // 缓冲区最大值，512K
    private static final int BUFFER_SIZE_STEP = 1024 * 128; // 缓冲区自动调整的步长值，128K

    public EncryptForward(InputStream in, OutputStream out, Crypto crypto) {
        this.buffer = new byte[BUFFER_SIZE_MIN];
        this.in = in;
        this.out = out;
        this.isRunning = true;
        this.crypto = crypto;
    }

    public void run() {
        try {
            int len = 0;
            while ((len = in.read(buffer)) != -1 && isRunning) {
                byte[] rawData = Arrays.copyOfRange(buffer, 0, len);
                //Util.log("EncryptForward 39 "+Util.bytesToHexString(rawData));
                //Util.log("EncryptForward GET: "+Util.bytesToASCII(rawData));
                byte[] encryptData = crypto.encrypt(rawData);
                if (encryptData == null) {
                    break; // 加密出错，退出
                }
                out.write(encryptData);
                //logger.info("send en_len="+encryptData.length);
                //Util.log("EncryptForward SENT: "+Util.bytesToASCII(encryptData));
                out.flush();
                if (len == buffer.length && len < BUFFER_SIZE_MAX) { // 自动调整缓冲区大小
                    buffer = new byte[len + BUFFER_SIZE_STEP];
                } else if (len < (buffer.length - BUFFER_SIZE_STEP) && (buffer.length - BUFFER_SIZE_STEP) >= BUFFER_SIZE_MIN) {
                    buffer = new byte[buffer.length - BUFFER_SIZE_STEP];
                }
            }
        } catch (SocketException e) {
            isRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
