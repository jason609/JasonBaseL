package com.yj.jason.moudlelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Method;

//皮肤资源类
public class SkinResource {

    private Resources mSkinResource;

   // private String packageName="com.yj.jason.skin";
    private String packageName="";

    public SkinResource(Context context, String path) {
        try {
            Resources supRes=context.getResources();

            AssetManager assetManager=AssetManager.class.newInstance();

            Method method=AssetManager.class.getDeclaredMethod("addAssetPath",String.class);

            method.invoke(assetManager,path);

            mSkinResource=new Resources(assetManager,supRes.getDisplayMetrics(),supRes.getConfiguration());

            packageName=context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).applicationInfo.packageName;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


  public Drawable getDrawableByName(String resName){
        try {
            int resId= mSkinResource.getIdentifier(resName,"drawable",packageName);
            return mSkinResource.getDrawable(resId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
  }

  public ColorStateList getColorByName(String resName){
      try {
          int resId= mSkinResource.getIdentifier(resName,"color",packageName);
          return mSkinResource.getColorStateList(resId);
      }catch (Exception e){
          e.printStackTrace();
          return null;
      }
  }

}
