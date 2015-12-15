package com.idealsense.novahttpv3.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gongjiangpeng on 15/12/9.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NovaRequest {
    String name() default "";

    boolean isFile() default false;

    int requestID();
}
