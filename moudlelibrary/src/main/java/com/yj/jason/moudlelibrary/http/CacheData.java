package com.yj.jason.moudlelibrary.http;

public class CacheData {
    private String mKeyUrl;
    private String mResult;

    public CacheData(){

    }

    public CacheData(String url,String result){
        this.mKeyUrl=url;
        this.mResult=result;
    }

    public String getKeyUrl() {
        return mKeyUrl;
    }

    public String getResult() {
        return mResult;
    }
}
