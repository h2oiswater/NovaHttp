package com.lhave.yuebar.data.novahttpv3;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lhave.yuebar.data.novahttpv3.annotation.NovaRequest;
import com.lhave.yuebar.data.novahttpv3.annotation.NovaResponse;
import com.lhave.yuebar.data.novahttpv3.annotation.NovaUrl;
import com.lhave.yuebar.data.novahttpv3.model.NovaKeyValue;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gongjiangpeng on 15/12/9.
 */
public class RequestParamManager {

    List<NovaKeyValue> mFileParams;
    List<NovaKeyValue> mParams;
    List<NovaKeyValue> mResults;

    INetDream iNetDream;

    String mURL;

    boolean isPost = true;

    public static RequestParamManager createANewOne(){
        return new RequestParamManager();
    }

    private RequestParamManager(){
        mParams = new ArrayList<>();
        mFileParams = new ArrayList<>();
        mResults = new ArrayList<>();
    }

    public void collectParams(INetDream iNetDream, int requestID) {
        mParams.clear();
        mResults.clear();
        mFileParams.clear();
        mURL = "";
        isPost = true;
        this.iNetDream = iNetDream;

        List<Field> fields = new ArrayList<>();
        Field[] fieldsArray = iNetDream.getClass().getDeclaredFields();

        for(Field f:fieldsArray){
            fields.add(f);
        }


//        List<Field> fields = getAllField(iNetDream.getClass());

        NovaRequest reqAnn;
        NovaResponse repAnn;
        NovaUrl urlAnn;

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

                if( reqAnn.isFile() ){
                    mFileParams.add(new NovaKeyValue(paramName, fieldValue,  field.getType().getSimpleName()));
                }else{
                    mParams.add(new NovaKeyValue(paramName, fieldValue,  field.getType().getSimpleName()));
                }


                continue;
            }

            repAnn = field.getAnnotation(NovaResponse.class);

            if( repAnn != null && repAnn.requestID() == requestID ){
                String dataName = repAnn.mama();
                if( TextUtils.isEmpty(dataName) ) continue;
                mResults.add(new NovaKeyValue(dataName, field, field.getType().getSimpleName()));
                continue;
            }

            urlAnn = field.getAnnotation(NovaUrl.class);

