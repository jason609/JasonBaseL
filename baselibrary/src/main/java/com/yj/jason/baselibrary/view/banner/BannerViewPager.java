package com.yj.jason.baselibrary.view.banner;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.yj.jason.baselibrary.utils.Toaster;

import java.io.File;
import java.lang.reflect.Field;

public class BannerViewPager extends ViewPager{

    private Context mContext;

    //自定义的页面切换的scroll
    private  BannerScroller scroller;

    //自定义的adapter
    private BannerAdapter mBannerAdapter;

    private final int SCROLL_MSG=0*0011;

    //默认延迟 页面切换间隔时间
    private int mDelayTime=3500;

    private int currentPos;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //每隔多少秒后切换到下一页
            setCurrentItem(getCurrentItem()+1);
            //不断循环执行
            startRoll();
        }
    };
    private int mScrollDuration;

    public BannerViewPager(@NonNull Context context) {
        this(context,null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;

        //改变viewpgager  切换速率  通过反射拿mScroll
        try {
            Field filed = ViewPager.class.getDeclaredField("mScroller");

            scroller=new BannerScroller(context);

            filed.setAccessible(true);

            //设置参数  第一参数object代表当前属性在哪个类 第二个参数代表要设置的值
            filed.set(this,scroller);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //设置动画执行时间
    public void setScrollDuration(int mScrollDuration) {
        scroller.setScrollDuration(mScrollDuration);
    }


    public void setAdapter(@Nullable BannerAdapter adapter) {
        mBannerAdapter=adapter;
        setAdapter(new BannerPagerAdapter());
    }

    public void setPostiong(int pos){
        currentPos=pos;
    }

    //自动轮播
    public void startRoll(){
        //清除消息
        mHandler.removeMessages(SCROLL_MSG);
        //消息延迟时间  用户自定义  默认值
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG,mDelayTime);

        Log.i("tag","自动轮播=======");

    }


    //销毁handler 停止发送  解决内存泄漏

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(mContext instanceof Activity){
            Activity activity=(Activity) mContext;

            if(activity.isDestroyed()){
                if(mHandler!=null)
                    mHandler.removeMessages(SCROLL_MSG);
                mHandler=null;
            }
        }


    }

    private int lastPos;

    private class BannerPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            //无限循环  int最大值
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            //官方推荐这么写
            return view==object;
        }


        //创建viewpager 条目回调的方法
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            //采用adapter 设计模式 为了完全让用户自定义
            View view=mBannerAdapter.getView(currentPos);

            container.addView(view);

            return view;
        }


        //销毁viewpager条目回调的方法
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            object=null;
        }
    }

}
