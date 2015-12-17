package com.idealsense.novahttpv3.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gongjiangpeng on 15/9/15.
 */
public class User{

    @Expose
    @SerializedName("name")
    public String userName;
    @Expose
    @SerializedName("id")
    public String userID;
    @Expose
    public String token;
    @Expose
    public String avatar;
    @Expose
    public String mobile;
    @Expose
    public int status;
    @Expose
    public String gender;
    @Expose
    public String age;

    @Expose( serialize = false, deserialize = false)
    public String password;

    public String signature;

    public User(){
        userName = "";
        userID   = "";
        token    = "";
        avatar   = "";
        mobile   = "";
        status   = 0;
        gender   = "";
        age      = "";
    }

}
