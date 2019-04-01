package com.yj.jason.moudlelibrary.skin.attr;

import android.view.View;

import java.util.List;

public class SkinView {

    private View mView;

    private List<SkinAttr>mAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView=view;
        this.mAttrs=skinAttrs;
    }

    public void skin(){
        for(SkinAttr attr:mAttrs){
            attr.skin(mView);
        }
    }

}
