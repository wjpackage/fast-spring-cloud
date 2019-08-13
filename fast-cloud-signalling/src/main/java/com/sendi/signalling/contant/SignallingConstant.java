package com.sendi.signalling.contant;

import com.sendi.signalling.annotation.SignallingMessage;
import com.sendi.signalling.handler.IHandler;
import com.sendi.signalling.util.SpringContextHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fiendo
 * @version 1.0 (2019/7/12)
 */
public class SignallingConstant {

    public static final Map<String,IHandler> HANDLERS = new HashMap<>();

    public static final String MSG_END = "\n";
    public static final String FIELDS_SEPARATOR = ",";

    public static void init(){
        handlerInit();
    }

    /**
     * 消息处理器初始化，处理不同消息
     */
    private static void handlerInit(){
        HANDLERS.clear();
        Collection<IHandler> handlers =  SpringContextHolder.getApplicationContext().getBeansOfType(IHandler.class).values();
        for(IHandler handler : handlers){
            SignallingMessage signallingMessage = handler.getClass().getAnnotation(SignallingMessage.class);
            HANDLERS.put(signallingMessage.value(),handler);
        }
    }

}
