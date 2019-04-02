package com.yj.jason.baselibrary.utils;

import android.util.Log;

//日志工具类
public class MyLog {

    private static final boolean LOG_DEBUG=true;
    private static final String TAG="jason609";

    public static void i(String text){
        if(LOG_DEBUG){
            Log.i(TAG,text);
        }
    }
    public static void d(String text){
        if(LOG_DEBUG){
            Log.d(TAG,text);
        }
    }

}
