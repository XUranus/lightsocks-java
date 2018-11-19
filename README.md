# Lightsocks
一个基于socks5协议的的轻量级网络代理工具，低配版shadowsocks。
 - java编写
 - 后续可持续扩展加密算法...
 - bug很多，持续优化中...

## 参考
另一位作者用golang实现的 [lightsocks](https://github.com/gwuhaolin/lightsocks)，关于ss代理的原理可以看他的教程：[你也能写个 Shadowsocks](https://github.com/gwuhaolin/blog/issues/12)

## 使用
1. 本地配置
    ```js
    {
        "host":"127.0.0.1",
        "hostPort":8090,
        "localPort":8080,
        "method":"simple",
        "password":"F9C77CB58B2F6E05B01BB22B554DFECAF4D89CCF7B6F600C993E3D4B9362CD40373B35688EC9AC313433F8975FA05B84EF227839E27DCC0659BD2D90E4C3BBC1125A10299BE3084295988DA271262A3F6756A50A041FA836254687AE3A8C77C5B97A00FA38242715D6BA17ECF5020F738ACEDEA7FDADBF9D21F7529E7E20F157D0CBA3D7DBE0F3FB536A4E07E15CB35076C6D29218B1235E8943325166A975BE547483DAAB1EFF72C4488261D40DA1B8AAB4B6E86D0144195891D5EE0EE7644FC0D1D33CF08028794AAFA41D6C88E9302E9A03145D81D96B70B74909DDC2638F86BC65E6691694C8F64C118596DF41EB1C47EA459FA61AED0BDC7F2CF2E513FC"
    }
    ```
    保存为`localConfig.json`

2. 代理服务器配置
    ```js
    {
        "port":8090,
        "method":"simple",
        "password":"F9C77CB58B2F6E05B01BB22B554DFECAF4D89CCF7B6F600C993E3D4B9362CD40373B35688EC9AC313433F8975FA05B84EF227839E27DCC0659BD2D90E4C3BBC1125A10299BE3084295988DA271262A3F6756A50A041FA836254687AE3A8C77C5B97A00FA38242715D6BA17ECF5020F738ACEDEA7FDADBF9D21F7529E7E20F157D0CBA3D7DBE0F3FB536A4E07E15CB35076C6D29218B1235E8943325166A975BE547483DAAB1EFF72C4488261D40DA1B8AAB4B6E86D0144195891D5EE0EE7644FC0D1D33CF08028794AAFA41D6C88E9302E9A03145D81D96B70B74909DDC2638F86BC65E6691694C8F64C118596DF41EB1C47EA459FA61AED0BDC7F2CF2E513FC"
    }
    ```
    保存为`serverConfig.json`

    支持的method:
     - none(无加密，不要使用)
     - simple(256个byte乱排构成的十六进制密码，上述例子中所示，已经实现，有一定的加密能力)
     - AES(尚在学习密码学...未编写)
    **注意！当前AES加密还没完善，请勿使用**

3. 运行  
    本地：`java StartLocal localConfig.json`  
    服务器：`java StartServer serverCofig.json`

4. 配置本地socks5代理  
    地址为服务器地址，端口为`localConfig.json`中的`localPort`

## 其它语言实现
- [lightsocks-golang](https://github.com/gwuhaolin/lightsocks)：Golang实现版本
- [lightsocks-python](https://github.com/linw1995/lightsocks-python)：Python语言 实现版本
- [lightsocks-android](https://github.com/XanthusL/LightSocks-Android)：Android 实现版本
- [lightsocks-node](https://github.com/chrisyer/lightsocks-nodejs)：Node.js 实现版本
- [lightsocks-c](https://github.com/LeeReindeer/lightsocks-c)：C语言 实现版本