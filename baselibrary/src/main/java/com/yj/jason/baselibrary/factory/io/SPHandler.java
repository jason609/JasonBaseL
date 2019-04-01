package com.yj.jason.baselibrary.factory.io;

import android.view.View;

import com.yj.jason.baselibrary.utils.SharePreferenceUtil;

import java.security.Key;

public class SPHandler implements IOHandler{
    @Override
    public void saveString(String key, String value) {
        SharePreferenceUtil.getInstance().saveString(key,value).commit();
    }

    @Override
    public String getString(String key) {
        return SharePreferenceUtil.getInstance().getString(key);
    }
}
