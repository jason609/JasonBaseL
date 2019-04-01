package com.yj.jason.moudlelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yj.jason.moudlelibrary.skin.SkinManager;
import com.yj.jason.moudlelibrary.skin.SkinResource;

public enum SkinType {

    TEXT_COLOR("textColor"){
        @Override
        public void skin(View mView, String mResName) {
            SkinResource resource=getSkinResource();

           ColorStateList color= resource.getColorByName(mResName);

           if(color==null)return;

            TextView textView=(TextView) mView;

            textView.setTextColor(color);


        }
    },BACKGROUND("background"){
        @Override
        public void skin(View mView, String mResName) {

            //背景可能是图片  还可能是颜色

            SkinResource resource=getSkinResource();

            Drawable drawable= resource.getDrawableByName(mResName);

            if(drawable !=null){
                ImageView imageView=(ImageView) mView;
                imageView.setBackgroundDrawable(drawable);
            }

            ColorStateList color= resource.getColorByName(mResName);

           if(color!=null){
               mView.setBackgroundColor(color.getDefaultColor());
           }
        }
    },SRC("src"){
        @Override
        public void skin(View mView, String mResName) {
            SkinResource resource=getSkinResource();

            Drawable drawable= resource.getDrawableByName(mResName);

            if(drawable !=null){
                ImageView imageView=(ImageView) mView;
                imageView.setImageDrawable(drawable);

                return;
            }
        }
    };

    private static SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }


    private String mResName;

    SkinType(String resName){
        this.mResName=resName;
    }

    public abstract void skin(View mView, String mResName);

    public String getResName() {
        return mResName;
    }
}
