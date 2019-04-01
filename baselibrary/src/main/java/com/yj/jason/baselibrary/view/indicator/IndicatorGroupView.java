package com.yj.jason.baselibrary.view.indicator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

//指示器容器 包含item 和底部追踪器
public class IndicatorGroupView extends FrameLayout{

    private LinearLayout contianer;
    private View mBottomTrackView;
    private int mItemWidht;
    private FrameLayout.LayoutParams params;

    private int mInitLeftMargin;


    public IndicatorGroupView(@NonNull Context context) {
        this(context,null);
    }

    public IndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        contianer=new LinearLayout(context);//子布局的容器
        addView(contianer);
    }

    public void addItemView(View chileView) {
        contianer.addView(chileView);
    }

    public View getItemView(int position) {
        return contianer.getChildAt(position);
    }

    //添加底部跟踪指示器
    public void addBottomTrackView(View bottomTrackView, int mItemWidht) {
        if(bottomTrackView==null){
            return;
        }

        this.mBottomTrackView=bottomTrackView;
        this.mItemWidht=mItemWidht;
        addView(mBottomTrackView);
        params= (LayoutParams) mBottomTrackView.getLayoutParams();

        int trackWidth=params.width;

        if(trackWidth==params.MATCH_PARENT){
            trackWidth=mItemWidht;
        }

        if(trackWidth>mItemWidht){
            trackWidth=mItemWidht;
        }

        params.width=trackWidth;

        params.gravity= Gravity.BOTTOM;

        mInitLeftMargin=(mItemWidht-trackWidth)/2;

        params.leftMargin=mInitLeftMargin;

        mBottomTrackView.setLayoutParams(params);
    }


    //底部的指示器滚动
    public void scrollBottomTrackView(int position, float positionOffset) {
        if(mBottomTrackView==null)return;
        int leftMargin= (int) ((position+positionOffset)*mItemWidht);
        params.leftMargin=leftMargin+mInitLeftMargin;
        mBottomTrackView.setLayoutParams(params);
    }

    //点击移动  带动画
    public void scrollBottomTrackView(int position) {

        if(mBottomTrackView==null)return;

        int finalLeftMargin= (int) ((position)*mItemWidht)+mInitLeftMargin;
        int currentLeftMargin=params.leftMargin;

        int distance=finalLeftMargin-currentLeftMargin;

        //开启动画
        ValueAnimator animator=ObjectAnimator.ofFloat(currentLeftMargin,finalLeftMargin).setDuration((long) (Math.abs(distance)*0.4f));

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                 float currentLeftMargin= (float) valueAnimator.getAnimatedValue();
                params.leftMargin= (int) currentLeftMargin;
                mBottomTrackView.setLayoutParams(params);
            }
        });

        animator.setInterpolator(new DecelerateInterpolator());

        animator.start();
    }
}
