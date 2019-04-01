package com.yj.jason.baselibrary.view.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

//圆的指示器
public class DotIndicatorView extends View{

    private Drawable mDrawable;

    public DotIndicatorView(Context context) {
        this(context,null);
    }

    public DotIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DotIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawable(Drawable drawable) {
        this.mDrawable=drawable;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mDrawable!=null){
            /*mDrawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
            mDrawable.draw(canvas);*/
           Bitmap bitmap= drawableToBitmap(mDrawable);

           Bitmap circleBitmap=getCircleBitmap(bitmap);

           canvas.drawBitmap(circleBitmap,0,0,null);
            
        }
    }


    //bitmap  转化为圆形
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        //创建一个什么也没有的bitmap
        Bitmap circleBitmap=Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        //创建一个画布
        Canvas canvas=new Canvas(circleBitmap);

        Paint mPaint=new Paint();

        //设置抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);

        //防抖动
        mPaint.setDither(true);

        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,getMeasuredWidth()/2,mPaint);

        //再把原来的bitmap 绘制到圆上
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//取交集

        canvas.drawBitmap(bitmap,0,0,mPaint);

        return circleBitmap;
    }

    //drawable 转化为 bitmap
    private Bitmap drawableToBitmap(Drawable drawable) {

        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable) drawable).getBitmap();
        }

        //其他类型  colorDrawable

        //创建一个什么也没有的bitmap
        Bitmap outBitmap=Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        //创建一个画布
        Canvas canvas=new Canvas(outBitmap);

        //把drawable 画到bitmap上

        drawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());

        drawable.draw(canvas);

        return outBitmap;
    }
}
