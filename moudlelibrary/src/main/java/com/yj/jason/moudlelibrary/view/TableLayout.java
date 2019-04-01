package com.yj.jason.moudlelibrary.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

public class TableLayout extends HorizontalScrollView {


    public TableLayout(Context context) {
        this(context,null);
    }

    public TableLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept=super.onInterceptTouchEvent(ev);

       /* float lastx=0,lasty=0;


        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:
                lastx=ev.getX();
                lasty=ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float curreutx=ev.getX();
                float currenty=ev.getY();


                if(Math.abs(currenty-lasty)>20){
                    intercept=false;
                } else if(Math.abs(curreutx-lastx)>20){
                    intercept=false;
                }

                break;


        }*/

        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
      //  mGestureDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }



    //布局解析完毕才会回调的方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //1.指定宽高
      /* ViewGroup container=(ViewGroup) getChildAt(0);

       int count=container.getChildCount();

       if(count!=2){
           throw new RuntimeException("只能放置两个子View!");
       }*/


    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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
