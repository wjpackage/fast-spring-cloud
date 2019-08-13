package com.sendi.signalling.handler;

import com.sendi.signalling.config.SignallingThreadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author fiendo
 * @version 1.0 (2019/7/12)
 */
@Slf4j
@Component
public class HandlerFactory {

    private static HandlerFactory instance;

    /**
     * 线程池
     */
    private static ExecutorService pool;

    @Resource
    private SignallingThreadConfig config;

    /**
     * 单例私有实例化
     */
    private HandlerFactory(){
    }

    /**
     * 拿单例实例
     */
    public synchronized static HandlerFactory getInstance(){
        if (instance == null) {
            instance = new HandlerFactory();
        }
        return instance;
    }

    /**
     * 向线程池添加线程任务，返回此线程的
     * @param socket socket instance
     */
    public void execute(Socket socket, String charset){
        getInstance();
        HandlerThread handlerThread = new HandlerThread(socket,charset);
        pool.execute(handlerThread);
    }

    public void printHandlerPoolState(){
        log.info("Queue:{},Active:{},completed:{}",getActiveNum(),getQueueSize(),getCompletedTaskCount());
    }

    /**
     * 初始化线程池和服务
     */
    public void init(){
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(config.getMaximum());
        pool = new ThreadPoolExecutor(config.getCorePoolSize(),config.getMaximum(),config.getKeepAliveTime(), TimeUnit.MILLISECONDS,queue);
    }

    /**
     * 拿到活跃线程数
     */
    private int getActiveNum(){
        return ((ThreadPoolExecutor)pool).getActiveCount();
    }

    /**
     * 拿到已完成的线程数
     */
    private long getCompletedTaskCount(){
        return ((ThreadPoolExecutor)pool).getCompletedTaskCount();
    }

    /**
     * 拿到线程池队列长度
     */
    private int getQueueSize(){
        return ((ThreadPoolExecutor)pool).getQueue().size();
    }

}
