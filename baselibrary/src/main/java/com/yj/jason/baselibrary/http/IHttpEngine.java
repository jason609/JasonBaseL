package com.yj.jason.baselibrary.http;

import android.content.Context;

import java.util.Map;

//引擎规范
public interface IHttpEngine {

    //get请求
    void get(Context context,boolean cache,String url, Map<String ,Object> params,EngineCallBack callBack);

    //post请求
    void post(Context context,boolean cache,String url, Map<String ,Object> params, EngineCallBack callBack);

    //下载文件


    //上传文件


    //https 添加证书

}
