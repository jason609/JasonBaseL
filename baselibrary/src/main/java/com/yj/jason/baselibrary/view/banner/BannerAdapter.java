package com.yj.jason.baselibrary.view.banner;

import android.view.View;

public abstract class BannerAdapter {

    //根据位置获取Viewpager的子view
    public abstract View getView(int position);

    //获取轮播的数量
    public abstract int getCount();

    public  String getBannerDesc(int mCurrentPostion){
        return "";
    };
}
