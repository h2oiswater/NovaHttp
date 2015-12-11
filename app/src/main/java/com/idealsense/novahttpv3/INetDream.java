package com.idealsense.novahttpv3;

/**
 * Created by gongjiangpeng on 15/12/9.
 */
public interface INetDream {

    void pre(int requestID);

    void done(int requestID, String result);

    void error(int requestID, String errMsg);

}
