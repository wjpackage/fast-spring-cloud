package com.sendi.signalling.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fiendo
 * @version 1.0 (2019/7/13)
 */
@Data
@Component
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "signalling.handler.thread")
public class SignallingThreadConfig {
    /**
     * 同时运行最大线程数
     */
//    @Value("${signalling.handler.thread.core}")
    private int corePoolSize;
    /**
     * 线程池最大线程数
     */
//    @Value("${signalling.handler.thread.maximum}")
    private int maximum;
    /**
     * 线程最大激活时间
     */
//    @Value("${signalling.handler.thread.idle-alive-time}")
    private long keepAliveTime;


}
