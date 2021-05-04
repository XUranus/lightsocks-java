package socks;


import java.util.Arrays;

public class SocksConnectionRequest {
    /*
     * Data VER CMD RSV ATYP DST.ADDR           SDT.PORT
     *       1   1   1    1  Variable              2
     *       05  01  00  01  00000000             1010
     *       05  01  00  03  0962616964752E636F6D 0050
     * */
    private byte[] data;

    private SocksConnectionRequest(byte[] data) {
        this.data = data;
    }

    public static SocksConnectionRequest getNewInstance(byte[] data) {
        if(data.length<7) return null;
        if(data[0]!=0x05) return null;
        if(data[2]!=0x00) return null;
        if(data[3]!=0x01 && data[3]!=0x03 && data[3]!=0x04) return null;
        return new SocksConnectionRequest(data);
    }

    public byte getVersion() {
        return data[0];
    }

    public byte getCMD() {
        return data[1];
    }

    public String getAddress() {
        if(data[3]==0x01) { //ipv4
            return bytesToIpv4(Arrays.copyOfRange(data,4,data.length-2));
        } else if(data[3]==0x03) { //domain
            return bytesToDomain(Arrays.copyOfRange(data,5,data.length-2));
        } else if(data[3]==0x04) { //ipv6
            return bytesToIpv6(Arrays.copyOfRange(data,4,data.length-2));
        } else return null;
    }

    public int getDstPort() {
        return (data[data.length-2]&0xFF)*256 + (data[data.length-1]&0xFF);
    }

    private String bytesToIpv4(byte[] addrBytes) {
        String ipv4AddrString = "";
        for(int i=0;i<addrBytes.length;i++) {
            ipv4AddrString = ipv4AddrString.concat((addrBytes[i] & 0xFF) +".");
        }
        return ipv4AddrString.substring(0,ipv4AddrString.length()-1);
    }

    private String bytesToDomain(byte[] addrBytes) {
        StringBuffer domainString = new StringBuffer(addrBytes.length);
        for (int i = 0; i < addrBytes.length; i++) {
            domainString.append((char) addrBytes[i]);
        }
        return domainString.toString();
    }

    private String bytesToIpv6(byte[] addrBytes) {
        String ipv6AddrString = "";
        for(int i= 0;i<addrBytes.length;i++) {
            ipv6AddrString = ipv6AddrString.concat(String.valueOf(addrBytes[i])+":");
        }
        return ipv6AddrString.substring(0,ipv6AddrString.length()-1);
    }

}
