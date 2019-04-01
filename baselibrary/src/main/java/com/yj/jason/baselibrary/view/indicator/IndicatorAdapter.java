package com.yj.jason.baselibrary.view.indicator;

import android.view.View;
import android.view.ViewGroup;

public abstract class IndicatorAdapter {

    //获取条目总个数
    public abstract int getCount();

    //获取view
    public abstract View getView(ViewGroup parent,int position);

    //高亮选中指示器
    public  void highLightIndicator(View view){}


    //重置指示器
    public  void restoreIndicator(View view){}

    //添加底部指示器
    public View getBottomTrackView(){
        return null;
    }
}
