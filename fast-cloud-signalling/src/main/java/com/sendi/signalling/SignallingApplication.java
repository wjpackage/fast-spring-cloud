package com.sendi.signalling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author fiendo
 * @version 1.0 (2019/7/13)
 */
@SpringBootApplication(scanBasePackages="com.sendi.signalling")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.sendi.signalling.feign")
public class SignallingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SignallingApplication.class,args);
    }
}
