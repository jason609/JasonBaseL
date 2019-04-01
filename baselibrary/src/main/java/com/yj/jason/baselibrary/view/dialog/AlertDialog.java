package com.yj.jason.baselibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.jason.baselibrary.R;

import java.lang.ref.WeakReference;

public class AlertDialog extends Dialog{

    private AlertController mAlert;

    public AlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mAlert=new AlertController(this,getWindow());
    }


    public void setText(int viewId, CharSequence text) {
       mAlert.setText(viewId,text);
    }


    public  <T extends View>T getView(int viewId) {
        return mAlert.getView(viewId);
    }


    public void setOnClickListener(int viewId, View.OnClickListener listener) {
       mAlert.setOnClickListener(viewId,listener);
    }


    public static class Builder{
        private AlertController.AlertParams p;

        public Builder(Context context){
            this(context, R.style.dialog);

        }
        public Builder(Context context,int themeResId){
           p=new AlertController.AlertParams(context,themeResId);
        }


        public Builder setContentView(View view){
            p.mView=view;
            return this;
        }

        public Builder setContentView(int layoutId){
            p.mLayoutId=layoutId;
            return this;
        }


        //配置一些万能的参数 如全屏
        public Builder fullWidth(){
            p.mWidth= ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }


        //从底部弹出
        public Builder fromBottom(boolean isAnimation){
            if(isAnimation){
              //  p.mAnimation=R.style.
            }
            p.mGravity= Gravity.BOTTOM;
            return this;
        }


        //设置动画
        public Builder setAnimation(int animation){
            p.mAnimation=animation;
            return this;
        }


        //设置宽高
        public Builder setWidthAndHeight(int width,int height){
            p.mWidth= width;
            p.mHeight=height;
            return this;
        }

        public Builder setText(int viewId,CharSequence text){
            p.textMap.put(viewId,text);
            return this;
        }

        public Builder setOnClickListener(int viewId,View.OnClickListener listener){
            p.listenerMap.put(viewId,listener);
            return this;
        }


        public AlertDialog.Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.p.mOnCancelListener = onCancelListener;
            return this;
        }

        public AlertDialog.Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.p.mOnDismissListener = onDismissListener;
            return this;
        }

        public AlertDialog.Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.p.mOnKeyListener = onKeyListener;
            return this;
        }




        public AlertDialog create() {
            AlertDialog dialog = new AlertDialog(p.mContext, p.mThemeResId);
            p.apply(dialog.mAlert);
            dialog.setCancelable(this.p.mCancelable);
            if (this.p.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }

            dialog.setOnCancelListener(this.p.mOnCancelListener);
            dialog.setOnDismissListener(this.p.mOnDismissListener);
            if (this.p.mOnKeyListener != null) {
                dialog.setOnKeyListener(this.p.mOnKeyListener);
            }

            return dialog;
        }

        public AlertDialog show() {
            AlertDialog dialog = this.create();
            dialog.show();
            return dialog;
        }


    }

}
