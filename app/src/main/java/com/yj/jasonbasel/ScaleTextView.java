package com.yj.jasonbasel;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class ScaleTextView extends AppCompatTextView {

    private ScaleGestureDetector scaleGestureDetector;
    private float curScale,preScale=1.0f;
    private float textSize;

    public ScaleTextView(Context context) {
        this(context,null);
    }

    public ScaleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScaleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        scaleGestureDetector=new ScaleGestureDetector(context,new MyScaleGestureDetector());
        textSize=px2sp(context,getTextSize());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return scaleGestureDetector.onTouchEvent(motionEvent);
            }
        });
    }


    private class MyScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector){

            if(preScale>=5){
                preScale=5;
            }else if(preScale<=0.8){
                preScale=0.8f;
            }

            curScale = detector.getScaleFactor() * preScale;
           // Log.i("tag","scale==="+curScale);

            if(curScale>=5||curScale<0.8){//当放大倍数大于5或者缩小倍数小于0.1倍 就不伸缩图片 返回true取消处理伸缩手势事件
                preScale=curScale;
                return true;
            }


            setTextSize(textSize*curScale);


            preScale=curScale;//保存上一次的伸缩值
            return false;
        }
    }


    public int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


}
