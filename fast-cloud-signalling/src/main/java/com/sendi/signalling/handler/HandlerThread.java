package com.sendi.signalling.handler;

import com.sendi.signalling.contant.SignallingConstant;
import com.sendi.signalling.service.EMailService;
import com.sendi.signalling.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * @author fiendo
 * @version 1.0 (2019/7/12)
 */
@Slf4j
public class HandlerThread implements Runnable {

    private Socket socket;
    private String signallingCharset;


    HandlerThread(Socket socket, String charset) {
        this.socket = socket;
        this.signallingCharset = charset;
    }

    @Override
    public void run() {
        while(!socket.isClosed()){
            try {
                doReadAndWrite(socket);
            } catch (Exception e) {
                log.warn("", e);
                SpringContextHolder.getBean(EMailService.class).send("signallingReadWrite","信令Socket通道读写异常",e.getMessage());
            }
        }
    }

    /**
     * read message from client
     */
    private void doReadAndWrite(Socket socket, String charset) throws IOException {
        try (
                InputStream in = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(in, charset);
                BufferedReader bufferedReader = new BufferedReader(reader);
                OutputStream out = socket.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(out);
                BufferedWriter bufferedWriter = new BufferedWriter(writer)
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                handleMessage(line, bufferedWriter);
            }
        }
    }

    /**
     * read message
     */
    private void doReadAndWrite(Socket socket) throws IOException {
        doReadAndWrite(socket, signallingCharset);
    }

    /**
     * handle message
     */
    private void handleMessage(String msg, BufferedWriter bufferedWriter) throws IOException {
        log.info("receive msg:{}", msg);
        int firstSeparatorIndex = msg.indexOf(SignallingConstant.FIELDS_SEPARATOR);
        String type;
        if(firstSeparatorIndex == -1){
            type = msg;
        }else{
            type = msg.substring(0, firstSeparatorIndex);
        }
        IHandler handler = SignallingConstant.HANDLERS.get(type);
        String response = handler.run(msg);
        bufferedWriter.write(response);
        bufferedWriter.flush();
        log.info("response msg:{}", response);
    }

}
