package com.yj.jason.baselibrary.view.NavigationBar;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * description：添加默认的配置
 * <p>
 * Created by 曾辉 on 2016/6/30 22:35
 * QQ：240336124
 * Email: 240336124@qq.com
 * Version：1.0
 */
public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.NavigationParams> implements INavigation {

    private P params;

    private View view;

    public AbsNavigationBar(P params) {
        this.params = params;
        createAndBind();
    }

    protected String getString(int id) {
        return this.params.context.getResources().getString(id);
    }

    protected int getColor(int id) {
        return ContextCompat.getColor(this.params.context, id);
    }

    protected P getParams() {
        return params;
    }


    /**
     * 设置文本
     * @param viewId
     * @param text
     */
    protected void setText(int viewId, CharSequence text) {
        TextView tv = findViewById(viewId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    /**
     * 设置点击事件
     * @param viewId
     * @param listener
     */
    protected void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }


    /**
     * 设置背景资源
     * @param viewId
     * @param resourceId
     */
    protected void setImageResource(int viewId, int resourceId) {
        ImageView imageView = findViewById(viewId);
        if (imageView != null) {
            imageView.setImageResource(resourceId);
        }
    }

    protected <T extends View> T findViewById(int id) {
        return (T) view.findViewById(id);
    }


    /**
     * 创建和绑定布局
     */
    public void createAndBind() {
        if (params == null) {
            return;
        }

        view = LayoutInflater.from(params.context).inflate(bindLayoutId(), params.parent);

        if(params.context instanceof Activity) {
            Activity activity = (Activity) params.context;
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            ViewGroup contentView=(ViewGroup) decorView.getChildAt(0);
            contentView.addView(view,0);
        }

        applyView();
    }


    private static View getRootView(Context context) {
        Activity activity = (Activity) context;
        return activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }


    // 构建导航条类 这个类只是定义默认的配置 具体功能的实现一定由具体的实现类决定
    public abstract static class Builder {

        // 构建导航条方法
        public abstract AbsNavigationBar create();

        // 默认的配置参数
        public static class NavigationParams {
            public Context context;
            public ViewGroup parent;

            public NavigationParams(Context context, ViewGroup parent) {
                this.context = context;
                this.parent = parent;
            }
            public NavigationParams(Context context) {
                this.context = context;
            }
        }
    }
}

