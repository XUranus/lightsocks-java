package socks;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.RemoteServer;

import java.util.Arrays;

public class SocksConnectionRequest {
    /*
     * Data VER CMD RSV ATYP DST.ADDR           SDT.PORT
     *       1   1   1    1  Variable              2
     *       05  01  00  01  00000000             1010
     *       05  01  00  03  0962616964752E636F6D 0050
     * */


    private final static Logger logger = LoggerFactory.getLogger(SocksConnectionRequest.class);

    private final byte[] data;

    private byte version;
    private byte cmd;
    private byte rsv;
    private byte atyp;
    private String addr;
    private int port;

    private SocksConnectionRequest(byte[] data) {
        this.data = data;
        this.version = data[0];
        this.cmd = data[1];
        this.rsv = data[2];
        this.atyp = data[3];


        if (atyp == 0x01) { //ipv4
            addr = bytesToIpv4(Arrays.copyOfRange(data, 4, data.length - 2));
        } else if (atyp == 0x03) { //domain
            addr = bytesToDomain(Arrays.copyOfRange(data, 5, data.length - 2));
        } else if (atyp == 0x04) { //ipv6
            addr = bytesToIpv6(Arrays.copyOfRange(data, 4, data.length - 2));
        } else {
            logger.error("unknown atyp: " + atyp);
        }

        port = (data[data.length - 2] & 0xFF) * 256 + (data[data.length - 1] & 0xFF);
    }

    private static boolean validate(byte[] data) {
        if (data.length < 7) return false;
        if (data[0] != 0x05) return false;
        if (data[2] != 0x00) return false;
        return data[3] == 0x01 || data[3] == 0x03 || data[3] == 0x04;
    }

    public static SocksConnectionRequest fromBytes(byte[] data) {
        if(validate(data)) {
            return new SocksConnectionRequest(data);
        } else {
            return null;
        }
    }


    public String getAddress() {
        return addr;
    }

    public int getDstPort() {
        return port;
    }

    private String bytesToIpv4(byte[] addrBytes) {
        String ipv4AddrString = "";
        for (byte addrByte : addrBytes) {
            ipv4AddrString = ipv4AddrString.concat((addrByte & 0xFF) + ".");
        }
        return ipv4AddrString.substring(0, ipv4AddrString.length() - 1);
    }

    private String bytesToDomain(byte[] addrBytes) {
        StringBuilder domainString = new StringBuilder(addrBytes.length);
        for (byte addrByte : addrBytes) {
            domainString.append((char) addrByte);
        }
        return domainString.toString();
    }

    private String bytesToIpv6(byte[] addrBytes) {
        String ipv6AddrString = "";
        for (byte addrByte : addrBytes) {
            ipv6AddrString = ipv6AddrString.concat(String.valueOf(addrByte) + ":");
        }
        return ipv6AddrString.substring(0, ipv6AddrString.length() - 1);
    }

}
