package com.yj.jason.baselibrary.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;

class AlertController {

    private AlertDialog mDialog;
    private Window mWindow;

    private ViewHelper helper;

    public AlertController(AlertDialog alertDialog, Window window) {
        this.mDialog=alertDialog;
        this.mWindow=window;
    }


    public void setHelper(ViewHelper helper) {
        this.helper = helper;
    }

    public void setText(int viewId, CharSequence text) {
        helper.setText(viewId,text);
    }


    public  <T extends View>T getView(int viewId) {
        return helper.getView(viewId);
    }


    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        helper.setOnClickListener(viewId,listener);
    }


    public AlertDialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public static class AlertParams{

        public Context mContext;
        public int mThemeResId;
        //点击空白是否能够取消
        public boolean mCancelable=true;
        //dialog 取消监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //dialog 消失监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //dialog 按键监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        public View mView;
        public int mLayoutId;


        public SparseArray<CharSequence> textMap=new SparseArray<>();
        public SparseArray<View.OnClickListener> listenerMap=new SparseArray<>();
        public int mWidth= ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight= ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mAnimation=0;
        public int mGravity= Gravity.CENTER;



        public AlertParams(Context context, int themeResId) {
               this.mContext=context;
               this.mThemeResId=themeResId;
        }

        //绑定和设置参数
        public void apply(AlertController mAlert) {

            ViewHelper viewHelper=null;
            if(mLayoutId!=0){
                viewHelper=new ViewHelper(mContext,mLayoutId);
            }

            if(mView!=null){
                viewHelper=new ViewHelper();
                viewHelper.setContentView(mView);
            }

            if(viewHelper==null){
                throw new IllegalArgumentException("请设置布局setContentView()");
            }


            mAlert.getDialog().setContentView(viewHelper.getContentView());


            mAlert.setHelper(viewHelper);


            int size=textMap.size();

            for(int i=0;i<size;i++){
                mAlert.setText(textMap.keyAt(i),textMap.valueAt(i));
            }


            int num=listenerMap.size();

            for(int i=0;i<num;i++){
                mAlert.setOnClickListener(listenerMap.keyAt(i),listenerMap.valueAt(i));
            }


            Window window=mAlert.getWindow();

            window.setGravity(mGravity);

            if(mAnimation!=0) {
                window.setWindowAnimations(mAnimation);
            }

            WindowManager.LayoutParams params=window.getAttributes();
            params.width=mWidth;
            params.height=mHeight;
            window.setAttributes(params);

        }
    }
}
