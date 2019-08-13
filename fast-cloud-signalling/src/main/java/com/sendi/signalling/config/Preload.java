package com.sendi.signalling.config;

import com.sendi.signalling.contant.SignallingConstant;
import com.sendi.signalling.handler.HandlerFactory;
import com.sendi.signalling.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author fiendo
 * @version 1.0 (2019/7/12)
 */
@Slf4j
@Order(1)
@Component
public class Preload implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
        log.info("custom config initialization...start");
        SignallingConstant.init();
        SpringContextHolder.getBean(HandlerFactory.class).init();
        log.info("custom config initialization...end");
    }

}