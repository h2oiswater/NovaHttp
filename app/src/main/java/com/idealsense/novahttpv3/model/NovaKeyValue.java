package com.idealsense.novahttpv3.model;

/**
 * Created by gongjiangpeng on 15/12/9.
 */
public class NovaKeyValue {
    public String key;
    public String type;
    public Object data;

    public NovaKeyValue(String paramName, Object fieldValue, String type) {
        key = paramName;
        data = fieldValue;
        this.type = type;
    }
}
