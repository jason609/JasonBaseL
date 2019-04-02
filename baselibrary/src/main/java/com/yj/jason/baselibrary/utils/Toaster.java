package com.yj.jason.baselibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.Toast;

import com.yj.jason.baselibrary.R;

//Toast工具类
public  class Toaster {

    private static Context mContext;

    // Toast对象
    private static Toast toast;

    // 文字显示的颜色
    private static int messageColor=R.color.color_ffffff;

    public static void init(Context context){
        mContext=context.getApplicationContext();
    }

    public static void show(String text){
          if(mContext!=null) {
              showToast(mContext, text, Toast.LENGTH_SHORT);
          }else {
             MyLog.d("请初始化Toaster!!!");
          }
    }

    public static void show(int resId){
          showToast(mContext,mContext.getResources().getString(resId),Toast.LENGTH_LONG);
    }


    private static void showToast(Context context, String massage, int duration) {
        // 设置显示文字的颜色
        SpannableString spannableString = new SpannableString(massage);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, messageColor));
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (toast == null) {
            toast = Toast.makeText(context, spannableString, duration);
        } else {
            toast.setText(spannableString);
            toast.setDuration(duration);
        }
        // 设置显示的背景
      //  View view = toast.getView();
      //  view.setBackgroundResource(R.drawable.toast_frame_style);
        // 设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移200个单位，
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 50);
        toast.show();
    }



    /**
     * 在UI界面隐藏或者销毁前取消Toast显示
     */
    public static void cancel() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

}
