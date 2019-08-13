package com.sendi.signalling.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class JedisClusterConfig {

    @Value("${spring.redis.clusterNodes}")
    private String clusterNodes;
    @Value("${spring.redis.commandTimeout}")
    private int commandTimeout;
    @Value("${spring.redis.pool.maxActive}")
    private int maxActive;
    @Value("${spring.redis.pool.minIdle}")
    private int minIdle;
    @Value("${spring.redis.pool.maxWait}")
    private int maxWait;
    @Value("${spring.redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.maxAttempts}")
    private int maxAttempts;

    /**
     * 注意：
     * 这里返回的JedisCluster是单例的，并且可以直接注入到其他类中去使用
     *
     * @return
     */
    @Bean
    public JedisCluster getJedisCluster() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setMinIdle(minIdle);
        String[] serverArray = clusterNodes.split(",");//获取服务器数组
        Set<HostAndPort> nodes = new HashSet<>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }

        return new JedisCluster(nodes, commandTimeout, commandTimeout, maxAttempts, password, config);//不需要密码连接的创建对象方式
    }

}