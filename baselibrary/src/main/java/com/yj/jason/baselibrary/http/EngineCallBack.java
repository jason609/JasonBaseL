package com.yj.jason.baselibrary.http;

import android.content.Context;

import java.util.Map;

public interface EngineCallBack {

    //错误
    void onError(Exception e);

    //成功
    void onSuccess(String result);

    //执行之前回调
    void onPreExecute(Context context,Map<String,Object> params);

    //默认

    public final EngineCallBack DEFAULT_CALL_BACK=new EngineCallBack() {
        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }

        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }


    };

}
