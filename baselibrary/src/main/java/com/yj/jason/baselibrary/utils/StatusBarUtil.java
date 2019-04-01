package com.yj.jason.baselibrary.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

//状态栏工具类
public class StatusBarUtil {

    //为我们的activity的状态栏设置颜色
    public static void setStatusBarColor(Activity activity, int color) {

        //5.0以上

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }


        //4.4-5.0  采用一个技巧 首先弄成全屏 在状态栏的部分加一个布局

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View view = new View(activity);
            view.setBackgroundColor(color);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(view);
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            View activityView = contentView.getChildAt(0);
            activityView.setFitsSystemWindows(true);

        }
    }


    //设置activity的状态栏透明
    public static void setActivityTransluctent(Activity activity){
        //5.0以上

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView=activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        //4.4-5.0

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    //获取状态栏高度
    private static int getStatusBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int stautsBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(stautsBarHeightId);
    }


}
