package com.yj.jason.moudlelibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.yj.jason.moudlelibrary.activity.BaseSkinActivity;
import com.yj.jason.moudlelibrary.skin.attr.SkinView;
import com.yj.jason.moudlelibrary.skin.callback.ISkinChangeListener;
import com.yj.jason.moudlelibrary.skin.config.SkinConfig;
import com.yj.jason.moudlelibrary.skin.config.SkinPreUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//皮肤管理类
public class SkinManager {

    private static SkinManager mInstance;

    private Context mContext;


    private SkinResource mSkinResource;

    private Map<ISkinChangeListener,List<SkinView>>mSkinViews=new HashMap<>();

    static {
        mInstance=new SkinManager();
    }

    public static SkinManager getInstance(){
        return mInstance;
    }

    public void init(Context context){
        mContext=context.getApplicationContext();

        //做一些措施 防止皮肤被删除

        String currentSkinPath=SkinPreUtils.getInstance(mContext).getSkinPaht();

        File file=new File(currentSkinPath);

        if(!file.exists()){
            //如果文件不存在，清空sp
            SkinPreUtils.getInstance(mContext).clearSkinInfo();
            return;
        }

        String packageName=context.getPackageManager().getPackageArchiveInfo(currentSkinPath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName;

        if(TextUtils.isEmpty(packageName)){
            SkinPreUtils.getInstance(mContext).clearSkinInfo();
            return;
        }

        //做一些初始化的工作

        mSkinResource=new SkinResource(mContext,currentSkinPath);

    }


    //加载皮肤
    public int loakSkin(String path){

        File file=new File(path);

        if(!file.exists()){
            return SkinConfig.SKIN_FILE_NOEXISTS;
        }


        String packageName=mContext.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).applicationInfo.packageName;

        if(TextUtils.isEmpty(packageName)){
            SkinPreUtils.getInstance(mContext).clearSkinInfo();
            return SkinConfig.SKIN_FILE_NOERROR;
        }

        //当前皮肤如果一样  不用换

        String currentSkinPaht=SkinPreUtils.getInstance(mContext).getSkinPaht();

        if(currentSkinPaht.equals(path)){
            return SkinConfig.SKIN_CHANGE_NOTING;
        }

        //1.校验签名

        //2.初始化资源管理

        mSkinResource =new SkinResource(mContext,path);

       changeSkin();

        //保存皮肤的状态
        saveSkinStatus(path);


        return 0;
    }


    //改变皮肤
    private void changeSkin(){
        Set<ISkinChangeListener> keys= mSkinViews.keySet();


        for(ISkinChangeListener activity:keys){

            List<SkinView>skinViews=mSkinViews.get(activity);

            for(SkinView skinView:skinViews){
                skinView.skin();

                activity.changeSkin(mSkinResource);

            }
        }
    }


    private void saveSkinStatus(String path) {
        SkinPreUtils.getInstance(mContext).saveSkinPath(path);
    }

    //恢复默认
    public int retoreDefault(){
        String currentSkinPaht=SkinPreUtils.getInstance(mContext).getSkinPaht();

        if(TextUtils.isEmpty(currentSkinPaht)){
            return SkinConfig.SKIN_CHANGE_NOTING;
        }

        //当前app 的apk路径
        String path=mContext.getPackageResourcePath();

        mSkinResource =new SkinResource(mContext,path);

        changeSkin();

        SkinPreUtils.getInstance(mContext).clearSkinInfo();

        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    //获取skinView 集合  通过activity
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    public void regisiter(ISkinChangeListener activity, List<SkinView> skinViews) {
        mSkinViews.put(activity,skinViews);
    }


    public SkinResource getSkinResource() {
        return mSkinResource;
    }


    //检查是否需要换肤
    public boolean checkChangeSkin(SkinView skinView) {
        //如果当前保存了皮肤路径  换一下皮肤
        String currengSkinPath=SkinPreUtils.getInstance(mContext).getSkinPaht();
        if(!TextUtils.isEmpty(currengSkinPath)){
            skinView.skin();

            return true;
        }

        return false;

    }

    //防止内存泄漏
    public void unRegisiter(ISkinChangeListener listener) {
           mSkinViews.remove(listener);
    }

}
