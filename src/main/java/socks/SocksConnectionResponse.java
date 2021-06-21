package socks;

public class SocksConnectionResponse {

    /*
     a common success response
     */
    public static byte[] responseBytes() {
        byte[] response = new byte[10];
        response[0] = 0x05;
        response[1] = 0x00;
        response[2] = 0x00;
        response[3] = 0x01;

        response[4] = 0x00;
        response[5] = 0x00;
        response[6] = 0x00;
        response[7] = 0x00;

        response[8] = 0x10;
        response[9] = 0x10;
        return response;
    }
}
