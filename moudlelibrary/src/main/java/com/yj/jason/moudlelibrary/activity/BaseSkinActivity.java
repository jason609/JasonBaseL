package com.yj.jason.moudlelibrary.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.yj.jason.baselibrary.utils.StatusBarUtil;
import com.yj.jason.baselibrary.base.BaseActivity;
import com.yj.jason.moudlelibrary.skin.SkinAttrSupport;
import com.yj.jason.moudlelibrary.skin.SkinManager;
import com.yj.jason.moudlelibrary.skin.SkinResource;
import com.yj.jason.moudlelibrary.skin.attr.SkinAttr;
import com.yj.jason.moudlelibrary.skin.attr.SkinView;
import com.yj.jason.moudlelibrary.skin.callback.ISkinChangeListener;
import com.yj.jason.moudlelibrary.skin.config.SkinPreUtils;
import com.yj.jason.moudlelibrary.skin.support.SkinAppCompatViewInflater;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

//换肤预留activity
public class BaseSkinActivity extends BaseActivity implements LayoutInflaterFactory ,ISkinChangeListener{


    private SkinAppCompatViewInflater mAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater=LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(inflater,this);

        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        //拦截view 的创建

        //1.创建view

        View view=createView(parent,name,context,attrs);

      //  Log.e("tag","view==="+view);

        //2.解析属性 src textcolor  background 自定义属性

        if(view!=null) {

            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);

            SkinView skinView=new SkinView(view,skinAttrs);

            //3.统一交给skinManager管理
            
            manageSkinView(skinView);


            //4.判断一下要不要换肤
            if(SkinManager.getInstance().checkChangeSkin(skinView)){

                StatusBarUtil.setStatusBarColor(this, Color.parseColor("#FF4081"));

            }else {
                StatusBarUtil.setStatusBarColor(this, Color.parseColor("#303F9F"));
            }


        }



        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unRegisiter(this);
    }

    //统一管理skinView
    private void manageSkinView(SkinView skinView) {

       List<SkinView> skinViews= SkinManager.getInstance().getSkinViews(this);

       if(skinViews==null){
             skinViews=new ArrayList<>();

             SkinManager.getInstance().regisiter(this,skinViews);
       }

       skinViews.add(skinView);
    }


    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
    @SuppressLint("RestrictedApi")
    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }


    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public  void changeSkin(SkinResource skinResource) {
        if(!SkinPreUtils.getInstance(this).getSkinPaht().equals("")){
            StatusBarUtil.setStatusBarColor(this, Color.parseColor("#303F9F"));
        }else {
            StatusBarUtil.setStatusBarColor(this, Color.parseColor("#FF4081"));
        }
    }



}
