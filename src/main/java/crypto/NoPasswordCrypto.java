package crypto;

public class NoPasswordCrypto implements Crypto {
    /**
     * 极其不安全 无加密 仅仅测试用
     * */

    public byte[] encrypt(byte[] data) {
        return data;
    }

    public byte[] decrypt(byte[] data) {
        return data;
    }

}
