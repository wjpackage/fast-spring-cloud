package com.sendi.signalling.client;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * @author fiendo
 * @version 1.0 (2019/7/13)
 */
@Slf4j
public class SignallingClientDemo {

    public void run(String ip, int port) throws IOException {
        startClient(ip, port);
    }

    private void startClient(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        try (
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(in))
        ) {
            int times = 0;
            while (times <= 10) {
                String callId = Thread.currentThread().getId() + "" + System.currentTimeMillis();
                long start = System.currentTimeMillis();
                out.write( ("200,"+callId +","+(10000000000L + times )+","+(20000000000L + times)+"\n" ).getBytes());
                out.flush();
                String line = buff.readLine();
                long end = System.currentTimeMillis();
                log.info("cost: {} data: {}", end - start ,line);
                times++;
            }
        }
    }

}
