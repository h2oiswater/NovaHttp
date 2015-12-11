package com.idealsense.novahttpv3;

/**
 * Created by gongjiangpeng on 15/12/9.
 */
public class MasterFu {

    INetDream iNetDream;

    RequestParamManager mParamManager;

    public void bind(Object obj){
        iNetDream = (INetDream)obj;
    }

    public void go(int requestID){
        iNetDream.pre(requestID);

        if( mParamManager == null )mParamManager = RequestParamManager.createANewOne();

        mParamManager.collectParams(iNetDream, requestID);

    }

}
