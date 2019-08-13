package com.sendi.signalling.server;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fiendo
 * @version 1.0 (2019/7/13)
 */
@Slf4j
public class ServerCheckThread extends Thread {

    private ServerInstance serverInstance;

    private long checkDelay;

    private int listenPort;
    private int socketMaxConnection;
    private String signallingCharset;

    ServerCheckThread(ServerInstance instance, long delay, int port, int maxConnection, String charset) {
        this.serverInstance = instance;
        this.checkDelay = delay;
        this.listenPort = port;
        this.signallingCharset = charset;
        this.socketMaxConnection = maxConnection;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(checkDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isError = serverInstance == null || serverInstance.getServerSocket().isClosed();
            if (isError) {
                log.warn("socket服务端异常关闭，开始重启");
                serverInstance = new ServerInstance(listenPort, socketMaxConnection, signallingCharset);
                serverInstance.start();
            }
        }
    }
}
