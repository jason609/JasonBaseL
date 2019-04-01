package com.yj.jason.moudlelibrary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class DaoSupport<T> implements IDaoSupport<T> {

    private String TAG="DaoSupport";


    private SQLiteDatabase mSqLiteDatabase;
    private Class<T>mClazz;

    private static final Object[]mPutMathodArgs=new Object[2];
    private static final Map<String,Method> mMethodMap=new ArrayMap<>();

    public void init(SQLiteDatabase sqLiteDatabase,Class<T>clazz){
        this.mSqLiteDatabase=sqLiteDatabase;
        this.mClazz=clazz;

        //创建表

        StringBuffer sb=new StringBuffer();

        sb.append("create table if not exists ").append(DaoUtlis.getTableName(mClazz)).append("(id integer primary key autoincrement, ");

        //通过反射获取属性
        Field[]fields=mClazz.getDeclaredFields();

        for(Field field:fields){
            field.setAccessible(true);
            String name=field.getName();
            String type=field.getType().getSimpleName();//int boolean String

            //需要进行转换int---integer Strig---text

            sb.append(name).append(DaoUtlis.getColumType(type)).append(", ");
        }

        sb.replace(sb.length()-2,sb.length(),")");
        String createTalsql=sb.toString();
        Log.i(TAG,"创建表语句==="+createTalsql);
        mSqLiteDatabase.execSQL(createTalsql);

    }

    @Override
    public long insert(T obj) {
        ContentValues contentValues=contentValuesByObj(obj);

        //null
        return mSqLiteDatabase.insert(DaoUtlis.getTableName(mClazz),null,contentValues);
    }

    @Override
    public void insert(List<T> datas) {
        //批量插入采用事务  性能优化
        mSqLiteDatabase.beginTransaction();
        for(T data:datas){
            insert(data);
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    @Override
    public DaoQuerySupport<T> querySupport() {
        DaoQuerySupport daoQuerySupport=null;
        if(daoQuerySupport==null){
            daoQuerySupport=new DaoQuerySupport(mSqLiteDatabase,mClazz);
        }
        return daoQuerySupport;
    }



    @Override
    public int delete(String whereClause,String... whereArgs){
        return mSqLiteDatabase.delete(DaoUtlis.getTableName(mClazz),whereClause,whereArgs);
    }

    @Override
    public int update(T obj,String whereClause,String...whereArgs){
        ContentValues values=contentValuesByObj(obj);
        return mSqLiteDatabase.update(DaoUtlis.getTableName(mClazz),values,whereClause,whereArgs);
    }



    //obj 转成contentvalues
    private ContentValues contentValuesByObj(T obj) {
        ContentValues values=new ContentValues();
        Field[]fields=mClazz.getDeclaredFields();

        for(Field field:fields){
            try {
                field.setAccessible(true);
                String key=field.getName();
                Object value=field.get(obj);

                //缓存优化
                mPutMathodArgs[0]=key;
                mPutMathodArgs[1]=value;

                String filedTypeName=field.getType().getName();
                Method putMethod=mMethodMap.get(filedTypeName);

                if(putMethod==null) {
                  putMethod = values.getClass().getDeclaredMethod("put", String.class, value.getClass());

                  mMethodMap.put(filedTypeName,putMethod);
                }
                putMethod.invoke(values,mPutMathodArgs);


            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                mPutMathodArgs[0]=null;
                mPutMathodArgs[1]=null;
            }
        }

        return values;
    }


}
