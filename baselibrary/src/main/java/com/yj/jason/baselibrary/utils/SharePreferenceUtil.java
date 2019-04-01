package com.yj.jason.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

//SharePreference存储工具类
public class SharePreferenceUtil {

    private static SharePreferenceUtil mInstance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private SharePreferenceUtil(){}

    public static SharePreferenceUtil getInstance(){
        if(mInstance==null){
            synchronized (SharePreferenceUtil.class){
                if(mInstance==null){
                    mInstance=new SharePreferenceUtil();
                }
            }
        }
        return mInstance;
    }


    public void init(Context context){
        mSharedPreferences=context.getApplicationContext().getSharedPreferences("chace",Context.MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();
    }

    public SharePreferenceUtil saveString(String key,String value){
        mEditor.putString(key,value);
        return this;
    }
    public SharePreferenceUtil saveInt(String key,int value){
        mEditor.putInt(key,value);
        return this;
    }
    public SharePreferenceUtil saveFloat(String key,float value){
        mEditor.putFloat(key,value);
        return this;
    }
    public SharePreferenceUtil saveBoolean(String key,boolean value){
        mEditor.putBoolean(key,value);
        return this;
    }
    public SharePreferenceUtil saveLong(String key,long value){
        mEditor.putLong(key,value);
        return this;
    }

    public String getString(String key){
        return mSharedPreferences.getString(key,"");
    }
    public int getInt(String key,int defaultVaule){
        return mSharedPreferences.getInt(key,defaultVaule);
    }
    public Float getFloat(String key,float defaultVaule){
        return mSharedPreferences.getFloat(key,defaultVaule);
    }
    public Boolean getBoolean(String key,boolean defaultVaule){
        return mSharedPreferences.getBoolean(key,defaultVaule);
    }
    public Long getLong(String key,long defaultVaule){
        return mSharedPreferences.getLong(key,defaultVaule);
    }

    public void commit(){
        mEditor.commit();
    }

}
