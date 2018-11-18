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
        "method":"AES-256",
        "password":"your password"
    }
    ```
    保存为`localConfig.json`

2. 代理服务器配置
    ```js
    {
        "port":8090,
        "method":"AES-256",
        "password":"your password"
    }
    ```
    保存为`serverConfig.json`

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