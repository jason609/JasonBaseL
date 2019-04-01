package com.yj.jason.baselibrary.view.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class BannerScroller extends Scroller{

    //动画持续的时间
    private int mScrollDuration=950;

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy,int duration) {
        super.startScroll(startX, startY, dx, dy,mScrollDuration);
    }


    //设置动画执行时间
    public void setScrollDuration(int mScrollDuration) {
        this.mScrollDuration = mScrollDuration;
    }
}
