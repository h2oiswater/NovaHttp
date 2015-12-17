package com.idealsense.novahttpv3.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gongjiangpeng on 15/11/5.
 */
public class Wine implements Parcelable{
    @Expose
    public String id;
    @Expose
    public String name;
    @Expose
    public String type;
    @Expose
    public String market_price;
    @Expose
    public String sale_price;
    @Expose
    public String image;
    @Expose
    public String descript;
    @Expose
    public String price;

    // local use
    @SerializedName("number")
    public int currentCount = 0;

    protected Wine(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        market_price = in.readString();
        sale_price = in.readString();
        image = in.readString();
        descript = in.readString();
        currentCount = in.readInt();
    }

    public static final Creator<Wine> CREATOR = new Creator<Wine>() {
        @Override
        public Wine createFromParcel(Parcel in) {
            return new Wine(in);
        }

        @Override
        public Wine[] newArray(int size) {
            return new Wine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(market_price);
        dest.writeString(sale_price);
        dest.writeString(image);
        dest.writeString(descript);
        dest.writeInt(currentCount);
    }
}
