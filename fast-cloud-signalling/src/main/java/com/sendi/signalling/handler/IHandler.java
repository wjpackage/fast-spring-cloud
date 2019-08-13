package com.sendi.signalling.handler;

import java.io.BufferedWriter;

/**
 * @author fiendo
 * @version 1.0 (2019/7/12)
 * 消息处理接口类
 */
public interface IHandler {

    /**
     * 处理从信令侧客户端发送过来的数据，并返回响应
     * 消息结束符位\n
     * 消息内各个字段以,分隔
     * @param msg 请求信息
     * @return 响应信息，以\n结尾
     */
    String run(String msg);

}
