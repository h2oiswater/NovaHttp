package com.idealsense.novahttpv3.model;

/**
 * Created by gongjiangpeng on 15/12/9.
 */
public class NovaKeyValue {
    String key;
    Object data;

    public NovaKeyValue(String paramName, Object fieldValue) {
        key = paramName;
        data = fieldValue;
    }
}
