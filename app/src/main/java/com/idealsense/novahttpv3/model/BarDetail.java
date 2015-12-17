package com.idealsense.novahttpv3.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gongjiangpeng on 15/11/5.
 */
public class BarDetail extends Bar{


    public List<Comment> comments;

    public ArrayList<String> photos;

    public List<Wine> recommend_goods;

    public List<CostListBean> cost_lists;
}
