package com.yj.jason.moudlelibrary.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ListViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

public class DragVerticalListView extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private View mDragListView,mMenuView;
    private int mMenuHeight;
    private boolean intercept,mMenuIsOpen;


    public DragVerticalListView(@NonNull Context context) {
        this(context,null);
    }

    public DragVerticalListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragVerticalListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mViewDragHelper=ViewDragHelper.create(this,mCallBack);

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count=getChildCount();
        if(count!=2){
            throw new RuntimeException("DragVerticalListView只能包含两个布局");
        }

        mDragListView=getChildAt(1);

        mMenuView=getChildAt(0);

    }

    float lastx,lasty=0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(mMenuIsOpen){
            return true;
        }
       // intercept=super.onInterceptTouchEvent(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mViewDragHelper.processTouchEvent(ev);
                lastx=ev.getX();
                lasty=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float currenty=ev.getY();

                if(currenty>lasty&&!canChildScrollUp()){
                   return true;
                }

                break;

        }
        return super.onInterceptTouchEvent(ev);
    }


    public boolean canChildScrollUp() {
        if (mDragListView != null) {
            if (android.os.Build.VERSION.SDK_INT < 14) {
                if (mDragListView instanceof AbsListView) {
                    final AbsListView absListView = (AbsListView) mDragListView;
                    return absListView.getChildCount() > 0
                            && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                            .getTop() < absListView.getPaddingTop());
                } else {
                    return ViewCompat.canScrollVertically(mDragListView, -1) || mDragListView.getScrollY() > 0;
                }
            } else {
                return ViewCompat.canScrollVertically(mDragListView, -1);
            }
        }
        return false;
    }




    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed)
        mMenuHeight=mMenuView.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback mCallBack=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            //指定该子View 是否可以拖动
            return mDragListView==view;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            //垂直拖动移动的位置

            if(top<0){
                top=0;
            }

            if(top>mMenuHeight){
                top=mMenuHeight;
            }

            return top;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            Log.i("tags","手指松开==="+yvel);
            if(releasedChild==mDragListView) {
                if (mDragListView.getTop() > mMenuHeight / 2) {
                    mViewDragHelper.settleCapturedViewAt(0, mMenuHeight);
                    mMenuIsOpen=true;
                } else {
                    mMenuIsOpen=false;
                    mViewDragHelper.settleCapturedViewAt(0, 0);
                }

                invalidate();
            }
        }


        /*@Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            //水平拖动移动的位置
            return left;
        }*/
    };


    @Override
    public void computeScroll() {
       if(mViewDragHelper.continueSettling(true)){
           invalidate();
       }
    }
}
