package com.sendi.signalling.handler.impl;

import com.sendi.signalling.annotation.SignallingMessage;
import com.sendi.signalling.contant.SignallingConstant;
import com.sendi.signalling.handler.IHandler;
import org.springframework.stereotype.Component;

/**
 * @author fiendo
 * @version 1.0 (2019/7/12)
 */
@SignallingMessage("100")
@Component
public class HeartBeatHandler implements IHandler {

    @Override
    public String run(String msg) {
        return "101" + SignallingConstant.MSG_END;
    }

}
