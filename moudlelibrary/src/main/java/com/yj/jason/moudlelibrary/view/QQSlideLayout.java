package com.yj.jason.moudlelibrary.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

public class QQSlideLayout extends HorizontalScrollView {

    private int mMenuWidth;
    private int mMarginWidth=100;
    private int mScreenWidth;

    private View menuView,contentView,mShadView;

    private GestureDetector mGestureDetector;

    private boolean mMenuIsOpen=false;//菜单是否打开
    private boolean isIntercept=false;//是否拦截

    public QQSlideLayout(Context context) {
        this(context,null);
    }

    public QQSlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQSlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth=getScreenWidht(context);
        mMenuWidth=mScreenWidth-mMarginWidth;
        mGestureDetector=new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //处理快速滑动  只要快速滑动 就会回调
                //velocityX  快速往左边滑 是一个负数，快速往右边话是一个正数

                if(mMenuIsOpen){
                    if(velocityX<0&&Math.abs(velocityX)-Math.abs(velocityY)>0){
                         closeMenu();
                    }
                }else {
                    if(velocityX>0&&Math.abs(velocityX)-Math.abs(velocityY)>0){
                        openMenu();
                    }else {
                        closeMenu();
                    }
                }

                return true;
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(isIntercept)return true;

        //快速滑动触发了，不能往下执行
        if(
        mGestureDetector.onTouchEvent(ev)
        ){
            return true;
        }
        if(ev.getAction()==MotionEvent.ACTION_UP){
            int currentScrollX=getScrollX();

            if(currentScrollX>mMenuWidth/2){//超过菜单的一般  打开
                closeMenu();
            }else {//关闭
                openMenu();
            }

            return true;//确保super.onTouchEvent不会被执行

        }
        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isIntercept=false;
        if(mMenuIsOpen){
            float currentX=ev.getX();
            if(currentX>mMenuWidth){
                closeMenu();
                isIntercept=true;
                return true;
            }

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float scale=1f*l/mMenuWidth;//梯度值  1--》0

        float rightScale=0.7f+0.3f*scale;

        float leftScale=1-0.5f*scale;



        //缩放  默认以中心点缩放  设置缩放的中心点
      /*  ViewCompat.setPivotX(contentView,0);
        ViewCompat.setPivotY(contentView,contentView.getMeasuredHeight()/2);
        ViewCompat.setScaleX(contentView,rightScale);
        ViewCompat.setScaleY(contentView,rightScale);*/

    //    ViewCompat.setAlpha(menuView,leftScale);

      /*  ViewCompat.setPivotX(menuView,mMenuWidth);
        ViewCompat.setPivotY(menuView,menuView.getMeasuredHeight()/2);
        ViewCompat.setScaleX(menuView,leftScale);
        ViewCompat.setScaleY(menuView,leftScale);*/


        //设置平移
        ViewCompat.setTranslationX(menuView,l*0.6f);

        mShadView.setAlpha(1-scale);

        //抽屉效果
       /* ViewCompat.setTranslationX(menuView,l);
        ViewCompat.setTranslationY(menuView,0);*/


    }

    //关闭菜单
    private void closeMenu() {
        mMenuIsOpen=false;
        smoothScrollTo(mMenuWidth,0);
    }

    //打开菜单
    private void openMenu() {
        mMenuIsOpen=true;
        smoothScrollTo(0,0);
    }

    //布局解析完毕才会回调的方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //1.指定宽高
       ViewGroup container=(ViewGroup) getChildAt(0);

       int count=container.getChildCount();

       if(count!=2){
           throw new RuntimeException("只能放置两个子View!");
       }

        menuView=container.getChildAt(0);//菜单
        ViewGroup.LayoutParams menuParams=menuView.getLayoutParams();
        menuParams.width=mMenuWidth;
        menuView.setLayoutParams(menuParams);


        contentView=container.getChildAt(1);//内容

        ViewGroup.LayoutParams contentParams=contentView.getLayoutParams();

        container.removeView(contentView);

        RelativeLayout contentContainer=new RelativeLayout(getContext());
        contentContainer.addView(contentView);
        mShadView=new View(getContext());

        mShadView.setBackgroundColor(Color.parseColor("#55000000"));

        contentContainer.addView(mShadView);

        contentParams.width=mScreenWidth;
      //  contentView.setLayoutParams(contentParams);
        contentContainer.setLayoutParams(contentParams);

        container.addView(contentContainer);

        mShadView.setAlpha(0.0f);



    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(mMenuWidth,0);
    }

    //获取屏幕宽度
    private int getScreenWidht(Context mContext){
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

}
