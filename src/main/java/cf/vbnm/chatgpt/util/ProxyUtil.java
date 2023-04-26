package cf.vbnm.chatgpt.util;

import java.net.InetSocketAddress;
import java.net.Proxy;


public class ProxyUtil {
    private ProxyUtil() {
    }

    /**
     * http 代理
     *
     * @param ip   地址
     * @param port 端口
     * @return http 代理
     */
    public static Proxy http(String ip, int port) {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    }

    /**
     * socks5 代理
     *
     * @param ip   地址
     * @param port 端口
     * @return socks5 代理
     */
    public static Proxy socks5(String ip, int port) {
        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ip, port));
    }
}
