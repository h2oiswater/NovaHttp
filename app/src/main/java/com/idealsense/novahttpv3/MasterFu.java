package com.idealsense.novahttpv3;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by gongjiangpeng on 15/12/9.
 */
public class MasterFu {
    final String TAG = "MasterFu";

    INetDream iNetDream;

    RequestParamManager mParamManager;

    HttpUtils mHttpUtils;

    public static MasterFu create() {
        return new MasterFu();
    }

    private MasterFu(){
        mHttpUtils = new HttpUtils();
    }

    public void bind(Object obj){
        iNetDream = (INetDream)obj;
    }

    public void go(final int requestID){
        iNetDream.pre(requestID);

        if( mParamManager == null ) mParamManager = RequestParamManager.createANewOne();

        mParamManager.collectParams(iNetDream, requestID);

        mHttpUtils.send(HttpRequest.HttpMethod.POST, mParamManager.mURL, mParamManager.createParams(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.v(TAG, "onSuccess : " + responseInfo.result);
                mParamManager.setValues(responseInfo.result);
                iNetDream.done(requestID, responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.v(TAG, "onFailure : " + s);
            }
        });

    }

    public void go(){
        go(1);
    }

}