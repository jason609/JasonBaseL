package com.yj.jason.moudlelibrary.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class StepView extends View{

    private int mRadius=100;
    private int mStrokWidth=20;
    private int mOutStrokWidth=20;
    private Paint mLargeCirclePaint,mTextPaintCenter,mTextPaintTop,mTextPaintBottom,mOutCirclePaint;

    private String centerText="0";
    private String topText="饮食摄入";
    private String bottomText="kcal";


    private float sweepAngle=0,startAngle=-90;


    private int smallRadius=10;

    private float smallX,smallY;


    public StepView(Context context) {
        this(context,null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLargeCirclePaint=new Paint();
        mLargeCirclePaint.setColor(Color.WHITE);
        mLargeCirclePaint.setStyle(Paint.Style.STROKE);
        mLargeCirclePaint.setStrokeWidth(mStrokWidth);
        mLargeCirclePaint.setAntiAlias(true);

        mOutCirclePaint=new Paint();
        mOutCirclePaint.setColor(Color.RED);
        mOutCirclePaint.setStyle(Paint.Style.STROKE);
        mOutCirclePaint.setStrokeWidth(mStrokWidth);
        mOutCirclePaint.setAntiAlias(true);
        mOutCirclePaint.setStrokeCap(Paint.Cap.ROUND);



        mTextPaintCenter=new Paint();
        mTextPaintCenter.setColor(Color.RED);
        mTextPaintCenter.setTextSize(40);
        mTextPaintTop=new Paint();
        mTextPaintTop.setColor(Color.GREEN);
        mTextPaintTop.setTextSize(30);
        mTextPaintBottom=new Paint();
        mTextPaintBottom.setColor(Color.BLUE);
        mTextPaintBottom.setTextSize(30);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width,width);
    }

    public void setProgress(float progress){
        float total=360*progress;

        ValueAnimator animator = ValueAnimator.ofFloat(0, total);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                sweepAngle= (float) valueAnimator.getAnimatedValue();

                Log.i("tags","value==="+sweepAngle);

                //centerX + rectF.width() / 2 * (float) Math.sin(360 * currentPercent * Math.PI / 180),
                //                    centerY - rectF.width() / 2 * (float) Math.cos(360 * currentPercent * Math.PI / 180)

                smallX= (float) (getWidth()/2+(getWidth()/2-mStrokWidth-10)*Math.sin(sweepAngle*Math.PI/180));
                smallY= (float) (getHeight()/2-(getWidth()/2-mStrokWidth-10)*Math.cos(sweepAngle*Math.PI/180));

                centerText=(int)sweepAngle+"";

                invalidate();
            }
        });


        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(3000);

        animator.start();
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2-mStrokWidth-10,mLargeCirclePaint);

        float dy=0;

        Paint.FontMetricsInt fm = mTextPaintCenter.getFontMetricsInt();
        dy = getHeight()/2 -(fm.bottom-fm.top)/2- fm.top;


        Rect rect=new Rect();

        mTextPaintCenter.getTextBounds(centerText,0,centerText.length(),rect);


        canvas.drawText(centerText,(getWidth()-mStrokWidth*2)/2-rect.width()/2,dy,mTextPaintCenter);



        Paint.FontMetricsInt fm2 = mTextPaintTop.getFontMetricsInt();
        dy = getHeight()/2 -(fm2.bottom-fm2.top)/2- fm2.top;

        Rect rect1=new Rect();

        mTextPaintTop.getTextBounds(topText,0,topText.length(),rect1);

        canvas.drawText(topText,(getWidth()-mStrokWidth*2)/2-rect1.width()/2,dy-80,mTextPaintTop);




        Paint.FontMetricsInt fm3 = mTextPaintBottom.getFontMetricsInt();
        dy = getHeight()/2 -(fm3.bottom-fm3.top)/2- fm3.top;

        Rect rect2=new Rect();

        mTextPaintBottom.getTextBounds(bottomText,0,bottomText.length(),rect2);

        canvas.drawText(bottomText,(getWidth()-mStrokWidth*2)/2-rect2.width()/2,dy+80,mTextPaintBottom);


        RectF rect3=new RectF(mOutStrokWidth+10,mOutStrokWidth+10,getWidth()-mOutStrokWidth-10,getHeight()-mOutStrokWidth-10);

        canvas.drawArc(rect3,startAngle,sweepAngle,false,mOutCirclePaint);



        mLargeCirclePaint.setColor(Color.BLUE);

        canvas.drawCircle(smallX,smallY,smallRadius,mLargeCirclePaint);


        mLargeCirclePaint.setColor(Color.WHITE);

        canvas.drawCircle(smallX,smallY,2,mLargeCirclePaint);



    }
}
