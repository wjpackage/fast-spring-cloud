package com.sendi.signalling.server;

import com.sendi.signalling.handler.HandlerFactory;
import com.sendi.signalling.service.EMailService;
import com.sendi.signalling.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author fiendo
 * @version 1.0 (2019/7/12)
 */
@Slf4j
public class ServerInstance extends Thread {

    private ServerSocket serverSocket;
    private int listenPort;
    private int socketMaxConnection;
    private String signallingCharset;

    ServerInstance(int port,int maxConnection,String charset){
        this.listenPort = port;
        this.signallingCharset = charset;
        this.socketMaxConnection = maxConnection;
    }

    @Override
    public void run(){
        boolean isStartListen = false;
        while(!isStartListen){
            log.info("signalling server starting...");
            isStartListen = startListen();
            if(!isStartListen){
                try {
                    sleep(5* 1000);
                } catch (InterruptedException e) {
                    log.warn("",e);
                }
            }
        }
        log.info("signalling Server start,listen port:{}",listenPort);
        try {
            handleSelector();
        } catch (IOException e) {
            log.error("",e);
            SpringContextHolder.getBean(EMailService.class).send("signallingStart","信令Socket通道启动失败",e.getMessage());
        }
    }

    private boolean startListen(){
        try{
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(listenPort),socketMaxConnection);
            return true;
        }catch (IOException e){
            log.error("",e);
            SpringContextHolder.getBean(EMailService.class).send("signallingListenPortFail","信令Socket通道监听端口"+listenPort+"失败",e.getMessage());
            return false;
        }
    }

    private void handleSelector() throws IOException {
        while(!serverSocket.isClosed()){
            Socket socket = serverSocket.accept();
            HandlerFactory.getInstance().printHandlerPoolState();
            HandlerFactory.getInstance().execute(socket,signallingCharset);
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
