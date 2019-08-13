package com.sendi.signalling.annotation;


import java.lang.annotation.*;

/**
 * @author fiendo
 * @version 1.0 (2019/7/12)
 * 信令消息
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignallingMessage {

    String value() default "";

}
