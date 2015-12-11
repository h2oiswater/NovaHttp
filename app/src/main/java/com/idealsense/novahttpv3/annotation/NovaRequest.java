package com.idealsense.novahttpv3.annotation;

/**
 * Created by gongjiangpeng on 15/12/9.
 */
public @interface NovaRequest {
    String name() default "";

    boolean isFile() default false;

    int requestID();
}
