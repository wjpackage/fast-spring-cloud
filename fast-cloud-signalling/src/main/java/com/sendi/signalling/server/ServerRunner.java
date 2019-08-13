package com.sendi.signalling.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(100)
@Component
public class ServerRunner implements CommandLineRunner {

    @Value("${signalling.listen-port}")
    private Integer listenPort;

    @Value("${signalling.socketMaxConnection}")
    private Integer maxConnection;

    @Value("${signalling.charset}")
    private String charset;

    @Value("${signalling.socketSwitch}")
    private boolean socketSwitch;

    @Value("${signalling.checkDelay}")
    private Integer checkDelay;

    @Override
    public void run(String... strings) throws Exception {
        ServerInstance serverInstance = new ServerInstance(listenPort,maxConnection,charset);
        serverInstance.setDaemon(true);
        serverInstance.start();
        ServerCheckThread serverCheckThread = new ServerCheckThread(serverInstance,checkDelay,listenPort,maxConnection,charset);
        serverCheckThread.setDaemon(true);
        serverCheckThread.start();
    }

}
