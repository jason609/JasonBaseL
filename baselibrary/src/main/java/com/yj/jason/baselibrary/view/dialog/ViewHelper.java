package com.yj.jason.baselibrary.view.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

class ViewHelper {

    private View mContentView;

    private SparseArray<WeakReference<View>>mViews;

    public ViewHelper(Context mContext, int mLayoutId) {
        this();
        mContentView= LayoutInflater.from(mContext).inflate(mLayoutId,null);
    }

    public ViewHelper() {
        mViews=new SparseArray<>();
    }

    public void setContentView(View mView) {
        this.mContentView=mView;
    }

    public void setText(int viewId, CharSequence text) {
        TextView textView=getView(viewId);
        if(textView!=null){
            textView.setText(text);
        }
    }

    public  <T extends View>T getView(int viewId) {
        WeakReference<View>viewWeakReference=mViews.get(viewId);
        View view=null;

        if(viewWeakReference!=null){
            view=viewWeakReference.get();
        }

        if(view==null){
            view=mContentView.findViewById(viewId);
            if(view!=null)
            mViews.put(viewId,new WeakReference<View>(view));
        }

        return (T)view;
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view=getView(viewId);
        if(view!=null){
            view.setOnClickListener(listener);
        }
    }

    public View getContentView() {
        return mContentView;
    }
}
