package com.yj.jason.moudlelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;
    //持有外部数据库的引用
    private SQLiteDatabase mSqLiteDatabase;

    private DaoSupportFactory(){

        //把数据库放在内存卡里  细节处理  运行时权限  判断内存卡

        File dbRoot=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+
        "ndfy"+File.separator+"database");

        if(!dbRoot.exists()){
            dbRoot.mkdirs();
        }

        File dbFile=new File(dbRoot,"ndfy.db");

        //打开或者创建数据库
        mSqLiteDatabase=SQLiteDatabase.openOrCreateDatabase(dbFile,null);
    }

    public static DaoSupportFactory getFactory(){
        if(mFactory==null){
            synchronized (DaoSupportFactory.class){
                if(mFactory==null){
                    mFactory=new DaoSupportFactory();
                }
            }
        }

        return mFactory;
    }


    public <T>IDaoSupport<T> getDao(Class<T>clazz){
         IDaoSupport<T>daoSupport=new DaoSupport();
         daoSupport.init(mSqLiteDatabase,clazz);
         return daoSupport;
    }
}
