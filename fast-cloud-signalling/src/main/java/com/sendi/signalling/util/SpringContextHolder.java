package com.sendi.signalling.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author fiendo
 * @version 1.0 (2019/7/12)
 */
@Component
public class SpringContextHolder implements ApplicationContextAware{

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 获取上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过名字获取上下文中的bean
     *
     * @param name 对象注册名称
     * @param <T>  赋值对象类型
     * @return 具体对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 通过类型获取上下文中的bean
     *
     * @param clazz 对象类型
     * @param <T>   赋值对象类型
     * @return 具体对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }


}
