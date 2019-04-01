package com.yj.jason.moudlelibrary.http;

import android.content.Context;

import com.google.gson.Gson;
import com.yj.jason.baselibrary.http.EngineCallBack;
import com.yj.jason.baselibrary.http.HttpUtils;

import java.util.Map;

public abstract class HttpCallBack<T> implements EngineCallBack{

    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
           //添加公共参数
           onPreExecute();
    }


    @Override
    public void onSuccess(String result) {
           Gson gson=new Gson();
           T t= (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this));
           onSuccess(t);
    }

    //子类去重写  加载进度条
    public void onPreExecute(){

    }

    //返回可以直接操作的对象
    public abstract void onSuccess(T result);
}
