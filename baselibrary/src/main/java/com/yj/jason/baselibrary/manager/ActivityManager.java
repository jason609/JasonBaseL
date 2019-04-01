package com.yj.jason.baselibrary.manager;

import android.app.Activity;

import java.util.Stack;

//activity 统一管理类
public class ActivityManager {

    private static ActivityManager mInstance;

    private Stack<Activity>mActivities;//因为添加删除操作多和安全性考虑，选择stack

    private ActivityManager(){
        mActivities=new Stack<>();
    }

    public static ActivityManager getInstance(){
        if(mInstance==null){
            synchronized (ActivityManager.class){
                if(mInstance==null){
                    mInstance=new ActivityManager();
                }
            }
        }
        return mInstance;
    }


    //添加activity
    public void attch(Activity activity){
         mActivities.add(activity);
    }


    //删除activity
    public void detach(Activity detachActivity){
        int size=mActivities.size();

        for(int i=0;i<mActivities.size();i++){
            Activity activity=mActivities.get(i);
            if(activity==detachActivity){
                mActivities.remove(i);
                i--;
                size--;
            }
        }

    }


    //关闭activity
    public void finishActivity(Activity finishActivity){
        int size=mActivities.size();

        for(int i=0;i<mActivities.size();i++){
            Activity activity=mActivities.get(i);
            if(activity==finishActivity){
                mActivities.remove(i);
                activity.finish();
                i--;
                size--;
            }
        }

    }


    //根据类名关闭activity
    public void finishActivity(Class<? extends Activity> finishClass){
        int size=mActivities.size();
        for(int i=0;i<mActivities.size();i++){
            Activity activity=mActivities.get(i);
            if(activity.getClass().getCanonicalName().equals(finishClass.getCanonicalName())){
                mActivities.remove(i);
                activity.finish();
                i--;
                size--;
            }
        }
    }

    //退出应用  关闭所有activity
    public void exitApp(){
        int size=mActivities.size();
        for(int i=0;i<mActivities.size();i++){
                Activity activity=mActivities.get(i);
                mActivities.remove(i);
                activity.finish();
                i--;
                size--;
            }
    }


    //获取当前activity
    public Activity currentActivity(){
        return mActivities.lastElement();
    }


}
