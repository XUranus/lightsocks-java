package server;


import crypto.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class StreamForwardThread extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(StreamForwardThread.class);

    private final Crypto crypto;

    private static final int BUFFER_SIZE_MIN = 1024 * 128; // 128K
    private static final int BUFFER_SIZE_MAX = 1024 * 512; // 512K
    private static final int BUFFER_SIZE_STEP = 1024 * 128; // 128K

    // Do Not Close Any Stream In This Class !
    private final InputStream in;
    private final OutputStream out;
    private final boolean useEncrypt;
    private final byte[] buffer;

    /**
     * in ---decrypt/encrypted----> out
     */
    public StreamForwardThread(InputStream in, OutputStream out, Crypto crypto, Boolean useEncrypt) {
        this.in = in;
        this.out = out;
        this.buffer = new byte[BUFFER_SIZE_MIN];
        this.crypto = crypto;
        this.useEncrypt = useEncrypt;
    }

    public void run() {
        try {
            int len;

            if (useEncrypt) {
                while ((len = crypto.readEncrypt(in, buffer)) != -1) {
                    if (len == 0) continue;
                    byte[] tmp = Arrays.copyOf(buffer, len);
                    out.write(buffer, 0, len);
                }
            } else {
                while ((len = crypto.readDecrypt(in, buffer)) != -1) {
                    if (len == 0) continue;
                    byte[] tmp = Arrays.copyOf(buffer, len);
                    out.write(buffer, 0, len);
                }
            }

            out.flush();

        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
