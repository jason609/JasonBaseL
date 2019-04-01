package com.yj.jason.moudlelibrary.skin.config;

import android.content.Context;

public class SkinPreUtils {

    private static SkinPreUtils mInstance;

    private Context mContext;

    private SkinPreUtils(Context context){
           this.mContext=context.getApplicationContext();
    }


    public static SkinPreUtils getInstance(Context context) {
        if(mInstance==null){
            synchronized (SkinPreUtils.class){
                if(mInstance==null){
                    mInstance=new SkinPreUtils(context);
                }
            }
        }
        return mInstance;
    }


    //保存皮肤路径
    public void saveSkinPath(String skinPaht){
        mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME,Context.MODE_PRIVATE)
        .edit().putString(SkinConfig.SKIN_PATH_NAME,skinPaht).commit();
    }


    //获取皮肤路径
    public String getSkinPaht(){
       return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME,Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH_NAME,"");
    }


    //清空皮肤路径
    public void clearSkinInfo() {
        saveSkinPath("");
    }
}
