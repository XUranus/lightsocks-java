package socketThread;


import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Arrays;

public class DecryptForward extends Thread {
    /*
    read from A and encrypt send to B
    * */

    private final static Logger logger = LoggerFactory.getLogger(DecryptForward.class);

    private InputStream in;
    private OutputStream out;

    private byte[] buffer;
    private Crypto crypto;

    private static final int BUFFER_SIZE_MIN = 1024 * 128; // 缓冲区最小值，128K
    private static final int BUFFER_SIZE_MAX = 1024 * 512; // 缓冲区最大值，512K
    private static final int BUFFER_SIZE_STEP = 1024 * 128; // 缓冲区自动调整的步长值，128K

    private boolean isRunning;

    public DecryptForward(InputStream in, OutputStream out, Crypto crypto) {
        this.in = in;
        this.out = out;
        this.buffer = new byte[BUFFER_SIZE_MIN];
        this.isRunning = true;
        this.crypto = crypto;
    }

    public void run() {
        try {
            int len = 0;
            while((len=in.read(buffer))!=-1 && isRunning) {
                byte[] encryptData = Arrays.copyOfRange(buffer,0,len);
                //logger.info("get en-len="+encryptData.length);
                byte[] decryptData =  crypto.decrypt(encryptData);

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
        } catch (SocketException e) {
            //出现的问题：parent的socket在browser完成请求后就close了 in.read()会抛出socketException
            isRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
