package com.yj.jason.moudlelibrary.skin.attr;

import android.view.View;

public class SkinAttr {

    private String mResName;

    private SkinType mType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName=resName;
        this.mType=skinType;
    }


    public void skin(View mView) {
           mType.skin(mView,mResName);
    }
}

