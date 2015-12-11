package com.idealsense.novahttpv3;

import android.text.TextUtils;

import com.idealsense.novahttpv3.annotation.NovaRequest;
import com.idealsense.novahttpv3.annotation.NovaResponse;
import com.idealsense.novahttpv3.model.NovaKeyValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gongjiangpeng on 15/12/9.
 */
public class RequestParamManager {

    List<NovaKeyValue> mParams;
    List<NovaKeyValue> mResults;

    public static RequestParamManager createANewOne(){
        return new RequestParamManager();
    }

    private RequestParamManager(){
        mParams = new ArrayList<>();
        mResults = new ArrayList<>();
    }

    public void collectParams(INetDream iNetDream, int requestID) {
        mParams.clear();
        mResults.clear();

        List<Field> fields = getAllField(iNetDream.getClass());

        NovaRequest reqAnn;
        NovaResponse repAnn;

        for(Field field:fields){
            field.setAccessible(true);

            reqAnn = field.getAnnotation(NovaRequest.class);



            if( reqAnn != null && reqAnn.requestID() == requestID ){
                // check value
                Object fieldValue;
                try {
                    fieldValue = field.get(iNetDream);
                }catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    continue;
                }

                if( fieldValue == null ) continue;

                String paramName;

                paramName = reqAnn.name();
                if( TextUtils.isEmpty(paramName) ){
                    paramName = field.getName();
                }

                mParams.add(new NovaKeyValue(paramName, fieldValue));

                continue;
            }

            repAnn = field.getAnnotation(NovaResponse.class);

            if( repAnn != null && repAnn.requestID() == requestID ){
                String dataName = repAnn.mama();
                if( TextUtils.isEmpty(dataName) ) continue;
                mResults.add(new NovaKeyValue(dataName, field));
            }
        }
    }

    private List<Field> getAllField(Class<?> cls){
        List<Field> list = new ArrayList<>();

        Field[] fields = cls.getDeclaredFields();

        for(Field f:fields){
            list.add(f);
        }

        if( cls.getSuperclass() != null ){
            list.addAll(getAllField(cls.getSuperclass()));
        }

        return list;
    }
}