            if( urlAnn != null && urlAnn.requestID() == requestID ){
                try {
                    mURL = field.get(iNetDream).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    mURL = "";
                }

                isPost = urlAnn.isPost();

                continue;
            }
        }

        if( mResults.size() > 0 ){
            isPost = true;
        }
    }

    public RequestParams createParams(){
        RequestParams params = new RequestParams();


        for( NovaKeyValue keyValue:mParams ){
            // 哼 我就是要写HardCode
            if( keyValue.type.equals("List") ){

                List<?> list = (List<?>)keyValue.data;
                for(int i=0;i<list.size();i++){
                    if( isPost )
                        params.addBodyParameter(keyValue.key+"["+i+"]", list.get(i).toString());
                    else
                        params.addQueryStringParameter(keyValue.key+"["+i+"]", list.get(i).toString());
                }

            }else{
                // value
                if( isPost )
                    params.addBodyParameter(keyValue.key, keyValue.data.toString());
                else
                    params.addQueryStringParameter(keyValue.key, keyValue.data.toString());
            }
        }

        for( NovaKeyValue keyValue:mFileParams ){
            // 哼 我就是要写HardCode
            if( keyValue.type.equals("List") ){

                List<?> list = (List<?>)keyValue.data;
                for(int i=0;i<list.size();i++){
                    params.addBodyParameter( keyValue.key + "[" + i + "]", new File(list.get(i).toString()) );
                }

            }else{
                if( keyValue.data instanceof CharSequence ){
                    params.addBodyParameter(keyValue.key, new File((String) keyValue.data));
                }
            }
        }

        return params;
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

    public void setValues(String result) {
        for( NovaKeyValue keyValue:mResults ){
            String[] gsonKeys = keyValue.key.split("-");

            JSONObject obj = null;
            JSONArray array = null;

            try {
                obj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                array = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if( obj == null
                    && array == null ){
                return;
            }

            Field field = (Field) keyValue.data;
            field.setAccessible(true);

            // root value is a json array
            if( array != null && gsonKeys.length == 0){
                try {
                    field.set(iNetDream, getListOrArrayData(array, field));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                continue;
            }

            // root value is a json object
            if( obj != null && gsonKeys.length == 0){
                try {
                    field.set(iNetDream, new Gson().fromJson(obj.toString(), field.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                continue;
            }


            for( int i=0;i<gsonKeys.length;i++ ){
                if( i!=gsonKeys.length-1 ){
                    obj = obj.optJSONObject(gsonKeys[i]);
                }else{

                    if( obj != null ) {
                        try {
                            setValue(field, gsonKeys[i], obj);
                        }catch (IllegalAccessException e) {
                            e.printStackTrace();
                            continue;
                        }
                    }

                }


       }
        }
    }

    private Object getListOrArrayData(JSONArray array, Field field){
        if( array == null )return null;
        if( field.getType().getSimpleName().equals("List") ){
            final Class genericType = getGenericType(field);
            List list = new ArrayList();
            for( int i=0;i<array.length();i++ ){
                list.add(getSuitData(genericType, array, i));
            }
            return list;
        }else if( field.getType().getSimpleName().equals("[]") ){
            final Class genericType = getGenericType(field);
            Object[] resultArray = new Object[array.length()];
            for( int i=0;i<array.length();i++ ){
                resultArray[i]=(getSuitData(genericType, array, i));
            }
            return resultArray;
        }
        return null;
    }

    private Object getSuitData(Class cls, JSONArray array, int index){
        switch (cls.getSimpleName()) {
            case "String":
                return array.optString(index, "");
            case "int":
            case "Integer":
                return array.optInt(index, -1);
            case "long":
            case "Long":
                return array.optLong(index, -1l);
            case "boolean":
            case "Boolean":
                return array.optBoolean(index, false);
            case "double":
            case "Double":
                return array.optDouble(index, -1d);
            default:
                Object value = new Gson().fromJson(array.optJSONObject(index).toString(), cls);
                return value;
        }
    }

    private Object getSuitData(Class cls, String gsonKey, JSONObject obj){
        switch (cls.getSimpleName()) {
            case "String":
                return obj.optString(gsonKey, "");
            case "int":
            case "Integer":
                return obj.optInt(gsonKey, -1);
            case "long":
            case "Long":
                return obj.optLong(gsonKey, -1l);
            case "boolean":
            case "Boolean":
                return obj.optBoolean(gsonKey, false);
            case "double":
            case "Double":
                return obj.optDouble(gsonKey, -1d);
            default:
                Object value = new Gson().fromJson(obj.optJSONObject(gsonKey).toString(), cls);
                return value;
        }
    }

    private void setValue(Field field, String gsonKey, JSONObject obj) throws IllegalAccessException {
        switch (field.getType().getSimpleName()){
            case "String":
                field.set(iNetDream, obj.optString(gsonKey, ""));
                break;
            case "int":
            case "Integer":
                field.set(iNetDream, obj.optInt(gsonKey, -1));
                break;
            case "long":
            case "Long":
                field.set(iNetDream, obj.optLong(gsonKey, -1l));
                break;
            case "boolean":
            case "Boolean":
                field.set(iNetDream, obj.optBoolean(gsonKey, false));
                break;
            case "double":
            case "Double":
                field.set(iNetDream, obj.optDouble(gsonKey, -1d));
                break;
            case "List":
            case "[]":
                field.set(iNetDream,getListOrArrayData(obj.optJSONArray(gsonKey), field));
                break;
            default:
                if( obj.optJSONObject(gsonKey) != null ){
                    Object value = new Gson().fromJson(obj.optJSONObject(gsonKey).toString(), field.getType());
                    field.set(iNetDream, value);
                }
                break;
        }
    }

    Class getGenericType(Field field){
        Type genericType = field.getGenericType();

        if(genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;

            Class genericClazz = (Class) pt.getActualTypeArguments()[0];
            return genericClazz;
        }

        return null;
    }



}
